package com.eckmo.ippo.integration;

import com.eckmo.ippo.AbstractIntegrationTest;
import com.eckmo.ippo.domain.dto.request.LoginRequest;
import com.eckmo.ippo.domain.dto.request.RegisterRequest;
import com.eckmo.ippo.domain.enums.Role;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class AuthIntegrationTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "";
    }

    @Test
    void register_returnsJwtAndCreatedStatus() {
        var request = new RegisterRequest("Jane", "Doe", "jane.doe@example.com", "securePass1", Role.PATIENT);

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/auth/register")
        .then()
            .statusCode(201)
            .body("token", not(emptyOrNullString()))
            .body("type", equalTo("Bearer"))
            .body("email", equalTo("jane.doe@example.com"))
            .body("role", equalTo("PATIENT"));
    }

    @Test
    void login_withValidCredentials_returnsJwt() {
        // Register first
        var registerRequest = new RegisterRequest("John", "Smith", "john.smith@example.com", "myPassword1", Role.DOCTOR);
        given().contentType(ContentType.JSON).body(registerRequest).post("/api/auth/register").then().statusCode(201);

        // Then login
        var loginRequest = new LoginRequest("john.smith@example.com", "myPassword1");
        given()
            .contentType(ContentType.JSON)
            .body(loginRequest)
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(200)
            .body("token", not(emptyOrNullString()))
            .body("email", equalTo("john.smith@example.com"));
    }

    @Test
    void login_withWrongPassword_returns401() {
        var registerRequest = new RegisterRequest("Bob", "Wrong", "bob.wrong@example.com", "rightPassword1", Role.PATIENT);
        given().contentType(ContentType.JSON).body(registerRequest).post("/api/auth/register").then().statusCode(201);

        var loginRequest = new LoginRequest("bob.wrong@example.com", "wrongPassword");
        given()
            .contentType(ContentType.JSON)
            .body(loginRequest)
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(401);
    }

    @Test
    void register_withInvalidEmail_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                    {"firstName":"X","lastName":"Y","email":"not-an-email",
                     "password":"pass1234","role":"PATIENT"}
                    """)
        .when()
            .post("/api/auth/register")
        .then()
            .statusCode(400);
    }
}
