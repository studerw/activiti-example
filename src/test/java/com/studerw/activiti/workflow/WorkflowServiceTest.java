package com.studerw.activiti.workflow;

import com.studerw.activiti.document.DocumentService;
import com.studerw.activiti.model.DocType;
import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/testAppContext.xml"})
public class WorkflowServiceTest {
    @Autowired RuntimeService runtimeService;
    @Autowired TaskService taskService;
    @Autowired HistoryService historyService;
    @Autowired IdentityService identityService;
    @Autowired RepositoryService repositoryService;
    @Autowired DocumentService documentService;
    @Autowired @Qualifier("dataSource") javax.sql.DataSource datasource;
    @Autowired WorkflowService workflowService;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFindDefinitionByDocType() throws Exception {
        ProcessDefinition pd = this.workflowService.findDefinitionByDocType(DocType.BOOK_REPORT);
        assertNotNull(pd);

        ProcessDefinition pd1 = this.workflowService.findDefinitionByDocType(DocType.RECEIPT);
        assertNull(pd1);

    }
}