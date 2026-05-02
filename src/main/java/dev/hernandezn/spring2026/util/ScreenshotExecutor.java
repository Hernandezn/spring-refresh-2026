package dev.hernandezn.spring2026.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.openqa.selenium.InvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import dev.hernandezn.spring2026.port.WebScreenshotBrowser;
import net.coobird.thumbnailator.Thumbnails;

/**
 * Maintains a single-thread ExecutorService to enqueue screenshot requests for the internal Selenium WebDriver.
 * 
 * This ensures that concurrent requests are handled one at a time rather than attempting to use the same WebDriver at the same time.
 * 
 * This component also provides cleanup and fallback functionality for the internal WebDriver.
 */
@Component
public class ScreenshotExecutor {
	
	private final WebScreenshotBrowser browser;
	
	private final ExecutorService singleThreadExecutor;
	
	public ScreenshotExecutor(
		@Autowired
		@Qualifier("seleniumScreenshotter")
		WebScreenshotBrowser browser,
		
		@Autowired
		@Qualifier("screenshotTaskExecutor")
		ExecutorService singleThreadExecutor
	) {
		this.browser = browser;
		this.singleThreadExecutor = singleThreadExecutor;
	}
	
	/**
	 * Enqueues a request for the single-thread ExecutorService to instruct the internal WebDriver to gather a screenshot.
	 * 
	 * @param url Web page that this method will screenshot
	 * @return a Future that resolves to provide a byte array representing the requested screenshot
	 */
	public Future<byte[]> createScreenshot(String url) {
		return singleThreadExecutor.submit(() -> {
			try {
				
				browser.targetUrl(url);
				
			} catch(InvalidArgumentException e) {
				
				try {
					browser.targetUrl(swapURISchemes(url));
				} catch(InvalidArgumentException e2) {
					throw e;
				}
				
			}
			
			byte[] result = browser.takeScreenshotAfterMs(0);
			browser.flush();
			
//			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			Thumbnails.of(new ByteArrayInputStream(result))
//				.size(300, 300)
//				.outputFormat("png")
//				.toOutputStream(outputStream)
//			;
//			byte[] thumbnail = outputStream.toByteArray();
			
			return result;
		});
	}
	
	/**
	 * Swaps "http://" & "https://" as a fallback for any URL that didn't respond.
	 * 
	 * @param url
	 * @return
	 */
	private String swapURISchemes(String url) {
		String[] schemes = {"https://", "http://"};
		
		if(url.startsWith(schemes[1])) {
			String temp = schemes[0];
			schemes[0] = schemes[1];
			schemes[1] = temp;
		}
		
		return url.replaceFirst(schemes[0], schemes[1]);
	}
}
