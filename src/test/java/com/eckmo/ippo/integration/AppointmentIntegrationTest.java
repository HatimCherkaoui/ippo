package com.eckmo.ippo.integration;

import com.eckmo.ippo.AbstractIntegrationTest;
import com.eckmo.ippo.domain.dto.request.RegisterRequest;
import com.eckmo.ippo.domain.enums.Role;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class AppointmentIntegrationTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    private String doctorToken;
    private String patientToken;
    private long doctorId;
    private long patientId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        // Register doctor
        Response doctorReg = given()
            .contentType(ContentType.JSON)
            .body(new RegisterRequest("Alice", "Doctor", uniqueEmail("doctor"), "pass1234!", Role.DOCTOR))
            .post("/api/auth/register");
        doctorToken = doctorReg.jsonPath().getString("token");

        // Register patient
        Response patientReg = given()
            .contentType(ContentType.JSON)
            .body(new RegisterRequest("Bob", "Patient", uniqueEmail("patient"), "pass1234!", Role.PATIENT))
            .post("/api/auth/register");
        patientToken = patientReg.jsonPath().getString("token");

        String doctorEmail = doctorReg.jsonPath().getString("email");
        String patientEmail = patientReg.jsonPath().getString("email");

        // Fetch doctor profile to get ID — filter by email to avoid picking seeded data
        doctorId = given()
            .header("Authorization", "Bearer " + doctorToken)
            .get("/api/doctors")
            .jsonPath()
            .param("email", doctorEmail)
            .getLong("find { it.email == email }.id");

        // Fetch patient profile to get ID — filter by email to avoid picking seeded data
        patientId = given()
            .header("Authorization", "Bearer " + patientToken)
            .get("/api/patients")
            .jsonPath()
            .param("email", patientEmail)
            .getLong("find { it.email == email }.id");
    }

    @Test
    void createAppointment_asPatient_returnsCreated() {
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + patientToken)
            .body(appointmentBody(doctorId, patientId, LocalDateTime.now().plusDays(2)))
        .when()
            .post("/api/appointments")
        .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("status", equalTo("SCHEDULED"));
    }

    @Test
    void listAppointments_asDoctor_returnsOk() {
        // Create an appointment first
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + patientToken)
            .body(appointmentBody(doctorId, patientId, LocalDateTime.now().plusDays(3)))
            .post("/api/appointments");

        given()
            .header("Authorization", "Bearer " + doctorToken)
        .when()
            .get("/api/appointments")
        .then()
            .statusCode(200)
            .body("$", not(empty()));
    }

    @Test
    void listAppointments_unauthenticated_returns401() {
        given()
        .when()
            .get("/api/appointments")
        .then()
            .statusCode(401);
    }

    @Test
    void updateAppointmentStatus_asDoctor_returnsUpdated() {
        long apptId = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + patientToken)
            .body(appointmentBody(doctorId, patientId, LocalDateTime.now().plusDays(5)))
            .post("/api/appointments")
            .jsonPath().getLong("id");

        given()
            .header("Authorization", "Bearer " + doctorToken)
            .queryParam("status", "CONFIRMED")
        .when()
            .patch("/api/appointments/" + apptId + "/status")
        .then()
            .statusCode(200)
            .body("status", equalTo("CONFIRMED"));
    }

    @Test
    void deleteAppointment_asDoctor_returns204() {
        long apptId = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + patientToken)
            .body(appointmentBody(doctorId, patientId, LocalDateTime.now().plusDays(7)))
            .post("/api/appointments")
            .jsonPath().getLong("id");

        given()
            .header("Authorization", "Bearer " + doctorToken)
        .when()
            .delete("/api/appointments/" + apptId)
        .then()
            .statusCode(204);
    }

    // ---- helpers ----

    private String uniqueEmail(String prefix) {
        return prefix + "." + System.nanoTime() + "@test.com";
    }

    private String appointmentBody(long doctorId, long patientId, LocalDateTime at) {
        return """
                {"doctorId":%d,"patientId":%d,"scheduledAt":"%s","durationMinutes":30,"notes":"Test appointment"}
                """.formatted(doctorId, patientId, at);
    }
}
