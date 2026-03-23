package com.eckmo.ippo.service.impl;

import com.eckmo.ippo.domain.dto.request.LoginRequest;
import com.eckmo.ippo.domain.dto.request.RegisterRequest;
import com.eckmo.ippo.domain.dto.response.JwtResponse;
import com.eckmo.ippo.domain.entity.Assistant;
import com.eckmo.ippo.domain.entity.Doctor;
import com.eckmo.ippo.domain.entity.Patient;
import com.eckmo.ippo.domain.entity.User;
import com.eckmo.ippo.domain.enums.Role;
import com.eckmo.ippo.repository.AssistantRepository;
import com.eckmo.ippo.repository.DoctorRepository;
import com.eckmo.ippo.repository.PatientRepository;
import com.eckmo.ippo.repository.UserRepository;
import com.eckmo.ippo.security.JwtTokenProvider;
import com.eckmo.ippo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AssistantRepository assistantRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = (User) auth.getPrincipal();
        String token = jwtTokenProvider.generateToken(user);
        return new JwtResponse(token, user.getEmail(), user.getRole().name(),
                user.getFirstName(), user.getLastName());
    }

    @Override
    @Transactional
    public JwtResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already registered: " + request.email());
        }
        User user = userRepository.save(User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .build());

        createProfile(user, request.role());

        String token = jwtTokenProvider.generateToken(user);
        return new JwtResponse(token, user.getEmail(), user.getRole().name(),
                user.getFirstName(), user.getLastName());
    }

    private void createProfile(User user, Role role) {
        switch (role) {
            case DOCTOR    -> doctorRepository.save(Doctor.builder().user(user).specialization("General").build());
            case PATIENT   -> patientRepository.save(Patient.builder().user(user).build());
            case ASSISTANT -> assistantRepository.save(Assistant.builder().user(user).build());
        }
    }
}
