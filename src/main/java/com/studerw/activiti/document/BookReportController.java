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
import org.springframework.util.Assert;
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
    private static final Logger LOG = LoggerFactory.getLogger(BookReportController.class);

    @Autowired DocumentService docService;
    @Autowired LocalTaskService localTaskSrvc;

    @Override @ModelAttribute
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
    public String create(@Valid @ModelAttribute BookReport bookReport, BindingResult result,
                         @RequestParam(required = false, value = "isSubmit") boolean isSubmit,
                         final RedirectAttributes redirectAttributes) {
        LOG.debug("submitting bookReport: {}", bookReport);

        if (result.hasErrors()) {
            return "document/bookReport/create";
        }
        String docId = this.docService.createDocument(bookReport);
        checkSubmit(isSubmit, docId, redirectAttributes);

        return "redirect:/document/list.htm";
    }


    @RequestMapping(value = "/view.htm", method = RequestMethod.GET)
    public String view(ModelMap model,
                       @RequestParam(value = "id", required = true) String id){
        LOG.debug("viewing doc {} ", id);
        Assert.hasText(id);
        Document doc = docService.getDocument(id);
        model.addAttribute("document", doc);
        List<HistoricTask> hts = this.localTaskSrvc.getTaskHistory(id);
        model.addAttribute("historicTasks", hts);
        if (doc.isEditable(doc.getAuthor(), currentUserName())) {
            return "document/bookReport/edit";
        }
        model.addAttribute("msg", "The book report cannot be edited in its current state.");
        return "document/bookReport/view";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute BookReport bookReport,
                         BindingResult result,
                         @RequestParam(required = false, value = "isSubmit") boolean isSubmit,
                         final RedirectAttributes redirectAttributes) {
        LOG.debug("updating bookReport: {}", bookReport);

        if (!bookReport.isEditable(bookReport.getAuthor(), currentUserName())) {
            redirectAttributes.addFlashAttribute("msg", "This book report cannot currently be edited by you.</p>");
            return "redirect:/document/list.htm";
        }
        if (result.hasErrors()) {
            return "document/bookReport/edit";
        }

        this.docService.updateDocument(bookReport);
        String docId = bookReport.getId();
        checkSubmit(isSubmit, docId, redirectAttributes);
        return "redirect:/document/list.htm";
    }


    private void checkSubmit(boolean isSubmit, String docId, RedirectAttributes redirAttr) {
        if (isSubmit) {
            LOG.debug("Submitting to dynamic workflow docId {}", docId);
            this.docService.submitToWorkflow(docId);
        }
        if (isSubmit) {
            redirAttr.addFlashAttribute("msg", "Your Book Report has been submitted to the workflow.</br>" +
                    "You will receive alerts as it is processed.");
        } else {
            redirAttr.addFlashAttribute("msg", "Your book report has been Saved");
        }
    }

    private Document newBookReport() {
        Document document = new BookReport();
        document.setAuthor(currentUserName());
        document.setDocType(DocType.BOOK_REPORT);
        document.setCreatedDate(new Date());
        return document;
    }

    //    @RequestMapping(value = "/view.htm", method = RequestMethod.POST)
//    public String updateDocument(@Valid @ModelAttribute Document bookReport,
//                                 @RequestParam(required = false, value = "isSubmit") boolean isSubmit,
//                                 BindingResult result,
//                                 final RedirectAttributes redirectAttributes,
//                                 ModelMap model,
//                                 HttpServletRequest request) {
//        LOG.debug("updating bookReport: " + bookReport);
//        if (result.hasErrors()) {
//            return "bookReport/view";
//        }
//        this.docService.updateDocument(bookReport);
//        if (isSubmit) {
//            LOG.debug("Submitting to dynamic workflow docId {}", bookReport.getId());
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
}
