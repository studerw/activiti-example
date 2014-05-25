package com.studerw.activiti.alert;

import com.studerw.activiti.document.DocumentService;
import com.studerw.activiti.model.Document;
import com.studerw.activiti.model.HistoricTask;
import com.studerw.activiti.task.LocalTaskService;
import com.studerw.activiti.user.UserService;
import org.activiti.engine.identity.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class AlertController {
    private static final Logger log = LoggerFactory.getLogger(AlertController.class);

    @Autowired
    UserService userService;
    @Autowired
    AlertService alertService;

    @ModelAttribute
    public void addUserInfo(ModelMap model, HttpServletRequest request) {
        UserDetails user = userService.currentUser();
        model.addAttribute("userDetails", user);
        model.addAttribute("userName", user.getUsername());
        List<Group> groups = userService.getAssignmentGroups(request.getRemoteUser());
        model.addAttribute("groups", groups);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "/alerts.htm", method = RequestMethod.GET)
    public String getAlerts(ModelMap model, HttpServletRequest request) {
        model.addAttribute("alerts", alertService.readActiveAlertsByUser((String)model.get("userName")));
        return "alerts";
    }

    @RequestMapping(value = "/add.htm", method = RequestMethod.GET)
    public String addDocument(ModelMap model, HttpServletRequest request) {
        return "document/add";
    }
}
