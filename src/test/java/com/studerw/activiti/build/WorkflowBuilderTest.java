package com.studerw.activiti.build;

import com.google.common.collect.Lists;
import com.studerw.activiti.model.document.DocType;
import com.studerw.activiti.model.workflow.DynamicUserTask;
import com.studerw.activiti.model.workflow.DynamicUserTaskType;
import com.studerw.activiti.workflow.WFConstants;
import com.studerw.activiti.workflow.WorkflowBuilder;
import com.studerw.activiti.workflow.WorkflowService;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * @author William Studer
 *         Date: 5/25/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/testAppContext.xml"})
public class WorkflowBuilderTest {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowBuilderTest.class);
    @Autowired public WorkflowBuilder workflowBldr;
    @Autowired RuntimeService runtimeService;
    @Autowired TaskService taskService;
    @Autowired HistoryService historyService;
    @Autowired IdentityService identityService;
    @Autowired RepositoryService repositoryService;
    @Autowired WorkflowService workflowService;

    @Test
    @Ignore
    public void testRepoService() throws IOException {
        List<ProcessDefinition> pds = this.repositoryService.createProcessDefinitionQuery().list();
        LOG.debug("Number of pds: {}", pds.size());
        for (ProcessDefinition pd : pds) {
            BpmnModel model = repositoryService.getBpmnModel(pd.getId());
            InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
//            InputStream in = repositoryService.getProcessDiagram(pd.getId());
            FileUtils.copyInputStreamToFile(in, new File("target/" + pd.getName() + "_diagram.png"));
            IOUtils.closeQuietly(in);
        }
    }

    @Test
    public void testGeneratePng() throws IOException {
        List<DynamicUserTask> dynamicUserTasks = Lists.newArrayList();
        DynamicUserTask dynamicUserTask = new DynamicUserTask();
        DocType docType = DocType.GENERAL;
        String group = String.valueOf(new Random().nextInt());

        dynamicUserTask.getCandidateGroups().add("management");
        dynamicUserTask.setIndex(0);
        dynamicUserTask.setDynamicUserTaskType(DynamicUserTaskType.APPROVE_REJECT);
        dynamicUserTasks.add(dynamicUserTask);

        DynamicUserTask dynamicUserTask2 = new DynamicUserTask();
        dynamicUserTask2.getCandidateUsers().add("kermit");
        dynamicUserTask2.setDynamicUserTaskType(DynamicUserTaskType.COLLABORATION);
        dynamicUserTask2.setIndex(1);
        dynamicUserTasks.add(dynamicUserTask2);

        BpmnModel model = this.workflowBldr.buildModel(dynamicUserTasks, docType, group);
        assertNotNull(model);

        InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
        File f = new File("target/generated_ping_test_2_tasks.png");
        FileUtils.copyInputStreamToFile(in, f);
        IOUtils.closeQuietly(in);
        LOG.debug("image copied to {}", f.getAbsolutePath());
    }

    @Test
    @DirtiesContext
    public void testUpdateDynamicTasks() throws IOException {
        List<DynamicUserTask> dynamicUserTasks = Lists.newArrayList();
        DynamicUserTask dynamicUserTask = new DynamicUserTask();
        DocType bookReport = DocType.BOOK_REPORT;
        String group = "engineering";

        dynamicUserTask.getCandidateGroups().add(group);
        dynamicUserTask.setIndex(0);
        dynamicUserTask.setDynamicUserTaskType(DynamicUserTaskType.APPROVE_REJECT);
        dynamicUserTasks.add(dynamicUserTask);

        DynamicUserTask dynamicUserTask2 = new DynamicUserTask();
        dynamicUserTask2.getCandidateUsers().add("kermit");
        dynamicUserTask2.setDynamicUserTaskType(DynamicUserTaskType.COLLABORATION);
        dynamicUserTask2.setIndex(1);
        dynamicUserTasks.add(dynamicUserTask2);

        ProcessDefinition pd = this.workflowService.findProcDef(bookReport, group);
        assertNotNull(pd);
        List<DynamicUserTask> tasks = this.workflowBldr.getDynamicTasks(pd);
        assertTrue(tasks.size() == 4);

        ProcessDefinition updatedProcDef = this.workflowBldr.updateDynamicTasks(bookReport, group, dynamicUserTasks);
        assertNotNull(updatedProcDef);

        tasks = this.workflowBldr.getDynamicTasks(updatedProcDef);
        assertTrue(tasks.size() == 2);

        BpmnModel bpmnModel = this.repositoryService.getBpmnModel(updatedProcDef.getId());

        InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(bpmnModel);
        FileUtils.copyInputStreamToFile(in, new File("target/testUpdateWorkflow_2_tasks.png"));
        IOUtils.closeQuietly(in);
        LOG.debug("image copied to target/testUpdateWorkflow_2_tasks.png");

        byte[] bytes = new BpmnXMLConverter().convertToXML(bpmnModel, "UTF-8");
        FileUtils.writeByteArrayToFile(new File("target/testKUpdateWorkflow_2_tasks.xml"), bytes);
        LOG.debug("xml copied to target/testUpdateWorkflow_2_tasks.xml");
    }


    @Test
    @DirtiesContext
    public void testCreateGroupWorkflow() throws IOException {
        ProcessDefinition procDef = this.workflowBldr.createGroupWorkflow(DocType.BOOK_REPORT, "foo");
        LOG.debug(procDef.getKey());
        assertNotNull(procDef);
        BpmnModel model = repositoryService.getBpmnModel(procDef.getId());
        assertNotNull(model);
        InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
        FileUtils.copyInputStreamToFile(in, new File("target/testCreateGroupWorkflow.png"));
        IOUtils.closeQuietly(in);
        LOG.debug("Images generated to target/testCreateGroupWorkflow.png");
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidDocType() {
        this.workflowBldr.createGroupWorkflow(DocType.UNIT_TEST_NO_EXIST, "sales");
        fail("Should not get here");
    }

    @Test(expected = IllegalStateException.class)
    public void testExistingGroup() {
        this.workflowBldr.createGroupWorkflow(DocType.BOOK_REPORT, "engineering");
        fail("Should not get here");
    }

    @Test
    @DirtiesContext
    public void testGetDynamicTasks() {
        String key = WFConstants.createProcId(DocType.BOOK_REPORT, "engineering");
        ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery().
                processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY).
                processDefinitionKey(key).latestVersion().singleResult();
        assertNotNull(processDefinition);

        List<DynamicUserTask> dynamicTasks = this.workflowBldr.getDynamicTasks(processDefinition);
        LOG.debug("{}",dynamicTasks.size());
        assertTrue(dynamicTasks.size() == 4);
        assertTrue(dynamicTasks.get(0).getId().startsWith(WFConstants.TASK_ID_DOC_COLLABORATE));
        assertTrue(dynamicTasks.get(1).getId().startsWith(WFConstants.TASK_ID_DOC_COLLABORATE));
        assertTrue(dynamicTasks.get(2).getId().startsWith(WFConstants.TASK_ID_DOC_APPROVAL));
        assertTrue(dynamicTasks.get(3).getId().startsWith(WFConstants.TASK_ID_DOC_APPROVAL));

    }
}
