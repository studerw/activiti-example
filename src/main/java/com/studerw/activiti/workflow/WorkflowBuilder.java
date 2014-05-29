package com.studerw.activiti.workflow;

import com.google.common.collect.Lists;
import com.studerw.activiti.util.Workflow;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: studerw
 * Date: 5/29/14
 */
@Service("workflowBuilder")
public class WorkflowBuilder {

    @Autowired
    RuntimeService runtimeService;
    @Autowired
    RepositoryService repositoryService;

    BpmnModel defaultDocumentApprove(){
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
        userTask.setId("approveDocUserTask1");
        userTask.setName("Approve Document");
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

}
