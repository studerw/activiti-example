package com.studerw.activiti.workflow;

import com.studerw.activiti.model.document.DocType;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.*;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/testAppContext.xml"})
public class WorkflowParserTest {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowParserTest.class);

    @Autowired RepositoryService repoSrvc;
    @Autowired RuntimeService runtimeService;

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testGetTasksFromProcDef() throws Exception {
        String key = WFConstants.createProcId(DocType.BOOK_REPORT, "engineering");
        LOG.debug("Using key: {}", key);
        ProcessDefinition def = this.repoSrvc.createProcessDefinitionQuery().
                processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY).processDefinitionKey(key).latestVersion().singleResult();
        assertNotNull(def);
        ProcessDefinitionEntity pde = (ProcessDefinitionEntity) def;
        assertNotNull(pde);

        BpmnModel model = this.repoSrvc.getBpmnModel(def.getId());
        assertNotNull(model);
        Artifact artifact = model.getArtifact(WFConstants.SUBPROCESS_ID_DYNAMIC);
        org.activiti.bpmn.model.Process proc = model.getMainProcess();
        assertNotNull(proc);

        //InputStream in = repoSrvc.getProcessDiagram(def.getId());
        //assertNotNull(in);
        SubProcess subProcess = (SubProcess) proc.getFlowElement(WFConstants.SUBPROCESS_ID_DYNAMIC);

        assertNotNull(subProcess);
        Collection<FlowElement> flowElements = subProcess.getFlowElements();
        for (FlowElement flowElement : flowElements) {
            LOG.debug("FlowElement: {}", flowElement.getId());
        }

//        proc.findFlowElementsInSubProcessOfType(, FlowNode.class);

    }

    @Test
    public void copyDocType() throws IOException {
        String key = WFConstants.createProcId(DocType.BOOK_REPORT, "engineering");
        LOG.debug("Using key: {}", key);
        ProcessDefinition def = this.repoSrvc.createProcessDefinitionQuery().
                processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY).processDefinitionKey(key).latestVersion().singleResult();
        assertNotNull(def);
        ProcessDefinitionEntity pde = (ProcessDefinitionEntity) def;
        assertNotNull(pde);

        BpmnModel orig = this.repoSrvc.getBpmnModel(def.getId());
        org.activiti.bpmn.model.Process proc = orig.getMainProcess();

        BpmnModel clone = new BpmnModel();
        clone.setTargetNamespace(WFConstants.NAMESPACE_CATEGORY);
        proc.setId(WFConstants.createProcId(DocType.BOOK_REPORT, "blah"));
        proc.setName(DocType.BOOK_REPORT.name() + " for group: blah");
        clone.addProcess(proc);

        new BpmnAutoLayout(clone).execute();
        String procId = WFConstants.createProcId(DocType.BOOK_REPORT, "blah");


        String deployId = this.repoSrvc.createDeployment()
                .addBpmnModel("dynamic-model.bpmn", clone).name("Dynamic Process Deployment").deploy().getId();

        List<Deployment> deployments = repoSrvc.createDeploymentQuery().list();

        ProcessDefinition processDefinition = this.repoSrvc.createProcessDefinitionQuery().
                processDefinitionKey(procId).latestVersion().singleResult();

        Assert.assertNotNull(processDefinition);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(procId);
        assertNotNull(processInstance);


        List<ProcessDefinition> procDefs = this.repoSrvc.createProcessDefinitionQuery().latestVersion().list();
        for (ProcessDefinition procDef : procDefs) {
            LOG.debug(procDef.getKey());
        }


        InputStream in = this.repoSrvc.getProcessDiagram(processDefinition.getId());
        assertNotNull(in);
        FileUtils.copyInputStreamToFile(in, new File("target/diagram.png"));

        InputStream processBpmn = this.repoSrvc
                .getResourceAsStream(deployId, "dynamic-model.bpmn");
        FileUtils.copyInputStreamToFile(processBpmn,
                new File("target/" + procId + ".bpmn20.xml"));
        IOUtils.closeQuietly(in);
        //InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(clone);
        //this.repoSrvc.createDeployment().category(WFConstants.NAMESPACE_CATEGORY).addBpmnModel(
    }
}
