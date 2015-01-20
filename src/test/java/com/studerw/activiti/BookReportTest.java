package com.studerw.activiti;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.studerw.activiti.document.DocumentService;
import com.studerw.activiti.model.BookReport;
import com.studerw.activiti.model.DocState;
import com.studerw.activiti.model.DocType;
import com.studerw.activiti.util.Workflow;
import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * User: studerw
 * Date: 5/25/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/testAppContext.xml"})
public class BookReportTest {
    private static final Logger log = LogManager.getLogger(BookReportTest.class);
    @Autowired RuntimeService runtimeService;
    @Autowired TaskService taskService;
    @Autowired HistoryService historyService;
    @Autowired IdentityService identityService;
    @Autowired RepositoryService repositoryService;
    @Autowired DocumentService documentService;
    @Autowired @Qualifier("dataSource") javax.sql.DataSource datasource;

    @Before
    public void before(){
        setSpringSecurity("fozzie");
    }

    @Test
    public void testParseCategory(){
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(BookReport.WORKFLOW_ID).singleResult();
        assertNotNull("Book Report Process Definition is not null", processDefinition);

        String category = processDefinition.getCategory();
        log.debug("Category: {}", category);
        final Map<String, String> map = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(category);
        log.debug(map);
        assertTrue(map.keySet().containsAll(Arrays.asList(Workflow.CATEGORY_DOC_TYPE, Workflow.CATEGORY_GROUP)));
        String docType = map.get(Workflow.CATEGORY_DOC_TYPE);
        String group = map.get(Workflow.CATEGORY_GROUP);
        assertEquals(DocType.valueOf(docType), DocType.BOOK_REPORT);
        assertEquals(group, "engineering");
    }

    @Test
    public void testDeployed() throws Exception {
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery().list();
        for(ProcessDefinition definition: definitions){
            System.err.println(definition.getId());
        }

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(BookReport.WORKFLOW_ID).singleResult();
        assertNotNull("Book Report Process Definition is not null", processDefinition);

        String category = processDefinition.getCategory();
        log.debug("Category: {}", category);
    }

    @Test
    public void testProcess() throws Exception {
        BookReport bookReport = new BookReport();
        bookReport.setCreatedDate(new Date());
        bookReport.setAuthor("fozzie");
        bookReport.setTitle("test book report");
        bookReport.setContent("some content");
        bookReport.setSummary("summary");
        bookReport.setBookTitle("Some title");
        bookReport.setBookAuthor("John Smith");
        bookReport.setGroupId("engineering");
        log.debug(bookReport.toString());

        String id = this.documentService.createDocument(bookReport);

        ProcessInstance pi = runtimeService.startProcessInstanceByKey(BookReport.WORKFLOW_ID, id);
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        assertNotNull(task);
        taskService.complete(task.getId());
        BookReport updated = (BookReport) this.documentService.getDocument(id);
        log.debug(updated);

        assertNotNull("should have returned valid doc", updated);
        assertTrue("Should have state=Published", updated.getDocState() == DocState.PUBLISHED);
    }

    private void setSpringSecurity(String userName) {
        List<Group> groups = this.identityService.createGroupQuery().groupMember(userName).groupType("security-role").list();
        List<String> groupStr = Lists.newArrayList();
        for (Group g : groups) {
            groupStr.add(g.getId());
        }
        Collection<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(Joiner.on(",").skipNulls().join(groupStr));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(userName, "pw", true, true, true, true, auths);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
