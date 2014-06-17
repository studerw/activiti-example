package com.studerw.activiti.workflow;

import com.google.common.collect.Lists;
import com.studerw.activiti.model.Approval;
import com.studerw.activiti.util.Workflow;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * User: studerw
 * Date: 5/29/14
 */
@Service("workflowBuilder")
public class WorkflowBuilder {
    private static final Logger log = LoggerFactory.getLogger(WorkflowBuilder.class);
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    RepositoryService repositoryService;

    public BpmnModel documentApprove(List<Approval> approvals, String group) {
        BpmnModel model = new BpmnModel();
        org.activiti.bpmn.model.Process process = new Process();
        process.setId(Workflow.PROCESS_ID_DOC_APPROVAL + "-" + group);
        process.setName(Workflow.PROCESS_NAME_DOC_APPROVAL);
        model.addProcess(process);


        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");

        process.addFlowElement(startEvent);

        UserTask submitTask = new UserTask();
        submitTask.setId("submitDocUserTask");
        submitTask.setName("Submit Document for Approval");
        process.addFlowElement(submitTask);
        process.addFlowElement(createSequenceFlow(startEvent.getId(), submitTask.getId()));
        SubProcess sub = createApprovalSub(approvals);
        process.addFlowElement(sub);

        process.addFlowElement(createSequenceFlow(submitTask.getId(), sub.getId()));

        BoundaryEvent boundaryEvent = new BoundaryEvent();
        boundaryEvent.setId("rejectedErrorBoundaryEvent");
        boundaryEvent.setName("Rejected Error Event");
        boundaryEvent.setAttachedToRef(sub);
        ErrorEventDefinition errorDef = new ErrorEventDefinition();
        errorDef.setErrorCode("errorDocRejected");
        boundaryEvent.addEventDefinition(errorDef);
        process.addFlowElement(boundaryEvent);

        process.addFlowElement(createSequenceFlow(boundaryEvent.getId(), submitTask.getId(), "Rejected"));
        ServiceTask pub = new ServiceTask();
        pub.setId("publishDocServiceTask");
        pub.setName("Publish Approved Document");
        pub.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        pub.setImplementation("${documentWorkflow.publish(execution)}");

        process.addFlowElement(pub);
        process.addFlowElement(createSequenceFlow(sub.getId(), pub.getId()));

        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");

        process.addFlowElement(endEvent);
        process.addFlowElement(createSequenceFlow(pub.getId(), endEvent.getId()));

        //Generate graphical information
        new BpmnAutoLayout(model).execute();

        return model;
    }

    public BpmnModel defaultDocumentApprove() {
        BpmnModel model = new BpmnModel();
        org.activiti.bpmn.model.Process process = new Process();
        process.setId(Workflow.PROCESS_ID_DOC_APPROVAL);
        process.setName(Workflow.PROCESS_NAME_DOC_APPROVAL);
        model.addProcess(process);

        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");

        process.addFlowElement(startEvent);

        UserTask submitTask = new UserTask();
        submitTask.setId("submitDocUserTask");
        submitTask.setName("Submit Document for Approval");
        process.addFlowElement(submitTask);
        process.addFlowElement(createSequenceFlow(startEvent.getId(), submitTask.getId()));
        SubProcess sub = createDefaultSubProcess();
        process.addFlowElement(sub);

        process.addFlowElement(createSequenceFlow(submitTask.getId(), sub.getId()));

        BoundaryEvent boundaryEvent = new BoundaryEvent();
        boundaryEvent.setId("rejectedErrorBoundaryEvent");
        boundaryEvent.setName("Rejected Error Event");
        boundaryEvent.setAttachedToRef(sub);
        ErrorEventDefinition errorDef = new ErrorEventDefinition();
        errorDef.setErrorCode("errorDocRejected");
        boundaryEvent.addEventDefinition(errorDef);
        process.addFlowElement(boundaryEvent);

        process.addFlowElement(createSequenceFlow(boundaryEvent.getId(), submitTask.getId(), "Rejected"));
        ServiceTask pub = new ServiceTask();
        pub.setId("publishDocServiceTask");
        pub.setName("Publish Approved Document");
        pub.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        pub.setImplementation("${documentWorkflow.publish(execution)}");

        process.addFlowElement(pub);
        process.addFlowElement(createSequenceFlow(sub.getId(), pub.getId()));

        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");

        process.addFlowElement(endEvent);
        process.addFlowElement(createSequenceFlow(pub.getId(), endEvent.getId()));

        //Generate graphical information
        new BpmnAutoLayout(model).execute();

        return model;
    }

