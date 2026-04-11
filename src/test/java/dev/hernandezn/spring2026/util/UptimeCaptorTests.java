package dev.hernandezn.spring2026.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import dev.hernandezn.spring2026.service.ShutdownStatusService;
import dev.hernandezn.spring2026.service.UptimeHistoryService;

@ExtendWith(MockitoExtension.class)
public class UptimeCaptorTests {
	
	@Mock
	UptimeHistoryService uptimeHistoryService;
	
	@Mock
	ShutdownStatusService shutdownStatusService;
	
	@InjectMocks
	UptimeCaptor uptimeCaptor;
	
	@Test
	void captureStartup_shouldCaptureUptimeRecord() {
		long expectedUptimeHistoryId = 15L;
		
		when(uptimeHistoryService.captureStartup(any()))
			.thenReturn(expectedUptimeHistoryId)
		;
		
		uptimeCaptor.captureStartup();
		
		verify(
			uptimeHistoryService,
			times(1).description("UptimeCaptor should call on uptimeHistoryService to capture its new uptime history record")
		).captureStartup(any());
		
		assertEquals(
			expectedUptimeHistoryId,
			uptimeCaptor.getUptimeHistoryId(),
			"UptimeCaptor should persist the uptimeHistoryId after it's set by captureStartup"
		);
	}
	
	@Test
	void captureStartup_shouldCaptureUptimeRecordAfterFindingFailedFkeyConstraint() {
		long expectedUptimeHistoryId = 16L;
		
		when(uptimeHistoryService.captureStartup(any()))
			.thenThrow(new DataIntegrityViolationException("Foreign-key record not found in the database"))
			.thenReturn(expectedUptimeHistoryId)
		;
		
		uptimeCaptor.captureStartup();
		
		verify(
			uptimeHistoryService,
			times(2).description("UptimeCaptor should make 2 calls to capture an uptime history record that initially failed a foreign key check")
		).captureStartup(any());
		verify(
			shutdownStatusService,
			description("UptimeCaptor should call on its shutdownStatusService to initialize shutdown status records for a foreign key constraint")
		).initializeStatuses();
		assertEquals(
			expectedUptimeHistoryId,
			uptimeCaptor.getUptimeHistoryId(),
			"UptimeCaptor should persist the uptimeHistoryId after it's set by captureStartup"
		);
	}
	
	@Test
	void captureShutdown_shouldCallServiceToCaptureOkShutdown() {
		long expectedUptimeHistoryId = 17L;
		
		when(uptimeHistoryService.captureStartup(any())).thenReturn(expectedUptimeHistoryId);
		uptimeCaptor.captureStartup();
		
		ArgumentCaptor<Long> uptimeHistoryIdCaptor = ArgumentCaptor.forClass(Long.class);
		uptimeCaptor.captureShutdown();
		
		verify(
			uptimeHistoryService,
			description("UptimeCaptor should call uptimeHistoryService to capture shutdown")
		).captureOkShutdown(uptimeHistoryIdCaptor.capture(), any());
		assertEquals(
			expectedUptimeHistoryId,
			uptimeHistoryIdCaptor.getValue(),
			"UptimeCaptor should use the uptime history ID, set within captureStartup, to capture its shutdown"
		);
	}
}
