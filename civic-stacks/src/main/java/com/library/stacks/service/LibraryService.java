package com.library.stacks.service;

import com.library.stacks.model.*;
import com.library.stacks.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LibraryService {

    private final EBookLicenseRepository licenses;
    private final PatronRepository patrons;
    private final CheckoutRepository checkouts;

    public LibraryService(EBookLicenseRepository licenses, PatronRepository patrons, CheckoutRepository checkouts) {
        this.licenses = licenses;
        this.patrons = patrons;
        this.checkouts = checkouts;
    }

    /** POST /api/ebooks/checkout — borrow an e-book, enforcing the license's concurrent checkout cap. */
    public Checkout borrow(Long patronId, Long licenseId) {
        Patron patron = patrons.findById(patronId).orElseThrow(() -> new NoSuchElementException("Patron not found"));
        EBookLicense license = licenses.findById(licenseId).orElseThrow(() -> new NoSuchElementException("E-Book license not found"));

        long activeCount = checkouts.findAll().stream()
                .filter(c -> "ACTIVE".equals(c.getStatus()) && c.getLicense().getId().equals(licenseId))
                .count();

        if (activeCount >= license.getConcurrentCheckoutLimit()) {
            throw new IllegalStateException("Digital access denied: \"" + license.getTitle() +
                    "\" is at its concurrent checkout cap (" + license.getConcurrentCheckoutLimit() + ")");
        }

        Checkout checkout = new Checkout();
        checkout.setPatron(patron);
        checkout.setLicense(license);
        checkout.start();
        return checkouts.save(checkout);
    }

    /** PUT /api/ebooks/checkouts/{id}/return — revoke the digital key and settle any late access fee. */
    public Checkout returnEbook(Long checkoutId) {
        Checkout checkout = checkouts.findById(checkoutId).orElseThrow(() -> new NoSuchElementException("Checkout not found"));
        checkout.returnBook(LocalDate.now());
        return checkouts.save(checkout);
    }

    /** System duty: revoke digital keys whose loan period has lapsed without a return. */
    public List<Checkout> expireOverdueCheckouts() {
        List<Checkout> overdue = checkouts.findAll().stream().filter(Checkout::isOverdue).toList();
        overdue.forEach(c -> c.setStatus("EXPIRED"));
        return checkouts.saveAll(overdue);
    }

    /** Licenses that have hit (or are close to) their concurrent checkout cap — candidates for extra keys. */
    public List<EBookLicense> licensesNearCapacity() {
        return licenses.findAll().stream().filter(l -> {
            long activeCount = checkouts.findAll().stream()
                    .filter(c -> "ACTIVE".equals(c.getStatus()) && c.getLicense().getId().equals(l.getId()))
                    .count();
            return activeCount >= l.getConcurrentCheckoutLimit();
        }).toList();
    }
}
