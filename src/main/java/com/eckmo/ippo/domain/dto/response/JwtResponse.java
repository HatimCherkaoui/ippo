package com.eckmo.ippo.domain.dto.response;

public record JwtResponse(
        String token,
        String type,
        String email,
        String role,
        String firstName,
        String lastName
) {
    public JwtResponse(String token, String email, String role, String firstName, String lastName) {
        this(token, "Bearer", email, role, firstName, lastName);
    }
}
