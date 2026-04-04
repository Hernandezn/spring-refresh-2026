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

@Component
public class SeleniumScreenshotExecutor {
	
	private final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	
	@Autowired
	private WebDriver seleniumWebDriver;
	
	public Future<byte[]> takeScreenshot(String url) {
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
			
			return ((TakesScreenshot) seleniumWebDriver).getScreenshotAs(OutputType.BYTES);
		});
	}
	
	private String swapURISchemes(String url) throws InvalidArgumentException {
		String[] schemes = {"https://", "http://"};
		
		if(url.startsWith(schemes[1])) {
			String temp = schemes[0];
			schemes[0] = schemes[1];
			schemes[1] = temp;
		}
		
		return url.replaceFirst(schemes[0], schemes[1]);
	}
	
	@PreDestroy
	public void quit() {
		seleniumWebDriver.quit();
		singleThreadExecutor.shutdown();
	}
}
