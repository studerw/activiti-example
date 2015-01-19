package com.studerw.activiti.document;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.studerw.activiti.document.dao.BookReportDao;
import com.studerw.activiti.document.dao.InvoiceDao;
import com.studerw.activiti.model.*;
import com.studerw.activiti.user.InvalidAccessException;
import com.studerw.activiti.user.UserService;
import com.studerw.activiti.util.Workflow;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
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
 * User: studerw
 * Date: 5/18/14
 */
@Service("documentService")
public class DocumentService {
    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);
    protected IdentityService identityService;
    protected RuntimeService runtimeService;
    protected TaskService taskService;
    protected UserService userService;
    protected InvoiceDao invoiceDao;
    protected BookReportDao bookReportDao;

    @Autowired
    public void setInvoiceDao(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    @Autowired
    public void setBookReportDao(BookReportDao bookReportDao) {
        this.bookReportDao = bookReportDao;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Autowired
    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

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
        if (DocType.BOOK_REPORT.equals(document.getDocType())){
            newId = this.bookReportDao.create((BookReport)document);
        }
        else if  (DocType.INVOICE.equals(document.getDocType())){
            newId = this.invoiceDao.create((Invoice)document);
        }
        else {
            throw new IllegalArgumentException("Unknown doc type: " + document.getDocType());
        }
        return newId;
    }

    public void submitForApproval(String docId) {
        Document doc = this.invoiceDao.read(docId);
        log.debug("beginning (or continuing) doc approval workflow for doc {}. ", doc.getId());
        UserDetails userDetails = this.userService.currentUser();
        if (!StringUtils.equals(userDetails.getUsername(), doc.getAuthor())) {
            throw new InvalidAccessException("Only the author of a doc can submit for approval");
        }
        doc.setDocState(DocState.WAITING_FOR_APPROVAL);

        //Workflow
        //TODO check author and currentUser
        Map<String, Object> processVariables = Maps.newHashMap();
        processVariables.put("initiator", doc.getAuthor());
        processVariables.put("docId", doc.getId());
        processVariables.put("docAuthor", doc.getAuthor());
        try {
            identityService.setAuthenticatedUserId(userDetails.getUsername());
            ProcessInstance current = this.getCurrentProcess(docId);
            if (current == null) {
                String key = String.format("%s-%s", Workflow.PROCESS_ID_DOC_APPROVAL, doc.getGroupId());
                current = runtimeService.startProcessInstanceByKey(key, doc.getId());
            }
            Task task = taskService.createTaskQuery().processInstanceId(current.getProcessInstanceId()).singleResult();
            taskService.setAssignee(task.getId(), userDetails.getUsername());

            if (DocType.BOOK_REPORT.equals(doc.getDocType())){
                this.bookReportDao.update((BookReport) doc);
            }
            else if  (DocType.INVOICE.equals(doc.getDocType())){
                this.invoiceDao.update((Invoice) doc);
            }
            else {
                throw new IllegalArgumentException("Unknown doc type: " + doc.getDocType());
            }
            taskService.setVariableLocal(task.getId(), "taskOutcome", "Submitted for Approval");
            taskService.setVariables(task.getId(), processVariables);
            taskService.complete(task.getId());
        }
        finally {
            identityService.setAuthenticatedUserId(null);
        }
    }

    /**
     * It's possible this document is being resubmitted after reject - no need to create a new process.
     *
     * @param docId
     * @return the associated ProcessInstance or null if one does not exist
     */
    private ProcessInstance getCurrentProcess(String docId) {
        List<ProcessInstance> instances =
                runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(docId).list();
        if (instances.size() == 0) {
            return null;
        } else if (instances.size() > 1) {
            throw new IllegalStateException("More than one process found for document: " + docId + " - zero or one should have been found.");
        } else {
            return instances.get(0);
        }
    }

    @Transactional
    public void updateDocument(Document document) {
        String id = document.getId();
        if (DocType.BOOK_REPORT.equals(document.getDocType())){
            this.bookReportDao.update((BookReport)document);
        }
        else if  (DocType.INVOICE.equals(document.getDocType())){
            this.invoiceDao.update((Invoice)document);
        }
        else {
            throw new IllegalArgumentException("Unknown doc type: " + document.getDocType());
        }
    }

    @Transactional(readOnly = true)
    public Document getDocument(String id) {
        try {
            return this.bookReportDao.read(id);
        }
        catch(Throwable t){}

        return this.invoiceDao.read(id);
    }
}
