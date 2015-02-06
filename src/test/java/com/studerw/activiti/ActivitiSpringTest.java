package com.studerw.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/testAppContext.xml"})
public class ActivitiSpringTest {
    private static final Logger LOG = LoggerFactory.getLogger(ActivitiSpringTest.class);

    @Autowired RuntimeService runtimeService;
    @Autowired TaskService taskService;
    @Autowired HistoryService historyService;
    @Autowired IdentityService identityService;

    @Test
    public void testSetup() {
        assertNotNull(runtimeService);
        assertNotNull(taskService);
    }

    @Ignore
    @Test
    public void testDummyProcess() {
        Map<String, Object> variableMap = new HashMap<String, Object>();
        identityService.setAuthenticatedUserId("kermit");
        runtimeService.startProcessInstanceByKey("dummyProcess", variableMap);
        //Task task = taskService.createTaskQuery().singleResult();
        //assertEquals("Dummy Task", task.getName());
        //taskService.complete(task.getId());
        //assertEquals(0, runtimeService.createProcessInstanceQuery().count());
    }

    @Test
    public void testHistory() {
        List<HistoricProcessInstance> instances;
        instances = historyService.createHistoricProcessInstanceQuery().finished().list();
        for (HistoricProcessInstance instance : instances) {
            LOG.debug(instance.getId());
        }
    }


    @Test
    public void testUsers() {
        List<User> users = identityService.createUserQuery().list();
        int originalCount = users.size();
        LOG.debug("count:  {}", users.size());
        for (User user : users) {
            LOG.debug(user.getId());
        }

        User user = identityService.newUser("newUser");
        user.setFirstName("Bill");
        user.setLastName("Smith");
        user.setPassword("password");
        identityService.saveUser(user);
        identityService.createMembership("newUser", "user");

        List<User> users2 = identityService.createUserQuery().list();
        for (User user2 : users2) {
            LOG.debug(user2.getId());
            List<Group> groups = this.identityService.createGroupQuery().groupMember(user2.getId()).groupType("security-role").list();
            for (Group group : groups) {
                LOG.debug("{} - {}", group.getId(), group.getType());
            }
        }

    }
}
    
