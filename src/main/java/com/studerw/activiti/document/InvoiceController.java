package com.studerw.activiti.document;

import com.studerw.activiti.model.DocType;
import com.studerw.activiti.model.Document;
import com.studerw.activiti.model.HistoricTask;
import com.studerw.activiti.model.Invoice;
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
@RequestMapping("/document/invoice")
public class InvoiceController extends DocumentController {
    private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    DocumentService docService;
    @Autowired
    LocalTaskService localTaskSrvc;

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
    public String addDocumentForm(ModelMap model) {
        model.addAttribute("invoice", newInvoice());
        return "document/invoice/create";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute Invoice invoice,
                         BindingResult result,
                         @RequestParam(required = false, value = "isSubmit") boolean isSubmit,
                         final RedirectAttributes redirectAttributes,
                         HttpServletRequest request) {
        log.debug("submitting invoice: {}", invoice);

        if (result.hasErrors()) {
            return "document/invoice/create";
        }
        String docId = this.docService.createDocument(invoice);

        if (isSubmit) {
            log.debug("Submitting for approval docId {}", docId);
            this.docService.submitForApproval(docId);
        }
        if (isSubmit) {
            redirectAttributes.addFlashAttribute("msg", "<p>Your Document has been submitted for approval.<p/>" +
                    "<p>You will receive alerts when it has been reviewed.</p>");
        } else {
            redirectAttributes.addFlashAttribute("msg", "Your Document has been Saved");
        }

        return "redirect:/document/list.htm";
    }

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
            return "document/invoice/edit";
        } else if (doc.getAuthor().equals(currentUserName())) {
            model.addAttribute("msg", "The invoice cannot be edited in its current state.");
        } else {
            model.addAttribute("msg", "Only the original author may edit the invoice.");
        }
        return "document/invoice/view";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute Invoice invoice,
                         BindingResult result,
                         @RequestParam(required = false, value = "isSubmit") boolean isSubmit,
                         final RedirectAttributes redirectAttributes,
                         HttpServletRequest request) {
        log.debug("updating invoice: {}", invoice);

        if (!invoice.isEditable()) {
            redirectAttributes.addFlashAttribute("msg", "This document cannot currently be edited by you.</p>");
            return "redirect:/document/list.htm";
        }
        if (result.hasErrors()) {
            return "document/invoice/edit";
        }

        this.docService.updateDocument(invoice);
        String docId = invoice.getId();

        if (isSubmit) {
            log.debug("Submitting for approval docId {}", docId);
            this.docService.submitForApproval(docId);
        }
        if (isSubmit) {
            redirectAttributes.addFlashAttribute("msg", "<p>Your Document has been submitted for approval.<p/>" +
                    "<p>You will receive alerts when it has been reviewed.</p>");
        } else {
            redirectAttributes.addFlashAttribute("msg", "Your Document has been Updated");
        }

        return "redirect:/document/list.htm";
    }


    private Document newInvoice() {
        Document document = new Invoice();
        document.setAuthor(currentUserName());
        document.setDocType(DocType.INVOICE);
        document.setCreatedDate(new Date());
        return document;
    }
}
