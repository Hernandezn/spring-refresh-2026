package dev.hernandezn.spring2026.util;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

/**
 * Maintains a single-thread ExecutorService to enqueue screenshot requests for the internal Selenium WebDriver.
 * 
 * This ensures that concurrent requests are handled one at a time rather than attempting to use the same WebDriver at the same time.
 * 
 * This component also provides cleanup and fallback functionality for the internal WebDriver.
 */
@Component
public class SeleniumScreenshotExecutor {
	
	private final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	
	@Autowired
	private WebDriver seleniumWebDriver;
	
	/**
	 * Enqueues a request for the single-thread ExecutorService to instruct the internal WebDriver to gather a screenshot.
	 * 
	 * @param url Web page that this method will screenshot
	 * @return a Future that resolves to provide a byte array representing the requested screenshot
	 */
	public Future<byte[]> createScreenshot(String url) {
		return singleThreadExecutor.submit(() -> {
			try {
				
				seleniumWebDriver.get(url);
				
			} catch(InvalidArgumentException e) {
				
				try {
					seleniumWebDriver.get(swapURISchemes(url));
				} catch(InvalidArgumentException e2) {
					throw e;
				}
				
			}
			
			WebDriverWait waitForRender = new WebDriverWait(seleniumWebDriver, Duration.ofMillis(4000));
			waitForRender.until((driver) ->
				((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete") )
			;
			
			// ARTIFICIAL DELAY for animation element completion
			Thread.sleep(500);
			
			byte[]result = ((TakesScreenshot) seleniumWebDriver).getScreenshotAs(OutputType.BYTES);
			
			cleanupWebDriver();
			
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
	
	/**
	 * Cleans up the WebDriver.
	 * 
	 * This should be used to flush any hanging resources (like cookies & local storage items) 
	 * after the WebDriver is done interacting with a Web page.
	 */
	private void cleanupWebDriver() {
		JavascriptExecutor js = (JavascriptExecutor) seleniumWebDriver;
		js.executeScript("window.localStorage.clear()");
		js.executeScript("window.sessionStorage.clear()");
		
		seleniumWebDriver.manage().deleteAllCookies();
		seleniumWebDriver.get("about:blank");
		
		// clean up any other windows opened by the visited page
		String blankPage = seleniumWebDriver.getWindowHandle();
		for (String handle : seleniumWebDriver.getWindowHandles()) {
		    if (!handle.equals(blankPage)) {
		    	seleniumWebDriver.switchTo().window(handle).close();
		    }
		}
		seleniumWebDriver.switchTo().window(blankPage);
	}
	
	@PreDestroy
	public void quit() {
		seleniumWebDriver.quit();
		singleThreadExecutor.shutdown();
	}
}
