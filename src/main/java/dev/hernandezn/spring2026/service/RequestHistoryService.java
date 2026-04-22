package dev.hernandezn.spring2026.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.hernandezn.spring2026.entity.RequestHistory;
import dev.hernandezn.spring2026.enums.HttpRequestMethod;
import dev.hernandezn.spring2026.repo.RequestHistoryRepository;
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
	
	private final RequestHistoryRepository repo;
	
	private final UptimeCaptor uptimeCaptor;
	
	@Autowired
	public RequestHistoryService(RequestHistoryRepository repo, UptimeCaptor uptimeCaptor) {
		this.repo = repo;
		this.uptimeCaptor = uptimeCaptor;
	}
	
	@Transactional
	public void captureRequest(LocalDateTime now, HttpRequestMethod requestMethod, String requestTarget) {
		RequestHistory requestHistoryEntry = RequestHistory.builder()
			.uptimeHistoryId(uptimeCaptor.getUptimeHistoryId())
			.requestTime(now)
			.httpMethod(requestMethod)
			.pathUri(requestTarget)
		.build();
		
		repo.save(requestHistoryEntry);
	}
}
