package com.eckmo.ippo.domain.dto.response;

import com.eckmo.ippo.domain.entity.Patient;

import java.time.LocalDate;

public record PatientResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDate dateOfBirth
) {
    public static PatientResponse from(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getUser().getFirstName(),
                patient.getUser().getLastName(),
                patient.getUser().getEmail(),
                patient.getPhone(),
                patient.getDateOfBirth()
        );
    }
}
