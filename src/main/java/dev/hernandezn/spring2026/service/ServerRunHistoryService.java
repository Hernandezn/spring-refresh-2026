package dev.hernandezn.spring2026.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.hernandezn.spring2026.entity.ServerRunHistory;
import dev.hernandezn.spring2026.repo.ServerRunHistoryRepo;
import jakarta.transaction.Transactional;

@Service
public class ServerRunHistoryService {
	@Autowired
	private ServerRunHistoryRepo historyRepo;
	
	@Transactional
	public Long captureStartup(LocalDateTime now) {
		
		ServerRunHistory startupRecord = ServerRunHistory.builder()
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
