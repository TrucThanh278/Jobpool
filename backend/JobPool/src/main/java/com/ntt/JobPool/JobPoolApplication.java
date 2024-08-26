package com.ntt.JobPool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// @SpringBootApplication(exclude = {
// 		SecurityAutoConfiguration.class,
// 		ManagementWebSecurityAutoConfiguration.class
// })
@SpringBootApplication
public class JobPoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobPoolApplication.class, args);
	}

}