    /**
     * @param group - If null is passed, the default document approval workflow will be used.
     * @return a sorted list of approvals contained in the workflow associated with the given group
     */
    public List<Approval> getDocApprovalsByGroup(String group) {
        String base = Workflow.PROCESS_ID_DOC_APPROVAL;
        String procId = StringUtils.isBlank(group) ? base : base + "-" + group;
        log.debug("building approval list for procDef: " + procId);
        ProcessDefinition pd =
                this.repositoryService.createProcessDefinitionQuery().processDefinitionKey(procId).latestVersion().singleResult();
        BpmnModel model = this.repositoryService.getBpmnModel(pd.getId());
        Process process = model.getProcesses().get(0);

        SubProcess sub = (SubProcess) process.getFlowElement(Workflow.SUB_PROC_ID_DOC_APPROVAL);
        log.debug(sub.getName());
        Collection<FlowElement> flowElements = sub.getFlowElements();
        List<UserTask> userTasks = Lists.newArrayList();
        for (FlowElement el : flowElements) {
            if (el.getClass().equals(org.activiti.bpmn.model.UserTask.class)) {
                userTasks.add((UserTask) (el));
            }
        }

        List<Approval> approvals = Lists.newArrayList();
        int i = 1;
        for (UserTask userTask : userTasks) {
            approvals.add(fromUserTask(userTask, i));
            i++;
        }
        return approvals;
    }

