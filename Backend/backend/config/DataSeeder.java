package com.hospital.backend.config;

import com.hospital.backend.model.Appointment;
import com.hospital.backend.model.Billing;
import com.hospital.backend.model.Doctor;
import com.hospital.backend.model.Patient;
import com.hospital.backend.repository.AppointmentRepository;
import com.hospital.backend.repository.BillingRepository;
import com.hospital.backend.repository.DoctorRepository;
import com.hospital.backend.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private com.hospital.backend.repository.UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Seed default admin user
        if (userRepository.count() == 0) {
            com.hospital.backend.model.User admin = new com.hospital.backend.model.User(
                    "admin",
                    passwordEncoder.encode("admin123"),
                    "ROLE_ADMIN"
            );
            userRepository.save(admin);
            System.out.println("Seeded default admin user: admin / admin123");
        }

        // Only seed if no doctors exist
        if (doctorRepository.count() == 0) {
            System.out.println("No data found in MySQL. Seeding sample database records...");

            // 1. Seed Doctors
            Doctor doc1 = new Doctor("Dr. Alice Smith", "alice.smith@hospital.com", "+1-555-0199", "Cardiology", "Cardiology Dept", "Mon-Wed: 9 AM - 3 PM");
            Doctor doc2 = new Doctor("Dr. Robert Chen", "robert.chen@hospital.com", "+1-555-0120", "Pediatrics", "Pediatrics Dept", "Tue-Thu: 10 AM - 4 PM");
            Doctor doc3 = new Doctor("Dr. Sarah Jenkins", "sarah.jenkins@hospital.com", "+1-555-0155", "Neurology", "Neurology Dept", "Mon, Fri: 9 AM - 1 PM");
            Doctor doc4 = new Doctor("Dr. David Miller", "david.miller@hospital.com", "+1-555-0188", "Orthopedics", "Orthopedics Dept", "Wed-Fri: 1 PM - 5 PM");
            Doctor doc5 = new Doctor("Dr. Emily Taylor", "emily.taylor@hospital.com", "+1-555-0111", "General Medicine", "General Outpatient", "Mon-Fri: 8 AM - 12 PM");

            List<Doctor> doctors = doctorRepository.saveAll(Arrays.asList(doc1, doc2, doc3, doc4, doc5));
            System.out.println("Seeded " + doctors.size() + " doctors.");

            // 2. Seed Patients
            Patient pat1 = new Patient("John Doe", "john.doe@email.com", "+1-555-1234", "Male", LocalDate.of(1985, 5, 12), "123 Maple Street, NY", "Chronic hypertension, allergy to penicillin.");
            Patient pat2 = new Patient("Mary Johnson", "mary.johnson@email.com", "+1-555-5678", "Female", LocalDate.of(1992, 10, 22), "456 Oak Avenue, CA", "Asthma, mild dust allergy.");
            Patient pat3 = new Patient("Robert Williams", "robert.w@email.com", "+1-555-9012", "Male", LocalDate.of(1978, 3, 5), "789 Pine Road, TX", "Type 2 Diabetes, on Metformin.");
            Patient pat4 = new Patient("Patricia Brown", "patricia.b@email.com", "+1-555-3456", "Female", LocalDate.of(2005, 8, 14), "101 Cedar Lane, FL", "No major medical history, seasonal pollen allergy.");
            Patient pat5 = new Patient("Michael Jones", "michael.j@email.com", "+1-555-7890", "Male", LocalDate.of(1963, 12, 30), "202 Birch Court, IL", "Post-coronary angioplasty (2024), taking Aspirin.");
            Patient pat6 = new Patient("Linda Davis", "linda.d@email.com", "+1-555-2345", "Female", LocalDate.of(1999, 1, 15), "303 Walnut Way, WA", "Frequent migraines, prescription lens user.");

            List<Patient> patients = patientRepository.saveAll(Arrays.asList(pat1, pat2, pat3, pat4, pat5, pat6));
            System.out.println("Seeded " + patients.size() + " patients.");

            // 3. Seed Appointments
            Appointment appt1 = new Appointment(pat1, doc1, LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0), "SCHEDULED", "Routine cardiac checkup");
            Appointment appt2 = new Appointment(pat2, doc2, LocalDateTime.now().plusDays(2).withHour(11).withMinute(30).withSecond(0).withNano(0), "SCHEDULED", "Asthma follow-up");
            Appointment appt3 = new Appointment(pat3, doc5, LocalDateTime.now().minusDays(2).withHour(9).withMinute(0).withSecond(0).withNano(0), "COMPLETED", "Blood sugar monitoring");
            Appointment appt4 = new Appointment(pat4, doc2, LocalDateTime.now().plusDays(3).withHour(14).withMinute(0).withSecond(0).withNano(0), "SCHEDULED", "General pediatric consultation");
            Appointment appt5 = new Appointment(pat5, doc1, LocalDateTime.now().minusDays(5).withHour(10).withMinute(0).withSecond(0).withNano(0), "COMPLETED", "Post-surgery recovery check");
            Appointment appt6 = new Appointment(pat6, doc3, LocalDateTime.now().plusDays(1).withHour(13).withMinute(30).withSecond(0).withNano(0), "SCHEDULED", "Severe headache assessment");

            List<Appointment> appointments = appointmentRepository.saveAll(Arrays.asList(appt1, appt2, appt3, appt4, appt5, appt6));
            System.out.println("Seeded " + appointments.size() + " appointments.");

            // 4. Seed Billings
            Billing bill1 = new Billing(pat1, 250.00, LocalDate.now().minusDays(1), "PAID", "Cardiology Consultation, ECG");
            Billing bill2 = new Billing(pat2, 120.00, LocalDate.now().minusDays(2), "PAID", "Pediatric Consultation");
            Billing bill3 = new Billing(pat3, 75.00, LocalDate.now(), "UNPAID", "General Medicine Consultation, Blood Glucose Test");
            Billing bill4 = new Billing(pat5, 1200.00, LocalDate.now().minusDays(5), "PAID", "Cardiology Consultation, Echocardiogram, Lab Work");
            Billing bill5 = new Billing(pat6, 350.00, LocalDate.now().minusDays(1), "UNPAID", "Neurology Consultation, MRI Scan Brain (Partial)");

            List<Billing> billings = billingRepository.saveAll(Arrays.asList(bill1, bill2, bill3, bill4, bill5));
            System.out.println("Seeded " + billings.size() + " billing records.");
        } else {
            System.out.println("MySQL database already contains records. Skipping seeder.");
        }
    }
}
