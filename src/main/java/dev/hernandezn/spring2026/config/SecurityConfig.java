package dev.hernandezn.spring2026.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
	
	// https://docs.spring.io/spring-security/reference/api/java/org/springframework/security/config/annotation/web/builders/HttpSecurity.html
	@Bean
 	public SecurityFilterChain securityFilterChain(HttpSecurity http)  {
 		http
 			// cross-site request forgery - added protection for endpoints that change application state (like POST & DELETE requests)
// 			.csrf(csrf -> csrf.disable())
 			.authorizeHttpRequests((authorize) -> 
 				authorize
 					// allows the static index html page to be served, but no other endpoints
 					.requestMatchers(HttpMethod.GET, "/", "/index.html", "/static/index.html")
 					.permitAll()
 					
 					// only allows AUTHENTICATED clients to be served by  any other endpoints
 					.anyRequest()
 					.authenticated()
// 					.hasRole("USER")
 			)
 			// renders a "/login" HTML page and redirects unauthorized requests to that endpoint
// 			.formLogin(Customizer.withDefaults())
		;
 		
 		return http.build();
 	}
	
	@Bean
 	public UserDetailsService userDetailsService() {
 		UserDetails user = User
 			.withUsername("user")
 			.password("{noop}password")
 			.roles("USER")
 			.build()
		;
 		
 		UserDetails admin = User
 			.withUsername("admin")
 			.password("{noop}password")
 			.roles("ADMIN")
 			.build()
		;
 		
 		return new InMemoryUserDetailsManager(user);
 	}
}
