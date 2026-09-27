package com.library.stacks.model;

import jakarta.persistence.*;

@Entity
@Table(name = "patrons")
public class Patron {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String residencyStatus;     // RESIDENT, NON_RESIDENT
    private String membershipStatus = "ACTIVE"; // ACTIVE, EXPIRED

    public Patron() {}

    public Patron(String name, String email, String residencyStatus) {
        this.name = name;
        this.email = email;
        this.residencyStatus = residencyStatus;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getResidencyStatus() { return residencyStatus; }
    public void setResidencyStatus(String residencyStatus) { this.residencyStatus = residencyStatus; }
    public String getMembershipStatus() { return membershipStatus; }
    public void setMembershipStatus(String membershipStatus) { this.membershipStatus = membershipStatus; }
}
