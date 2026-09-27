package com.library.stacks.controller;

import com.library.stacks.model.*;
import com.library.stacks.repository.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ebooks")
@CrossOrigin(origins = "*")
public class MasterDataController {

    private final PublisherRepository publishers;
    private final PatronRepository patrons;

    public MasterDataController(PublisherRepository publishers, PatronRepository patrons) {
        this.publishers = publishers;
        this.patrons = patrons;
    }

    @GetMapping("/publishers")
    public List<Publisher> listPublishers() { return publishers.findAll(); }

    @PostMapping("/publishers")
    public Publisher addPublisher(@RequestBody Publisher publisher) { return publishers.save(publisher); }

    @GetMapping("/patrons")
    public List<Patron> listPatrons() { return patrons.findAll(); }

    @PostMapping("/patrons")
    public Patron addPatron(@RequestBody Patron patron) { return patrons.save(patron); }
}
