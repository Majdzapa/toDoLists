package com.todolists.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
// we'll add the SpringSecurity
	// we'll add the SpringSecurity
	@Override
	public void addCorsMappings(CorsRegistry corsRegistry) {
		corsRegistry.addMapping("/**").allowedMethods("*");
		corsRegistry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST",
				"PUT", "DELETE");

	}


}