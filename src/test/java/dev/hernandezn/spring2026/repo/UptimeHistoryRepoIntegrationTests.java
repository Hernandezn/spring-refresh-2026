package dev.hernandezn.spring2026.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import dev.hernandezn.spring2026.entity.ShutdownStatus;
import dev.hernandezn.spring2026.entity.UptimeHistory;
import jakarta.persistence.EntityManager;

/**
 * Integration testing between the repo's custom query and the database.
 * 
 * Uses ActiveProfiles to target application-test.properties for configuration.
 * 
 * Uses TestInstance PER CLASS to use shutdownStatusRepo.saveAll in a 
 * BeforeAll method, as the BeforeAll method would otherwise need to be static 
 * and thereby lose access to the instanced repo methods.
 * 
 * Uses DataJpaTest to run an H2 database and reset it for every test class.
 */
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DataJpaTest
public class UptimeHistoryRepoIntegrationTests {
	
	@Autowired
	private UptimeHistoryRepo uptimeHistoryRepo;
	
	@Autowired
	private ShutdownStatusRepo shutdownStatusRepo;
	
	@Autowired
	private EntityManager entityManager;
	
	/**
	 * These entries are required to satisfy a foreign key requirement for 
	 * saving UptimeHistory entries.
	 */
	@BeforeAll
	void shutdownStatusTable() {
		shutdownStatusRepo.saveAll(
			List.of(
				new ShutdownStatus((short)0, "OK"),
				new ShutdownStatus((short)1, "UNPLANNED")
			)
		);
	}
	
	@Test
	void updateShutdownStatusById_updateRecord() {
		UptimeHistory historyRecord = UptimeHistory.builder()
			.shutdownStatusCode((short) 1)
			.startupTime(LocalDateTime.MIN)
			.build()
		;
		historyRecord = uptimeHistoryRepo.save(historyRecord);
		
		long uptimeId = historyRecord.getId();
		short newShutdownStatus = 0;
		// must be truncated to microseconds, because database datetime types don't usually support the default nanoseconds
		LocalDateTime assignedShutdownTime = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
		
		int rowsUpdated = uptimeHistoryRepo.updateShutdownStatusById(uptimeId, newShutdownStatus, assignedShutdownTime);
		entityManager.clear(); // clears cached entities so the below findById call reaches for the DB to get an updated record
		Optional<UptimeHistory> retrievedRecord = uptimeHistoryRepo.findById(uptimeId);
		
		assertEquals(
			rowsUpdated, 
			1, 
			"Only 1 row should be updated"
		);
		assertTrue(
			retrievedRecord.isPresent(), 
			"The retrieved record should be present"
		);
		assertEquals( 
			newShutdownStatus, 
			retrievedRecord.get().getShutdownStatusCode(),
			"The shutdown status should be updated to 0"
		);
		assertEquals(
			assignedShutdownTime,
			retrievedRecord.get().getShutdownTime(),
			"The shutdown time should be the one assigned by the update query"
		);
	}
}
