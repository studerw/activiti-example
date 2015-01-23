package com.studerw.activiti.document;

import com.studerw.activiti.model.document.BookReport;
import com.studerw.activiti.model.document.DocType;
import com.studerw.activiti.model.document.Document;
import com.studerw.activiti.model.task.HistoricTask;
import com.studerw.activiti.task.LocalTaskService;
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
@RequestMapping("/document/bookReport")
public class BookReportController extends DocumentController {
    private static final Logger log = LoggerFactory.getLogger(BookReportController.class);

    @Autowired DocumentService docService;
    @Autowired LocalTaskService localTaskSrvc;

    @Override
    @ModelAttribute
    public void addModelInfo(ModelMap model, HttpServletRequest request) {
        super.addModelInfo(model, request);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "/create.htm", method = RequestMethod.GET)
    public String createForm(ModelMap model) {
        model.addAttribute("bookReport", newBookReport());
        return "document/bookReport/create";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute BookReport bookReport,
                                   BindingResult result,
                                   @RequestParam(required = false, value = "isSubmit") boolean isSubmit,
                                   final RedirectAttributes redirectAttributes,
                                   HttpServletRequest request) {
        log.debug("submitting bookReport: {}", bookReport);

        if (result.hasErrors()) {
            return "document/bookReport/create";
        }
        String docId = this.docService.createDocument(bookReport);

        if (isSubmit) {
            log.debug("Submitting to dynamic workflow docId {}", docId);
            this.docService.submitToWorkflow(docId);
        }
        if (isSubmit) {
            redirectAttributes.addFlashAttribute("msg", "<p>Your Document has been submitted for approval.<p/>" +
                    "<p>You will receive alerts when it has been reviewed.</p>");
        } else {
            redirectAttributes.addFlashAttribute("msg", "Your Document has been Saved");
        }

        return "redirect:/document/list.htm";
    }

//    @RequestMapping(value = "/view.htm", method = RequestMethod.POST)
//    public String updateDocument(@Valid @ModelAttribute Document bookReport,
//                                 @RequestParam(required = false, value = "isSubmit") boolean isSubmit,
//                                 BindingResult result,
//                                 final RedirectAttributes redirectAttributes,
//                                 ModelMap model,
//                                 HttpServletRequest request) {
//        log.debug("updating bookReport: " + bookReport);
//        if (result.hasErrors()) {
//            return "bookReport/view";
//        }
//        this.docService.updateDocument(bookReport);
//        if (isSubmit) {
//            log.debug("Submitting to dynamic workflow docId {}", bookReport.getId());
//            this.docService.submitToWorkflow(bookReport.getId());
//        }
//        if (isSubmit) {
//            redirectAttributes.addFlashAttribute("msg", "<p>Your Document has been submitted for approval.<p/>" +
//                    "<p>You will receive alerts when it has been reviewed.</p>");
//        } else {
//            redirectAttributes.addFlashAttribute("msg", "<p>Your Document has been Saved</p>");
//        }
//
//        return "redirect:/bookReport/list.htm";
//    }
//
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String view(ModelMap model,
                               @PathVariable(value = "id") String id,
                               final RedirectAttributes redirectAttributes,
                               HttpServletRequest request) {
        log.debug("viewing doc {} ", id);
        Document doc = docService.getDocument(id);
        model.addAttribute("document", doc);
        List<HistoricTask> hts = this.localTaskSrvc.getDocApprovalHistory(id);
        model.addAttribute("historicTasks", hts);
        if (doc.getAuthor().equals(currentUserName()) && doc.isEditable()) {
            return "document/bookReport/edit";
        } else if (doc.getAuthor().equals(currentUserName())) {
            model.addAttribute("msg", "The bookReport cannot be edited in its current state.");
        } else {
            model.addAttribute("msg", "Only the original author may edit the bookReport.");
        }
        return "document/bookReport/view";
    }

    @RequestMapping(value="/{id}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute BookReport bookReport,
                         BindingResult result,
                         @RequestParam(required = false, value = "isSubmit") boolean isSubmit,
                         final RedirectAttributes redirectAttributes,
                         HttpServletRequest request) {
        log.debug("updating bookReport: {}", bookReport);

        if (!bookReport.isEditable()){
            redirectAttributes.addFlashAttribute("msg", "This document cannot currently be edited by you.</p>");
            return "redirect:/document/list.htm";
        }
        if (result.hasErrors()) {
            return "document/bookReport/edit";
        }

        this.docService.updateDocument(bookReport);
        String docId = bookReport.getId();

        if (isSubmit) {
            log.debug("Submitting to dynamic workflow docId {}", docId);
            this.docService.submitToWorkflow(docId);
        }
        if (isSubmit) {
            redirectAttributes.addFlashAttribute("msg", "<p>Your Document has been submitted for approval.<p/>" +
                    "<p>You will receive alerts when it has been reviewed.</p>");
        } else {
            redirectAttributes.addFlashAttribute("msg", "Your Document has been Updated");
        }

        return "redirect:/document/list.htm";
    }


    private Document newBookReport() {
        Document document = new BookReport();
        document.setAuthor(currentUserName());
        document.setDocType(DocType.BOOK_REPORT);
        document.setCreatedDate(new Date());
        return document;
    }
}
