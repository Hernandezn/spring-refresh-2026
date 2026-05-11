package dev.hernandezn.spring2026.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.hernandezn.spring2026.service.ThumbnailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles requests for turning file uploads into custom-sized thumbnails.
 */
@RestController
@Slf4j
@RequestMapping("/thumbnail")
public class ThumbnailController {
	
	private final ThumbnailService thumbnailService;
	
	@Autowired
	public ThumbnailController (ThumbnailService thumbnailService) {
		this.thumbnailService = thumbnailService;
	}
	
	@Operation(
		summary="Convert an input image into a scaled-down thumbnail JPG image", 
		description="Provide an image file & maximum thumbnail dimensions for the server to return a scaled JPG thumbnail of that image.",
		responses= {
			@ApiResponse(
				responseCode="200",
				description="Successfully returned thumbnail",
				content=@Content(
					mediaType="image/jpeg",
					schema=@Schema(
						type="string",
						format="binary"
					)
				)
			)
		}
	)
	@PostMapping
	public ResponseEntity<byte[]> thumbnail(
		@RequestParam("thumbnail_input") MultipartFile image,
		@RequestParam("thumbnail_width") int maxWidth,
		@RequestParam("thumbnail_height") int maxHeight
	) {
		try {
			return ResponseEntity.ok()
				.contentType(MediaType.IMAGE_JPEG)
				.header("Content-Disposition", "inline; filename=\"thumbnail.jpg\"")
				.body(thumbnailService.getThumbnailOf(image, maxWidth, maxHeight))
			;
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
