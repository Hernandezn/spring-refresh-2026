package dev.hernandezn.spring2026.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ThumbnailService {
	
	
	public byte[] getThumbnailOf(MultipartFile image, int maxThumbWidth, int maxThumbHeight) throws IOException {
//		BufferedImage img0 = ImageIO.read(new ByteArrayInputStream(imageBytes)); // conversion from byte array
		BufferedImage img = ImageIO.read(image.getInputStream());
		
		int initWidth = img.getWidth();
		int initHeight = img.getHeight();
		
		double scale = Math.min(
			(double) maxThumbWidth / initWidth,
			(double) maxThumbHeight / initHeight
		);
		
		int scaledWidth = (int) (initWidth * scale);
		int scaledHeight = (int) (initHeight * scale);
		
		// doesn't have the image data, just the size and an RGB color space
		BufferedImage thumbnail = new BufferedImage(
			scaledWidth,
			scaledHeight,
			BufferedImage.TYPE_INT_RGB
		);
		
		// handle for drawing graphics onto the thumbnail
		Graphics2D graphicsWorker = thumbnail.createGraphics();
		// specifies how colors at points from the source image become colors at points on the thumbnail
		graphicsWorker.setRenderingHint(
			RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR // looks at the 4 nearest points & interpolates their RGB values
		);
		graphicsWorker.drawImage(
			img,
			0, 0,
			scaledWidth, scaledHeight, 
			null
		);
		graphicsWorker.dispose();
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		ImageIO.write(thumbnail, "jpg", outStream);
		
		return outStream.toByteArray();
	}
}
