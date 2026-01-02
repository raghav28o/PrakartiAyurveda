package com.PrakartiAyurVeda;

import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PrakartiAyurVedaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrakartiAyurVedaApplication.class, args);
	}


	@Bean
	public SimpleLoggerAdvisor simpleLoggerAdvisor() {
		return new SimpleLoggerAdvisor();
	}

}
