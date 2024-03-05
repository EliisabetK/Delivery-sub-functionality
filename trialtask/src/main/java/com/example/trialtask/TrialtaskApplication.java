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

		long unixTimestamp = 1709650800L;
		LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTimestamp), ZoneOffset.UTC);
		System.out.println("DateTime: " + dateTime);

		SpringApplication.run(TrialtaskApplication.class, args);

	}
}
