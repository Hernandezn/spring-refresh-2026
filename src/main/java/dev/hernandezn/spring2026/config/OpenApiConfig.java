package dev.hernandezn.spring2026.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

/**
 * Bean provider for Spring OpenAPI to deliver project info in Swagger UI
 */
@Configuration
public class OpenApiConfig {
	
	@Bean
	public OpenAPI configuredOpenAPI() {
		return new OpenAPI().info(
			new Info()
				.title("Spring Refresh 2026")
				.version("0.0.1")
				.description("A development sandbox for a Spring Boot REST API server")
				.contact(new Contact()
					.name("Nicolas Hernandez")
					.url("https://github.com/Hernandezn")
				)
		);
	}
}
