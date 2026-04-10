package dev.hernandezn.spring2026.port;

import org.openqa.selenium.InvalidArgumentException;

/**
 * Previously, some functionality in SeleniumScreenshotExecutor required the 
 * WebDriver to be casted to TakesScreenshot & JavascriptExecutor.
 * 
 * After headaches over how to test the SeleniumScreenshotExecutor & trying 
 * workarounds for the ClassCastExceptions coming from WebDriver not being an 
 * instance of the other two interfaces, I opted to have WebDriver processes 
 * abstracted under instances of this interface. That way, I can just mock 
 * this interface instead of dealing with Selenium-style casts.
 */
public interface WebScreenshotBrowser {
	void targetUrl(String url) throws InvalidArgumentException;
	byte[] takeScreenshot();
	byte[] takeScreenshotAfterMs(long milliseconds);
	void flush();
	void quit();
}
