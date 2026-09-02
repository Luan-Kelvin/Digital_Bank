package com.Lk.DigitalBank;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DigitalBankApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// termine os patch em credit card, lembre de criar dto para cada tipo de aletrção
	}
}
