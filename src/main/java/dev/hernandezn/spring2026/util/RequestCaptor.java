package dev.hernandezn.spring2026.util;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import dev.hernandezn.spring2026.enums.HttpRequestMethod;
import dev.hernandezn.spring2026.service.RequestHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Intercepts incoming HTTP requests to log incoming traffic.
 * 
 * Requests are captured both in database entries through RequestHistoryService and in standard text logs through Slf4j logger.
 * 
 * This interceptor is added to Spring's request-handling flow through:
 * dev.hernandezn.spring2026.config.RequestCaptorConfig.java
 */
@Component
@Slf4j
public class RequestCaptor implements HandlerInterceptor {
	
	@Autowired
	RequestHistoryService requestHistoryService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpRequestMethod method = HttpRequestMethod.valueOf(request.getMethod());
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        
        String target;
        if(query == null) {
        	target = uri;
        } else {
        	target = uri + "?" + query;
        }
        
        LocalDateTime now = LocalDateTime.now();
        requestHistoryService.captureRequest(now, method, target);
        
        log.info("Http request [" + method + " " + target + "] captured at: " + now);
		
		return true;
	}
}
