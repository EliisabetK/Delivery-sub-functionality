package com.example.trialtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@EnableScheduling
@SpringBootApplication
public class TrialtaskApplication {

	public static void main(String[] args) {
		// long unixTimestamp = 1709925299L;
		// LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTimestamp), ZoneOffset.UTC);
		// System.out.println("DateTime: " + dateTime);

		System.out.println("Interface to get delivery fees: http://localhost:8080/swagger-ui/index.html");
		System.out.println("Access to the database: http://localhost:8080/h2-console");
		System.out.println("Username and password should be empty");
		SpringApplication.run(TrialtaskApplication.class, args);
	}
}
