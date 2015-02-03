package com.studerw.activiti.build;

import com.google.common.collect.Lists;
import com.studerw.activiti.model.document.DocType;
import com.studerw.activiti.model.workflow.DynamicUserTask;
import com.studerw.activiti.model.workflow.DynamicUserTaskType;
import com.studerw.activiti.workflow.WFConstants;
import com.studerw.activiti.workflow.WorkflowBuilder;
import com.studerw.activiti.workflow.WorkflowService;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author William Studer
 *         Date: 5/25/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/testAppContext.xml"})
public class WorkflowBuilderTest {
    private static final Logger LOG = LogManager.getLogger(WorkflowBuilderTest.class);
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
    public void testBuildDefault() throws IOException {
        BpmnModel model = workflowBldr.defaultDocument("foo");
        Process process = model.getProcesses().get(0);
        SubProcess sub = (SubProcess) process.getFlowElement(WFConstants.SUBPROCESS_ID_DYNAMIC);
        LOG.debug(sub.getName());
        Collection<FlowElement> flowElements = sub.getFlowElements();
        List<org.activiti.bpmn.model.UserTask> userTasks = Lists.newArrayList();
        for (FlowElement el : flowElements) {
            LOG.debug(el.getClass().getName() + " -- " + el.getId());
            if (el.getClass().equals(org.activiti.bpmn.model.UserTask.class)) {
                userTasks.add((org.activiti.bpmn.model.UserTask) (el));
            }
        }

        String deployId = this.repositoryService.createDeployment()
                .addBpmnModel(process.getId() + ".bpmn", model).name("Dynamic Process Deployment - " + process.getId()).deploy().getId();
        ProcessDefinition updatedProcDef = workflowService.findProcDefByDocTypeAndGroup(DocType.UNIT_TEST_NO_EXIST, WFConstants.WORKFLOW_GROUP_NONE);

        new BpmnAutoLayout(model).execute();

        Assert.notNull(updatedProcDef, "something went wrong creating the new processDefinition: " + process.getId());

        InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
        FileUtils.copyInputStreamToFile(in, new File("target/testbuilddefault.png"));
        IOUtils.closeQuietly(in);
    }


    @Test
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
        dynamicUserTask.setIndex(1);
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
    }

    @Test
    public void testBuildWFWithUserTasks() throws IOException {
        List<DynamicUserTask> dynamicUserTasks = Lists.newArrayList();
        DynamicUserTask dynamicUserTask = new DynamicUserTask();
        dynamicUserTask.getCandidateGroups().add("engineering");
        dynamicUserTask.setIndex(0);
        dynamicUserTask.setDynamicUserTaskType(DynamicUserTaskType.APPROVE_REJECT);
        dynamicUserTasks.add(dynamicUserTask);

        DynamicUserTask dynamicUserTask2 = new DynamicUserTask();
        dynamicUserTask2.getCandidateUsers().add("kermit");
        dynamicUserTask2.setDynamicUserTaskType(DynamicUserTaskType.COLLABORATION);
        dynamicUserTask2.setIndex(1);
        dynamicUserTasks.add(dynamicUserTask2);

        DynamicUserTask dynamicUserTask3 = new DynamicUserTask();
        dynamicUserTask3.getCandidateUsers().add("kermit");
        dynamicUserTask3.setDynamicUserTaskType(DynamicUserTaskType.APPROVE_REJECT);
        dynamicUserTask3.setIndex(2);
        dynamicUserTasks.add(dynamicUserTask3);

        BpmnModel model = workflowBldr.documentWithTasks(dynamicUserTasks, DocType.BOOK_REPORT, "engineering");
        assertNotNull("model should be valid", model);

        Process proc = model.getMainProcess();
        String deployId = this.repositoryService.createDeployment()
                .addBpmnModel(proc.getId() + ".bpmn", model).name("Dynamic Process Deployment - " + proc.getId()).deploy().getId();
        ProcessDefinition updatedProcDef = workflowService.findProcDefByDocTypeAndGroup(DocType.BOOK_REPORT, "engineering");


        Assert.notNull(updatedProcDef, "something went wrong creating the new processDefinition: " + proc.getId());
        BpmnModel bpmnModel = this.repositoryService.getBpmnModel(updatedProcDef.getId());
        InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(bpmnModel);
        FileUtils.copyInputStreamToFile(in, new File("target/some_group_diagram.png"));
        IOUtils.closeQuietly(in);
        LOG.debug("image copied to target/testBuildWFWithUserTasks.png");

//
    }

    @Test
    public void testBuildWFWithOneUserTask() throws IOException {
        List<DynamicUserTask> dynamicUserTasks = Lists.newArrayList();
        DynamicUserTask dynamicUserTask = new DynamicUserTask();
        dynamicUserTask.getCandidateGroups().add("engineering");
        dynamicUserTask.setIndex(0);
        dynamicUserTask.setDynamicUserTaskType(DynamicUserTaskType.APPROVE_REJECT);
        dynamicUserTasks.add(dynamicUserTask);
        BpmnModel model = workflowBldr.documentWithTasks(dynamicUserTasks, DocType.BOOK_REPORT, "engineering");
        assertNotNull("model should be valid", model);

        Process proc = model.getMainProcess();
        String deployId = this.repositoryService.createDeployment()
                .addBpmnModel(proc.getId() + ".bpmn", model).name("Dynamic Process Deployment - " + proc.getId()).deploy().getId();
        ProcessDefinition updatedProcDef = workflowService.findProcDefByDocTypeAndGroup(DocType.BOOK_REPORT, "engineering");

        new BpmnAutoLayout(model).execute();

        Assert.notNull(updatedProcDef, "something went wrong creating the new processDefinition: " + proc.getId());

//        BpmnModel model = workflowBldr.documentWithTasks(userTasks, DocType.BOOK_REPORT, "engineering");
//
//        InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
//        FileUtils.copyInputStreamToFile(in, new File("target/some_group_diagram.png"));
//        IOUtils.closeQuietly(in);
    }


    @Test
    public void testBuildDefaultWF() throws IOException {
        BpmnModel model = workflowBldr.defaultDocument("foo");
        BpmnXMLConverter converter = new BpmnXMLConverter();
        byte[] bytes = converter.convertToXML(model);
        System.out.println(new String(bytes, "UTF-8"));
//        InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
//        FileUtils.copyInputStreamToFile(in, new File("target/default_diagram.png"));
//        IOUtils.closeQuietly(in);
    }

    @Test
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
    public void testGetDynamicTasks(){
        ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery().processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY).
                processDefinitionKey(WFConstants.createProcId(DocType.BOOK_REPORT, "engineering")).latestVersion().singleResult();
        assertNotNull(processDefinition);
        List<DynamicUserTask> dynamicTasks = this.workflowBldr.getDynamicTasks(processDefinition);
        LOG.debug(dynamicTasks.size());
        assertTrue(dynamicTasks.size() == 4);
        assertTrue(dynamicTasks.get(0).getId().startsWith(WFConstants.TASK_ID_DOC_COLLABORATE));
        assertTrue(dynamicTasks.get(1).getId().startsWith(WFConstants.TASK_ID_DOC_COLLABORATE));
        assertTrue(dynamicTasks.get(2).getId().startsWith(WFConstants.TASK_ID_DOC_APPROVAL));
        assertTrue(dynamicTasks.get(3).getId().startsWith(WFConstants.TASK_ID_DOC_APPROVAL));

    }
}
