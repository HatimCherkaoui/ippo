package com.eckmo.ippo.service;

import com.eckmo.ippo.domain.dto.response.AppointmentResponse;
import com.eckmo.ippo.domain.dto.response.PatientResponse;

import java.util.List;

public interface PatientService {
    List<PatientResponse> findAll();
    PatientResponse findById(Long id);
    List<AppointmentResponse> getAppointments(Long patientId);
}
