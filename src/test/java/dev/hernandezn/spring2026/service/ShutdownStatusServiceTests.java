package dev.hernandezn.spring2026.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hernandezn.spring2026.entity.ShutdownStatus;
import dev.hernandezn.spring2026.repo.ShutdownStatusRepository;

@ExtendWith(MockitoExtension.class)
class ShutdownStatusServiceTests {
	
	@Mock
	private ShutdownStatusRepository shutdownStatusRepo;
	
	@InjectMocks
	private ShutdownStatusService shutdownStatusService;
	
	@Test
	void initializeStatuses_shouldSaveStatusesToDatabase() {
		shutdownStatusService.initializeStatuses();
		
		ArgumentCaptor<List<ShutdownStatus>> argCaptor = ArgumentCaptor.forClass(List.class);
		verify(shutdownStatusRepo).saveAll(argCaptor.capture());
		
		// this check only works while both the List and ShutdownStatus have well-defined per-field equals() methods
		assertEquals(
			List.of(
				new ShutdownStatus((short) 0, "OK"),
				new ShutdownStatus((short) 1, "UNPLANNED")
			),
			argCaptor.getValue(),
			"There should be exactly 2 initial statuses, which match the default values (ID 0, Description \"OK\") & (ID 1, Description \"UNPLANNED\")"
		);
	}
}
