package com.studerw.activiti.workflow;

import com.studerw.activiti.model.document.DocType;
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
import java.util.List;

/**
 * @author William Studer
 *         Date: 5/29/14
 */
@Service("workflowService")
public class WorkflowService {
    private static final Logger log = LoggerFactory.getLogger(WorkflowService.class);
    @Autowired RuntimeService runtimeService;
    @Autowired RepositoryService repoSrvc;


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

    /**
     *
     * @param docType
     * @param group
     * @return true if the specific {@code DocType} and Group workflow exists, false if not
     * e.g. process Id of deployed definition would equal {@code BOOK_REPORT_engineering}
     */
    public boolean groupWorkflowExists(DocType docType, String group) {
        log.debug("Checking for workflow exists of doctype={} and group={}", docType.name(), group);
        String processIdStr = String.format("%s_%s", docType.name(), group);
        return (this.repoSrvc.createProcessDefinitionQuery().processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY).
                processDefinitionKey(processIdStr).latestVersion().singleResult()) != null;
    }

    /**
     *
     * @param docType
     * @return true if one or more workflows of the specific {@code DocType} exists, false if not.
     * e.g. process Id of deployed definition(s) would be like {@code BOOK_REPORT_engineering} or just {@code BOOK_REPORT}
     */
    public boolean docTypeWorkflowsExist(DocType docType){
        log.debug("Checking for workflow exists of doctype={}.", docType.name());
        String processIdStr = String.format("%s_%s", docType.name(), "%");
        log.info("using key LIKE={}", processIdStr);
        return !(this.repoSrvc.createProcessDefinitionQuery().processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY).
                processDefinitionKeyLike(processIdStr).latestVersion().list().isEmpty());
    }

    /**
     *
     * @param docType
     * @return List (may be empty) of process definitions for the given doc type.
     */
    public List<ProcessDefinition> findProcDefinitionsByDocType(DocType docType) {
        log.debug("Checking for workflow exists of doctype={}.", docType.name());
        String processIdStr = String.format("%s_%s", docType.name(), "%");
        log.debug("Finding workflows of id: {}", processIdStr);
        List<ProcessDefinition> pds = this.repoSrvc.createProcessDefinitionQuery().processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY).
                processDefinitionKeyLike(processIdStr).
                latestVersion().list();
        log.debug("Found {} of id={}", pds.size(), processIdStr);
        return pds;
    }

    /**
     *
     * @param docType
     * @return true if the base workflow for the {@code DocType} exists. Essentially, this is a process definition
     * with the {@code processId=DOC_TYPE_NONE} and the namespace set to default namespace
     * @see {@link com.studerw.activiti.workflow.WFConstants#NAMESPACE_CATEGORY}
     */
    public boolean baseDocTypeWorkflowExists(DocType docType){
        log.debug("Checking for base workflow exists of doctype={}.", docType.name());
        String processIdStr = String.format("%s_%s", docType.name(), WFConstants.WORKFLOW_GROUP_NONE);
        log.info("search for base doc workflow using processId={}", processIdStr);
        return this.repoSrvc.createProcessDefinitionQuery().processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY).
                processDefinitionKey(processIdStr).latestVersion().singleResult() != null;
    }

    /**
     * @param docType
     * @return latest process definition for the given doc type and group or null if none exits.
     */
    public ProcessDefinition findProcDefByDocTypeAndGroup(DocType docType, String group) {
        log.debug("Checking for workflow exists of doctype={} and group={}", docType.name(), group);
        String processIdStr = String.format("%s_%s", docType.name(), group);
        ProcessDefinition pd = this.repoSrvc.createProcessDefinitionQuery().processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY).
                processDefinitionKey(processIdStr).latestVersion().singleResult();
        return pd;
    }

    public ProcessDefinition findBaseProcDef(DocType docType) {
        log.debug("Checking for base workflow exists of doctype={}", docType.name());
        String processIdStr = String.format("%s_%s", docType.name(), WFConstants.WORKFLOW_GROUP_NONE);
        ProcessDefinition pd = this.repoSrvc.createProcessDefinitionQuery().processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY).
                processDefinitionKey(processIdStr).latestVersion().singleResult();
        return pd;
    }

    /**
     * <p>
     * This is a convenience method that will try for the most specific workflow (group and docType),
     * but will fall back to just general docType if no group workflow exists.
     * @param docType
     * @return latest process definition for the given group and/or docType or null if neither (i.e. no group and also no docType) exits.
     */
    public ProcessDefinition findProcDef(DocType docType, String group) {
        log.debug("Checking for workflow exists of doctype={} and group={}", docType.name(), group);
        ProcessDefinition pd = this.findProcDefByDocTypeAndGroup(docType, group);
        if (pd == null) {
            log.debug("no group workflow exists of doctype={} and group={} -> checking for base wf.", docType.name(), group);
            pd = this.findBaseProcDef(docType);
        }
        return pd;
    }


    public void updateWorkflow(BpmnModel model, String group) {
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
     * be a valid {@link com.studerw.activiti.model.document.DocType} or null if no definition exists.
     */
    /*public ProcessDefinition findDefinitionByDocType(DocType docType) {
        log.debug("searching for process definition for docType={}", docType);
        ProcessDefinition pd = repoSrvc.createProcessDefinitionQuery().processDefinitionCategory(docType.name()).singleResult();
        return pd;

    }
    */
    /**
     * @param group - If no {@code Group} is passed, the default document approval workflow will be used.
     * @return a sorted list of approvals contained in the workflow associated with the given group
     */
    /*public List<UserTask> getDocApprovalsByGroup(String group) {
        String base = "FOO";//TODOWorkflow.PROCESS_ID_DOC_APPROVAL;
        String procId = StringUtils.isBlank(group) ? base : base + "-" + group;
        log.debug("building approval list for procDef: " + procId);
        ProcessDefinition pd =
                this.repoSrvc.createProcessDefinitionQuery().processDefinitionKey(procId).latestVersion().singleResult();
        BpmnModel model = this.repoSrvc.getBpmnModel(pd.getId());
        org.activiti.bpmn.model.Process process = model.getProcesses().get(0);

        SubProcess sub = (SubProcess) process.getFlowElement(Workflow.SUBPROCESS_ID_DYNAMIC);
        log.debug(sub.getName());
        Collection<FlowElement> flowElements = sub.getFlowElements();
        List<org.activiti.bpmn.model.UserTask> userTasks = Lists.newArrayList();
        for (FlowElement el : flowElements) {
            if (el.getClass().equals(org.activiti.bpmn.model.UserTask.class)) {
                userTasks.add((org.activiti.bpmn.model.UserTask) (el));
            }
        }

        List<UserTask> approvals = Lists.newArrayList();
        int i = 1;
        for (org.activiti.bpmn.model.UserTask userTask : userTasks) {
            approvals.add(fromUserTask(userTask, i));
            i++;
        }
        return approvals;
    }
*/
}
