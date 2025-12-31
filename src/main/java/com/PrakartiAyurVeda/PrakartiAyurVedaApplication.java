package com.PrakartiAyurVeda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PrakartiAyurVedaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrakartiAyurVedaApplication.class, args);
	}

}
