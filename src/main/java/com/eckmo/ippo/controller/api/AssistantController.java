package com.eckmo.ippo.controller.api;

import com.eckmo.ippo.domain.dto.response.AssistantResponse;
import com.eckmo.ippo.service.AssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assistants")
@RequiredArgsConstructor
public class AssistantController {

    private final AssistantService assistantService;

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'ASSISTANT')")
    public ResponseEntity<List<AssistantResponse>> findAll() {
        return ResponseEntity.ok(assistantService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ASSISTANT')")
    public ResponseEntity<AssistantResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(assistantService.findById(id));
    }
}
