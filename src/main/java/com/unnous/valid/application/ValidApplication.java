package com.unnous.valid.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.unnous.valid")
public class ValidApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValidApplication.class, args);
	}

}
