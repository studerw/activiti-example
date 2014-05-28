package com.studerw.activiti.document;

import com.studerw.activiti.model.Document;
import com.studerw.activiti.model.HistoricTask;
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
    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

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

    @RequestMapping(value = "/add.htm", method = RequestMethod.GET)
    public String addDocument(ModelMap model, HttpServletRequest request) {
        model.addAttribute("document", newDocument());
        return "document/add";
    }

    @RequestMapping(value = "/add.htm", method = RequestMethod.POST)
    public String submitDocument(@Valid @ModelAttribute Document document,
                                 BindingResult result,
                                 @RequestParam(required = false, value = "isSubmit") boolean isSubmit,
                                 final RedirectAttributes redirectAttributes,
                                 HttpServletRequest request) {
        log.debug("submitting document: " + document);
        if (result.hasErrors()) {
            return "document/add";
        }
        String docId = this.docService.createDocument(document);
        if (isSubmit) {
            log.debug("Submitting for approval docId {}", docId);
            this.docService.submitForApproval(docId);
        }
        if (isSubmit) {
            redirectAttributes.addFlashAttribute("msg", "Your Document has been submitted for approval.<br/>" +
                    "You will receive alerts when it has been reviewed");
        } else {
            redirectAttributes.addFlashAttribute("msg", "Your Document has been Saved");
        }

        return "redirect:/document/list.htm";
    }


    @RequestMapping(value = "/view.htm", method = RequestMethod.POST)
    public String updateDocument(@Valid @ModelAttribute Document document,
                                 @RequestParam(required = false, value = "isSubmit") boolean isSubmit,
                                 BindingResult result,
                                 final RedirectAttributes redirectAttributes,
                                 ModelMap model,
                                 HttpServletRequest request) {
        log.debug("updating document: " + document);
        if (result.hasErrors()) {
            return "document/view";
        }
        this.docService.updateDocument(document);
        if (isSubmit) {
            log.debug("Submitting for approval docId {}", document.getId());
            this.docService.submitForApproval(document.getId());
        }
        if (isSubmit) {
            redirectAttributes.addFlashAttribute("msg", "Your Document has been submitted for approval.<br/>" +
                    "You will receive alerts when it has been reviewed");
        } else {
            redirectAttributes.addFlashAttribute("msg", "Your Document has been Saved");
        }

        return "redirect:/document/list.htm";
    }

    @RequestMapping(value = "/view.htm", method = RequestMethod.GET)
    public String viewDocument(ModelMap model,
                               @RequestParam(value = "id", required = true) String id,
                               final RedirectAttributes redirectAttributes,
                               HttpServletRequest request) {
        log.debug("viewing doc {} ", id);
        Document doc = docService.getDocument(id);
        model.addAttribute("document", doc);
        List<HistoricTask> hts = this.localTaskSrvc.getDocApprovalHistory(id);
        model.addAttribute("historicTasks", hts);
        if (doc.getAuthor().equals(currentUserName()) && doc.isEditable()) {
            return "document/edit";
        } else if (doc.getAuthor().equals(currentUserName())) {
            model.addAttribute("msg", "The document cannot be edited in its current state.");
        } else {
            model.addAttribute("msg", "Only the original author may edit the document");
        }
        return "document/view";
    }

    private Document newDocument() {
        Document document = new Document();
        document.setAuthor(currentUserName());
        document.setCreatedDate(new Date());
        return document;
    }
}
