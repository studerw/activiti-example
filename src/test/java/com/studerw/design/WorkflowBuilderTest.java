package com.studerw.design;

import com.studerw.activiti.util.Workflow;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: studerw
 * Date: 5/25/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/testAppContext.xml"})
public class WorkflowBuilderTest {
    private static final Logger log = Logger.getLogger(WorkflowBuilderTest.class);
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    HistoryService historyService;
    @Autowired
    IdentityService identityService;
    @Autowired
    RepositoryService repositoryService;

    @Test
    public void testRepoService() throws IOException {
        List<ProcessDefinition> pds = this.repositoryService.createProcessDefinitionQuery().list();
        log.debug("Number of pds: " + pds.size());
        for(ProcessDefinition pd: pds){
            BpmnModel model = repositoryService.getBpmnModel(pd.getId());
            InputStream in = ProcessDiagramGenerator.generatePngDiagram(model);
            FileUtils.copyInputStreamToFile(in, new File("target/" + pd.getName() + "_diagram.png"));
            IOUtils.closeQuietly(in);
        }
    }

    @Test
    public void testBuildDefault() throws Exception {
    }


}
