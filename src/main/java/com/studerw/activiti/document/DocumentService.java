package com.studerw.activiti.document;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.studerw.activiti.document.dao.BookReportDao;
import com.studerw.activiti.document.dao.InvoiceDao;
import com.studerw.activiti.model.document.BookReport;
import com.studerw.activiti.model.document.DocType;
import com.studerw.activiti.model.document.Document;
import com.studerw.activiti.model.document.Invoice;
import com.studerw.activiti.user.InvalidAccessException;
import com.studerw.activiti.user.UserService;
import com.studerw.activiti.workflow.WorkflowService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author William Studer
 * Date: 5/18/14
 */
@Service("documentService")
public class DocumentService {
    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);
    @Autowired protected IdentityService identityService;
    @Autowired protected RuntimeService runtimeService;
    @Autowired protected RepositoryService repositoryService;
    @Autowired protected TaskService taskService;
    @Autowired protected UserService userService;
    @Autowired protected InvoiceDao invoiceDao;
    @Autowired protected BookReportDao bookReportDao;
    @Autowired protected WorkflowService workflowService;

    @Transactional(readOnly = true)
    public List<Document> getGroupDocumentsByUser(String userId) {
        List<Document> docs = Lists.newArrayList();
        List<Group> groups = this.userService.getAssignmentGroups(userId);
        List<String> groupIds = Lists.newArrayList();
        for (Group group : groups) {
            groupIds.add(group.getId());
        }
        for (Document doc : this.invoiceDao.readAll()) {
            if (groupIds.contains(doc.getGroupId())) {
                docs.add(doc);
            }
        }
        for (Document doc : this.bookReportDao.readAll()) {
            if (groupIds.contains(doc.getGroupId())) {
                docs.add(doc);
            }
        }
        Collections.sort(docs);
        return docs;
    }

    @Transactional
    public String createDocument(Document document) {
        String id = document.getId();
        if (!("TEMP".equals(id) || StringUtils.isBlank(id))) {
            throw new IllegalArgumentException("Can't save new doc with id already set");
        }
        document.setId(null);
        String newId = null;
        if (DocType.BOOK_REPORT.equals(document.getDocType())) {
            newId = this.bookReportDao.create((BookReport) document);
        } else if (DocType.INVOICE.equals(document.getDocType())) {
            newId = this.invoiceDao.create((Invoice) document);
        } else {
            throw new IllegalArgumentException("Unknown doc type: " + document.getDocType());
        }
        return newId;
    }

    @Transactional
    public void submitToWorkflow(String docId) {
        Document doc = this._getDocument(docId);
        log.debug("beginning (or continuing) doc workflow for doc {}. ", doc.getId());
        UserDetails userDetails = this.userService.currentUser();
        if (!StringUtils.equals(userDetails.getUsername(), doc.getAuthor())) {
            throw new InvalidAccessException("Only the author of a doc can submit for approval");
        }

        try {
            identityService.setAuthenticatedUserId(userDetails.getUsername());
            ProcessInstance current = workflowService.findProcessByBusinessKey(docId);
            if (current != null) {
                throw new IllegalStateException("Running WF Process with key: " + docId + " already exists.");
            }
            DocType docType = doc.getDocType();
            String group = doc.getGroupId();
            ProcessDefinition procDef = this.workflowService.findProcDefByGroup(docType, group);
            if (procDef == null) {
                throw new IllegalArgumentException("No workflow exists for doctype=" + docType.name() + " and group=" + group);
            }
            current = runtimeService.startProcessInstanceByKey(procDef.getKey());
            Task task = taskService.createTaskQuery().processInstanceId(current.getProcessInstanceId()).singleResult();
            taskService.setAssignee(task.getId(), userDetails.getUsername());

            /*if (DocType.BOOK_REPORT.equals(doc.getDocType())) {
                this.bookReportDao.update((BookReport) doc);
            } else if (DocType.INVOICE.equals(doc.getDocType())) {
                this.invoiceDao.update((Invoice) doc);
            } else {
                throw new IllegalArgumentException("Unknown doc type: " + doc.getDocType());
            }*/
            taskService.setVariableLocal(task.getId(), "taskOutcome", "Submitted for Approval");
            Map<String, Object> processVariables = Maps.newHashMap();
            processVariables.put("initiator", doc.getAuthor());
            processVariables.put("businessKey", doc.getId());
            processVariables.put("docAuthor", doc.getAuthor());
            taskService.setVariables(task.getId(), processVariables);
            taskService.complete(task.getId());
        }
        finally {
            identityService.setAuthenticatedUserId(null);
        }
    }


    @Transactional
    public void updateDocument(Document document) {
        if (DocType.BOOK_REPORT.equals(document.getDocType())) {
            this.bookReportDao.update((BookReport) document);
        } else if (DocType.INVOICE.equals(document.getDocType())) {
            this.invoiceDao.update((Invoice) document);
        } else {
            throw new IllegalArgumentException("Unknown doc type: " + document.getDocType());
        }
    }

    @Transactional(readOnly = true)
    public Document getDocument(String id) {
        return this._getDocument(id);
    }


    /**
     * We use a private method here to enable internal calls without messing with spring proxies.
     *
     * @param id
     * @return null
     */
    private Document _getDocument(String id) {
        try {
            return this.bookReportDao.read(id);
        }
        catch (Throwable t) {
        }
        return this.invoiceDao.read(id);
    }

    /**
     * @param docType
     * @param group
     * @return a valid process definition by DocType and Group or null if none exits
     */
    /*
    public ProcessDefinition findProcDefByTypeAndGroup(DocType docType, String group) {
        log.debug("Finding process definition by docType={} and group={}", docType.name(), group);
        return repositoryService.createProcessDefinitionQuery().processDefinitionCategory(docType.name()).
                processDefinitionKey(group).singleResult();
    }*/
}
