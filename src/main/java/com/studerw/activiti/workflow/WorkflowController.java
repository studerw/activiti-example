package com.studerw.activiti.workflow;

import com.studerw.activiti.model.Response;
import com.studerw.activiti.web.BaseController;
import org.activiti.engine.identity.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class WorkflowController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(WorkflowController.class);

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
    }


    @RequestMapping(value = "/workflows.htm", method = RequestMethod.GET)
    public String getAlerts(ModelMap model, HttpServletRequest request) {
        return "workflows";
    }
}
