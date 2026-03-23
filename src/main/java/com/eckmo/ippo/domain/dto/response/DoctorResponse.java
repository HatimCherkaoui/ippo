package com.eckmo.ippo.domain.dto.response;

import com.eckmo.ippo.domain.entity.Doctor;

public record DoctorResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String specialization,
        String licenseNumber
) {
    public static DoctorResponse from(Doctor doctor) {
        return new DoctorResponse(
                doctor.getId(),
                doctor.getUser().getFirstName(),
                doctor.getUser().getLastName(),
                doctor.getUser().getEmail(),
                doctor.getSpecialization(),
                doctor.getLicenseNumber()
        );
    }
}
