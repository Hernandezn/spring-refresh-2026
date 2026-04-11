package dev.hernandezn.spring2026.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hernandezn.spring2026.enums.HttpRequestMethod;
import dev.hernandezn.spring2026.service.RequestHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class RequestCaptorTests {
	
	@Mock
	RequestHistoryService requestHistoryService;
	
	@InjectMocks
	RequestCaptor requestCaptor;
	
	@Mock
	HttpServletRequest request;
	
	@Mock
	HttpServletResponse response;
	
	@BeforeEach
	void setupRequest() {
		when(request.getRequestURI()).thenReturn("/test");
	}
	
	@Test
	void preHandle_shouldCaptureRequestWithoutURLParams() {
		when(request.getMethod()).thenReturn("GET");
		when(request.getQueryString()).thenReturn(null);
		
		assertTrue(requestCaptor.preHandle(request, response, null));
		
		ArgumentCaptor<LocalDateTime> timeCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
		ArgumentCaptor<HttpRequestMethod> httpMethodCaptor = ArgumentCaptor.forClass(HttpRequestMethod.class);
		ArgumentCaptor<String> urlPathCaptor = ArgumentCaptor.forClass(String.class);
		
		verify(requestHistoryService).captureRequest(
			timeCaptor.capture(), httpMethodCaptor.capture(), urlPathCaptor.capture()
		);
		
		assertNotNull(timeCaptor.getValue());
		assertEquals(
			HttpRequestMethod.GET,
			httpMethodCaptor.getValue(),
			"RequestCaptor preHandle should capture GET requests"
		);
		assertEquals(
			"/test",
			urlPathCaptor.getValue(),
			"RequestCaptor preHandle should capture API requests that don't have query parameters"
		);
	}
	
	@Test
	void preHandle_shouldCaptureRequestWithURLParams() {
		when(request.getRequestURI()).thenReturn("/test");
		when(request.getMethod()).thenReturn("POST");
		when(request.getQueryString()).thenReturn("message=Spring");
		
		assertTrue(requestCaptor.preHandle(request, response, null));
		
		ArgumentCaptor<LocalDateTime> timeCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
		ArgumentCaptor<HttpRequestMethod> httpMethodCaptor = ArgumentCaptor.forClass(HttpRequestMethod.class);
		ArgumentCaptor<String> urlPathCaptor = ArgumentCaptor.forClass(String.class);
		
		verify(requestHistoryService).captureRequest(
			timeCaptor.capture(), httpMethodCaptor.capture(), urlPathCaptor.capture()
		);
		
		assertNotNull(timeCaptor.getValue());
		assertEquals(
			HttpRequestMethod.POST,
			httpMethodCaptor.getValue(),
			"RequestCaptor preHandle should capture POST requests"
		);
		assertEquals(
			"/test?message=Spring",
			urlPathCaptor.getValue(),
			"RequestCaptor preHandle should capture API requests that have query parameters"
		);
	}
}
