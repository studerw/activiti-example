package com.studerw.activiti.user;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.studerw.activiti.model.UserForm;
import com.studerw.activiti.util.Workflow;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * User: studerw
 * Date: 5/18/14
 */
@Service("userService")
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    public static final String SYSTEM_USER = "SYSTEM";
    protected IdentityService identityService;
    protected RuntimeService runtimeService;
    protected TaskService taskService;

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

    public UserDetails currentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails;
    }

    public void submitForApproval(UserForm userForm) {
        User user = identityService.createUserQuery().userId(userForm.getUserName()).singleResult();
        if (user != null) {
            throw new IllegalArgumentException("User with name: " + userForm.getUserName() + " already exists");
        }
        Map<String, Object> processVariables = Maps.newHashMap();
        processVariables.put("approved", Boolean.FALSE);
        processVariables.put("userForm", userForm);
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(Workflow.PROCESS_ID_USER_APPROVAL, processVariables);
        Task task = taskService.createTaskQuery().processInstanceId(pi.getProcessInstanceId()).singleResult();
        taskService.addCandidateGroup(task.getId(), userForm.getGroup());

        log.debug("beginning user registration workflow with instance id: " + pi.getId());
    }


    public Map<String, List<Group>> userWithAssignmentGroups() {
        Map<String, List<Group>> userMap = Maps.newHashMap();
        List<User> users = identityService.createUserQuery().list();
        for (User currUser : users) {
            log.debug(currUser.getId());
            List<Group> groups = this.identityService.createGroupQuery().
                    groupMember(currUser.getId()).groupType("assignment").list();
            List<Group> currentGroups = Lists.newArrayList();
            for (Group group : groups) {
                log.debug("    " + group.getId() + " - " + group.getType());
                currentGroups.add(group);
            }
            userMap.put(currUser.getId(), currentGroups);
        }
        return userMap;
    }

    public Map<String, List<String>> userWithAssignmentGroupStr() {
        Map<String, List<String>> userMap = Maps.newHashMap();
        List<User> users = identityService.createUserQuery().list();
        for (User currUser : users) {
            log.debug(currUser.getId());
            List<Group> groups = this.identityService.createGroupQuery().
                    groupMember(currUser.getId()).groupType("assignment").list();
            List<String> currentGroups = Lists.newArrayList();
            for (Group group : groups) {
                log.debug("    " + group.getId() + " - " + group.getType());
                currentGroups.add(group.getId());
            }
            userMap.put(currUser.getId(), currentGroups);
        }
        return userMap;
    }

    public List<Group> getAssignmentGroups(String userId) {
        List<Group> groups = identityService.createGroupQuery().groupMember(userId)
                .groupType("assignment")
                .orderByGroupId().asc().list();
        return groups;
    }

    public List<UserForm> getAllUsers(){
        List<User> users = this.identityService.createUserQuery().orderByUserId().asc().list();
        List<UserForm> userForms = Lists.newArrayList();
        for(User user : users){
            userForms.add(UserForm.fromUser(user));
        }
        return userForms;
    }

    public List<Group> getAllAssignmentGroups() {
        List<Group> groups = identityService.createGroupQuery()
                .groupType("assignment")
                .orderByGroupId().asc().list();
        return groups;
    }
}
