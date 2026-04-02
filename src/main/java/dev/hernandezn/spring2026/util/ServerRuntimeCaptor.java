package dev.hernandezn.spring2026.util;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import dev.hernandezn.spring2026.service.ServerRunHistoryService;
import dev.hernandezn.spring2026.service.ShutdownStatusService;
import jakarta.annotation.PreDestroy;

@Component
public class ServerRuntimeCaptor {
	private Long serverRunId = 0L;
	
	@Autowired
	private ServerRunHistoryService historyService;
	
	@Autowired
	private ShutdownStatusService statusService;
	
	@EventListener(ApplicationReadyEvent.class)
	public void captureStartup() {
		try {
			serverRunId = historyService.captureStartup(LocalDateTime.now());
		} catch (DataIntegrityViolationException exc) {
			statusService.initializeStatuses();
			
			serverRunId = historyService.captureStartup(LocalDateTime.now());
		}
	}
	
	@PreDestroy
	public void captureShutdown() {
		historyService.captureOkShutdown(serverRunId, LocalDateTime.now());
	}
}
