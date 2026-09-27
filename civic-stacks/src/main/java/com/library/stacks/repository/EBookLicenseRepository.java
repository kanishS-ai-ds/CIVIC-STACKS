package com.library.stacks.repository;

import com.library.stacks.model.EBookLicense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EBookLicenseRepository extends JpaRepository<EBookLicense, Long> {
}
