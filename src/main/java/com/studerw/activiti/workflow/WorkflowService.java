package com.studerw.activiti.workflow;

import com.google.common.collect.Lists;
import com.studerw.activiti.model.document.DocType;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Various methods to find process definitions by group and {@link com.studerw.activiti.model.document.DocType}.
 * Be aware that all BPMN docs must have a namepsace equal to {@code studerw.com} or whatever is set in the
 * {@code WFConstants} class.
 *
 * @author William Studer
 *         Date: 5/29/14
 */
@Service("workflowService")
public class WorkflowService {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowService.class);

    @Autowired protected RuntimeService runtimeService;
    @Autowired protected RepositoryService repoSrvc;
    @Autowired ApplicationContext appContext;

    /**
     * @param processId the process <strong>Definition</strong> Id - NOT the process Instance Id.
     * @return png image of diagram - nothing highlighted since this is the process definition - not a specific instance.
     */
    public byte[] getProcessDefinitionDiagram(String processId) throws IOException {
        ProcessDefinition pd =
                this.repoSrvc.createProcessDefinitionQuery().processDefinitionKey(processId).latestVersion().singleResult();
        LOG.debug("Getting process diagram for processId: {}", pd.getId());
        BpmnModel bpmnModel = repoSrvc.getBpmnModel(pd.getId());
        new BpmnAutoLayout(bpmnModel).execute();
        try (InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(bpmnModel)) {
            //InputStream in = new DefaultProcessDiagramGenerator().generatePngDiagram(model);
            byte[] bytes = IOUtils.toByteArray(in);
            LOG.debug("Got bytes of size: {}", bytes.length);
            return bytes;
        }
    }

    /**
     * @param docId The document id.
     * @return png image of diagram with current activity highlighted.
     */
    public byte[] getActiveDocumentDiagram(String docId) throws IOException {
        LOG.debug("getting active diagram for doc: {}", docId);
        //http://forums.activiti.org/content/process-diagram-highlighting-current-process
        ProcessInstance pi =
                runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(docId).singleResult();

        RepositoryServiceImpl impl = (RepositoryServiceImpl) repoSrvc;
        ProcessDefinitionEntity pde = (ProcessDefinitionEntity) impl.getDeployedProcessDefinition(pi.getProcessDefinitionId());
        BpmnModel bpmnModel = repoSrvc.getBpmnModel(pde.getId());
        new BpmnAutoLayout(bpmnModel).execute();
        try (InputStream in = new DefaultProcessDiagramGenerator().generateDiagram(bpmnModel, "png", runtimeService.getActiveActivityIds(pi.getProcessInstanceId()))) {
//        InputStream in = this.appContext.getResource("classpath:800x200.png").getInputStream();
            byte[] bytes = IOUtils.toByteArray(in);
            LOG.debug("Got bytes of size: " + bytes.length);
            return bytes;
        }
    }

    /**
     * @param docType
     * @param group
     * @return true if the specific {@code DocType} and Group workflow exists, false if not
     * e.g. process Id of deployed definition would equal {@code BOOK_REPORT_engineering}
     */
    public boolean groupWorkflowExists(DocType docType, String group) {
        LOG.debug("Checking for workflow exists of doctype={} and group={}", docType.name(), group);
        String processIdStr = WFConstants.createProcId(docType, group);
        return (this.repoSrvc.createProcessDefinitionQuery().processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY).
                processDefinitionKey(processIdStr).latestVersion().singleResult()) != null;
    }

    /**
     *
     * @return the list (possibly empty) of DocTypes that have at least a base type (i.e. group = NONE) defined.
     */
    public List<DocType> findExistingBaseDocTypes() {
        List<DocType> docTypes = Lists.newArrayList();
        String likeQuery = String.format("%s%s%s", "%", WFConstants.PROCESS_GROUP_DIVIDER, WFConstants.WORKFLOW_GROUP_NONE);
        LOG.debug("using likeQuery for baseTypes = {}", likeQuery);
                List < ProcessDefinition > processDefinitions = this.repoSrvc.createProcessDefinitionQuery().
                        processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY).
                        latestVersion().
                        processDefinitionKeyLike(likeQuery).list();
        LOG.debug("Base type = {}", String.valueOf(docTypes));
        for (ProcessDefinition processDefinition : processDefinitions) {
            String[] parsed = this.parseProcessId(processDefinition.getKey());
            DocType docType = null;
            try {
                docType = DocType.valueOf(parsed[0]);

            }
            catch(Exception e) {
                LOG.error("Invalid docType detected in workflow (ignoring): {}", parsed[0]);
            }
            if (docType != null){
                docTypes.add(docType);

            }
        }
        LOG.debug("Found baseDocTypes: {}", docTypes);
        return docTypes;
    }



    /**
     * @param docType
     * @return true if the base workflow for the {@code DocType} exists. Essentially, this is a process definition
     * with the {@code processId=DOC_TYPE_NONE} and the namespace set to default namespace
     * @see {@link com.studerw.activiti.workflow.WFConstants#NAMESPACE_CATEGORY}
     */
    public boolean baseDocTypeWorkflowExists(DocType docType) {
        LOG.debug("Checking for base workflow exists of doctype={}.", docType.name());
        String processIdStr = WFConstants.createProcId(docType, WFConstants.WORKFLOW_GROUP_NONE);
        LOG.info("search for base doc workflow using processId={}", processIdStr);
        return this.repoSrvc.createProcessDefinitionQuery().processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY).
                processDefinitionKey(processIdStr).latestVersion().singleResult() != null;
    }

    /**
     * @param docType
     * @return latest process definition for the given doc type and group or null if none exits.
     */
    public ProcessDefinition findProcDefByDocTypeAndGroup(DocType docType, String group) {
        LOG.debug("Checking for workflow exists of doctype={} and group={}", docType.name(), group);
        String processIdStr = WFConstants.createProcId(docType, group);
        ProcessDefinition pd = this.repoSrvc.createProcessDefinitionQuery().processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY).
                processDefinitionKey(processIdStr).latestVersion().singleResult();
        return pd;
    }

    /**
     * @param docType
     * @return the base workflow for a given document type (i.e. the process id of the process is {@code DocType_NONE})
     * or null if no base document exists.
     */
    public ProcessDefinition findBaseProcDef(DocType docType) {
        LOG.debug("Checking for base workflow exists of doctype={}", docType.name());
        String processIdStr = WFConstants.createProcId(docType, WFConstants.WORKFLOW_GROUP_NONE);
        ProcessDefinition pd = this.repoSrvc.createProcessDefinitionQuery().processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY).
                processDefinitionKey(processIdStr).latestVersion().singleResult();
        return pd;
    }

    /**
     * <p/>
     * This is a convenience method that will try for the most specific workflow (group and docType),
     * but will fall back to just general docType if no group workflow exists.
     *
     * @param docType
     * @return latest process definition for the given group and/or docType or null if neither (i.e. no group and also no docType) exits.
     */
    public ProcessDefinition findProcDef(DocType docType, String group) {
        ProcessDefinition pd = this.findProcDefByDocTypeAndGroup(docType, group);
        if (pd == null) {
            LOG.debug("no group workflow exists of doctype={} and group={} -> checking for base wf.", docType.name(), group);
            pd = this.findBaseProcDef(docType);
        }
        return pd;
    }

    /**
     * @param onlyLatestVersion retrieve only the latest version of each Process Definition
     * @return all process definitions in the Activiti workflow
     */
    //TODO this should be protected by admin only
    public List<ProcessDefinition> getAllProcDefs(boolean onlyLatestVersion) {
        LOG.debug("Lookign up all process definitions with latestVersion={}", onlyLatestVersion);
        ProcessDefinitionQuery query = repoSrvc.createProcessDefinitionQuery().processDefinitionCategory(WFConstants.NAMESPACE_CATEGORY);
        if (onlyLatestVersion) {
            query.latestVersion();
        }
        List<ProcessDefinition> definitions = query.list();
        LOG.debug("Found {} definitions.", definitions.size());
        return definitions;
    }

    public void updateWorkflow(BpmnModel model, String group) {
        String modelName = String.format("%s-doc-approve-model.bpmn", group);
        String deployName = String.format("Group %s Document Approve", group);

        LOG.info("updating doc approval for group: {}", group);
        Deployment deployment = this.repoSrvc.createDeployment()
                .addBpmnModel(modelName, model).name(deployName)
                .deploy();
    }

    /**
     * @param businessKey the document Id as returned from DAO classes
     * @return the associated ProcessInstance or null if one does not exist
     */
    public ProcessInstance findProcessInstanceByBusinessKey(String businessKey) {
        return runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
    }


    protected String[] parseProcessId(String processId) {
        if (!processId.contains(WFConstants.PROCESS_GROUP_DIVIDER)) {
            throw new IllegalArgumentException("Invalid processId (must be in form: <DocType>_<group|NONE> ");
        }
        int index = processId.lastIndexOf(WFConstants.PROCESS_GROUP_DIVIDER);

        String docType = processId.substring(0, index);
        String group = processId.substring(index + 1, processId.length());
        LOG.debug("{} - {}", docType, group);
        Assert.hasText(docType);
        Assert.hasText(group);
        return new String[]{docType, group};
    }
    /**
     * @param docType
     * @return returns the matching {@code ProcessDefinition} based on the namespace (i.e. category) which must
     * be a valid {@link com.studerw.activiti.model.document.DocType} or null if no definition exists.
     */
    /*public ProcessDefinition findDefinitionByDocType(DocType docType) {
        LOG.debug("searching for process definition for docType={}", docType);
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
        LOG.debug("building approval list for procDef: " + procId);
        ProcessDefinition pd =
                this.repoSrvc.createProcessDefinitionQuery().processDefinitionKey(procId).latestVersion().singleResult();
        BpmnModel model = this.repoSrvc.getBpmnModel(pd.getId());
        org.activiti.bpmn.model.Process process = model.getProcesses().get(0);

        SubProcess sub = (SubProcess) process.getFlowElement(Workflow.SUBPROCESS_ID_DYNAMIC);
        LOG.debug(sub.getName());
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
