package com.studerw.activiti.workflow;

import com.studerw.activiti.model.document.DocType;
import org.activiti.bpmn.model.*;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/testAppContext.xml"})
public class WorkflowParserTest {
    private static final Logger LOG = LogManager.getLogger(WorkflowParserTest.class);
    @Autowired WorkflowParser parser;
    @Autowired RepositoryService repoSrvc;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetTasksFromProcDef() throws Exception {
        String key = WFConstants.createProcId(DocType.BOOK_REPORT, "engineering");
        LOG.debug("Using key: {}", key);
        ProcessDefinition def = this.repoSrvc.createProcessDefinitionQuery().
                processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY).processDefinitionKey(key).latestVersion().singleResult();
        assertNotNull(def);
        ProcessDefinitionEntity pde = (ProcessDefinitionEntity)def;
        assertNotNull(pde);

        BpmnModel model = this.repoSrvc.getBpmnModel(def.getId());
        assertNotNull(model);
        Artifact artifact = model.getArtifact(WFConstants.SUBPROCESS_ID_DYNAMIC);
        org.activiti.bpmn.model.Process proc = model.getMainProcess();
        assertNotNull(proc);

        //InputStream in = repoSrvc.getProcessDiagram(def.getId());
        //assertNotNull(in);
        SubProcess subProcess = (SubProcess)proc.getFlowElement(WFConstants.SUBPROCESS_ID_DYNAMIC);

        assertNotNull(subProcess);

//        proc.findFlowElementsInSubProcessOfType(, FlowNode.class);

    }
}