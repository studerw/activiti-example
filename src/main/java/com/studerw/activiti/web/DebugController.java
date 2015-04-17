package com.studerw.activiti.web;

import com.studerw.activiti.util.SpringContextListener;
import com.studerw.activiti.workflow.WorkflowService;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@Controller
public class DebugController extends BaseController{
    private static final Logger LOG = LoggerFactory.getLogger(DebugController.class);

    @Autowired SpringContextListener springContextListener;
    @Autowired WorkflowService workflowService;

    @ModelAttribute
    public void addModelInfo(ModelMap model, HttpServletRequest request) {
        super.addModelInfo(model, request);
        Authentication auth = (Authentication) request.getUserPrincipal();
        Set<String> authorities = AuthorityUtils.authorityListToSet(auth.getAuthorities());
        model.addAttribute("authorities", authorities);
        model.addAttribute("appContexts", springContextListener.getAppContexts());
        model.addAttribute("procDefs", workflowService.getAllProcDefs(false));
    }

    @RequestMapping(value = "/debug.htm", method = RequestMethod.GET)
    public String error(ModelMap model, HttpServletRequest request) {
        return "debug";
    }

}

