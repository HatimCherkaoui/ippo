package com.eckmo.ippo.service;

import com.eckmo.ippo.domain.dto.request.RegisterRequest;
import com.eckmo.ippo.domain.dto.response.JwtResponse;
import com.eckmo.ippo.domain.entity.Doctor;
import com.eckmo.ippo.domain.entity.Patient;
import com.eckmo.ippo.domain.entity.User;
import com.eckmo.ippo.domain.enums.Role;
import com.eckmo.ippo.repository.AssistantRepository;
import com.eckmo.ippo.repository.DoctorRepository;
import com.eckmo.ippo.repository.PatientRepository;
import com.eckmo.ippo.repository.UserRepository;
import com.eckmo.ippo.security.JwtTokenProvider;
import com.eckmo.ippo.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock DoctorRepository doctorRepository;
    @Mock PatientRepository patientRepository;
    @Mock AssistantRepository assistantRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtTokenProvider jwtTokenProvider;
    @Mock AuthenticationManager authenticationManager;

    @InjectMocks AuthServiceImpl authService;

    @Test
    void register_throws_whenEmailAlreadyExists() {
        var request = new RegisterRequest("John", "Doe", "exists@test.com", "password!", Role.PATIENT);
        when(userRepository.existsByEmail("exists@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already registered");
    }

    @Test
    void register_createsDoctorProfile_whenRoleIsDoctor() {
        var request = new RegisterRequest("Sarah", "J", "new@test.com", "password!", Role.DOCTOR);
        User saved = User.builder().id(1L).email("new@test.com").role(Role.DOCTOR)
                .firstName("Sarah").lastName("J").build();

        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(saved);
        when(doctorRepository.save(any())).thenReturn(Doctor.builder().user(saved).build());
        when(jwtTokenProvider.generateToken(any())).thenReturn("token123");

        JwtResponse response = authService.register(request);

        assertThat(response.token()).isEqualTo("token123");
        assertThat(response.role()).isEqualTo("DOCTOR");
        verify(doctorRepository).save(any(Doctor.class));
        verify(patientRepository, never()).save(any());
    }

    @Test
    void register_createsPatientProfile_whenRoleIsPatient() {
        var request = new RegisterRequest("Alice", "B", "alice@test.com", "password!", Role.PATIENT);
        User saved = User.builder().id(2L).email("alice@test.com").role(Role.PATIENT)
                .firstName("Alice").lastName("B").build();

        when(userRepository.existsByEmail("alice@test.com")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any())).thenReturn(saved);
        when(patientRepository.save(any())).thenReturn(Patient.builder().user(saved).build());
        when(jwtTokenProvider.generateToken(any())).thenReturn("token456");

        JwtResponse response = authService.register(request);

        assertThat(response.token()).isEqualTo("token456");
        verify(patientRepository).save(any(Patient.class));
        verify(doctorRepository, never()).save(any());
    }
}
