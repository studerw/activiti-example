package com.studerw.activiti;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.studerw.activiti.document.DocumentService;
import com.studerw.activiti.model.document.BookReport;
import com.studerw.activiti.model.document.DocState;
import com.studerw.activiti.task.LocalTaskService;
import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author William Studer
 *         Date: 5/25/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/testAppContext.xml"})
public class BookReportTest {
    private static final Logger LOG = LoggerFactory.getLogger(BookReportTest.class);
    @Autowired RuntimeService runtimeService;
    @Autowired TaskService taskService;
    @Autowired LocalTaskService localTaskService;
    @Autowired HistoryService historyService;
    @Autowired IdentityService identityService;
    @Autowired RepositoryService repositoryService;
    @Autowired DocumentService documentService;
    @Autowired @Qualifier("dataSource") javax.sql.DataSource datasource;

    @Before
    public void before() {
        setSpringSecurity("fozzie");
    }

//    @Test
//    public void testParseCategory() {
//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
//                processDefinitionKey(BookReport.WORKFLOW_ID).singleResult();
//        assertNotNull("Book Report Process Definition is not null", processDefinition);
//
//        String category = processDefinition.getCategory();
//        LOG.debug("Category: {}", category);
//        final Map<String, String> map = Splitter.on('&').trimResults().withKeyValueSeparator("=").split(category);
//        LOG.debug(map);
//        assertTrue(map.keySet().containsAll(Arrays.asList(Workflow.CATEGORY_DOC_TYPE, Workflow.CATEGORY_GROUP)));
//        String docType = map.get(Workflow.CATEGORY_DOC_TYPE);
//        String group = map.get(Workflow.CATEGORY_GROUP);
//        assertEquals(DocType.valueOf(docType), DocType.BOOK_REPORT);
//        assertEquals(group, "engineering");
//    }

//    @Test
//    public void testDeployed() throws Exception {
//        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery().list();
//        for (ProcessDefinition definition : definitions) {
//            System.err.println(definition.getId());
//        }
//
//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
//                processDefinitionKey(BookReport.WORKFLOW_ID).singleResult();
//        assertNotNull("Book Report Process Definition is not null", processDefinition);
//
//        String category = processDefinition.getCategory();
//        LOG.debug("Category: {}", category);
//    }

    @Test
    @Ignore
    public void testEmptyBookReportProcess() throws Exception {
        BookReport bookReport = new BookReport();
        bookReport.setCreatedDate(new Date());
        bookReport.setAuthor("fozzie");
        bookReport.setTitle("test book report");
        bookReport.setContent("some content");
        bookReport.setSummary("summary");
        bookReport.setBookTitle("Some title");
        bookReport.setBookAuthor("John Smith");
        bookReport.setGroupId("engineering");
        LOG.debug(bookReport.toString());

        String id = this.documentService.createDocument(bookReport);

        ProcessInstance pi = runtimeService.startProcessInstanceByKey(BookReport.WORKFLOW_ID, id);
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        assertNotNull(task);
        taskService.complete(task.getId());
        BookReport updated = (BookReport) this.documentService.getDocument(id);
        LOG.debug("{}",updated);

        assertNotNull("should have returned valid doc", updated);
        assertTrue("Should have state=Published", updated.getDocState() == DocState.PUBLISHED);
    }

    @Test
    @Ignore
    public void testBookReportWithTasks() throws Exception {
        final String defKey = "bookReportWithTasksWorkflow";
        BookReport updated = null;

        BookReport bookReport = new BookReport();
        bookReport.setCreatedDate(new Date());
        bookReport.setAuthor("fozzie");
        bookReport.setTitle("test book report");
        bookReport.setContent("some content");
        bookReport.setSummary("summary");
        bookReport.setBookTitle("Some title");
        bookReport.setBookAuthor("John Smith");
        bookReport.setGroupId("engineering");
        LOG.debug(bookReport.toString());

        String id = this.documentService.createDocument(bookReport);

        ProcessInstance pi = runtimeService.startProcessInstanceByKey(defKey, id);

        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        taskService.complete(task.getId());

        updated = (BookReport) this.documentService.getDocument(id);
        LOG.debug("{}", updated);
        assertTrue("Should have state=WAITING_FOR_COLLABORATION", updated.getDocState() == DocState.WAITING_FOR_COLLABORATION);

        task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        assertEquals(task.getTaskDefinitionKey(), "COLLABORATE_DOC_USER_TASK_1");
        localTaskService.collaborateTask(task.getId(), "some colloboration comment 1");
        updated = (BookReport) this.documentService.getDocument(id);
        LOG.debug("{}",updated);
        assertTrue("Should have state=WAITING_FOR_COLLABORATION", updated.getDocState() == DocState.WAITING_FOR_COLLABORATION);


        task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        assertEquals(task.getTaskDefinitionKey(), "COLLABORATE_DOC_USER_TASK_2");
        localTaskService.collaborateTask(task.getId(), "some colloboration comment 2");
        updated = (BookReport) this.documentService.getDocument(id);
        LOG.debug("{}",updated);
        assertTrue("Should have state=WAITING_FOR_APPROVAL", updated.getDocState() == DocState.WAITING_FOR_APPROVAL);


        task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        assertEquals(task.getTaskDefinitionKey(), "APPROVE_REJECT_DOC_USER_TASK_1");
        localTaskService.approveOrRejectDoc(true, "some comment 1", task.getId());
        updated = (BookReport) this.documentService.getDocument(id);
        LOG.debug("{}",updated);
        assertTrue("Should have state=WAITING_FOR_APPROVAL", updated.getDocState() == DocState.WAITING_FOR_APPROVAL);

        task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        assertEquals(task.getTaskDefinitionKey(), "APPROVE_REJECT_DOC_USER_TASK_2");
        localTaskService.approveOrRejectDoc(true, "some comment 2", task.getId());
        updated = (BookReport) this.documentService.getDocument(id);
        LOG.debug("{}",updated);

        updated = (BookReport) this.documentService.getDocument(id);
        LOG.debug("{}",updated);
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
