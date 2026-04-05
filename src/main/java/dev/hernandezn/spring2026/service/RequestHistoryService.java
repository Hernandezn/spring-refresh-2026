package dev.hernandezn.spring2026.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.hernandezn.spring2026.entity.RequestHistory;
import dev.hernandezn.spring2026.enums.RequestHttpMethod;
import dev.hernandezn.spring2026.repo.RequestHistoryRepo;
import dev.hernandezn.spring2026.util.UptimeCaptor;
import jakarta.transaction.Transactional;

/**
 * Captures database entries detailing HTTP requests to the server.
 * 
 * Runs on every request through:
 * dev.hernandezn.spring2026.util.RequestCaptor.java
 */
@Service
public class RequestHistoryService {
	
	@Autowired
	private RequestHistoryRepo repo;
	
	@Transactional
	public void captureRequest(LocalDateTime now, RequestHttpMethod requestMethod, String requestTarget) {
		RequestHistory requestHistoryEntry = RequestHistory.builder()
			.uptimeHistoryId(UptimeCaptor.uptimeHistoryId)
			.requestTime(now)
			.httpMethod(requestMethod)
			.pathUri(requestTarget)
		.build();
		
		repo.save(requestHistoryEntry);
	}
}
