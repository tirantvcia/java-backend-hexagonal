package com.savily.hexagonal.backend.testing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class BackendTestingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendTestingApplication.class, args);
	}

}
