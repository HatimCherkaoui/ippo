package com.eckmo.ippo.controller.web;

import com.eckmo.ippo.service.AppointmentService;
import com.eckmo.ippo.service.DoctorService;
import com.eckmo.ippo.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails user, Model model) {
        var appointments = appointmentService.findAll();
        model.addAttribute("appointments", appointments);
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("totalAppointments", appointments.size());
        model.addAttribute("currentUser", user.getUsername());
        return "dashboard";
    }

    @GetMapping("/appointments")
    public String appointments(Model model) {
        model.addAttribute("appointments", appointmentService.findAll());
        return "appointments";
    }

    @GetMapping("/schedule")
    public String schedule(Model model) {
        model.addAttribute("doctors", doctorService.findAll());
        return "schedule";
    }
}
