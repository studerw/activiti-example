package com.studerw.activiti.document;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.studerw.activiti.model.Document;
import com.studerw.activiti.model.UserForm;
import com.studerw.activiti.user.InvalidAccessException;
import com.studerw.activiti.user.UserService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    protected DocumentDao docDao;

    @Autowired
    public void setDocDao(DocumentDao docDao) {
        this.docDao = docDao;
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
    public void setRuntimeService(RuntimeService runtimeService){
        this.runtimeService = runtimeService;
    }

    @Autowired
    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Transactional(readOnly=true)
    public List<Document> getGroupDocumentsByUser(String userId){
        List<Document> docs = Lists.newArrayList();
        List<Group> groups = this.userService.getAssignmentGroups(userId);
        List<String> groupIds = Lists.newArrayList();
        for(Group group : groups){
            groupIds.add(group.getId());
        }
        for(Document doc : this.docDao.readAll()){
            if (groupIds.contains(doc.getGroupId())){
                docs.add(doc);
            }
        }
        Collections.sort(docs);
        return docs;
    }

    @Transactional
    public String createDocument(Document document){
        String id = document.getId();
        if (!("TEMP".equals(id) || StringUtils.isBlank(id))){
            throw new IllegalArgumentException("Can't save new doc with id already set");
        }
        document.setId(null);
        String newId = this.docDao.create(document);
        return newId;
    }
    public void submitForApproval(String docId){
        Document doc = this.docDao.read(docId);
        log.debug("beginning doc approval workflow for doc {}. ", doc.getId());
        UserDetails userDetails = this.userService.currentUser();
        if(!StringUtils.equals(userDetails.getUsername(), doc.getAuthor())){
            throw new InvalidAccessException("Only the author of a doc can submit for approval");
        }
        doc.setState(Document.STATE_WAITING_FOR_APPROVAL);

        //Workflow
        //TODO check author and currentUser
        Map<String,Object> processVariables = Maps.newHashMap();
        processVariables.put("approved", Boolean.FALSE);
        processVariables.put("initiator", doc.getAuthor());
        try {
            identityService.setAuthenticatedUserId(userDetails.getUsername());
            ProcessInstance pi = runtimeService.startProcessInstanceByKey("docApproval", doc.getId(), processVariables);
            Task task = taskService.createTaskQuery().processInstanceId(pi.getProcessInstanceId()).singleResult();
            task.setAssignee(userDetails.getUsername());
            //taskService.addCandidateGroup(task.getId(), document.getGroupId());
            this.docDao.update(doc);
            taskService.complete(task.getId());
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
    }


    @Transactional
    public void updateDocument(Document document){
        String id = document.getId();
        this.docDao.update(document);
    }

    @Transactional(readOnly=true)
    public Document getDocument(String id){
        return this.docDao.read(id);
    }
}
