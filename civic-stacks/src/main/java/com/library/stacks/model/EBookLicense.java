package com.library.stacks.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ebook_licenses")
public class EBookLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String isbn;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    private String licenseType;         // PERPETUAL, ANNUAL, ACADEMIC_PASS
    private int concurrentCheckoutLimit; // e.g. 100-user concurrent bundle
    private double unitCost;            // acquisition cost, capitalized as a digital asset
    private String status = "ACTIVE";   // ACTIVE, EXPIRED

    public EBookLicense() {}

    public EBookLicense(String title, String isbn, Publisher publisher, String licenseType,
                         int concurrentCheckoutLimit, double unitCost) {
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.licenseType = licenseType;
        this.concurrentCheckoutLimit = concurrentCheckoutLimit;
        this.unitCost = unitCost;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Publisher getPublisher() { return publisher; }
    public void setPublisher(Publisher publisher) { this.publisher = publisher; }
    public String getLicenseType() { return licenseType; }
    public void setLicenseType(String licenseType) { this.licenseType = licenseType; }
    public int getConcurrentCheckoutLimit() { return concurrentCheckoutLimit; }
    public void setConcurrentCheckoutLimit(int concurrentCheckoutLimit) { this.concurrentCheckoutLimit = concurrentCheckoutLimit; }
    public double getUnitCost() { return unitCost; }
    public void setUnitCost(double unitCost) { this.unitCost = unitCost; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
