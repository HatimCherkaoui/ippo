package com.eckmo.ippo.config;

import com.eckmo.ippo.domain.entity.*;
import com.eckmo.ippo.domain.enums.AppointmentStatus;
import com.eckmo.ippo.domain.enums.Role;
import com.eckmo.ippo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    @Bean
    CommandLineRunner seedDatabase(UserRepository userRepo,
                                   DoctorRepository doctorRepo,
                                   PatientRepository patientRepo,
                                   AssistantRepository assistantRepo,
                                   AppointmentRepository appointmentRepo,
                                   PasswordEncoder encoder) {
        return args -> {
            if (userRepo.count() > 0) {
                log.info("Database already seeded — skipping.");
                return;
            }

            // Doctor
            User doctorUser = userRepo.save(User.builder()
                    .firstName("Sarah").lastName("Johnson").email("doctor@ippo.com")
                    .password(encoder.encode("password")).role(Role.DOCTOR).build());
            Doctor doctor = doctorRepo.save(Doctor.builder()
                    .user(doctorUser).specialization("Cardiology").licenseNumber("LIC-001").build());

            // Assistant
            User assistantUser = userRepo.save(User.builder()
                    .firstName("Mark").lastName("Stevens").email("assistant@ippo.com")
                    .password(encoder.encode("password")).role(Role.ASSISTANT).build());
            assistantRepo.save(Assistant.builder().user(assistantUser).assignedDoctor(doctor).build());

            // Patient
            User patientUser = userRepo.save(User.builder()
                    .firstName("Alice").lastName("Brown").email("patient@ippo.com")
                    .password(encoder.encode("password")).role(Role.PATIENT).build());
            Patient patient = patientRepo.save(Patient.builder()
                    .user(patientUser).phone("+1-555-0100").dateOfBirth(LocalDate.of(1990, 5, 15)).build());

            // Appointments
            appointmentRepo.save(Appointment.builder()
                    .doctor(doctor).patient(patient)
                    .scheduledAt(LocalDateTime.now().plusDays(1))
                    .durationMinutes(30).status(AppointmentStatus.CONFIRMED)
                    .notes("Routine cardiovascular checkup").build());

            appointmentRepo.save(Appointment.builder()
                    .doctor(doctor).patient(patient)
                    .scheduledAt(LocalDateTime.now().plusDays(4))
                    .durationMinutes(45).status(AppointmentStatus.SCHEDULED)
                    .notes("Follow-up ECG review").build());

            log.info("Database seeded successfully!");
            log.info("Credentials:");
            log.info("  Doctor    → doctor@ippo.com    / password");
            log.info("  Assistant → assistant@ippo.com / password");
            log.info("  Patient   → patient@ippo.com   / password");
        };
    }
}
