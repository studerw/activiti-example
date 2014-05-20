package com.studerw.activiti.document;

import com.studerw.activiti.model.Document;
import com.studerw.activiti.model.UserForm;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.identity.User;
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
 */
@Service("documentWorkflow")
public class documentWorkflow {
    private static final Logger log = LoggerFactory.getLogger(documentWorkflow.class);

    @Autowired
    IdentityService identityService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    DocumentService docSrvc;


    public void approved(Execution execution) {
        log.debug("doc approved - process id: " + execution.getProcessInstanceId());

        ProcessInstance pi = runtimeService.createProcessInstanceQuery().
                processInstanceId(execution.getProcessInstanceId()).singleResult();
        String docId = pi.getBusinessKey();
        Document doc = this.docSrvc.getDocument(docId);
        Map<String, Object> vars = runtimeService.getVariables(execution.getId());
        log.debug("setting doc {} with title = {}: state set to PUBLISHED", doc.getId(), doc.getTitle());
        doc.setState("PUBLISHED");
        this.docSrvc.updateDocument(doc);

    }

    public void denied(Execution execution) {
        log.debug("process id: " + execution.getProcessInstanceId());
        log.debug("user account denied");
    }
}
