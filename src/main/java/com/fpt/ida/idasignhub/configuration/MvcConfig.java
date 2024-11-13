package com.fpt.ida.idasignhub.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("https://isoma.fpt.com/", "https://157.66.96.69", "http://157.66.96.69:8082/","http://localhost/", "https://example.com", "http://localhost:81/")  // List specific origins
				.allowedMethods("GET", "POST", "PUT", "DELETE")
				.allowedHeaders("*")
				.allowCredentials(true);  // Enable credentials
	}

	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/home").setViewName("/dashboard/index");
		registry.addViewController("/").setViewName("/dashboard/index");
		registry.addViewController("/cau-hinh-ky-so").setViewName("/config/index");

	}
}
