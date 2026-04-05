package dev.hernandezn.spring2026.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="A test response JSON item")
public record DemoDTO(
	@Schema(description="The number of requests (including this request) since the server's last reboot.", example="1")
	long counter,
	
	@Schema(description="A simple \"Hello\" message.", example="Hello, World!")
	String message
) { }
