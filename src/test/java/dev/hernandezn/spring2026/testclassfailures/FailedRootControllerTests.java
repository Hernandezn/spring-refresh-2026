package dev.hernandezn.spring2026.testclassfailures;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
//import org.springframework.boot.webmvc.autoconfigure.WebMvcAutoConfiguration;
//import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import dev.hernandezn.spring2026.dto.DemoDTO;
//import dev.hernandezn.spring2026.util.RequestCaptor;
//
//import import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * 
 * 
 * FAILED testing class for root controller.
 * 
 * 
 * Kept here for future reference, as I was trying too many things, and it was 
 * getting bloated.
 * 
 * 
 * Failed due to response body being empty in every request sent to the 
 * controller. This is confirmed through .andDo(print()) having a "Body = " 
 * statement before printing out the next field in the MockHttpServletResponse.
 * 
 * 
 */

//@WebMvcTest(controllers=RootController.class)
////@Import(JacksonAutoConfiguration.class)
//@ImportAutoConfiguration(WebMvcAutoConfiguration.class)
public class FailedRootControllerTests {
	
//	@Autowired
//	private MockMvc mockMvc;
//	
//	private ObjectMapper objectMapper = new ObjectMapper();
//	
//	@MockitoBean
//	private RequestCaptor mockCaptor;
//	
//	/**
//	 * This simple test checks the JSON of an object against the JSON returned by the service.
//	 * 
//	 * @throws Exception
//	 */
//	@Test
//	void shouldReturnHelloWorldDemoDTOFromEndpoint() throws Exception {
//		DemoDTO expectedDTO = new DemoDTO(1L, "Hello, World!");
//		String expectedJson = objectMapper.writeValueAsString(expectedDTO);
//		
//		mockMvc.perform(get("/test").accept(MediaType.APPLICATION_JSON))
//			.andExpect(status().isOk())
//			.andDo(print())
////			.andExpect(
////				content().
////				json(
//////					expectedJson
////					"""
////						{"counter":1,"message":"Hello, World!"}
////					"""
////				)
////			)
//		;
//	}
//	
//	/**
//	 * This more complex test adds a request parameter to the get method.
//	 * 
//	 * It also captures a response before deserializing it to a DemoDTO. This DemoDTO is then compared to an expected case using JUnit.
//	 * 
//	 * @throws Exception
//	 */
//	@Test
//	void shouldReturnRequestedMessageDemoDTOFromEndpoint() throws Exception {
//		
//		// adds a URL parameter; equivalent to /test?message=Spring
//		String response = mockMvc.perform(get("/test").param("message", "Spring"))
//			.andExpect(status().isOk())
//			.andReturn()
//			.getResponse()
//			.getContentAsString()
//		;
//		
//		DemoDTO responseDto = objectMapper.readValue(response, DemoDTO.class);
//		DemoDTO expectedDto = new DemoDTO(2L, "Hello, Spring!");
//		
////		// the JUnit way of doing this
////		assertEquals(expectedDto.message(), responseDto.message());
////		assertEquals(expectedDto.counter(), responseDto.counter());
//		
////		// the assertJ way of doing this
////		assertThat(responseDto).usingRecursiveComparison().isEqualTo(expectedDto);
//		
//		// the JUnit way again, but with DemoDTO having a Lombok Data annotation (it implements a per-field equals method)
//		assertEquals(expectedDto, responseDto);
//	}
//	
//	/**
//	 * This test checks individual fields from the JSON returned in the request.
//	 * 
//	 * This sends serial requests and ensures an expected state across these requests.
//	 * 
//	 * As this /test endpoint isn't idempotent (NOT standard practice), the expected state here will change per request.
//	 * 
//	 * @throws Exception
//	 */
//	@Test
//	void shouldReturnSerialCountDemoDTOsFromEndpoint() throws Exception {
//		mockMvc.perform(get("/test"))
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("$.message", is("Hello, World!")))
//			.andExpect(jsonPath("$.counter", is(3L)))
//		;
//		
//		mockMvc.perform(get("/test"))
//			.andExpect(status().isOk())
//			.andExpect(jsonPath("$.counter", is(4L)))
//		;
//	}
}
