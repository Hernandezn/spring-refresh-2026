package dev.hernandezn.spring2026.util;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import dev.hernandezn.spring2026.service.ShutdownStatusService;
import dev.hernandezn.spring2026.service.UptimeHistoryService;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Captures when the server starts up & shuts down.
 * 
 * Uptimes are captured both in database entries through UptimeHistoryService and in standard text logs through Slf4j logger.
 */
@Component
@Slf4j
public class UptimeCaptor {
	
	@Getter
	private Long uptimeHistoryId = 0L;
	
	@Autowired
	private UptimeHistoryService uptimeHistoryService;
	
	@Autowired
	private ShutdownStatusService shutdownStatusService;
	
	@EventListener(ApplicationReadyEvent.class)
	public void captureStartup() {
		LocalDateTime now;
		
		try {
			now = LocalDateTime.now();
			
			uptimeHistoryId = uptimeHistoryService.captureStartup(now);
		} catch (DataIntegrityViolationException exc) {
			
			// satisfies FKEY constraint if local database isn't initialized
			shutdownStatusService.initializeStatuses();
			
			now = LocalDateTime.now();
			
			uptimeHistoryId = uptimeHistoryService.captureStartup(now);
		}
		
		log.info("Startup captured at: " + now);
	}
	
	@PreDestroy
	public void captureShutdown() {
		LocalDateTime now = LocalDateTime.now();
		
		uptimeHistoryService.captureOkShutdown(uptimeHistoryId, now);
		
		log.info("Shutdown captured at: " + now);
	}
}
