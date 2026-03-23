package com.eckmo.ippo;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Base class for integration tests.
 * <p>
 * Starts a single shared PostgreSQL container once via a static initializer block.
 * Using a static initializer (rather than {@code @Testcontainers} / {@code @Container})
 * prevents the JUnit extension from stopping the container between test classes, which
 * would otherwise invalidate Spring's cached {@link org.springframework.context.ApplicationContext}
 * and cause "connection refused" errors in subsequent test classes.
 * <p>
 * The container is cleaned up by Testcontainers' Ryuk sidecar when the JVM exits.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
public abstract class AbstractIntegrationTest {

    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:17-alpine")
                    .withDatabaseName("ippo_db")
                    .withUsername("postgres")
                    .withPassword("postgres");

    static {
        POSTGRES.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }
}
