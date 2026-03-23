package com.eckmo.ippo.service.impl;

import com.eckmo.ippo.domain.dto.response.AppointmentResponse;
import com.eckmo.ippo.domain.dto.response.DoctorResponse;
import com.eckmo.ippo.domain.entity.Doctor;
import com.eckmo.ippo.exception.ResourceNotFoundException;
import com.eckmo.ippo.repository.AppointmentRepository;
import com.eckmo.ippo.repository.DoctorRepository;
import com.eckmo.ippo.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public List<DoctorResponse> findAll() {
        return doctorRepository.findAll().stream()
                .map(DoctorResponse::from)
                .toList();
    }

    @Override
    public DoctorResponse findById(Long id) {
        return doctorRepository.findById(id)
                .map(DoctorResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + id));
    }

    @Override
    public List<AppointmentResponse> getSchedule(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + doctorId));
        return appointmentRepository.findByDoctor(doctor).stream()
                .map(AppointmentResponse::from)
                .toList();
    }
}
