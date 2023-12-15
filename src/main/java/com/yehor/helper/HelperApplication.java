package com.yehor.helper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.yehor.helper.services.EquationService;
import com.yehor.helper.services.RootService;

@SpringBootApplication
public class HelperApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelperApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(EquationService equationService, RootService rootService) {
		return args -> {
			String eq = "2*x+5=17";
			System.out.println("");
		};
	}

}
