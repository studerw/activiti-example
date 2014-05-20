package com.studerw.activiti.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/index.htm")
public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model, HttpServletRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = request.getRemoteUser();
		model.addAttribute("message", "Hello world!");
        model.addAttribute("userName", userName);
        model.addAttribute("userDetails", userDetails);
		return "index";
	}
}
