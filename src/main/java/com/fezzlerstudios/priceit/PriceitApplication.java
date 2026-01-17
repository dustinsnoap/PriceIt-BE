package com.fezzlerstudios.priceit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.fezzlerstudios.priceit")
public class PriceitApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceitApplication.class, args);
	}

}
