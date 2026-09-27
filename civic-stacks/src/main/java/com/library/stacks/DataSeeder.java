package com.library.stacks;

import com.library.stacks.model.*;
import com.library.stacks.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final PublisherRepository publishers;
    private final PatronRepository patrons;
    private final EBookLicenseRepository licenses;
    private final DepartmentBudgetRepository budgets;

    public DataSeeder(PublisherRepository publishers, PatronRepository patrons,
                       EBookLicenseRepository licenses, DepartmentBudgetRepository budgets) {
        this.publishers = publishers;
        this.patrons = patrons;
        this.licenses = licenses;
        this.budgets = budgets;
    }

    @Override
    public void run(String... args) {
        if (publishers.count() > 0) return; // already seeded

        Publisher penguin = publishers.save(new Publisher("Penguin Civic Press", "rights@penguincivic.example", "+91-8000000001"));
        Publisher openStax = publishers.save(new Publisher("OpenShelf Academic", "licensing@openshelf.example", "+91-8000000002"));

        licenses.save(new EBookLicense("The Municipal Reader", "978-1-000-00001-1", penguin, "PERPETUAL", 5, 1200.0));
        licenses.save(new EBookLicense("Civic Data Structures", "978-1-000-00002-8", openStax, "ANNUAL", 3, 800.0));
        licenses.save(new EBookLicense("Journal of Urban Studies", "978-1-000-00003-5", openStax, "ACADEMIC_PASS", 2, 450.0));

        patrons.save(new Patron("Meera Krishnan", "meera@example.com", "RESIDENT"));
        patrons.save(new Patron("Arjun Mehta", "arjun@example.com", "NON_RESIDENT"));
        patrons.save(new Patron("Sara Thomas", "sara@example.com", "NON_RESIDENT"));

        budgets.save(new DepartmentBudget("City Digital Media Unit", 50000, 8000));
    }
}
