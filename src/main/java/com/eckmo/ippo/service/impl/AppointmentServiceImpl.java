package com.eckmo.ippo.service.impl;

import com.eckmo.ippo.domain.dto.request.AppointmentRequest;
import com.eckmo.ippo.domain.dto.response.AppointmentResponse;
import com.eckmo.ippo.domain.entity.Appointment;
import com.eckmo.ippo.domain.entity.Doctor;
import com.eckmo.ippo.domain.entity.Patient;
import com.eckmo.ippo.domain.enums.AppointmentStatus;
import com.eckmo.ippo.exception.ResourceNotFoundException;
import com.eckmo.ippo.repository.AppointmentRepository;
import com.eckmo.ippo.repository.DoctorRepository;
import com.eckmo.ippo.repository.PatientRepository;
import com.eckmo.ippo.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public AppointmentResponse create(AppointmentRequest request) {
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + request.doctorId()));
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + request.patientId()));

        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .scheduledAt(request.scheduledAt())
                .durationMinutes(request.durationMinutes() > 0 ? request.durationMinutes() : 30)
                .notes(request.notes())
                .build();

        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse findById(Long id) {
        return appointmentRepository.findById(id)
                .map(AppointmentResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id));
    }

    @Override
    public List<AppointmentResponse> findAll() {
        return appointmentRepository.findAll().stream()
                .map(AppointmentResponse::from)
                .toList();
    }

    @Override
    public List<AppointmentResponse> findByDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + doctorId));
        return appointmentRepository.findByDoctor(doctor).stream()
                .map(AppointmentResponse::from)
                .toList();
    }

    @Override
    public List<AppointmentResponse> findByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + patientId));
        return appointmentRepository.findByPatient(patient).stream()
                .map(AppointmentResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public AppointmentResponse update(Long id, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id));
        Doctor doctor = doctorRepository.findById(request.doctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + request.doctorId()));
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + request.patientId()));

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setScheduledAt(request.scheduledAt());
        appointment.setDurationMinutes(request.durationMinutes() > 0 ? request.durationMinutes() : 30);
        appointment.setNotes(request.notes());

        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public AppointmentResponse updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id));
        appointment.setStatus(status);
        return AppointmentResponse.from(appointmentRepository.save(appointment));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Appointment not found: " + id);
        }
        appointmentRepository.deleteById(id);
    }
}
