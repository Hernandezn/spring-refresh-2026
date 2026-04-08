package dev.hernandezn.spring2026.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.hernandezn.spring2026.util.RequestCaptor;

@WebMvcTest(controllers=RootController.class)
@TestPropertySource(properties="request-captor.enabled=false")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RootControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private RequestCaptor mockCaptor;
	
	@Test
	@Order(1)
	void testEndpoint_getWithNoParameters() throws Exception {
		mockMvc.perform(get("/test"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.counter").value(1))
			.andExpect(jsonPath("$.message").value("Hello, World!"))
		;
	}
	
	@Test
	@Order(2)
	void testEndpoint_getWithMessageParameter() throws Exception {
		mockMvc.perform(get("/test").param("message", "Spring"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.counter").value(2))
			.andExpect(jsonPath("$.message").value("Hello, Spring!"))
		;
	}
	
	// GETs are supposed to be idempotent, but just for this test endpoint, there's a state change between GET requests
	@Test
	@Order(3)
	void testEndpoint_getWithMultipleIncrementingRequests() throws Exception {
		mockMvc.perform(get("/test"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.counter").value(3))
		;
		
		mockMvc.perform(get("/test"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.counter").value(4))
		;
	}
}
