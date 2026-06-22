package com.hospital.backend.controller;

import com.hospital.backend.repository.AppointmentRepository;
import com.hospital.backend.repository.BillingRepository;
import com.hospital.backend.repository.DoctorRepository;
import com.hospital.backend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BillingRepository billingRepository;

    @GetMapping("/stats")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalPatients = patientRepository.count();
        long totalDoctors = doctorRepository.count();
        long scheduledAppointments = appointmentRepository.countByStatus("SCHEDULED");
        long completedAppointments = appointmentRepository.countByStatus("COMPLETED");
        long cancelledAppointments = appointmentRepository.countByStatus("CANCELLED");
        
        Double totalRevenue = billingRepository.sumTotalPaidRevenue();
        if (totalRevenue == null) {
            totalRevenue = 0.0;
        }

        long unpaidBillsCount = billingRepository.countByStatus("UNPAID");
        long paidBillsCount = billingRepository.countByStatus("PAID");

        stats.put("totalPatients", totalPatients);
        stats.put("totalDoctors", totalDoctors);
        stats.put("scheduledAppointments", scheduledAppointments);
        stats.put("completedAppointments", completedAppointments);
        stats.put("cancelledAppointments", cancelledAppointments);
        stats.put("totalRevenue", totalRevenue);
        stats.put("unpaidBillsCount", unpaidBillsCount);
        stats.put("paidBillsCount", paidBillsCount);

        // Department breakdown or simple sample stats for charts
        Map<String, Integer> departmentStats = new HashMap<>();
        doctorRepository.findAll().forEach(doctor -> {
            String dept = doctor.getDepartment();
            if (dept != null && !dept.isEmpty()) {
                departmentStats.put(dept, departmentStats.getOrDefault(dept, 0) + 1);
            }
        });
        stats.put("departmentDoctorCounts", departmentStats);

        return stats;
    }
}
