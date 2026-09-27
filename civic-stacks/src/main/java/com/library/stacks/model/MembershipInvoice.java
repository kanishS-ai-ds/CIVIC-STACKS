package com.library.stacks.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "membership_invoices")
public class MembershipInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patron_id")
    private Patron patron;

    private String description;      // e.g. "Annual Non-Resident Card - FY26"
    private double amount;
    private LocalDate issueDate = LocalDate.now();
    private String status = "OPEN";  // OPEN, PAID

    public MembershipInvoice() {}

    public MembershipInvoice(Patron patron, String description, double amount) {
        this.patron = patron;
        this.description = description;
        this.amount = amount;
    }

    public Long getId() { return id; }
    public Patron getPatron() { return patron; }
    public void setPatron(Patron patron) { this.patron = patron; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDate getIssueDate() { return issueDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
