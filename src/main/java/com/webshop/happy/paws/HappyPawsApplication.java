package com.webshop.happy.paws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;

@SpringBootApplication(exclude = GsonAutoConfiguration.class)
public class HappyPawsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HappyPawsApplication.class, args);
	}

}
