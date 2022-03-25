package com.birdseyeapi.birdseyeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BirdseyeapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BirdseyeapiApplication.class, args);
	}
	
	// @Bean
	// public WebMvcConfigurer corsConfigurer() {
	// 	return new WebMvcConfigurer() {
	// 		@Override
	// 		public void addCorsMappings(CorsRegistry registry) {
	// 			registry
	// 				.addMapping("/**")
	// 				.allowedOrigins(
	// 					"http://localhost:3000", 
	// 					"http://126.85.37.36:3000",
	// 					"https://birds-eye.ts-soda.net2");
	// 		}
	// 	};
	// }
}
