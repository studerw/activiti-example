package com.studerw.activiti.workflow;

import com.google.common.collect.Lists;
import com.studerw.activiti.model.document.DocType;
import com.studerw.activiti.model.workflow.DynamicUserTask;
import com.studerw.activiti.model.workflow.DynamicUserTaskType;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Used to build and modify workflows for addition of dynamic tasks
 *
 * @author William Studer
 *         Date: 5/29/14
 */
@Service("workflowBuilder")
public class WorkflowBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowBuilder.class);

    /**
     * Build the minimal base document definition needed for dynamic tasks ({@code NONE} group is used).
     * The ID is set to {@code WFConstants.WORKFLOW_GROUP_NONE} and
     * the category (i.e. Namespace) set to {@code DocType.GENERAL};
     *
     * @param name
     * @return BpmnModel
     */
    public BpmnModel defaultDocument(String name) {
        BpmnModel model = new BpmnModel();
        model.setTargetNamespace(WFConstants.NAMESPACE_CATEGORY);
        org.activiti.bpmn.model.Process process = new Process();
        process.setId(String.format("%s_%s", DocType.GENERAL.name(), WFConstants.WORKFLOW_GROUP_NONE));
        process.setName(name);
        model.addProcess(process);

        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");

        process.addFlowElement(startEvent);

        org.activiti.bpmn.model.UserTask submitTask = new org.activiti.bpmn.model.UserTask();
        submitTask.setId("submitDocUserTask");
        submitTask.setName("Submit Document to Workflow");
        process.addFlowElement(submitTask);
        process.addFlowElement(createSequenceFlow(startEvent.getId(), submitTask.getId()));

        ErrorEventDefinition errorDef = new ErrorEventDefinition();
        errorDef.setErrorCode("errorDocRejected");

        SubProcess sub = createEmptyDynamicSubProcess(errorDef);
        process.addFlowElement(sub);

        process.addFlowElement(createSequenceFlow(submitTask.getId(), sub.getId()));

        BoundaryEvent boundaryEvent = new BoundaryEvent();
        boundaryEvent.setId("rejectedErrorBoundaryEvent");
        boundaryEvent.setName("Rejected Error Event");
        boundaryEvent.setAttachedToRef(sub);
        boundaryEvent.addEventDefinition(errorDef);
        process.addFlowElement(boundaryEvent);

        process.addFlowElement(createSequenceFlow(boundaryEvent.getId(), submitTask.getId(), "Rejected"));
/*
        ServiceTask pub = new ServiceTask();
        pub.setId("publishDocServiceTask");
        pub.setName("Publish Approved Document");
        pub.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        pub.setImplementation("${documentWorkflow.publish(execution)}");

        process.addFlowElement(pub);
*/

        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");
        process.addFlowElement(endEvent);
        process.addFlowElement(createSequenceFlow(sub.getId(), endEvent.getId()));

//        process.addFlowElement(createSequenceFlow(pub.getId(), endEvent.getId()));

        //Generate graphical information
        //TODOnew BpmnAutoLayout(model).execute();
        return model;
    }

    /**
     * @param dynamicUserTasks
     * @param docType
     * @param group
     * @return fully populated BpmnModel with appropriate ids, namespace, sub process tasks, etc.
     */
    public BpmnModel documentWithTasks(List<DynamicUserTask> dynamicUserTasks, DocType docType, String group) {
        Assert.notNull(docType);
        Assert.hasText(group);
        BpmnModel model = new BpmnModel();
        model.setTargetNamespace(WFConstants.NAMESPACE_CATEGORY);
        org.activiti.bpmn.model.Process process = new Process();
        process.setId(String.format("%s_%s", docType.name(), group));
        process.setName(String.format("Generated workflow for docType=%s and Group=%s", docType.name(), group));

        model.addProcess(process);

        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");
        process.addFlowElement(startEvent);

        org.activiti.bpmn.model.UserTask submitTask = new org.activiti.bpmn.model.UserTask();
        submitTask.setId("submitDocUserTask");
        submitTask.setName("Submit doc to Workflow");
        process.addFlowElement(submitTask);
        process.addFlowElement(createSequenceFlow(startEvent.getId(), submitTask.getId()));

        ErrorEventDefinition errorDef = new ErrorEventDefinition();
        errorDef.setErrorCode("errorDocRejected");

        SubProcess sub = createDynamicSubProcess(dynamicUserTasks, errorDef);
        process.addFlowElement(sub);

        process.addFlowElement(createSequenceFlow(submitTask.getId(), sub.getId()));

        BoundaryEvent boundaryEvent = new BoundaryEvent();
        boundaryEvent.setId("rejectedErrorBoundaryEvent");
        boundaryEvent.setName("Rejected Error Event");
        boundaryEvent.setAttachedToRef(sub);
        boundaryEvent.addEventDefinition(errorDef);
        process.addFlowElement(boundaryEvent);

        process.addFlowElement(createSequenceFlow(boundaryEvent.getId(), submitTask.getId(), "Rejected"));
/*
        ServiceTask pub = new ServiceTask();
        pub.setId("publishDocServiceTask");
        pub.setName("Publish Approved Document");
        pub.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        pub.setImplementation("${documentWorkflow.publish(execution)}");

        process.addFlowElement(pub);
        process.addFlowElement(createSequenceFlow(sub.getId(), pub.getId()));
*/

        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");

        process.addFlowElement(endEvent);
        process.addFlowElement(createSequenceFlow(sub.getId(), endEvent.getId()));

        //Generate graphical information
        //new BpmnAutoLayout(model).execute();TODO
        return model;
    }


    protected SubProcess createEmptyDynamicSubProcess(ErrorEventDefinition errorDef) {
        SubProcess sub = new SubProcess();
        sub.setId(WFConstants.SUBPROCESS_ID_DYNAMIC);
        sub.setName(WFConstants.SUBPROCESS_NAME_DYNAMIC);

        StartEvent start = new StartEvent();
        start.setId("dynamic_sub_process_start_event");
        start.setName("Start Dynamic SubProcess");
        sub.addFlowElement(start);

        EndEvent end = new EndEvent();
        end.setId("dynamic_sub_process_end_event");
        end.setName("End Dynamic SubProcess");
        sub.addFlowElement(end);

        EndEvent errorEnd = new EndEvent();
        errorEnd.setId("rejectedErrorEndEvent");
        errorEnd.setName("ErrorEnd");
        errorEnd.addEventDefinition(errorDef);
        sub.addFlowElement(errorEnd);

        sub.addFlowElement(createSequenceFlow(start.getId(), end.getId()));

        return sub;

    }

    protected SubProcess createDynamicSubProcess(List<DynamicUserTask> dynamicUserTasks, ErrorEventDefinition errorDef) {
        SubProcess sub = new SubProcess();
        sub.setId(WFConstants.SUBPROCESS_ID_DYNAMIC);
        sub.setName(WFConstants.SUBPROCESS_NAME_DYNAMIC);

        StartEvent start = new StartEvent();
        start.setId("dynamic_sub_process_start_event");
        start.setName("Start Dynamic SubProcess");
        sub.addFlowElement(start);

        EndEvent end = new EndEvent();
        end.setId("dynamic_sub_process_end_event");
        end.setName("End Dynamic SubProcess");
        sub.addFlowElement(end);

        EndEvent errorEnd = new EndEvent();
        errorEnd.setId("rejectedErrorEndEvent");
        errorEnd.setName("ErrorEnd");
        errorEnd.addEventDefinition(errorDef);
        sub.addFlowElement(errorEnd);

        if (dynamicUserTasks.isEmpty()) {
            sub.addFlowElement(createSequenceFlow(start.getId(), end.getId()));
            return sub;
        }

        List<DynamicUserTask> collaborations = Lists.newArrayList();
        List<DynamicUserTask> approvals = Lists.newArrayList();
        for (DynamicUserTask ut : dynamicUserTasks) {
            if (DynamicUserTaskType.APPROVE_REJECT.equals(ut.getDynamicUserTaskType())) {
                approvals.add(ut);
            } else if (DynamicUserTaskType.COLLABORATION.equals(ut.getDynamicUserTaskType())) {
                collaborations.add(ut);
            } else {
                throw new IllegalArgumentException("Invalid user task type: " + ut.getDynamicUserTaskType());
            }
        }

        List<org.activiti.bpmn.model.UserTask> created = Lists.newArrayList();
        int approvalCount = 1;
        int collabCount = 1;
        boolean first = true;
        for (DynamicUserTask from : dynamicUserTasks) {
            if (DynamicUserTaskType.APPROVE_REJECT.equals(from.getDynamicUserTaskType())) {
                org.activiti.bpmn.model.UserTask approvalTask = approvalTask(sub, errorEnd, from, approvalCount, approvals.size());
                created.add(approvalTask);
                approvalCount++;
            } else if (DynamicUserTaskType.COLLABORATION.equals(from.getDynamicUserTaskType())) {
                org.activiti.bpmn.model.UserTask collabTask = collaborationTask(sub, from, collabCount, collaborations.size());
                created.add(collabTask);
                collabCount++;
            } else {
                throw new IllegalArgumentException("Invalid user task type: " + from.getDynamicUserTaskType());
            }

        }

        sub.addFlowElement(createSequenceFlow(start.getId(), created.get(0).getId()));
        int lastIndex = created.size() - 1;
        sub.addFlowElement(createSequenceFlow(created.get(lastIndex).getId(), end.getId()));
        return sub;
    }


    protected org.activiti.bpmn.model.UserTask approvalTask(SubProcess sub, EndEvent errorEnd, DynamicUserTask from, int current, int total) {
        org.activiti.bpmn.model.UserTask to = new org.activiti.bpmn.model.UserTask();
        to.setId(String.format("%s_%d", WFConstants.TASK_ID_DOC_APPROVAL, current));
        if (StringUtils.isBlank(from.getName())) {
            to.setName(String.format("Approve Document (%d / %d)", current, total));
        } else {
            to.setName(from.getName());
        }

        ActivitiListener onCreateApproval = new ActivitiListener();
        onCreateApproval.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        onCreateApproval.setImplementation("${docWorkflowListener.onCreateApproval(execution, task)}");
        onCreateApproval.setEvent("create");
        to.setTaskListeners(Lists.newArrayList(onCreateApproval));


        if (from.getCandidateGroups().isEmpty() && from.getCandidateUsers().isEmpty()) {
            throw new IllegalArgumentException("user task does not have any candidate users / groups assigned");
/*
                ActivitiListener taskListener = new ActivitiListener();
                taskListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
                taskListener.setImplementation("${documentWorkflow.setAssignee(execution, task)}");
                taskListener.setEvent("create");
                userTask.setTaskListeners(Lists.newArrayList(taskListener));
*/
        }
        to.setCandidateGroups(from.getCandidateGroups());
        to.setCandidateUsers(from.getCandidateUsers());

        sub.addFlowElement(to);

        ExclusiveGateway gw = new ExclusiveGateway();
        gw.setId(String.format("exclusivegateway_approval_%d_of_%d", current, total));
        gw.setName(String.format("Exclusive Approval Gateway %d of %d", current, total));
        sub.addFlowElement(gw);
        sub.addFlowElement(createSequenceFlow(to.getId(), gw.getId()));

        //-------------------------------------------------------------
        SequenceFlow rejectedFlow = new SequenceFlow();
        rejectedFlow.setId(String.format("docRejectedSubFlow_%d_of_%d", current, total));
        rejectedFlow.setName(String.format("Doc Rejected %d of %d", current, total));
        rejectedFlow.setSourceRef(gw.getId());
        rejectedFlow.setTargetRef(errorEnd.getId());

        ActivitiListener rejectedListener = new ActivitiListener();
        rejectedListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        rejectedListener.setImplementation("${documentWorkflow.onRejected(execution)}");
        rejectedListener.setEvent("take");
        rejectedFlow.setExecutionListeners(Lists.newArrayList(rejectedListener));
        rejectedFlow.setConditionExpression("${approved == false}");
        sub.addFlowElement(rejectedFlow);

        //-----------------------------------------------
        SequenceFlow approvedFlow = new SequenceFlow();
        approvedFlow.setId(String.format("docApprovedSubFlow_%d_of_%d", current, total));
        approvedFlow.setName(String.format("Doc Approved %d of %d", current, total));
        approvedFlow.setSourceRef(gw.getId());

        ActivitiListener approvedListener = new ActivitiListener();
        approvedListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        approvedListener.setImplementation("${documentWorkflow.onApproved(execution)}");
        approvedListener.setEvent("take");
        approvedFlow.setExecutionListeners(Lists.newArrayList(approvedListener));
        approvedFlow.setConditionExpression("${approved == true}");
        sub.addFlowElement(approvedFlow);

        return to;
    }

    protected org.activiti.bpmn.model.UserTask collaborationTask(SubProcess sub, DynamicUserTask from, int current, int total) {
        org.activiti.bpmn.model.UserTask to = new org.activiti.bpmn.model.UserTask();
        to.setId(String.format("%s_%d", WFConstants.TASK_ID_DOC_COLLABORATE, current));
        if (StringUtils.isBlank(from.getName())) {
            to.setName(String.format("Document Collaboration (%d / %d)", current, total));
        } else {
            to.setName(from.getName());
        }

        ActivitiListener onCreate = new ActivitiListener();
        onCreate.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        onCreate.setImplementation("${docWorkflowListener.onCreateCollaborate(execution, task)}");
        onCreate.setEvent("create");

        ActivitiListener onComplete = new ActivitiListener();
        onComplete.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        onComplete.setImplementation("${docWorkflowListener.onCompleteCollaborate(execution, task)}");
        onComplete.setEvent("complete");

        to.setTaskListeners(Lists.newArrayList(onCreate, onComplete));
        return to;
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

    /*protected UserTask fromUserTask(org.activiti.bpmn.model.UserTask userTask, int position) {
        UserTask uTask = new UserTask();
        uTask.setIndex(position);
        uTask.setCandidateGroups(Lists.newArrayList(userTask.getCandidateGroups()));
        uTask.setCandidateUsers(Lists.newArrayList(userTask.getCandidateUsers()));
        uTask.setName(userTask.getName());
        uTask.setId(userTask.getId());
        return uTask;
    }*/
}
