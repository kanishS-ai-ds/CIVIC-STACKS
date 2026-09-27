package com.library.stacks.controller;

import com.library.stacks.model.Checkout;
import com.library.stacks.repository.CheckoutRepository;
import com.library.stacks.service.LibraryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ebooks")
@CrossOrigin(origins = "*")
public class CheckoutController {

    private final LibraryService libraryService;
    private final CheckoutRepository checkouts;

    public CheckoutController(LibraryService libraryService, CheckoutRepository checkouts) {
        this.libraryService = libraryService;
        this.checkouts = checkouts;
    }

    @GetMapping("/checkouts")
    public List<Checkout> listAll() {
        return checkouts.findAll();
    }

    /** Borrow an e-book; rejected if active checkouts already match the license's concurrent cap. */
    @PostMapping("/checkout")
    public Checkout checkout(@RequestParam Long patronId, @RequestParam Long licenseId) {
        return libraryService.borrow(patronId, licenseId);
    }

    @PutMapping("/checkouts/{id}/return")
    public Checkout returnEbook(@PathVariable Long id) {
        return libraryService.returnEbook(id);
    }

    @PostMapping("/checkouts/expire-overdue")
    public List<Checkout> expireOverdue() {
        return libraryService.expireOverdueCheckouts();
    }
}
