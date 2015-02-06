/*
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

import static org.junit.Assert.*;

*/
/**
 * @author William Studer
 *         Date: 5/25/14
 *//*

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/testAppContext.xml"})
public class DeprecatedBuilderTest {
    private static final Logger LOG = LoggerFactory.getLogger(DeprecatedBuilderTest.class);
    @Autowired public WorkflowBuilder workflowBldr;
    @Autowired RuntimeService runtimeService;
    @Autowired TaskService taskService;
    @Autowired HistoryService historyService;
    @Autowired IdentityService identityService;
    @Autowired RepositoryService repositoryService;
    @Autowired WorkflowService workflowService;


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

        BpmnModel model = workflowBldr.buildModel(dynamicUserTasks, DocType.BOOK_REPORT, "engineering");
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
        BpmnModel model = workflowBldr.buildModel(dynamicUserTasks, DocType.BOOK_REPORT, "engineering");
        assertNotNull("model should be valid", model);

        Process proc = model.getMainProcess();
        String deployId = this.repositoryService.createDeployment()
                .addBpmnModel(proc.getId() + ".bpmn", model).name("Dynamic Process Deployment - " + proc.getId()).deploy().getId();
        ProcessDefinition updatedProcDef = workflowService.findProcDefByDocTypeAndGroup(DocType.BOOK_REPORT, "engineering");

        new BpmnAutoLayout(model).execute();

        Assert.notNull(updatedProcDef, "something went wrong creating the new processDefinition: " + proc.getId());

//        BpmnModel model = workflowBldr.buildModel(userTasks, DocType.BOOK_REPORT, "engineering");
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


}
*/
