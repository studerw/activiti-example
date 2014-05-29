package com.studerw.activiti.workflow;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: studerw
 * Date: 5/29/14
 */
@Service("workflowService")
public class WorkflowService {
    private static final Logger log = LoggerFactory.getLogger(WorkflowService.class);
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    RepositoryService repoSrvc;

    /**
     *
     * @param processId the process <strong>Definition</strong> Id - NOT the process Instance Id.
     * @return png image of diagram - nothing highlighted since this is the process definition - not a specific instance.
     */
    byte[] getProcessDefinitionDiagram(String processId) throws IOException {
        ProcessDefinition pd =
                this.repoSrvc.createProcessDefinitionQuery().processDefinitionKey(processId).latestVersion().singleResult();
        log.debug("Getting process diagram for processId: " + pd.getId());
            BpmnModel model = repoSrvc.getBpmnModel(pd.getId());
        InputStream in = ProcessDiagramGenerator.generatePngDiagram(model);
        byte[] bytes = IOUtils.toByteArray(in);
        IOUtils.closeQuietly(in);
        log.debug("Got bytes of size: " + bytes.length);
        return bytes;
    }

    byte[] getActiveDocumentDiagram(String docId) throws IOException {
        log.debug("getting active diagram for doc: " + docId);
        //http://forums.activiti.org/content/process-diagram-highlighting-current-process
        ProcessInstance pi =
                runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(docId).singleResult();

        RepositoryServiceImpl impl = (RepositoryServiceImpl)repoSrvc;
        ProcessDefinitionEntity pde = (ProcessDefinitionEntity) impl.getDeployedProcessDefinition(pi.getProcessDefinitionId());
        BpmnModel bpmnModel = repoSrvc.getBpmnModel(pde.getId());
        InputStream in = ProcessDiagramGenerator.generateDiagram(bpmnModel, "png", runtimeService.getActiveActivityIds(pi.getProcessInstanceId()));
        byte[] bytes = IOUtils.toByteArray(in);
        IOUtils.closeQuietly(in);
        log.debug("Got bytes of size: " + bytes.length);
        return bytes;
    }
}
