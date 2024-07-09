package com.swyp.gaezzange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GaezzangeApplication {

	public static void main(String[] args) {
//		System.setProperty("server.servlet.contextPath", "/api");
		SpringApplication.run(GaezzangeApplication.class, args);
	}

}
