package com.library.stacks.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "checkouts")
public class Checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patron_id")
    private Patron patron;

    @ManyToOne
    @JoinColumn(name = "license_id")
    private EBookLicense license;

    private LocalDate checkoutDate = LocalDate.now();
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double lateAccessFee;
    private String status;   // ACTIVE, RETURNED, EXPIRED

    private static final int LOAN_PERIOD_DAYS = 14;
    private static final double LATE_FEE_PER_DAY = 0.50;

    public Checkout() {}

    public void start() {
        this.checkoutDate = LocalDate.now();
        this.dueDate = this.checkoutDate.plusDays(LOAN_PERIOD_DAYS);
        this.status = "ACTIVE";
    }

    /** Digital key revocation: returns the e-book and computes any late access fee. */
    public void returnBook(LocalDate returned) {
        this.returnDate = returned;
        if (returned.isAfter(dueDate)) {
            long lateDays = java.time.temporal.ChronoUnit.DAYS.between(dueDate, returned);
            this.lateAccessFee = lateDays * LATE_FEE_PER_DAY;
        }
        this.status = "RETURNED";
    }

    public boolean isOverdue() {
        return "ACTIVE".equals(status) && dueDate != null && LocalDate.now().isAfter(dueDate);
    }

    public Long getId() { return id; }
    public Patron getPatron() { return patron; }
    public void setPatron(Patron patron) { this.patron = patron; }
    public EBookLicense getLicense() { return license; }
    public void setLicense(EBookLicense license) { this.license = license; }
    public LocalDate getCheckoutDate() { return checkoutDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public double getLateAccessFee() { return lateAccessFee; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
