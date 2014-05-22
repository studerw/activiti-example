package com.studerw.activiti.document;

import com.studerw.activiti.model.Document;
import com.studerw.activiti.model.UserForm;
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
@RequestMapping("/document")
public class DocumentController {
    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    UserService userService;
    @Autowired
    DocumentService docService;

    @ModelAttribute
    public void addUserInfo(ModelMap model, HttpServletRequest request){
        UserDetails user = userService.currentUser();
        model.addAttribute("userDetails", user);
        model.addAttribute("userName", user.getUsername());
        List<Group> groups = userService.getAssignmentGroups(request.getRemoteUser());
        model.addAttribute("groups", groups);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value="/list.htm", method = RequestMethod.GET)
	public String getDocuments(ModelMap model, HttpServletRequest request) {
        model.addAttribute("documents", this.docService.getGroupDocumentsByUser(request.getRemoteUser()));
		return "document/list";
	}

    @RequestMapping(value="/add.htm", method = RequestMethod.GET)
    public String addDocument(ModelMap model, HttpServletRequest request) {
        model.addAttribute("document", newDocument());
        return "document/add";
    }

    @RequestMapping(value="/add.htm", method = RequestMethod.POST)
    public String submitDocument(@Valid @ModelAttribute Document document,
                                 BindingResult result,
                                 final RedirectAttributes redirectAttributes,
                                 HttpServletRequest request) {
        log.debug("submitting document: " + document);
        if (result.hasErrors()) {
            return "document/add";
        }
        this.docService.createDocument(document);
        redirectAttributes.addFlashAttribute("msg", "Your Document has been submitted for approval<br/>" +
                "You will receive alerts when it has been reviewed");
        return "redirect:/document/list.htm";
    }


    @RequestMapping(value="/view.htm", method = RequestMethod.GET)
    public String viewDocument(ModelMap modelMap,
                               @RequestParam(value = "id", required = true) String id,
                               HttpServletRequest request) {
        log.debug("viewing doc {} ", id);
        Document doc = docService.getDocument(id);
        modelMap.addAttribute("document", doc);
        return "document/view";
    }

    private Document newDocument(){
        UserDetails user = userService.currentUser();
        Document document = new Document();
        document.setAuthor(user.getUsername());
        document.setCreatedDate(new Date());
        return document;
    }

}
