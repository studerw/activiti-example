package com.studerw.activiti.user;

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
@Service("userAccountWorkflow")
public class UserAccountWorkflow {
    private static final Logger log = LoggerFactory.getLogger(UserAccountWorkflow.class);

    @Autowired
    IdentityService identityService;
    @Autowired
    RuntimeService runtimeService;


    public void approved(Execution execution) {
        log.debug("process id: " + execution.getProcessInstanceId());
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().
                processInstanceId(execution.getProcessInstanceId()).singleResult();
        Map<String, Object> vars = runtimeService.getVariables(execution.getId());
        UserForm userForm = (UserForm) vars.get("userForm");
        User user = identityService.newUser(userForm.getUserName());
        user.setEmail(userForm.getEmail());
        user.setPassword((userForm.getPassword()));
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        identityService.saveUser(user);
        identityService.createMembership(userForm.getUserName(), "user");
        identityService.createMembership(userForm.getUserName(), userForm.getGroup());
        log.info("user account approved - new user with name {} created", user.getId());
    }

    public void denied(Execution execution) {
        log.debug("process id: " + execution.getProcessInstanceId());
        log.debug("user account denied");
    }
}