    protected SequenceFlow createSequenceFlow(String from, String to) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        return flow;
    }

    protected SequenceFlow createSequenceFlow(String from, String to, String name) {
        SequenceFlow flow = new SequenceFlow();
        flow.setSourceRef(from);
        flow.setTargetRef(to);
        flow.setName(name);
        return flow;
    }

    protected SubProcess createDefaultSubProcess() {
        SubProcess sub = new SubProcess();
        sub.setId("approvalSubProcess");
        sub.setName("Document Approval Subprocess");

        StartEvent start = new StartEvent();
        start.setId("approvalSubProcessStartEvent");
        start.setName("Start Approval SubProcess");
        sub.addFlowElement(start);

        EndEvent end = new EndEvent();
        end.setId("approvalSubProcessEndEvent");
        end.setName("End Approval SubProcess");
        sub.addFlowElement(end);

        EndEvent errorEnd = new EndEvent();
        errorEnd.setId("rejectedErrorEndEvent");
        errorEnd.setName("ErrorEnd");
        ErrorEventDefinition errorDef = new ErrorEventDefinition();
        errorDef.setErrorCode("errorDocRejected");
        errorEnd.addEventDefinition(errorDef);
        sub.addFlowElement(errorEnd);

        UserTask userTask = new UserTask();
        userTask.setId(Workflow.TASK_ID_DOC_APPROVAL);
        userTask.setName("Approve Document (1 / 1)");
        ActivitiListener taskListener = new ActivitiListener();
        taskListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        taskListener.setImplementation("${documentWorkflow.setAssignee(execution, task)}");
        taskListener.setEvent("create");
        userTask.setTaskListeners(Lists.newArrayList(taskListener));
        sub.addFlowElement(userTask);

        ExclusiveGateway gw = new ExclusiveGateway();
        gw.setId("exclusivegateway1");
        gw.setName("Exclusive Gateway");
        sub.addFlowElement(gw);

        SequenceFlow rejectedFlow = new SequenceFlow();
        rejectedFlow.setId("docRejectedSubFlow");
        rejectedFlow.setName("Doc Rejected");
        rejectedFlow.setSourceRef(gw.getId());
        rejectedFlow.setTargetRef(errorEnd.getId());
        ActivitiListener rejectedListener = new ActivitiListener();
        rejectedListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        rejectedListener.setImplementation("${documentWorkflow.rejected(execution)}");
        rejectedListener.setEvent("take");
        rejectedFlow.setExecutionListeners(Lists.newArrayList(rejectedListener));
        rejectedFlow.setConditionExpression("${approved == false}");
        sub.addFlowElement(rejectedFlow);

        SequenceFlow approvedFlow = new SequenceFlow();
        approvedFlow.setId("docApprovedSubFlow");
        approvedFlow.setName("Doc Approved");
        approvedFlow.setSourceRef(gw.getId());
        approvedFlow.setTargetRef(end.getId());
        ActivitiListener approvedListener = new ActivitiListener();
        approvedListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        approvedListener.setImplementation("${documentWorkflow.approved(execution)}");
        approvedListener.setEvent("take");
        approvedFlow.setExecutionListeners(Lists.newArrayList(approvedListener));
        approvedFlow.setConditionExpression("${approved == true}");
        sub.addFlowElement(approvedFlow);

        sub.addFlowElement(createSequenceFlow(start.getId(), userTask.getId()));
        sub.addFlowElement(createSequenceFlow(userTask.getId(), gw.getId()));

        return sub;

    }

    protected SubProcess createApprovalSub(List<Approval> approvals) {
        SubProcess sub = new SubProcess();
        sub.setId(Workflow.SUB_PROC_ID_DOC_APPROVAL);
        sub.setName("Document Approval Subprocess");

        StartEvent start = new StartEvent();
        start.setId("approvalSubProcessStartEvent");
        start.setName("Start Approval SubProcess");
        sub.addFlowElement(start);

        EndEvent end = new EndEvent();
        end.setId("approvalSubProcessEndEvent");
        end.setName("End Approval SubProcess");
        sub.addFlowElement(end);

        EndEvent errorEnd = new EndEvent();
        errorEnd.setId("rejectedErrorEndEvent");
        errorEnd.setName("ErrorEnd");
        ErrorEventDefinition errorDef = new ErrorEventDefinition();
        errorDef.setErrorCode("errorDocRejected");
        errorEnd.addEventDefinition(errorDef);
        sub.addFlowElement(errorEnd);

        if (approvals.isEmpty()) {
            sub.addFlowElement(createSequenceFlow(start.getId(), end.getId()));
            return sub;
        }
        else {
            sub.addFlowElement(createSequenceFlow(start.getId(), "approveDocUserTask_1"));
        }
        int i = 1;
        for (Approval approval : approvals) {
            UserTask userTask = new UserTask();
            if (StringUtils.isBlank(approval.getName())){
                approval.setName(String.format("Approve Document (%d / %d)", i, approvals.size()));
            }
            userTask.setId(String.format("approveDocUserTask_%d", i));
            userTask.setName(approval.getName());
            if (!approval.getCandidateGroups().isEmpty()) {
                userTask.setCandidateGroups(approval.getCandidateGroups());
            }
            if (!approval.getCandidateUsers().isEmpty()) {
                userTask.setCandidateUsers(approval.getCandidateUsers());
            }
            if (approval.getCandidateGroups().isEmpty() && approval.getCandidateUsers().isEmpty()) {
                ActivitiListener taskListener = new ActivitiListener();
                taskListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
                taskListener.setImplementation("${documentWorkflow.setAssignee(execution, task)}");
                taskListener.setEvent("create");
                userTask.setTaskListeners(Lists.newArrayList(taskListener));
            }

            sub.addFlowElement(userTask);

            ExclusiveGateway gw = new ExclusiveGateway();
            gw.setId(String.format("exclusivegateway_%d", i));
            gw.setName(String.format("Exclusive Gateway %d", i));
            sub.addFlowElement(gw);
            sub.addFlowElement(createSequenceFlow(userTask.getId(), gw.getId()));

            SequenceFlow rejectedFlow = new SequenceFlow();
            rejectedFlow.setId(String.format("docRejectedSubFlow_%d", i));
            rejectedFlow.setName(String.format("Doc Rejected %d", i));
            rejectedFlow.setSourceRef(gw.getId());
            rejectedFlow.setTargetRef(errorEnd.getId());
            ActivitiListener rejectedListener = new ActivitiListener();
            rejectedListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
            rejectedListener.setImplementation("${documentWorkflow.rejected(execution)}");
            rejectedListener.setEvent("take");
            rejectedFlow.setExecutionListeners(Lists.newArrayList(rejectedListener));
            rejectedFlow.setConditionExpression("${approved == false}");
            sub.addFlowElement(rejectedFlow);

            SequenceFlow approvedFlow = new SequenceFlow();
            approvedFlow.setId(String.format("docApprovedSubFlow_%d", i));
            approvedFlow.setName(String.format("Doc Approved %d", i));
            approvedFlow.setSourceRef(gw.getId());
            //We're on the last one
            if (i == approvals.size()) {
                approvedFlow.setTargetRef(end.getId());
            } else {
                approvedFlow.setTargetRef(String.format("approveDocUserTask_%d", i + 1));
            }


            ActivitiListener approvedListener = new ActivitiListener();
            approvedListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
            approvedListener.setImplementation("${documentWorkflow.approved(execution)}");
            approvedListener.setEvent("take");
            approvedFlow.setExecutionListeners(Lists.newArrayList(approvedListener));
            approvedFlow.setConditionExpression("${approved == true}");
            sub.addFlowElement(approvedFlow);
            i++;
        }

        return sub;

    }

    protected Approval fromUserTask(UserTask userTask, int position) {
        Approval approval = new Approval();
        approval.setPosition(position);
        approval.setCandidateGroups(Lists.newArrayList(userTask.getCandidateGroups()));
        approval.setCandidateUsers(Lists.newArrayList(userTask.getCandidateUsers()));
        approval.setName(userTask.getName());
        approval.setId(userTask.getId());

        return approval;

    }
}
