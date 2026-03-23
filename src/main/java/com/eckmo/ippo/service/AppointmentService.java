package com.eckmo.ippo.service;

import com.eckmo.ippo.domain.dto.request.AppointmentRequest;
import com.eckmo.ippo.domain.dto.response.AppointmentResponse;
import com.eckmo.ippo.domain.enums.AppointmentStatus;

import java.util.List;

public interface AppointmentService {
    AppointmentResponse create(AppointmentRequest request);
    AppointmentResponse findById(Long id);
    List<AppointmentResponse> findAll();
    List<AppointmentResponse> findByDoctor(Long doctorId);
    List<AppointmentResponse> findByPatient(Long patientId);
    AppointmentResponse update(Long id, AppointmentRequest request);
    AppointmentResponse updateStatus(Long id, AppointmentStatus status);
    void delete(Long id);
}
