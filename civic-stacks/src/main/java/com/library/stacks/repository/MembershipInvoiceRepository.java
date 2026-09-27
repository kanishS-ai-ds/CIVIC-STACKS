package com.library.stacks.repository;

import com.library.stacks.model.MembershipInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipInvoiceRepository extends JpaRepository<MembershipInvoice, Long> {
}
