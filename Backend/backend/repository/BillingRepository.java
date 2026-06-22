package com.hospital.backend.repository;

import com.hospital.backend.model.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    List<Billing> findByStatus(String status);
    List<Billing> findByPatientId(Long patientId);

    @Query("SELECT SUM(b.amount) FROM Billing b WHERE b.status = 'PAID'")
    Double sumTotalPaidRevenue();
    
    long countByStatus(String status);
}
