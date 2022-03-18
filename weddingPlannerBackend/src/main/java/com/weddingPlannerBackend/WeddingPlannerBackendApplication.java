package com.weddingPlannerBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication( exclude = {SecurityAutoConfiguration.class} )
@EnableScheduling
public class WeddingPlannerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeddingPlannerBackendApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
}
