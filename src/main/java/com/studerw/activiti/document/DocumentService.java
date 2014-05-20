package com.studerw.activiti.document;

import com.google.common.collect.Maps;
import com.studerw.activiti.model.UserForm;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void submitForApproval(UserForm userForm){
        User user = identityService.createUserQuery().userId(userForm.getUserName()).singleResult();
        if (user != null){
            throw new IllegalArgumentException("User with name: " + userForm.getUserName() + " already exists");
        }
        Map<String,Object> processVariables = Maps.newHashMap();
        processVariables.put("approved", Boolean.FALSE);
        processVariables.put("userForm", userForm);
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("newChromeUser", processVariables);
        Task task = taskService.createTaskQuery().processInstanceId(pi.getProcessInstanceId()).singleResult();
        taskService.addCandidateGroup(task.getId(), userForm.getGroup());

        log.debug("beginning user registration workflow with instance id: " + pi.getId());
    }

}
