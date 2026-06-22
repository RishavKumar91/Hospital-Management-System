package com.hospital.backend.controller;

import com.hospital.backend.model.Appointment;
import com.hospital.backend.model.Doctor;
import com.hospital.backend.model.Patient;
import com.hospital.backend.repository.AppointmentRepository;
import com.hospital.backend.repository.DoctorRepository;
import com.hospital.backend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:5173")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        return appointmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId()).orElse(null);
        Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElse(null);

        if (patient == null) {
            return ResponseEntity.badRequest().body("Error: Patient not found");
        }
        if (doctor == null) {
            return ResponseEntity.badRequest().body("Error: Doctor not found");
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setStatus(request.getStatus() != null ? request.getStatus() : "SCHEDULED");
        appointment.setReason(request.getReason());

        return ResponseEntity.ok(appointmentRepository.save(appointment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id, @RequestBody AppointmentRequest request) {
        return appointmentRepository.findById(id)
                .map(appointment -> {
                    if (request.getPatientId() != null) {
                        Patient patient = patientRepository.findById(request.getPatientId()).orElse(null);
                        if (patient != null) appointment.setPatient(patient);
                    }
                    if (request.getDoctorId() != null) {
                        Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElse(null);
                        if (doctor != null) appointment.setDoctor(doctor);
                    }
                    if (request.getAppointmentDate() != null) {
                        appointment.setAppointmentDate(request.getAppointmentDate());
                    }
                    if (request.getStatus() != null) {
                        appointment.setStatus(request.getStatus());
                    }
                    if (request.getReason() != null) {
                        appointment.setReason(request.getReason());
                    }
                    return ResponseEntity.ok(appointmentRepository.save(appointment));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            return ResponseEntity.ok().<Void>build();
        }
        return ResponseEntity.notFound().build();
    }

    // DTO class for Request Mapping
    public static class AppointmentRequest {
        private Long patientId;
        private Long doctorId;
        private LocalDateTime appointmentDate;
        private String status;
        private String reason;

        public Long getPatientId() {
            return patientId;
        }

        public void setPatientId(Long patientId) {
            this.patientId = patientId;
        }

        public Long getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(Long doctorId) {
            this.doctorId = doctorId;
        }

        public LocalDateTime getAppointmentDate() {
            return appointmentDate;
        }

        public void setAppointmentDate(LocalDateTime appointmentDate) {
            this.appointmentDate = appointmentDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
