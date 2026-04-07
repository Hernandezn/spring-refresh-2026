package dev.hernandezn.spring2026.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.hernandezn.spring2026.util.RequestCaptor;

@WebMvcTest(controllers=RootController.class)
@TestPropertySource(properties="request-captor.enabled=false")
public class RootControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private RequestCaptor mockCaptor;
	
	@Test
	void testEndpoint_withDefaultMessage() throws Exception {
		mockMvc.perform(get("/test"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.counter").value(1))
			.andExpect(jsonPath("$.message").value("Hello, World!"))
		;
		
		mockMvc.perform(get("/test").param("message", "Spring"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.counter").value(2))
			.andExpect(jsonPath("$.message").value("Hello, Spring!"))
		;
	}
}
