package dev.hernandezn.spring2026.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.hernandezn.spring2026.util.RequestCaptor;

/**
 * Adds the RequestCaptor component to the HTTP request input flow.
 * 
 * The ConditionalProperty sets a Spring configuration variable that allows 
 * this configuration to be disabled during controller unit testing.
 */
@Configuration
@ConditionalOnProperty(name="request-captor.enabled", havingValue="true", matchIfMissing=true)
public class RequestCaptorConfig implements WebMvcConfigurer {
	
	private final RequestCaptor captor;
	
	@Autowired
	public RequestCaptorConfig(RequestCaptor captor) {
		this.captor = captor;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(captor);
	}
}
