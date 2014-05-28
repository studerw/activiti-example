package com.studerw.design;

import com.google.common.collect.Lists;
import com.studerw.activiti.util.Workflow;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.constants.BpmnXMLConstants;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: studerw
 * Date: 5/25/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/testAppContext.xml"})
public class DocApproveTest {
    private static final Logger log = Logger.getLogger(DocApproveTest.class);
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
    public void testDynamicDeploy() throws Exception {
        String fakeGroup = "fakeGroup";
        String procId = Workflow.PROCESS_ID_DOC_APPROVAL + "-" + fakeGroup;
        // 1. Build up the model from scratch
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        model.addProcess(process);
        process.setId(procId);

        process.addFlowElement(createStartEvent());

        //pub.addAttribute(createExpression("documentWorkflow.publish(execution)"));
        //pub.setImplementation("${documentWorkflow.publish(execution)}");
//        pub.setExtensionId(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE,
//                BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_EXTENSIONID);


        UserTask submitTask = new UserTask();
        submitTask.setId("submitDocUserTask");
        submitTask.setName("Submit Document for Approval");
        process.addFlowElement(submitTask);
        process.addFlowElement(createSequenceFlow("start", submitTask.getId()));
        SubProcess sub = createSubProcess();
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

        process.addFlowElement(createEndEvent());
        process.addFlowElement(createSequenceFlow(pub.getId(), "end"));

        // 2. Generate graphical information
        new BpmnAutoLayout(model).execute();

        // 3. Deploy the process to the engine
        Deployment deployment = this.repositoryService.createDeployment()
                .addBpmnModel("dynamic-model.bpmn", model).name("Dynamic process deployment")
                .deploy();

        // 4. Start a process instance
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(procId);

        // 6. Save process diagram to a file
        InputStream processDiagram = repositoryService.getProcessDiagram(processInstance.getProcessDefinitionId());
        FileUtils.copyInputStreamToFile(processDiagram, new File("target/diagram.png"));

        // 7. Save resulting BPMN xml to a file
        InputStream processBpmn = repositoryService.getResourceAsStream(deployment.getId(), "dynamic-model.bpmn");
        FileUtils.copyInputStreamToFile(processBpmn, new File("target/process.bpmn20.xml"));
    }

    protected UserTask createUserTask(String id, String name, String assignee) {
        UserTask userTask = new UserTask();
        userTask.setName(name);
        userTask.setId(id);
        userTask.setAssignee(assignee);
        return userTask;
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


    protected StartEvent createStartEvent() {
        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");
        return startEvent;
    }

    protected EndEvent createEndEvent() {
        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");
        return endEvent;
    }

    protected ExtensionAttribute createExpression(String expression) {
        ExtensionAttribute exp = new ExtensionAttribute();

        exp.setNamespacePrefix(BpmnXMLConstants.ACTIVITI_EXTENSIONS_PREFIX);
        exp.setNamespace(BpmnXMLConstants.ACTIVITI_EXTENSIONS_NAMESPACE);
        exp.setName(BpmnXMLConstants.ATTRIBUTE_TASK_SERVICE_EXPRESSION);
        exp.setValue(expression);
        return exp;

    }

    protected SubProcess createSubProcess() {
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
