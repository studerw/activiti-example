package com.studerw.activiti.design;

import com.google.common.collect.Lists;
import com.studerw.activiti.model.Approval;
import com.studerw.activiti.util.Workflow;
import com.studerw.activiti.workflow.WorkflowBuilder;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
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
 * User: studerw
 * Date: 5/25/14
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
            BpmnModel model = repositoryService.getBpmnModel(pd.getId());
            InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
            FileUtils.copyInputStreamToFile(in, new File("target/" + pd.getName() + "_diagram.png"));
            IOUtils.closeQuietly(in);
        }
    }

    @Test
    public void testBuildDefault() {
        BpmnModel model = workflowBldr.defaultDocumentApprove();
        Process process = model.getProcesses().get(0);
        SubProcess sub = (SubProcess) process.getFlowElement(Workflow.SUB_PROC_ID_DOC_APPROVAL);
        log.debug(sub.getName());
        Collection<FlowElement> flowElements = sub.getFlowElements();
        List<UserTask> userTasks = Lists.newArrayList();
        for (FlowElement el : flowElements) {
            log.debug(el.getClass().getName() + " -- " + el.getId());
            if (el.getClass().equals(org.activiti.bpmn.model.UserTask.class)) {
                userTasks.add((UserTask) (el));
            }
        }

        int i = 1;
        for (UserTask uTask : userTasks) {
            Approval approval = new Approval();
            approval.setPosition(i);
            i++;

            approval.setCandidateGroups(Lists.newArrayList(uTask.getCandidateGroups()));
            approval.setCandidateUsers(Lists.newArrayList(uTask.getCandidateUsers()));
        }
    }


    @Test
    public void testBuildWF() throws IOException {
        List<Approval> approvals = Lists.newArrayList();

        Approval approval = new Approval();
        approval.getCandidateGroups().add("engineering");
        approvals.add(approval);

        Approval approval2 = new Approval();
        approval2.getCandidateUsers().add("kermit");
        approvals.add(approval2);

        BpmnModel model = workflowBldr.documentApprove(approvals, "engineering");

        InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
        FileUtils.copyInputStreamToFile(in, new File("target/some_group_diagram.png"));
        IOUtils.closeQuietly(in);
    }

    @Test
    public void testBuildDefaultWF() throws IOException {
        BpmnModel model = workflowBldr.defaultDocumentApprove();
        BpmnXMLConverter converter = new BpmnXMLConverter();
        byte[] bytes = converter.convertToXML(model);
        System.out.println(new String(bytes, "UTF-8"));
        InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
        FileUtils.copyInputStreamToFile(in, new File("target/default_diagram.png"));
        IOUtils.closeQuietly(in);
    }
}
