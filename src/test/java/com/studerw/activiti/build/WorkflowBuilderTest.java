package com.studerw.activiti.build;

import com.google.common.collect.Lists;
import com.studerw.activiti.model.document.DocType;
import com.studerw.activiti.model.workflow.UserTask;
import com.studerw.activiti.model.workflow.UserTaskType;
import com.studerw.activiti.workflow.WFConstants;
import com.studerw.activiti.workflow.WorkflowBuilder;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SubProcess;
import org.activiti.engine.*;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * @author William Studer
 *         Date: 5/25/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/testAppContext.xml"})
public class WorkflowBuilderTest {
    private static final Logger log = LogManager.getLogger(WorkflowBuilderTest.class);
    @Autowired public WorkflowBuilder workflowBldr;
    @Autowired RuntimeService runtimeService;
    @Autowired TaskService taskService;
    @Autowired HistoryService historyService;
    @Autowired IdentityService identityService;
    @Autowired RepositoryService repositoryService;

    @Test
    public void testRepoService() throws IOException {
        List<ProcessDefinition> pds = this.repositoryService.createProcessDefinitionQuery().list();
        log.debug("Number of pds: " + pds.size());
        for (ProcessDefinition pd : pds) {

//            BpmnModel model = repositoryService.getBpmnModel(pd.getId());
//            InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
            InputStream in = repositoryService.getProcessDiagram(pd.getId());
            FileUtils.copyInputStreamToFile(in, new File("target/" + pd.getName() + "_diagram.png"));
            IOUtils.closeQuietly(in);
        }
    }

    @Test
    public void testBuildDefault() {
        BpmnModel model = workflowBldr.defaultDocument("foo");
        Process process = model.getProcesses().get(0);
        SubProcess sub = (SubProcess) process.getFlowElement(WFConstants.SUBPROCESS_ID_DYNAMIC);
        log.debug(sub.getName());
        Collection<FlowElement> flowElements = sub.getFlowElements();
        List<org.activiti.bpmn.model.UserTask> userTasks = Lists.newArrayList();
        for (FlowElement el : flowElements) {
            log.debug(el.getClass().getName() + " -- " + el.getId());
            if (el.getClass().equals(org.activiti.bpmn.model.UserTask.class)) {
                userTasks.add((org.activiti.bpmn.model.UserTask) (el));
            }
        }
    }


    @Test
    public void testBuildWFWithUserTasks() throws IOException {
        List<UserTask> userTasks = Lists.newArrayList();
        UserTask userTask = new UserTask();
        userTask.getCandidateGroups().add("engineering");
        userTask.setPosition(1);
        userTask.setUserTaskType(UserTaskType.APPROVE_REJECT);
        userTasks.add(userTask);

        UserTask userTask2 = new UserTask();
        userTask2.getCandidateUsers().add("kermit");
        userTask2.setUserTaskType(UserTaskType.COLLABORATION);
        userTask.setPosition(2);
        userTasks.add(userTask2);

        BpmnModel model = workflowBldr.documentWithTasks(userTasks, DocType.BOOK_REPORT, "engineering");

        InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
        FileUtils.copyInputStreamToFile(in, new File("target/some_group_diagram.png"));
        IOUtils.closeQuietly(in);
    }

    @Test
    public void testBuildDefaultWF() throws IOException {
        BpmnModel model = workflowBldr.defaultDocument("foo");
        BpmnXMLConverter converter = new BpmnXMLConverter();
        byte[] bytes = converter.convertToXML(model);
        System.out.println(new String(bytes, "UTF-8"));
        InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
        FileUtils.copyInputStreamToFile(in, new File("target/default_diagram.png"));
        IOUtils.closeQuietly(in);
    }
}
