package dev.hernandezn.spring2026.util;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import dev.hernandezn.spring2026.enums.RequestHttpMethod;
import dev.hernandezn.spring2026.service.RequestHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestCaptor implements HandlerInterceptor {
	
	@Autowired
	RequestHistoryService requestHistoryService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String uri = request.getRequestURI();
        RequestHttpMethod method = RequestHttpMethod.valueOf(request.getMethod());
        
        requestHistoryService.captureRequest(LocalDateTime.now(), method, uri);
		
		return true;
	}
}
