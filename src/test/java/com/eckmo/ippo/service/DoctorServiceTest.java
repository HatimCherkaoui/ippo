package com.eckmo.ippo.service;

import com.eckmo.ippo.domain.dto.response.DoctorResponse;
import com.eckmo.ippo.domain.entity.Doctor;
import com.eckmo.ippo.domain.entity.User;
import com.eckmo.ippo.domain.enums.Role;
import com.eckmo.ippo.exception.ResourceNotFoundException;
import com.eckmo.ippo.repository.AppointmentRepository;
import com.eckmo.ippo.repository.DoctorRepository;
import com.eckmo.ippo.service.impl.DoctorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock DoctorRepository doctorRepository;
    @Mock AppointmentRepository appointmentRepository;

    @InjectMocks DoctorServiceImpl doctorService;

    private Doctor doctor;

    @BeforeEach
    void setUp() {
        User user = User.builder().id(1L).firstName("Sarah").lastName("Johnson")
                .email("doctor@test.com").role(Role.DOCTOR).password("pw").build();
        doctor = Doctor.builder().id(1L).user(user).specialization("Cardiology").licenseNumber("LIC-001").build();
    }

    @Test
    void findAll_returnsAllDoctors() {
        when(doctorRepository.findAll()).thenReturn(List.of(doctor));

        List<DoctorResponse> result = doctorService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().specialization()).isEqualTo("Cardiology");
    }

    @Test
    void findById_returnsDoctor_whenExists() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        DoctorResponse response = doctorService.findById(1L);

        assertThat(response.email()).isEqualTo("doctor@test.com");
        assertThat(response.licenseNumber()).isEqualTo("LIC-001");
    }

    @Test
    void findById_throws_whenNotFound() {
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> doctorService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Doctor not found");
    }

    @Test
    void getSchedule_returnsEmptyList_whenNoAppointments() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctor(doctor)).thenReturn(List.of());

        assertThat(doctorService.getSchedule(1L)).isEmpty();
    }

    @Test
    void getSchedule_throws_whenDoctorNotFound() {
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> doctorService.getSchedule(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
