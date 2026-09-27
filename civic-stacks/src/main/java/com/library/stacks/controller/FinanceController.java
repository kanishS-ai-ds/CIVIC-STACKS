package com.library.stacks.controller;

import com.library.stacks.model.*;
import com.library.stacks.repository.*;
import com.library.stacks.service.FinanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ebooks")
@CrossOrigin(origins = "*")
public class FinanceController {

    private final MembershipInvoiceRepository invoices;
    private final LicenseBillRepository bills;
    private final PatronRepository patrons;
    private final PublisherRepository publishers;
    private final FinanceService financeService;

    public FinanceController(MembershipInvoiceRepository invoices, LicenseBillRepository bills,
                              PatronRepository patrons, PublisherRepository publishers,
                              FinanceService financeService) {
        this.invoices = invoices;
        this.bills = bills;
        this.patrons = patrons;
        this.publishers = publishers;
        this.financeService = financeService;
    }

    // ---- Sales order -> Customer Invoice (non-resident membership fee) ----
    @GetMapping("/invoices")
    public List<MembershipInvoice> listInvoices() { return invoices.findAll(); }

    @PostMapping("/invoices")
    public MembershipInvoice createInvoice(@RequestParam Long patronId, @RequestParam String description,
                                            @RequestParam double amount) {
        Patron patron = patrons.findById(patronId).orElseThrow();
        return invoices.save(new MembershipInvoice(patron, description, amount));
    }

    @PutMapping("/invoices/{id}/pay")
    public MembershipInvoice payInvoice(@PathVariable Long id) {
        MembershipInvoice inv = invoices.findById(id).orElseThrow();
        inv.setStatus("PAID");
        return invoices.save(inv);
    }

    // ---- Purchase order -> Vendor Bill (publisher license fee) ----
    @GetMapping("/vendor-bills")
    public List<LicenseBill> listBills() { return bills.findAll(); }

    @PostMapping("/vendor-bills")
    public LicenseBill createBill(@RequestParam Long publisherId, @RequestParam String description,
                                   @RequestParam double amount) {
        Publisher publisher = publishers.findById(publisherId).orElseThrow();
        return bills.save(new LicenseBill(publisher, description, amount));
    }

    @PutMapping("/vendor-bills/{id}/pay")
    public LicenseBill payBill(@PathVariable Long id) {
        LicenseBill bill = bills.findById(id).orElseThrow();
        bill.setStatus("PAID");
        return bills.save(bill);
    }

    // ---- Reports ----
    @GetMapping("/reports/pnl")
    public Map<String, Object> pnl() { return financeService.profitAndLoss(); }

    @GetMapping("/reports/budget")
    public Map<String, Object> budget() { return financeService.budgetReport(); }
}
