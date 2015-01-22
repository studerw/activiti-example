package com.studerw.activiti.workflow;

import com.studerw.activiti.model.DocType;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: studerw
 * Date: 5/29/14
 */
@Service("workflowService")
public class WorkflowService {
    private static final Logger log = LoggerFactory.getLogger(WorkflowService.class);
    @Autowired RuntimeService runtimeService;
    @Autowired RepositoryService repoSrvc;

    public static String getApprovalKeyByGroup(String group) {
        return null;//TODOString.format("%s-%s",Workflow.PROCESS_ID_DOC_APPROVAL, group);
    }

    /**
     * @param processId the process <strong>Definition</strong> Id - NOT the process Instance Id.
     * @return png image of diagram - nothing highlighted since this is the process definition - not a specific instance.
     */
    public byte[] getProcessDefinitionDiagram(String processId) throws IOException {
        ProcessDefinition pd =
                this.repoSrvc.createProcessDefinitionQuery().processDefinitionKey(processId).latestVersion().singleResult();
        log.debug("Getting process diagram for processId: " + pd.getId());
        BpmnModel model = repoSrvc.getBpmnModel(pd.getId());
        InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
        byte[] bytes = IOUtils.toByteArray(in);
        IOUtils.closeQuietly(in);
        log.debug("Got bytes of size: " + bytes.length);
        return bytes;
    }

    /**
     * @param docId The document id.
     * @return png image of diagram with current activity highlighted.
     */
    public byte[] getActiveDocumentDiagram(String docId) throws IOException {
        log.debug("getting active diagram for doc: " + docId);
        //http://forums.activiti.org/content/process-diagram-highlighting-current-process
        ProcessInstance pi =
                runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(docId).singleResult();

        RepositoryServiceImpl impl = (RepositoryServiceImpl) repoSrvc;
        ProcessDefinitionEntity pde = (ProcessDefinitionEntity) impl.getDeployedProcessDefinition(pi.getProcessDefinitionId());
        BpmnModel bpmnModel = repoSrvc.getBpmnModel(pde.getId());
        InputStream in = new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, "png", runtimeService.getActiveActivityIds(pi.getProcessInstanceId()));
        byte[] bytes = IOUtils.toByteArray(in);
        IOUtils.closeQuietly(in);
        log.debug("Got bytes of size: " + bytes.length);
        return bytes;
    }

    public boolean groupDocApproveWorkflowExists(String group) {
        String key = getApprovalKeyByGroup(group);
        ProcessDefinition pd = this.repoSrvc.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();
        return pd != null;
    }

    public void updateGroupDocApproveWorkflow(BpmnModel model, String group) {
        String modelName = String.format("%s-doc-approve-model.bpmn", group);
        String deployName = String.format("Group %s Document Approve", group);

        log.info("updating doc approval for group: {}", group);
        Deployment deployment = this.repoSrvc.createDeployment()
                .addBpmnModel(modelName, model).name(deployName)
                .deploy();
    }

    /**
     * @param businessKey the document Id as returned from DAO classes
     * @return the associated ProcessInstance or null if one does not exist
     */
    public ProcessInstance findProcessByBusinessKey(String businessKey) {
        return runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
    }

    /**
     * @param docType
     * @return returns the matching {@code ProcessDefinition} based on the namespace (i.e. category) which must
     * be a valid {@link com.studerw.activiti.model.DocType} or null if no definition exists.
     */
    public ProcessDefinition findDefinitionByDocType(DocType docType) {
        log.debug("searching for process definition for docType={}", docType);
        ProcessDefinition pd = repoSrvc.createProcessDefinitionQuery().processDefinitionCategory(docType.name()).singleResult();
        return pd;

    }
}
