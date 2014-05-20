package com.studerw.activiti.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/document")
public class DocumentController {
    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

    @RequestMapping(value="/list.htm", method = RequestMethod.GET)
	public String getDocuments(ModelMap model, HttpServletRequest request) {
        String userName = request.getRemoteUser();
		model.addAttribute("message", "Hello world!");
        model.addAttribute("userName", userName);
		return "document/list";
	}
}
