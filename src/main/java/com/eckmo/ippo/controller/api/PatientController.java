package com.eckmo.ippo.controller.api;

import com.eckmo.ippo.domain.dto.response.AppointmentResponse;
import com.eckmo.ippo.domain.dto.response.PatientResponse;
import com.eckmo.ippo.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientResponse>> findAll() {
        return ResponseEntity.ok(patientService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.findById(id));
    }

    @GetMapping("/{id}/appointments")
    public ResponseEntity<List<AppointmentResponse>> getAppointments(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getAppointments(id));
    }
}
