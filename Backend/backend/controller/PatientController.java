package com.hospital.backend.controller;

import com.hospital.backend.model.Patient;
import com.hospital.backend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:5173")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping
    public List<Patient> getAllPatients(@RequestParam(required = false) String name) {
        if (name != null && !name.trim().isEmpty()) {
            return patientRepository.findByNameContainingIgnoreCase(name);
        }
        return patientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patientDetails) {
        return patientRepository.findById(id)
                .map(patient -> {
                    patient.setName(patientDetails.getName());
                    patient.setEmail(patientDetails.getEmail());
                    patient.setPhone(patientDetails.getPhone());
                    patient.setGender(patientDetails.getGender());
                    patient.setDateOfBirth(patientDetails.getDateOfBirth());
                    patient.setAddress(patientDetails.getAddress());
                    patient.setMedicalHistory(patientDetails.getMedicalHistory());
                    Patient updatedPatient = patientRepository.save(patient);
                    return ResponseEntity.ok(updatedPatient);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return ResponseEntity.ok().<Void>build();
        }
        return ResponseEntity.notFound().build();
    }
}
