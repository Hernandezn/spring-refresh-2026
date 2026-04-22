package dev.hernandezn.spring2026.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hernandezn.spring2026.entity.RequestHistory;
import dev.hernandezn.spring2026.enums.HttpRequestMethod;
import dev.hernandezn.spring2026.repo.RequestHistoryRepository;
import dev.hernandezn.spring2026.util.UptimeCaptor;

@ExtendWith(MockitoExtension.class)
class RequestHistoryServiceTests {
	
	@Mock
	private RequestHistoryRepository requestHistoryRepo;
	
	@Mock
	private UptimeCaptor uptimeCaptor;
	
	@InjectMocks
	private RequestHistoryService requestHistoryService;
	
	@Test
	void captureRequest_shouldCreateAndSaveRequestHistory() {
		Long requestUptimeHistoryId = 10L;
		LocalDateTime requestTime = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
		HttpRequestMethod requestMethod = HttpRequestMethod.GET;
		String requestTarget = "/some-test-path";
		
		when(uptimeCaptor.getUptimeHistoryId()).thenReturn(requestUptimeHistoryId);
		
		requestHistoryService.captureRequest(requestTime, requestMethod, requestTarget);
		
		ArgumentCaptor<RequestHistory> argCaptor = ArgumentCaptor.forClass(RequestHistory.class);
		verify(requestHistoryRepo).save(argCaptor.capture());
		
		RequestHistory requestRecord = argCaptor.getValue();
		assertEquals(
			requestUptimeHistoryId, 
			requestRecord.getUptimeHistoryId(),
			"The record saved to the database should have the uptime history ID that was returned from the uptimeCaptor"
		);
		assertEquals(
			requestTime, 
			requestRecord.getRequestTime(),
			"The record saved to the database should have the request time that was given to the captureRequest method"
		);
		assertEquals(
			requestMethod,
			requestRecord.getHttpMethod(),
			"The record saved to the database should have the HTTP method that was given to the captureRequest method"
		);
		assertEquals(
			requestTarget, 
			requestRecord.getPathUri(),
			"The record saved to the database should have the request path that was given to the captureRequest method"
		);
	}
}
