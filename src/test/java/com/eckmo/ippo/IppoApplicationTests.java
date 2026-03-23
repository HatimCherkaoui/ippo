package com.eckmo.ippo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Full integration test — requires a running PostgreSQL instance.
 * Run with a real DB or use Testcontainers to enable this test.
 */
@SpringBootTest
@Disabled("Requires PostgreSQL — enable when a database is available")
class IppoApplicationTests {

    @Test
    void contextLoads() {
    }

}


