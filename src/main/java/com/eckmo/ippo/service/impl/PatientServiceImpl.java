package com.eckmo.ippo.service.impl;

import com.eckmo.ippo.domain.dto.response.AppointmentResponse;
import com.eckmo.ippo.domain.dto.response.PatientResponse;
import com.eckmo.ippo.domain.entity.Patient;
import com.eckmo.ippo.exception.ResourceNotFoundException;
import com.eckmo.ippo.repository.AppointmentRepository;
import com.eckmo.ippo.repository.PatientRepository;
import com.eckmo.ippo.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public List<PatientResponse> findAll() {
        return patientRepository.findAll().stream()
                .map(PatientResponse::from)
                .toList();
    }

    @Override
    public PatientResponse findById(Long id) {
        return patientRepository.findById(id)
                .map(PatientResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
    }

    @Override
    public List<AppointmentResponse> getAppointments(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + patientId));
        return appointmentRepository.findByPatient(patient).stream()
                .map(AppointmentResponse::from)
                .toList();
    }
}
