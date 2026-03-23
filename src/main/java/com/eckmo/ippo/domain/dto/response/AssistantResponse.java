package com.eckmo.ippo.domain.dto.response;

import com.eckmo.ippo.domain.entity.Assistant;

public record AssistantResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Long assignedDoctorId,
        String assignedDoctorName
) {
    public static AssistantResponse from(Assistant assistant) {
        return new AssistantResponse(
                assistant.getId(),
                assistant.getUser().getFirstName(),
                assistant.getUser().getLastName(),
                assistant.getUser().getEmail(),
                assistant.getAssignedDoctor() != null ? assistant.getAssignedDoctor().getId() : null,
                assistant.getAssignedDoctor() != null ? assistant.getAssignedDoctor().getUser().getFullName() : null
        );
    }
}
