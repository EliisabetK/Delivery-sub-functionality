package com.example.trialtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TrialtaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrialtaskApplication.class, args);

	}
}
