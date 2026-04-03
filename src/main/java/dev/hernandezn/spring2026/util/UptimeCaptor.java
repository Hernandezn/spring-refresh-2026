package dev.hernandezn.spring2026.util;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import dev.hernandezn.spring2026.controller.RootController;
import dev.hernandezn.spring2026.service.UptimeHistoryService;
import dev.hernandezn.spring2026.service.ShutdownStatusService;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UptimeCaptor {
	public static Long uptimeHistoryId = 0L;
	
	@Autowired
	private UptimeHistoryService historyService;
	
	@Autowired
	private ShutdownStatusService statusService;
	
	@EventListener(ApplicationReadyEvent.class)
	public void captureStartup() {
		LocalDateTime now;
		
		try {
			now = LocalDateTime.now();
			
			uptimeHistoryId = historyService.captureStartup(now);
		} catch (DataIntegrityViolationException exc) {
			statusService.initializeStatuses();
			
			now = LocalDateTime.now();
			
			uptimeHistoryId = historyService.captureStartup(now);
		}
		
		log.info("Startup captured at: " + now);
	}
	
	@PreDestroy
	public void captureShutdown() {
		LocalDateTime now = LocalDateTime.now();
		
		historyService.captureOkShutdown(uptimeHistoryId, now);
		
		log.info("Shutdown captured at: " + now);
	}
}
