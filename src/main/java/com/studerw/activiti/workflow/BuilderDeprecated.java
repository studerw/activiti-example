package com.studerw.activiti.workflow;

import com.studerw.activiti.model.document.DocType;
import org.activiti.bpmn.model.*;

import com.google.common.collect.Lists;
import com.studerw.activiti.model.document.DocType;
import com.studerw.activiti.model.workflow.DynamicUserTask;
import com.studerw.activiti.model.workflow.DynamicUserTaskType;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * @author studerw
 */
@Deprecated
public class BuilderDeprecated {
    /**
     * Build the minimal base document definition needed for dynamic tasks).
     * The group {@code WFConstants.WORKFLOW_GROUP_NONE} and
     * the docType is set to {@code DocType.GENERAL};
     *
     * @param name
     * @return BpmnModel
     */
/*
    public BpmnModel defaultDocument(String name) {
        BpmnModel model = new BpmnModel();
        model.setTargetNamespace(WFConstants.NAMESPACE_CATEGORY);
        org.activiti.bpmn.model.Process process = new org.activiti.bpmn.model.Process();
        process.setId(WFConstants.createProcId(DocType.UNIT_TEST_NO_EXIST, WFConstants.WORKFLOW_GROUP_NONE));
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
        errorDef.setErrorCode(WFConstants.ERROR_DOC_REJECTED);

        SubProcess sub = createEmptyDynamicSubProcess(errorDef);
        process.addFlowElement(sub);

        process.addFlowElement(createSequenceFlow(submitTask.getId(), sub.getId()));

        BoundaryEvent boundaryEvent = new BoundaryEvent();
        boundaryEvent.setId(WFConstants.REJECTED_BOUNDARY_EVENT_ID);
        boundaryEvent.setName("Rejected Error Event");
        boundaryEvent.setAttachedToRef(sub);
        boundaryEvent.addEventDefinition(errorDef);
        process.addFlowElement(boundaryEvent);

        process.addFlowElement(createSequenceFlow(boundaryEvent.getId(), submitTask.getId(), "Rejected"));
*/
/*
        ServiceTask pub = new ServiceTask();
        pub.setId("publishDocServiceTask");
        pub.setName("Publish Approved Document");
        pub.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
        pub.setImplementation("${documentWorkflow.publish(execution)}");

        process.addFlowElement(pub);
*//*


        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");
        process.addFlowElement(endEvent);
        process.addFlowElement(createSequenceFlow(sub.getId(), endEvent.getId()));

//        process.addFlowElement(createSequenceFlow(pub.getId(), endEvent.getId()));

        //Generate graphical information
        //TODOnew BpmnAutoLayout(model).execute();
        return model;
    }
*/

//    /**
//     * @param dynamicUserTasks
//     * @param docType
//     * @param group
//     * @return fully populated BpmnModel with appropriate ids, namespace, sub process tasks, etc.
//     */
//    public BpmnModel buildModel(List<DynamicUserTask> dynamicUserTasks, DocType docType, String group) {
//        Assert.notNull(docType);
//        Assert.hasText(group);
//        BpmnModel model = new BpmnModel();
//        model.setTargetNamespace(WFConstants.NAMESPACE_CATEGORY);
//        org.activiti.bpmn.model.Process process = new Process();
//        process.setId(String.format("%s_%s", docType.name(), group));
//        process.setName(String.format("Generated workflow for docType=%s and Group=%s", docType.name(), group));
//
//        model.addProcess(process);
//
//        StartEvent startEvent = new StartEvent();
//        startEvent.setId("start");
//        process.addFlowElement(startEvent);
//
//        org.activiti.bpmn.model.UserTask submitTask = new org.activiti.bpmn.model.UserTask();
//        submitTask.setId("submitDocUserTask");
//        submitTask.setName("Submit doc to Workflow");
//        process.addFlowElement(submitTask);
//        process.addFlowElement(createSequenceFlow(startEvent.getId(), submitTask.getId()));
//
//        ErrorEventDefinition errorDef = new ErrorEventDefinition();
//        errorDef.setErrorCode(WFConstants.ERROR_DOC_REJECTED);
//
//        SubProcess sub = createDynamicSubProcess(dynamicUserTasks, errorDef);
//        process.addFlowElement(sub);
//
//        process.addFlowElement(createSequenceFlow(submitTask.getId(), sub.getId()));
//
//        BoundaryEvent boundaryEvent = new BoundaryEvent();
//        boundaryEvent.setId(WFConstants.REJECTED_BOUNDARY_EVENT_ID);
//        boundaryEvent.setName("Rejected Error Event");
//        boundaryEvent.setAttachedToRef(sub);
//        boundaryEvent.addEventDefinition(errorDef);
//        process.addFlowElement(boundaryEvent);
//
//        process.addFlowElement(createSequenceFlow(boundaryEvent.getId(), submitTask.getId(), "Rejected"));
///*
//        ServiceTask pub = new ServiceTask();
//        pub.setId("publishDocServiceTask");
//        pub.setName("Publish Approved Document");
//        pub.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_EXPRESSION);
//        pub.setImplementation("${documentWorkflow.publish(execution)}");
//
//        process.addFlowElement(pub);
//        process.addFlowElement(createSequenceFlow(sub.getId(), pub.getId()));
//*/
//
//        EndEvent endEvent = new EndEvent();
//        endEvent.setId("end");
//
//        process.addFlowElement(endEvent);
//        process.addFlowElement(createSequenceFlow(sub.getId(), endEvent.getId()));
//
//        //Generate graphical information
//        new BpmnAutoLayout(model).execute();
//        return model;
//    }
//

/*
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
*/

}
