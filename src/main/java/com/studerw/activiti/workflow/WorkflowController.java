package com.studerw.activiti.workflow;

import com.google.common.collect.Lists;
import com.studerw.activiti.model.Response;
import com.studerw.activiti.model.document.DocType;
import com.studerw.activiti.model.workflow.UserTask;
import com.studerw.activiti.model.workflow.UserTaskType;
import com.studerw.activiti.web.BaseController;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.identity.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/workflow")
public class WorkflowController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(WorkflowController.class);

    @Autowired WorkflowService workflowSrvc;
    @Autowired WorkflowBuilder workflowBldr;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @Override
    @ModelAttribute
    public void addModelInfo(ModelMap model, HttpServletRequest request) {
        super.addModelInfo(model, request);
        List<Group> groups = userService.getAllAssignmentGroups();
        model.addAttribute("groups", groups);
        //model.addAttribute("defaultDocProcId", "FOO");//TODOWorkflow.PROCESS_ID_DOC_APPROVAL);
        model.addAttribute("docTypes", DocType.asList());
        model.addAttribute("userTaskTypes", UserTaskType.asList());
    }


    @RequestMapping(value = "/index.htm", method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "workflow/index";
    }

    @RequestMapping(value = "/tasks/{docType}/{group}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<String>> userTasksByGroup(
            @RequestBody List<UserTask> userTasks,
            @PathVariable(value = "group") String group,
            @PathVariable(value = "docType") DocType docType) throws InterruptedException {
        log.debug(userTasks.toString());
        BpmnModel model = this.workflowBldr.documentWithTasks(userTasks, docType, group);
        this.workflowSrvc.updateWorkflow(model, group);

        //wait for the model diagram to catch up (maybe)
        Response res = new Response(true, group);
        return new ResponseEntity<Response<String>>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "/tasks/{docType}/{group}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<UserTask>>> tasksByDocAndGroup
            (@PathVariable(value = "group") String group,
             @PathVariable(value = "docType") DocType docType){
        String groupId = null;
        if (!workflowSrvc.groupWorkflowExists(docType, group)) {
            groupId = null;
        }

        List<UserTask> userTasks = Lists.newArrayList();//TODO workflowBldr.getDocApprovalsByGroup(groupId);
        log.debug("returning json response of {} approvals", userTasks.size());
        Response res = new Response(true, groupId, userTasks);
        return new ResponseEntity<Response<List<UserTask>>>(res, HttpStatus.OK);
    }


    @RequestMapping(value = "/diagrams/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDiagram(
            @PathVariable("id") String id) throws IOException {

        log.debug("fetching diagram for process {}", id);

        byte[] bytes = workflowSrvc.getProcessDefinitionDiagram(id);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "image/png");
        return new ResponseEntity<byte[]>(bytes, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/document/{docId}/diagram", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getActiveDocDiagram(
            @PathVariable("docId") String docId) throws IOException {
        log.debug("fetching diagram for docId{}", docId);

        byte[] bytes = workflowSrvc.getActiveDocumentDiagram(docId);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "image/png");
        return new ResponseEntity<byte[]>(bytes, responseHeaders, HttpStatus.OK);

    }
}
