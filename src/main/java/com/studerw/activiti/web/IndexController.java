package com.studerw.activiti.web;

import com.studerw.activiti.task.LocalTaskService;
import org.activiti.engine.identity.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/index.htm")
public class IndexController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    LocalTaskService localTaskSrvc;

    @Override
    @ModelAttribute
    public void addModelInfo(ModelMap model, HttpServletRequest request) {
        super.addModelInfo(model, request);
        List<Group> groups = userService.getAssignmentGroups(currentUserName());
        model.addAttribute("groups", groups);

        int taskCount = localTaskSrvc.getTasks(currentUserName()).size();
        model.addAttribute("taskCount", taskCount);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap model, HttpServletRequest request) {
        return "index";
    }
}
