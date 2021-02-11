package com.kuke.videomeeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class VideomeetingApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideomeetingApplication.class, args);
	}

}
