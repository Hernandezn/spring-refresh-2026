package dev.hernandezn.spring2026.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hernandezn.spring2026.dto.DemoDTO;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test")
public class RootController {
	private final String template = "API Endpoint reached! Your message: %s";
	private final AtomicLong requestCounter = new AtomicLong();
	
	// Having both the name="message" RequestParam argument and the parameter be named "message" in the method will each work on their own
	// The required=false statement is also a redundancy that's just there for demonstration
	@GetMapping
	public DemoDTO testEndpoint(
		HttpServletRequest request,
		@RequestParam(name="message", required=false, defaultValue="No message") String message,
		Model model
	) {
		// builds a model as an internal map, for O(1) accessing
		model.addAttribute("message", message);
		model.getAttribute(message);
		
		return new DemoDTO(requestCounter.incrementAndGet(), template.formatted(message));
	}
}
