package com.library.stacks.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "license_bills")
public class LicenseBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    private String description;      // e.g. "100-user concurrent license bundle - PO#4021"
    private double amount;
    private LocalDate billDate = LocalDate.now();
    private String status = "UNPAID"; // UNPAID, PAID

    public LicenseBill() {}

    public LicenseBill(Publisher publisher, String description, double amount) {
        this.publisher = publisher;
        this.description = description;
        this.amount = amount;
    }

    public Long getId() { return id; }
    public Publisher getPublisher() { return publisher; }
    public void setPublisher(Publisher publisher) { this.publisher = publisher; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public LocalDate getBillDate() { return billDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
