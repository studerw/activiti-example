package com.studerw.activiti.workflow;

import com.studerw.activiti.document.DocumentService;
import com.studerw.activiti.model.document.DocType;
import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/testAppContext.xml"})
public class WorkflowServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowServiceTest.class);

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
        List<ProcessDefinition> definitions = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition definition : definitions) {
            LOG.debug("{} - {}", definition.getId(), definition.getKey());
        }

    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    @Ignore
    public void testGetProcessDefinitionDiagram() throws Exception {
        fail("not implemented");
    }

    @Test
    @Ignore

    public void testGetActiveDocumentDiagram() throws Exception {
        fail("not implemented");
    }

    @Test
    public void testGetAllProcDefs() {
        List<ProcessDefinition> definitions = this.workflowService.getAllProcDefs(false);
        assertTrue(definitions.size() == 5);

        definitions = this.workflowService.getAllProcDefs(true);
        assertTrue(definitions.size() == 5);

        for (ProcessDefinition definition : definitions) {
            LOG.debug(definition.getDescription());
        }

    }

    @Test
    public void testGroupWorkflowExists() throws Exception {
        String group = "engineering";
        boolean exists = this.workflowService.groupWorkflowExists(DocType.BOOK_REPORT, group);
        assertTrue("BOOK_WORKFLOW_engineering should exist.", exists);

        group = "FOO_SDFSFSF_SDFSDF";
        exists = this.workflowService.groupWorkflowExists(DocType.BOOK_REPORT, group);
        assertFalse("random group workflow should NOT exist.", exists);

    }


    @Test
    @Ignore
    public void testUpdateWorkflow() throws Exception {
        fail("not implemented");
    }

    @Test
    public void testBaseDocTypeWorkflowExists() throws Exception {
        boolean exists = this.workflowService.baseDocTypeWorkflowExists(DocType.BOOK_REPORT);
        assertTrue("BOOK_REPORT base workflow should exist.", exists);

        exists = this.workflowService.baseDocTypeWorkflowExists(DocType.UNIT_TEST_NO_EXIST);
        assertFalse("NO_EXIST docType base workflow should NOT exist.", exists);
    }

    @Test
    public void testFindProcDef() {
        ProcessDefinition pd = this.workflowService.findProcDef(DocType.BOOK_REPORT, "engineering");
        LOG.debug("found {}", pd.getKey());
        assertNotNull("Should have engineering workflow for BOOK_REPORT", pd);

        ProcessDefinition pd1 = this.workflowService.findProcDef(DocType.BOOK_REPORT, "foo");
        LOG.debug("found {}", pd1.getKey());
        assertNotNull("Should have base workflow for BOOK_REPORT", pd1);

        assertNotEquals("engineering and base should be different", pd.getId(), pd1.getId());

        ProcessDefinition pd2 = this.workflowService.findProcDef(DocType.UNIT_TEST_NO_EXIST, "foo");
        assertNull(pd2);
    }

    @Test
    public void testFindBaseProcDef() {
        ProcessDefinition pd = this.workflowService.findBaseProcDef(DocType.BOOK_REPORT);
        assertNotNull(pd);

        pd = this.workflowService.findBaseProcDef(DocType.UNIT_TEST_NO_EXIST);
        assertNull(pd);
    }

    @Test
    public void testGetBaseDocTypes() {
        List<DocType> docTypes = this.workflowService.findExistingBaseDocTypes();
        assertTrue(docTypes.size() == 2);
        assertTrue(docTypes.contains(DocType.BOOK_REPORT));
        assertTrue(docTypes.contains(DocType.INVOICE));
    }

    @Test
    @Ignore
    public void testFindProcessByBusinessKey() throws Exception {
        fail("not implemented");
    }

    /*public String getProcessDefXml(String key){
        ProcessDefinition defintion = this.repoSrvc.createProcessDefinitionQuery().processDefinitionKey(key).singleResult();
        if (defintion == null) {
            throw new IllegalArgumentException("Invalid process defintion key: " + key);
        }
        ProcessDefinitionEntity entity = (ProcessDefinitionEntity)defintion;
        return null;
    }*/

}
