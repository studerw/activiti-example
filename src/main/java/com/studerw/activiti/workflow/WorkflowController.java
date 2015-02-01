package com.studerw.activiti.workflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studerw.activiti.model.Response;
import com.studerw.activiti.model.document.DocType;
import com.studerw.activiti.model.workflow.DynamicUserTask;
import com.studerw.activiti.model.workflow.DynamicUserTaskType;
import com.studerw.activiti.web.BaseController;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/workflow")
public class WorkflowController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowController.class);

    @Autowired protected WorkflowService workflowSrvc;
    @Autowired protected WorkflowBuilder workflowBldr;
    protected ObjectMapper objectMapper = new ObjectMapper();


    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @Override
    @ModelAttribute
    public void addModelInfo(ModelMap model, HttpServletRequest request)  {
        super.addModelInfo(model, request);
        List<Group> groups = userService.getAllAssignmentGroups();
        model.addAttribute("groups", groups);
        //model.addAttribute("defaultDocProcId", "FOO");//TODOWorkflow.PROCESS_ID_DOC_APPROVAL);
        model.addAttribute("docTypes", this.workflowSrvc.findExistingBaseDocTypes());

        List<DynamicUserTaskType> taskTypes = DynamicUserTaskType.asList();
        model.addAttribute("dynamicTaskTypes", taskTypes);
        try {
            String json = objectMapper.writeValueAsString(taskTypes);
            model.addAttribute("dynamicTaskTypesJson", taskTypes);
        }
        catch(JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }

    }


    @RequestMapping(value = "/index.htm", method = RequestMethod.GET)
    public String index(ModelMap model) {
        return "workflow/index";
    }

    @RequestMapping(value = "/tasks/{docType}/{group}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<String>> userTasksByGroup(
            @RequestBody List<DynamicUserTask> dynamicUserTasks,
            @PathVariable(value = "group") String group,
            @PathVariable(value = "docType") DocType docType) throws InterruptedException {
        LOG.debug(dynamicUserTasks.toString());
        BpmnModel model = this.workflowBldr.documentWithTasks(dynamicUserTasks, docType, group);
        this.workflowSrvc.updateWorkflow(model, group);

        //wait for the model diagram to catch up (maybe)
        Response res = new Response(true, group);
        return new ResponseEntity<Response<String>>(res, HttpStatus.OK);
    }

    @RequestMapping(value = "/tasks/{docType}/{group}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<List<DynamicUserTask>>> dynamicTasksByDocAndGroup
            (@PathVariable(value = "group") String group,
             @PathVariable(value = "docType") DocType docType) {

        LOG.debug("finding dynamic tasks for docType = {} and group = {}", docType, group);
        List<DynamicUserTask> dynamicUserTasks = null;

        ProcessDefinition procDef = this.workflowSrvc.findProcDef(docType, group);
        if (procDef == null) {
            String errMsg = String.format("There is no defined workgroup for docType: %s", docType.name());
            Response res = new Response(false, errMsg);
            return new ResponseEntity<Response<List<DynamicUserTask>>>(res, HttpStatus.OK);
        }
        LOG.debug("returning json response of {} approvals", dynamicUserTasks.size());
        Response res = new Response(true, group, dynamicUserTasks);
        return new ResponseEntity<Response<List<DynamicUserTask>>>(res, HttpStatus.OK);
    }



}
