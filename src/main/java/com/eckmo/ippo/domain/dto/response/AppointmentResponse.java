package com.eckmo.ippo.domain.dto.response;

import com.eckmo.ippo.domain.entity.Appointment;
import com.eckmo.ippo.domain.enums.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        DoctorResponse doctor,
        PatientResponse patient,
        LocalDateTime scheduledAt,
        int durationMinutes,
        AppointmentStatus status,
        String notes,
        LocalDateTime createdAt
) {
    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                DoctorResponse.from(appointment.getDoctor()),
                PatientResponse.from(appointment.getPatient()),
                appointment.getScheduledAt(),
                appointment.getDurationMinutes(),
                appointment.getStatus(),
                appointment.getNotes(),
                appointment.getCreatedAt()
        );
    }
}
