package dev.hernandezn.spring2026.config;

import org.openqa.selenium.devtools.v139.fetch.model.AuthChallengeResponse.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
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
 			// cross-site request forgery - added protection to NOT DISABLE for endpoints that change application state (like POST & DELETE requests)
			.csrf(csrf -> csrf.disable())
 			.authorizeHttpRequests(authorize -> 
 				authorize
// 					// allows the static index html page to be served without authentication; permitAll() permits all
// 					.requestMatchers(HttpMethod.GET, "/", "/index.html", "/static/index.html")
// 					.permitAll()
// 					
// 					// allows the login page below to be served without authentication
// 					.requestMatchers("/login").permitAll()
// 					
// 					// only allows AUTHENTICATED clients to be served by  any other endpoints
// 					.anyRequest()
// 					.authenticated()
 				
 					.requestMatchers("/user-endpoint").hasRole("USER")
					.requestMatchers("/admin-endpoint").hasRole("ADMIN")
 					.anyRequest().permitAll()
 			)
 			// server-side renders "/login" and "/logout" HTML pages and redirects unauthorized requests to "/login"
// 			.formLogin(Customizer.withDefaults())
 			// enables the /login endpoint without rendering any HTML views; this view must be defined by the developer
 			.formLogin(form -> 
 				form
// 					.loginPage("/login").permitAll()
 				
 					// POST endpoint; needing CSRF disabled here
 					.loginProcessingUrl("/login")
 					.successHandler((request, response, auth) -> {
 						// handling response like I might do with Node.JS
 						response.setStatus(200);
 						response.getWriter().write("Login successful");
 					})
 					.failureHandler((request, response, ex) -> {
 						response.setStatus(401);
 						response.getWriter().write("Login failed");
 					})
			)
 			// enables a POST /logout endpoint
 			.logout(logout -> 
 				logout
 					.logoutUrl("/logout")
 					.logoutSuccessHandler((request, response, auth) -> {
 						response.setStatus(200);
 						response.getWriter().write("Logged out");
 					})
			)
		;
 		
 		return http.build();
 	}
	
	// registers basic users
	@Bean
 	public UserDetailsService userDetailsService() {
 		UserDetails user = User.withUsername("user")
 			// "noop" for no password encoding
 			.password("{noop}password")
 			.roles("USER")
 			.build()
		;
 		
 		UserDetails admin = User.withUsername("admin")
 			.password("{noop}password")
 			.roles("USER", "ADMIN")
 			.build()
		;
 		
 		// the type telegraphs it; this is just for in-memory users, not for any live base of users
 		return new InMemoryUserDetailsManager(user, admin);
 	}
}
