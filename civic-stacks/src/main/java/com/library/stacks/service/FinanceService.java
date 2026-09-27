package com.library.stacks.service;

import com.library.stacks.model.*;
import com.library.stacks.repository.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FinanceService {

    private final MembershipInvoiceRepository invoices;
    private final LicenseBillRepository bills;
    private final CheckoutRepository checkouts;
    private final DepartmentBudgetRepository budgets;

    public FinanceService(MembershipInvoiceRepository invoices, LicenseBillRepository bills,
                           CheckoutRepository checkouts, DepartmentBudgetRepository budgets) {
        this.invoices = invoices;
        this.bills = bills;
        this.checkouts = checkouts;
        this.budgets = budgets;
    }

    /** GET /api/ebooks/reports/pnl -> Profit & Loss summary */
    public Map<String, Object> profitAndLoss() {
        double subscriptionRevenue = invoices.findAll().stream()
                .mapToDouble(MembershipInvoice::getAmount).sum();
        double lateAccessFeeRevenue = checkouts.findAll().stream()
                .mapToDouble(Checkout::getLateAccessFee).sum();
        double licensingExpenses = bills.findAll().stream()
                .mapToDouble(LicenseBill::getAmount).sum();

        Map<String, Object> report = new HashMap<>();
        report.put("subscriptionRevenue", subscriptionRevenue);
        report.put("lateAccessFeeRevenue", lateAccessFeeRevenue);
        report.put("totalRevenue", subscriptionRevenue + lateAccessFeeRevenue);
        report.put("digitalLicensingExpenses", licensingExpenses);
        report.put("netIncome", (subscriptionRevenue + lateAccessFeeRevenue) - licensingExpenses);
        return report;
    }

    /** GET /api/ebooks/reports/budget -> per-department media acquisition budget vs actual */
    public Map<String, Object> budgetReport() {
        double actualLicensingSpend = bills.findAll().stream().mapToDouble(LicenseBill::getAmount).sum();
        double subscriptionRevenue = invoices.findAll().stream().mapToDouble(MembershipInvoice::getAmount).sum();

        Map<String, Object> report = new HashMap<>();
        report.put("departments", budgets.findAll().stream().map(b -> {
            Map<String, Object> row = new HashMap<>();
            row.put("department", b.getDepartment());
            row.put("acquisitionBudget", b.getAcquisitionBudget());
            row.put("actualLicensingSpend", actualLicensingSpend);
            row.put("serverCostBudget", b.getServerCostBudget());
            row.put("subscriptionRevenue", subscriptionRevenue);
            return row;
        }).toList());
        return report;
    }
}
