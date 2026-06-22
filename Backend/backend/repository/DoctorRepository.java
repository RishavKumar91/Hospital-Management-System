package com.hospital.backend.repository;

import com.hospital.backend.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByNameContainingIgnoreCase(String name);
    List<Doctor> findBySpecializationContainingIgnoreCase(String specialization);
    List<Doctor> findByDepartmentContainingIgnoreCase(String department);
}
