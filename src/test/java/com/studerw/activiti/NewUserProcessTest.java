package com.studerw.activiti;

import com.google.common.collect.Maps;
import com.studerw.activiti.model.UserForm;
import com.studerw.activiti.model.task.CandidateTask;
import com.studerw.activiti.task.LocalTaskService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/testAppContext.xml"})
public class NewUserProcessTest {
    private static final Logger LOG = LoggerFactory.getLogger(NewUserProcessTest.class);
    @Autowired RuntimeService runtimeService;
    @Autowired TaskService taskService;
    @Autowired HistoryService historyService;
    @Autowired IdentityService identityService;
    @Autowired Validator validator;
    @Autowired LocalTaskService localTaskService;

    @Test
    public void testSetup() {
        assertNotNull(runtimeService);
        assertNotNull(taskService);
    }

    @Test
    @Ignore
    //TODO fix this - NPE on taskQuery
    public void testNewUserProcess() throws InvocationTargetException, IllegalAccessException {
        identityService.setAuthenticatedUserId("fozzie");
        Map<String, Object> processVariables = Maps.newHashMap();
        processVariables.put("approved", Boolean.FALSE);
        UserForm userForm = new UserForm("newUser324", "pwabcdedfgss", "me@here.com", "jim", "smith", "engineering");
        Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm);
        assertTrue(violations.size() == 0);
        LOG.debug("{}",violations.size());

        processVariables.put("userForm", userForm);
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("newUser", processVariables);

        Task task = taskService.createTaskQuery().taskCandidateGroup("admin").
                //includeProcessVariables().
                        singleResult();
        assertNotNull(task);
        CandidateTask candidateTask = CandidateTask.fromTask(task);
        LOG.debug("{}",candidateTask);
        List<Task> tasks = taskService.createTaskQuery().
                taskCandidateOrAssigned("kermit").
                orderByTaskCreateTime().asc().list();
        LOG.debug("tasks size: {}", tasks.size());
//        assertTrue(tasks.size() == 1);
        LOG.debug("owner: {}", task.getOwner());
        identityService.setAuthenticatedUserId("kermit");
        taskService.setAssignee(task.getId(), "kermit");
        taskService.addComment(task.getId(), pi.getId(), "Here is a comment");
        List<Comment> comments = taskService.getTaskComments(task.getId());
        assertTrue(comments.size() == 1);
        LOG.debug(comments.get(0).getFullMessage());
        LOG.debug(comments.get(0).getTime().toString());
        Map<String, Object> taskVariables = new HashMap<String, Object>();
        taskVariables.put("approved", true);
        taskService.complete(task.getId(), taskVariables);

        //HistoryService historyService.
    }


}

    
