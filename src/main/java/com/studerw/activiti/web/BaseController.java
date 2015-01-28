package com.studerw.activiti.web;

import com.studerw.activiti.alert.AlertService;
import com.studerw.activiti.model.Alert;
import com.studerw.activiti.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BaseController {
    @Autowired protected AlertService alertService;
    @Autowired protected UserService userService;

    @Value("${jdbc.type}")
    protected String jdbcType;

    @ModelAttribute
    public void addModelInfo(ModelMap model, HttpServletRequest request) {
        UserDetails user = userService.currentUser();
        model.addAttribute("userDetails", user);
        //add the jdbcType (e.g. Postgres, H2) to the footer for dev purposes.
        model.addAttribute("jdbcType", jdbcType);
        List<Alert> alerts = alertService.readActiveAlertsByUser(user.getUsername());
        model.addAttribute("alerts", alerts);
    }

    protected String currentUserName() {
        return userService.currentUser().getUsername();
    }

}
