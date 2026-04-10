package dev.hernandezn.spring2026.controller;

// named the same as the MockMvcResultMathers content() method; don't get them mixed
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.hernandezn.spring2026.dto.DemoDTO;
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
		
		// this is another way to assess the JSON returned
		// can be better for longer items, to avoid long .andExpect() chains & make expected fields' details detachable from the test
		// private ObjectMapper objectMapper can be @Autowired in Spring's test context
		ObjectMapper objectMapper = new ObjectMapper();
		DemoDTO expectedReturnValue = new DemoDTO(4L, "Hello, World!");
		
		mockMvc.perform(get("/test"))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(expectedReturnValue)))
		;
	}
}
