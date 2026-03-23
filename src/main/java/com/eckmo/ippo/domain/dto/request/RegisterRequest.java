package com.eckmo.ippo.domain.dto.request;

import com.eckmo.ippo.domain.enums.Role;
import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8) String password,
        @NotNull Role role
) {}
