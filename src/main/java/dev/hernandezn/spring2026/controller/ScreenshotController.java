package dev.hernandezn.spring2026.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hernandezn.spring2026.service.ScreenshotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles HTTP requests for screenshots.
 */
@RestController
@Slf4j
@RequestMapping("/screenshot")
public class ScreenshotController {
	
	@Autowired
	ScreenshotService service;
	
	@Operation(
		summary="Take a website screenshot remotely", 
		description="Provide a website URL for the server to create and return a live screenshot of that website.",
		responses= {
			@ApiResponse(
				responseCode="200",
				description="Successfully acquired screenshot",
				content=@Content(
					mediaType="image/png",
					schema=@Schema(
						type="string",
						format="binary"
					)
				)
			)
		}
	)
	@GetMapping
	public ResponseEntity<byte[]> screenshot(
		HttpServletRequest request,
		@RequestParam(required=false) String url
	) {
		if(url == null) {
			url = "https://www.google.com/";
		}
		
		return ResponseEntity.ok()
			.contentType(MediaType.IMAGE_PNG)
			.header("Content-Disposition", "filename=\"screenshot.png\"")
			.body(service.fetchScreenshot(url))
		;
	}
}
