package com.library.stacks.repository;

import com.library.stacks.model.DepartmentBudget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentBudgetRepository extends JpaRepository<DepartmentBudget, Long> {
}
