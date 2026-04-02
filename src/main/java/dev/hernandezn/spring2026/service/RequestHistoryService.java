package dev.hernandezn.spring2026.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.hernandezn.spring2026.entity.RequestHistory;
import dev.hernandezn.spring2026.enums.RequestHttpMethod;
import dev.hernandezn.spring2026.repo.RequestHistoryRepo;
import dev.hernandezn.spring2026.util.ServerRuntimeCaptor;
import jakarta.transaction.Transactional;

@Service
public class RequestHistoryService {
	
	@Autowired
	private RequestHistoryRepo repo;
	
	@Transactional
	public void captureRequest(LocalDateTime now, RequestHttpMethod requestMethod, String requestUri) {
		RequestHistory requestHistoryEntry = RequestHistory.builder()
			.serverRunId(ServerRuntimeCaptor.serverRunId)
			.requestTime(now)
			.httpMethod(requestMethod)
			.pathUri(requestUri)
		.build();
		
		repo.save(requestHistoryEntry);
	}
}
