package com.hospital.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "billings")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "billing_date", nullable = false)
    private LocalDate billingDate;

    @Column(nullable = false)
    private String status; // PAID, UNPAID

    @Column(name = "services_rendered", columnDefinition = "TEXT")
    private String servicesRendered; // Consultation, Lab Test, Medication, ICU Charges

    // Constructors
    public Billing() {}

    public Billing(Patient patient, Double amount, LocalDate billingDate, String status, String servicesRendered) {
        this.patient = patient;
        this.amount = amount;
        this.billingDate = billingDate;
        this.status = status;
        this.servicesRendered = servicesRendered;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(LocalDate billingDate) {
        this.billingDate = billingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServicesRendered() {
        return servicesRendered;
    }

    public void setServicesRendered(String servicesRendered) {
        this.servicesRendered = servicesRendered;
    }
}
