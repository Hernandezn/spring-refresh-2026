package dev.hernandezn.spring2026.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.hernandezn.spring2026.util.ScreenshotExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles the orders for & delivery of screenshot image data provided by SeleniumScreenshotExecutor.
 * 
 * Takes simple data and outputs simple data, with this service class interfacing with the underlying complexities.
 */
@Service
@Slf4j
public class ScreenshotService {
	
	private final ScreenshotExecutor screenshotExecutor;
	
	@Autowired
	public ScreenshotService(ScreenshotExecutor screenshotExecutor) {
		this.screenshotExecutor = screenshotExecutor;
	}
	
	public byte[] fetchScreenshot(String url) {
		try {
			url = normalizeUrl(url);
			
			Future<byte[]> futureScreenshot = screenshotExecutor.createScreenshot(url);
			
			return futureScreenshot.get(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.warn("Screenshot interrupted for URL: {}", url);
		} catch(ExecutionException | URISyntaxException e) {
			log.error(String.format("Failed to retrieve the screenshot from the input URL: %s", url), e);
		} catch (TimeoutException e) {
			log.error(String.format("The input URL took too long to screenshot: %s", url), e);
		}
		
		return new byte[0];
	}
	
	/**
	 * Ensures the input URL String has a valid URI scheme (http:// or https://)
	 * 
	 * @param url
	 * @return the input URL String with a valid URI scheme
	 * @throws URISyntaxException when the input isn't blank but can't be parsed as a URI
	 */
	private String normalizeUrl(String url) throws URISyntaxException {
		if(url == null || url.isBlank()) {
			return "https://www.google.com";
		}
		
		URI uri = new URI(url.trim());
		
		if(uri.getScheme() == null) {
			uri = new URI("https://" + url);
		}
		
		return uri.toString();
	}
}
