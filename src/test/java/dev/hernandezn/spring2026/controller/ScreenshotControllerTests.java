package dev.hernandezn.spring2026.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// CAREFUL here; this shares a name with the MockMvcResultMatchers method
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import dev.hernandezn.spring2026.service.ScreenshotService;
import dev.hernandezn.spring2026.util.RequestCaptor;

@WebMvcTest(controllers=ScreenshotController.class)
@TestPropertySource(properties="request-captor.enabled=false")
public class ScreenshotControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private RequestCaptor mockCaptor;
	
	@MockitoBean
	private ScreenshotService mockScreenshotService;
	
	@Test
	void screenshotEndpoint_withNoParameters() throws Exception {
		byte[] mockImageData = new byte[] {2, 4, 6, 8, 10};
		when(mockScreenshotService.takeScreenshot(anyString())).thenReturn(mockImageData);
		
		mockMvc.perform(get("/screenshot"))
			.andExpect(status().isOk())
			.andExpect(header().string("Content-Disposition", "filename=\"screenshot.png\""))
			.andExpect(content().contentType(MediaType.IMAGE_PNG))
			.andExpect(content().bytes(mockImageData))
		;
		
		verify(mockScreenshotService).takeScreenshot("https://www.google.com/");
	}
	
	@Test
	void screenshotEndpoint_withUrlParameter() throws Exception {
		byte[] mockFailedImage = new byte[] {};
		byte[] mockSuccessImage = new byte[] {1, 2, 3, 4, 5};
		
		String urlParam = "example.com";
		
		when(mockScreenshotService.takeScreenshot(anyString())).thenReturn(mockFailedImage);
		when(mockScreenshotService.takeScreenshot(urlParam)).thenReturn(mockSuccessImage);
		
		mockMvc.perform(get("/screenshot").param("url", urlParam))
			.andExpect(status().isOk())
			.andExpect(header().string("Content-Disposition", "filename=\"screenshot.png\""))
			.andExpect(content().contentType(MediaType.IMAGE_PNG))
			.andExpect(content().bytes(mockSuccessImage))
		;
		
		verify(mockScreenshotService).takeScreenshot(urlParam);
	}
}
