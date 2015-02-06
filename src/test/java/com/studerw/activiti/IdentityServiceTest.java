package com.studerw.activiti;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/testAppContext.xml"})
//@TransactionConfiguration(defaultRollback=true)
public class IdentityServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(IdentityServiceTest.class);

    @Autowired IdentityService identityService;

    @Test
    public void testSetup() {
        assertNotNull(this.identityService);
    }

    @Test
    @Transactional
    public void testUsers() {
        List<User> users = identityService.createUserQuery().list();
        int originalCount = users.size();
        for (User user : users) {
            LOG.debug(user.getId());
        }

        User user = identityService.newUser("newUser");
        user.setFirstName("Bill");
        user.setLastName("Studer");
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

        int finalCount = users2.size();
        assertTrue(finalCount == originalCount + 1);

        User user2 = this.identityService.createUserQuery().userId("newUser").singleResult();
        LOG.debug("user pws: {}", user2.getPassword());
        assertEquals(user2.getPassword(), "password");

    }

    @Test
    public void testGroups() {
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

    
