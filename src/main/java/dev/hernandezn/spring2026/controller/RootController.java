package dev.hernandezn.spring2026.controller;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hernandezn.spring2026.dto.DemoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles HTTP requests.
 * 
 * Provides testing endpoint support.
 */
@RestController
@Slf4j
@RequestMapping("/")
public class RootController {
	private static final String TEMPLATE = "Hello, %s!";
	private static final AtomicLong REQUEST_COUNTER = new AtomicLong();
	
	/**
	 * Self-explanatory. Does not speak to other application layers.
	 * 
	 * THIS IS NOT IDEMPOTENT, as it increments the requestCounter in this class.
	 * 
	 * @param request 
	 * @param message String that can be added as a URL parameter to modify the output message
	 * @param model per-request data mapping item that could be used by a view controller like Thymeleaf
	 * @return
	 */
	@Operation(
		summary="Test basic functionality", 
		description="Initial endpoint. A \"Hello World\" for the REST API server.",
		responses= {
			@ApiResponse(
				responseCode="200",
				description="API endpoint successfully reached",
				content=@Content(
					schema=@Schema(
						implementation=DemoDTO.class
					)
				)
			)
		}
	)
	@GetMapping("test")
	public DemoDTO testEndpoint(
		HttpServletRequest request,
		@RequestParam(name="message", required=false, defaultValue="World") String message,
		Model model
	) {
		// builds a model as an internal map, for O(1) accessing
		model.addAttribute("message", message);
		model.getAttribute(message);
		
		return new DemoDTO(REQUEST_COUNTER.incrementAndGet(), TEMPLATE.formatted(message));
	}
}
