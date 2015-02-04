package com.studerw.activiti.document;

import com.studerw.activiti.model.*;
import com.studerw.activiti.task.LocalTaskService;
import com.studerw.activiti.web.BaseController;
import org.activiti.engine.identity.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/document")
public class DocumentController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    DocumentService docService;
    @Autowired
    LocalTaskService localTaskSrvc;

    @Override
    @ModelAttribute
    public void addModelInfo(ModelMap model, HttpServletRequest request) {
        super.addModelInfo(model, request);
        List<Group> groups = userService.getAssignmentGroups(request.getRemoteUser());
        model.addAttribute("groups", groups);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "/list.htm", method = RequestMethod.GET)
    public String getDocuments(ModelMap model, HttpServletRequest request) {
        model.addAttribute("documents", this.docService.getGroupDocumentsByUser(currentUserName()));
        return "document/list";
    }
}
