package com.academy.config;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationConfigTest {

    @Test
    void datasourceUrlShouldTargetAcademyDatabase() throws IOException {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            assertNotNull(input, "application.properties should exist");
            properties.load(input);
        }

        assertEquals(
                "jdbc:mysql://localhost:3306/academy?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                properties.getProperty("spring.datasource.url")
        );
    }
}
