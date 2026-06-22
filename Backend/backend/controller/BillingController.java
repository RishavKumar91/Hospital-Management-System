package com.hospital.backend.controller;

import com.hospital.backend.model.Billing;
import com.hospital.backend.model.Patient;
import com.hospital.backend.repository.BillingRepository;
import com.hospital.backend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/billings")
@CrossOrigin(origins = "http://localhost:5173")
public class BillingController {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping
    public List<Billing> getAllBillings() {
        return billingRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Billing> getBillingById(@PathVariable Long id) {
        return billingRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createBilling(@RequestBody BillingRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId()).orElse(null);

        if (patient == null) {
            return ResponseEntity.badRequest().body("Error: Patient not found");
        }

        Billing billing = new Billing();
        billing.setPatient(patient);
        billing.setAmount(request.getAmount());
        billing.setBillingDate(request.getBillingDate() != null ? request.getBillingDate() : LocalDate.now());
        billing.setStatus(request.getStatus() != null ? request.getStatus() : "UNPAID");
        billing.setServicesRendered(request.getServicesRendered());

        return ResponseEntity.ok(billingRepository.save(billing));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBilling(@PathVariable Long id, @RequestBody BillingRequest request) {
        return billingRepository.findById(id)
                .map(billing -> {
                    if (request.getPatientId() != null) {
                        Patient patient = patientRepository.findById(request.getPatientId()).orElse(null);
                        if (patient != null) billing.setPatient(patient);
                    }
                    if (request.getAmount() != null) {
                        billing.setAmount(request.getAmount());
                    }
                    if (request.getBillingDate() != null) {
                        billing.setBillingDate(request.getBillingDate());
                    }
                    if (request.getStatus() != null) {
                        billing.setStatus(request.getStatus());
                    }
                    if (request.getServicesRendered() != null) {
                        billing.setServicesRendered(request.getServicesRendered());
                    }
                    return ResponseEntity.ok(billingRepository.save(billing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBilling(@PathVariable Long id) {
        if (billingRepository.existsById(id)) {
            billingRepository.deleteById(id);
            return ResponseEntity.ok().<Void>build();
        }
        return ResponseEntity.notFound().build();
    }

    // DTO class for Request Mapping
    public static class BillingRequest {
        private Long patientId;
        private Double amount;
        private LocalDate billingDate;
        private String status;
        private String servicesRendered;

        public Long getPatientId() {
            return patientId;
        }

        public void setPatientId(Long patientId) {
            this.patientId = patientId;
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
}
