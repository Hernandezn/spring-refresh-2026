package dev.hernandezn.spring2026.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.hernandezn.spring2026.util.SeleniumScreenshotExecutor;

@Service
public class ScreenshotService {
	@Autowired
	SeleniumScreenshotExecutor screenshotExecutor;
	
	public byte[] takeScreenshot(String url) {
		try {
			url = normalizeUrl(url);
			
			Future<byte[]> futureScreenshot = screenshotExecutor.takeScreenshot(url);
			
			return futureScreenshot.get(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException | URISyntaxException e) {
			e.printStackTrace();
			
			return new byte[0];
		}
	}
	
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
