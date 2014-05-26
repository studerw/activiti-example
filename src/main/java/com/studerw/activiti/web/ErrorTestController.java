package com.studerw.activiti.web;

import com.studerw.activiti.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
/**
 * Few controller methods to test ContentNegotiationManagement wired
 * up in mvc-dispatcher-servlet.xml
 */
public class ErrorTestController {
    private static final Logger log = LoggerFactory.getLogger(ErrorTestController.class);

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error(ModelMap model, HttpServletRequest request) {
        if (true) {
            throw new RuntimeException("threw exception for testing");
        }
        return "blah";
    }

    @RequestMapping(value = "/noerror", method = RequestMethod.GET)
    public String noError(ModelMap model, HttpServletRequest request) {
        model.addAttribute("response", new Response(true, "no particular message"));
        return "error";
    }

    @RequestMapping(value = "/noerrornoview", method = RequestMethod.GET)
    public void noErrorNoView(ModelMap model, HttpServletRequest request) {
        model.addAttribute("response", new Response(true, "no particular message - no error no view name"));

    }

}

