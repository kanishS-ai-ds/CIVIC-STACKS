package com.library.stacks.model;

import jakarta.persistence.*;

@Entity
@Table(name = "department_budgets")
public class DepartmentBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String department;          // Analytic account, e.g. "City Digital Media Unit"
    private double acquisitionBudget;   // allocated media acquisition budget
    private double serverCostBudget;    // allocated hosting / server budget

    public DepartmentBudget() {}

    public DepartmentBudget(String department, double acquisitionBudget, double serverCostBudget) {
        this.department = department;
        this.acquisitionBudget = acquisitionBudget;
        this.serverCostBudget = serverCostBudget;
    }

    public Long getId() { return id; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public double getAcquisitionBudget() { return acquisitionBudget; }
    public void setAcquisitionBudget(double acquisitionBudget) { this.acquisitionBudget = acquisitionBudget; }
    public double getServerCostBudget() { return serverCostBudget; }
    public void setServerCostBudget(double serverCostBudget) { this.serverCostBudget = serverCostBudget; }
}
