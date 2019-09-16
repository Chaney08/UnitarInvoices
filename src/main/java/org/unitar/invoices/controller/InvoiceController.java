package org.unitar.invoices.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.unitar.invoices.model.Invoice;
import org.unitar.invoices.model.InvoiceRow;
import org.unitar.invoices.model.User;
import org.unitar.invoices.repository.InvoiceRepository;
import org.unitar.invoices.repository.InvoiceRowRepository;
import org.unitar.invoices.utils.WebUtils;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
@RequestMapping(path="/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepo;
    @Autowired
    private InvoiceRowRepository invoiceRowRepo;

    //Create this invoice here so we can dynamically add to it
    Invoice invoice = new Invoice();


    @Autowired
    private WebUtils webUtils;

    @RequestMapping(value = { "/", "/invoiceDashboard" })
    public String invoiceDashboard(Model model) {
        //Get the user and load their owned invoices to the page
        User user = webUtils.getUser();
        model.addAttribute("invoices", user.getOwnedInvoices());
        return "invoiceTemplates/invoiceDashboard";
    }
    @GetMapping(value = "/invoiceCreation")
    public String invoiceCreation(Model model) {

        invoice = new Invoice();
        model.addAttribute("invoiceCreation", invoice);
        return "invoiceTemplates/invoiceCreation";
    }

    @PostMapping(value = "/invoiceCreation")
    public String invoiceCreation(@ModelAttribute("invoiceCreation") @Valid Invoice invoice,
                                   BindingResult result) {


        Invoice existing = invoiceRepo.findByInvoiceName(invoice.getInvoiceName());
        if (existing != null) {
            result.rejectValue("invoiceName", null, "There is already an invoice with that name");
        }

        if (result.hasErrors()) {
            return "invoiceTemplates/invoiceCreation";
        }else if(invoice.getInvoiceRows() == null){
            result.rejectValue("invoiceName", null, "Please ensure you have at least one row before proceeding");
            return "invoiceTemplates/invoiceCreation";
        }
        else {
            invoice.setUser(webUtils.getUser());

            invoiceRowRepo.saveAll(invoice.getInvoiceRows());

            invoiceRepo.save(invoice);

            invoice.getInvoiceRows().stream().forEach(x-> x.setInvoice(invoice));
            invoice.getInvoiceRows().stream().forEach(x -> invoiceRowRepo.save(x));

            return "redirect:/invoice/invoiceDashboard";
        }
    }

    @Transactional
    @RequestMapping(value = "/deleteInvoice", method = RequestMethod.GET)
    public String deleteInvoice(@RequestParam(name="invoiceId") long idForDeletion) {
        invoiceRepo.deleteInvoiceByInvoiceId(idForDeletion);
        return "redirect:/invoice/invoiceDashboard";
    }

    @GetMapping(value = "/invoiceUpdate")
    public String updateInvoice(Model model, @RequestParam(name="invoiceId") int idForEditing) {
        invoice = invoiceRepo.findInvoiceByInvoiceId(idForEditing);
        invoice.setInvoiceRows(invoiceRowRepo.findByInvoice(invoice));
        invoice.getInvoiceRows().stream().forEach(x-> x.setInvoice(invoice));
        model.addAttribute("invoiceUpdate", invoice);
        return "invoiceTemplates/invoiceUpdate";
    }

    @PostMapping(value = "/invoiceUpdate")
    public String updateInvoice(@ModelAttribute("invoiceUpdate") @Valid Invoice invoice,
                             BindingResult result) {
        this.invoice = invoice;

        int index = invoice.getInvoiceRows().size();
        if (result.hasErrors()) {
            return "invoiceTemplates/invoiceUpdate";
        }else {
            invoice.setUser(webUtils.getUser());
            invoiceRepo.save(invoice);

            invoice.getInvoiceRows().stream().forEach(x-> x.setInvoice(invoice));
            invoice.getInvoiceRows().stream().forEach(x -> invoiceRowRepo.delete(x));
            invoice.getInvoiceRows().stream().forEach(x -> invoiceRowRepo.save(x));
            return "redirect:/invoice/invoiceDashboard";
        }

    }

    //This method is for adding a new row to the invoice creation page
    @Transactional
    @RequestMapping(value = "/addNewRow", method = RequestMethod.GET)
    public String addNewRow(Model model) {
        invoice.addInvoiceRow(new InvoiceRow());
        model.addAttribute("invoiceCreation", invoice);
        return "invoiceTemplates/invoiceCreation";
    }
    @RequestMapping(value = "/deleteRow", method = RequestMethod.GET)
    public String deleteRow(Model model, @RequestParam(name="rowIndex") int rowIdForDeletion) {
        invoice.deleteRowByIndex(rowIdForDeletion);
        invoiceRowRepo.deleteInvoiceRowByRowId(rowIdForDeletion);
        model.addAttribute("invoiceCreation", invoice);
        return "invoiceTemplates/invoiceCreation";
    }
    @Transactional
    @RequestMapping(value = "/updateDeleteRow", method = RequestMethod.GET)
    public String updateDeleteRow(Model model, @RequestParam(name="rowId") int rowIdForDeletion,
                                  @ModelAttribute("invoiceUpdate") @Valid Invoice myInvoice,BindingResult result) {
        invoice.deleteRow(rowIdForDeletion);
        invoiceRowRepo.deleteInvoiceRowByRowId(rowIdForDeletion);
        invoiceRepo.save(invoice);
        model.addAttribute("invoiceUpdate", invoice);
        return "invoiceTemplates/invoiceUpdate";
    }


}
