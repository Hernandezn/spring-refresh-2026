package dev.hernandezn.spring2026.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.hernandezn.spring2026.util.RequestCaptor;

/**
 * Adds the RequestCaptor component to the HTTP request input flow.
 */
@Configuration
public class RequestCaptorConfig implements WebMvcConfigurer {
	@Autowired
	private RequestCaptor captor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(captor);
	}
}
