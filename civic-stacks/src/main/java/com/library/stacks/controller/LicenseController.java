package com.library.stacks.controller;

import com.library.stacks.model.EBookLicense;
import com.library.stacks.repository.EBookLicenseRepository;
import com.library.stacks.service.LibraryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ebooks/licenses")
@CrossOrigin(origins = "*")
public class LicenseController {

    private final EBookLicenseRepository licenses;
    private final LibraryService libraryService;

    public LicenseController(EBookLicenseRepository licenses, LibraryService libraryService) {
        this.licenses = licenses;
        this.libraryService = libraryService;
    }

    @GetMapping
    public List<EBookLicense> listAll() {
        return licenses.findAll();
    }

    @GetMapping("/near-capacity")
    public List<EBookLicense> nearCapacity() {
        return libraryService.licensesNearCapacity();
    }

    @PostMapping
    public EBookLicense register(@RequestBody EBookLicense license) {
        if (license.getStatus() == null) license.setStatus("ACTIVE");
        return licenses.save(license);
    }

    @PutMapping("/{id}/cap")
    public EBookLicense updateConcurrencyCap(@PathVariable Long id, @RequestParam int limit) {
        EBookLicense license = licenses.findById(id).orElseThrow();
        license.setConcurrentCheckoutLimit(limit);
        return licenses.save(license);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        licenses.deleteById(id);
    }
}
