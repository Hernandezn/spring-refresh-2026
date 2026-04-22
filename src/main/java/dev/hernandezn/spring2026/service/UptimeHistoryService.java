package dev.hernandezn.spring2026.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.hernandezn.spring2026.entity.UptimeHistory;
import dev.hernandezn.spring2026.repo.UptimeHistoryRepository;
import jakarta.transaction.Transactional;

/**
 * Captures database entries detailing uptime timeframes for the server.
 * 
 * Runs on server startup & shutdown through:
 * dev.hernandezn.spring2026.util.UptimeCaptor.java
 */
@Service
public class UptimeHistoryService {
	
	private final UptimeHistoryRepository historyRepo;
	
	@Autowired
	public UptimeHistoryService(UptimeHistoryRepository historyRepo) {
		this.historyRepo = historyRepo;
	}
	
	@Transactional
	public Long captureStartup(LocalDateTime now) {
		
		UptimeHistory startupRecord = UptimeHistory.builder()
			.startupTime(now)
			.shutdownStatusCode((short) 1)
		.build();

		startupRecord = historyRepo.save(startupRecord);
		
		return startupRecord.getId();
	}

	public void captureOkShutdown(Long serverRunId, LocalDateTime now) {
		historyRepo.updateShutdownStatusById(serverRunId, (short)0, now);
	}
}
