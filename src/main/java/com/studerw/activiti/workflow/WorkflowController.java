package com.studerw.activiti.workflow;

import com.google.common.collect.Lists;
import com.studerw.activiti.model.Approval;
import com.studerw.activiti.model.Response;
import com.studerw.activiti.model.TaskForm;
import com.studerw.activiti.util.Workflow;
import com.studerw.activiti.web.BaseController;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.identity.Group;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.io.InputStream;
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
        model.addAttribute("defaultDocProcId", Workflow.PROCESS_ID_DOC_APPROVAL);
    }


    @RequestMapping(value = "/index.htm", method = RequestMethod.GET)
    public String index(ModelMap model,
                        HttpServletRequest request) {
        return "workflow/index";
    }

    @RequestMapping(value = "/approvals/{group}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<String>>approvalsByGroupPost(@RequestBody List<Approval> approvals,
                        @PathVariable(value="group") String group,
                        HttpServletRequest request) throws InterruptedException {

        log.debug(approvals.toString());
        BpmnModel model = this.workflowBldr.documentApprove(approvals, group);
        this.workflowSrvc.updateGroupDocApproveWorkflow(model, group);

        //wait for the model diagram to catch up (maybe)
        Response res = new Response(true, group);
        return new ResponseEntity<Response<String>>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "/approvals/{group}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<Approval>>> approvalsByGroup(@PathVariable(value="group") String group,
                                                                     HttpServletRequest request) {
        String groupId = group;
        if(!workflowSrvc.groupDocApproveWorkflowExists(group)){
            groupId = null;
        }

        List<Approval> approvals = workflowBldr.getDocApprovalsByGroup(groupId);
        log.debug("returning json response of {} approvals",  approvals.size());
        Response res = new Response(true, groupId,  approvals);
        return new ResponseEntity<Response<List<Approval>>>(res, HttpStatus.OK);
    }


    @RequestMapping(value = "/diagrams/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDiagram(
            @PathVariable("id") String id,
            HttpServletRequest request)  throws IOException {

        log.debug("fetching diagram for process {}", id);

        byte[] bytes = workflowSrvc.getProcessDefinitionDiagram(id);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "image/png");
        return new ResponseEntity<byte[]>(bytes, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/document/{docId}/diagram", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getActiveDocDiagram(
            @PathVariable("docId") String docId,
            HttpServletRequest request)  throws IOException {

        log.debug("fetching diagram for docId{}", docId);

        byte[] bytes = workflowSrvc.getActiveDocumentDiagram(docId);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "image/png");
        List<Integer> integers = Lists.newArrayList();
        return new ResponseEntity<byte[]>(bytes, responseHeaders, HttpStatus.OK);

    }
}
