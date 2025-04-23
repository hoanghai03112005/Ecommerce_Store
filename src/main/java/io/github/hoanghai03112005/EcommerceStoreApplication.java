package io.github.hoanghai03112005;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackages = "io.github.hoanghai03112005.models")
@EnableScheduling
public class EcommerceStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceStoreApplication.class, args);
	}

}
