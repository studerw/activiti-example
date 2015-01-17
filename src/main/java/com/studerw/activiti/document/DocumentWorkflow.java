package com.studerw.activiti.document;

import com.studerw.activiti.alert.AlertService;
import com.studerw.activiti.model.Alert;
import com.studerw.activiti.model.DocState;
import com.studerw.activiti.model.Document;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * User: studerw
 * Date: 5/18/14
 *
 * Workflow methods for Document Approval, Reject, etc. Most likely embedded as bean services
 * within the BPMN workflow definitions.
 */
@Service("documentWorkflow")
public class DocumentWorkflow {
    private static final Logger log = LoggerFactory.getLogger(DocumentWorkflow.class);

    @Autowired
    protected IdentityService identityService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected DocumentService docSrvc;
    @Autowired
    protected AlertService alertService;
    @Autowired
    protected TaskService taskService;


    public void approved(DelegateExecution execution) {
        log.debug("doc approved - process id: " + execution.getProcessInstanceId());

        ProcessInstance pi = runtimeService.createProcessInstanceQuery().
                processInstanceId(execution.getProcessInstanceId()).singleResult();
        String docId = pi.getBusinessKey();
        Document doc = this.docSrvc.getDocument(docId);
        Map<String, Object> vars = runtimeService.getVariables(execution.getId());
        log.debug("setting doc {} with title = {}: state set to APPROVED", doc.getId(), doc.getTitle());
        //doc.setState(Document.STATE_APPROVED);
        String message = String.format("Document entitled '%s'  has been approved. ", doc.getTitle());
        this.alertService.sendAlert(doc.getAuthor(), Alert.SUCCESS, message);
        docSrvc.updateDocument(doc);
    }

    public void rejected(DelegateExecution execution) {
        log.debug("doc rejected - process id: " + execution.getProcessInstanceId());
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().
                processInstanceId(execution.getProcessInstanceId()).singleResult();
        String docId = pi.getBusinessKey();
        Document doc = this.docSrvc.getDocument(docId);
        Map<String, Object> vars = runtimeService.getVariables(execution.getId());
        log.debug("setting doc {} with title = {}: state set to REJECTED", doc.getId(), doc.getTitle());
        doc.setState(DocState.REJECTED);
        String message = String.format("Document entitled '%s' has been rejected", doc.getTitle(), doc.getId());
        this.alertService.sendAlert(doc.getAuthor(), Alert.DANGER, message);
        this.docSrvc.updateDocument(doc);

        log.info("document rejected: " + docId);
    }

    public void publish(Execution execution) {
        String pId = execution.getProcessInstanceId();
        log.debug("doc being published - procId={}", pId);
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().
                processInstanceId(execution.getProcessInstanceId()).singleResult();
        String docId = pi.getBusinessKey();
        Document doc = this.docSrvc.getDocument(docId);
        doc.setState(DocState.REJECTED);
        String message = String.format("Document entitled '%s' has been successfully published ", doc.getTitle());
        this.alertService.sendAlert(doc.getAuthor(), Alert.SUCCESS, message);
        this.docSrvc.updateDocument(doc);
    }

    /**
     * Task listener that runs when an approveDoc task is created. Sets the candidate group to the document's group.
     *
     * @param execution
     * @param task
     */
    public void setAssignee(Execution execution, DelegateTask task) {
        String pId = execution.getProcessInstanceId();
        log.debug("doc being published - procId={}", pId);
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().
                processInstanceId(execution.getProcessInstanceId()).singleResult();
        String docId = pi.getBusinessKey();
        Document doc = this.docSrvc.getDocument(docId);
        taskService.addCandidateGroup(task.getId(), doc.getGroupId());
    }
}
