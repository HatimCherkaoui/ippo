package com.eckmo.ippo.service;

import com.eckmo.ippo.domain.dto.request.AppointmentRequest;
import com.eckmo.ippo.domain.dto.response.AppointmentResponse;
import com.eckmo.ippo.domain.entity.Appointment;
import com.eckmo.ippo.domain.entity.Doctor;
import com.eckmo.ippo.domain.entity.Patient;
import com.eckmo.ippo.domain.entity.User;
import com.eckmo.ippo.domain.enums.AppointmentStatus;
import com.eckmo.ippo.domain.enums.Role;
import com.eckmo.ippo.exception.ResourceNotFoundException;
import com.eckmo.ippo.repository.AppointmentRepository;
import com.eckmo.ippo.repository.DoctorRepository;
import com.eckmo.ippo.repository.PatientRepository;
import com.eckmo.ippo.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock AppointmentRepository appointmentRepository;
    @Mock DoctorRepository doctorRepository;
    @Mock PatientRepository patientRepository;

    @InjectMocks AppointmentServiceImpl appointmentService;

    private Doctor doctor;
    private Patient patient;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        User doctorUser = User.builder().id(1L).firstName("Sarah").lastName("Johnson")
                .email("doctor@test.com").role(Role.DOCTOR).password("pw").build();
        doctor = Doctor.builder().id(1L).user(doctorUser).specialization("Cardiology").licenseNumber("LIC-001").build();

        User patientUser = User.builder().id(2L).firstName("Alice").lastName("Brown")
                .email("patient@test.com").role(Role.PATIENT).password("pw").build();
        patient = Patient.builder().id(1L).user(patientUser).build();

        appointment = Appointment.builder()
                .id(1L).doctor(doctor).patient(patient)
                .scheduledAt(LocalDateTime.now().plusDays(1))
                .durationMinutes(30).status(AppointmentStatus.SCHEDULED)
                .build();
    }

    @Test
    void findById_returnsAppointment_whenExists() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        AppointmentResponse response = appointmentService.findById(1L);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.status()).isEqualTo(AppointmentStatus.SCHEDULED);
    }

    @Test
    void findById_throws_whenNotFound() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void findAll_returnsAllAppointments() {
        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));

        assertThat(appointmentService.findAll()).hasSize(1);
    }

    @Test
    void create_savesAndReturnsAppointment() {
        AppointmentRequest request = new AppointmentRequest(1L, 1L, LocalDateTime.now().plusDays(2), 30, "Test");
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(appointmentRepository.save(any())).thenReturn(appointment);

        AppointmentResponse response = appointmentService.create(request);

        assertThat(response).isNotNull();
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void create_throws_whenDoctorNotFound() {
        AppointmentRequest request = new AppointmentRequest(99L, 1L, LocalDateTime.now().plusDays(2), 30, null);
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Doctor not found");
    }

    @Test
    void create_throws_whenPatientNotFound() {
        AppointmentRequest request = new AppointmentRequest(1L, 99L, LocalDateTime.now().plusDays(2), 30, null);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Patient not found");
    }

    @Test
    void updateStatus_updatesAndReturns() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);

        AppointmentResponse response = appointmentService.updateStatus(1L, AppointmentStatus.CONFIRMED);

        assertThat(response).isNotNull();
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void delete_deletesWhenExists() {
        when(appointmentRepository.existsById(1L)).thenReturn(true);

        appointmentService.delete(1L);

        verify(appointmentRepository).deleteById(1L);
    }

    @Test
    void delete_throws_whenNotFound() {
        when(appointmentRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> appointmentService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
