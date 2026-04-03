package dev.hernandezn.spring2026.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.hernandezn.spring2026.entity.UptimeHistory;
import dev.hernandezn.spring2026.repo.UptimeHistoryRepo;
import jakarta.transaction.Transactional;

@Service
public class UptimeHistoryService {
	@Autowired
	private UptimeHistoryRepo historyRepo;
	
	@Transactional
	public Long captureStartup(LocalDateTime now) {
		
		UptimeHistory startupRecord = UptimeHistory.builder()
			.startupTime(now)
			.shutdownStatusCode((short)1)
		.build();

		startupRecord = historyRepo.save(startupRecord);
		
		return startupRecord.getId();
	}

	public void captureOkShutdown(Long serverRunId, LocalDateTime now) {
		historyRepo.updateShutdownStatusById(serverRunId, (short)0, now);
	}
}
