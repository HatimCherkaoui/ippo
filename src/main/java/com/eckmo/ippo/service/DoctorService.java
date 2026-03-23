package com.eckmo.ippo.service;

import com.eckmo.ippo.domain.dto.response.AppointmentResponse;
import com.eckmo.ippo.domain.dto.response.DoctorResponse;

import java.util.List;

public interface DoctorService {
    List<DoctorResponse> findAll();
    DoctorResponse findById(Long id);
    List<AppointmentResponse> getSchedule(Long doctorId);
}
