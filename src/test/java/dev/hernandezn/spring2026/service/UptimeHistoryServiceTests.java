package dev.hernandezn.spring2026.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hernandezn.spring2026.entity.UptimeHistory;
import dev.hernandezn.spring2026.repo.UptimeHistoryRepository;

@ExtendWith(MockitoExtension.class)
public class UptimeHistoryServiceTests {
	@Mock
	UptimeHistoryRepository uptimeHistoryRepo;
	
	@InjectMocks
	UptimeHistoryService uptimeHistoryService;
	
	@Test
	void captureStartup_shouldSaveStartupRecord() {
		LocalDateTime startupTime = LocalDateTime.now();
		Long expectedId = 15L;
		
		// makes the save method return an UptimeHistory record that has its ID set
		when(uptimeHistoryRepo.save(any(UptimeHistory.class)))
			.thenAnswer(
				(uptimeHistoryMockInvocation) -> {
					// returning a cloned output so that the captured input remains pre-save / without the id mutation
					UptimeHistory historyEntryInput = uptimeHistoryMockInvocation.getArgument(0);
					UptimeHistory historyEntryOutput = UptimeHistory.builder()
						.id(expectedId)
						.startupTime(historyEntryInput.getStartupTime())
						.shutdownStatusCode(historyEntryInput.getShutdownStatusCode())
					.build();
					
					
					return historyEntryOutput;
				}
			)
		;
		
		Long actualId = uptimeHistoryService.captureStartup(startupTime);
		
		ArgumentCaptor<UptimeHistory> argCaptor = ArgumentCaptor.forClass(UptimeHistory.class);
		verify(uptimeHistoryRepo).save(argCaptor.capture());
		
		assertEquals(
			UptimeHistory.builder()
				.startupTime(startupTime)
				.shutdownStatusCode((short) 1)
			.build(),
			argCaptor.getValue(),
			"The record saved to the database should have the current startup time, shutdown status code 1, and no ID before saving"
		);
		assertEquals(
			expectedId,
			actualId,
			"The captureStartup method returns the ID value that's set to UptimeHistory record after saving to the database"
		);
	}
	
	@Test
	void captureOkShutdown_shouldUpdateUptimeRecord() {
		Long uptimeHistoryId = 15L;
		LocalDateTime shutdownTime = LocalDateTime.now();
		
		uptimeHistoryService.captureOkShutdown(uptimeHistoryId, shutdownTime);

		verify(
			uptimeHistoryRepo,
			description("Should request the repo to update the current Uptime History record with status code 0 and a shutdown time of now")
		).updateShutdownStatusById(uptimeHistoryId, (short) 0, shutdownTime);
	}
}
