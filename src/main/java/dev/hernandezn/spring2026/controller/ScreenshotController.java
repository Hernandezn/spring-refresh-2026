package dev.hernandezn.spring2026.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.hernandezn.spring2026.service.ScreenshotService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/screenshot")
public class ScreenshotController {
	
	@Autowired
	ScreenshotService service;
	
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
			.body(service.takeScreenshot(url))
		;
	}
}
