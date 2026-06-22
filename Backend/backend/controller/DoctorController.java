package com.hospital.backend.controller;

import com.hospital.backend.model.Doctor;
import com.hospital.backend.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:5173")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping
    public List<Doctor> getAllDoctors(@RequestParam(required = false) String name) {
        if (name != null && !name.trim().isEmpty()) {
            return doctorRepository.findByNameContainingIgnoreCase(name);
        }
        return doctorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        return doctorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Doctor createDoctor(@RequestBody Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctorDetails) {
        return doctorRepository.findById(id)
                .map(doctor -> {
                    doctor.setName(doctorDetails.getName());
                    doctor.setEmail(doctorDetails.getEmail());
                    doctor.setPhone(doctorDetails.getPhone());
                    doctor.setSpecialization(doctorDetails.getSpecialization());
                    doctor.setDepartment(doctorDetails.getDepartment());
                    doctor.setAvailability(doctorDetails.getAvailability());
                    Doctor updatedDoctor = doctorRepository.save(doctor);
                    return ResponseEntity.ok(updatedDoctor);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return ResponseEntity.ok().<Void>build();
        }
        return ResponseEntity.notFound().build();
    }
}
