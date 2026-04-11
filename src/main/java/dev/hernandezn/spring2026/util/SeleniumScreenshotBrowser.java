package dev.hernandezn.spring2026.util;

import java.time.Duration;

import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import dev.hernandezn.spring2026.port.WebScreenshotBrowser;

/**
 * Abstraction layer for Selenium-specific implementations.
 * 
 * DO NOT unit test this class, as that would just be testing Selenium 
 * behavior based on mocks. Nearly every line of execution is just up to 
 * Selenium whether it works or not, and I am not unit testing Selenium.
 * 
 * Any tests of this class would need to be integration or slice tests, using 
 * real WebDriver behavior instead of mocked behavior.
 */
// this class is NOT a project @Component; set up as a Bean before autowiring
public class SeleniumScreenshotBrowser implements WebScreenshotBrowser {
	private WebDriver driver;
	
	public SeleniumScreenshotBrowser(
		WebDriver driver
	) {
		if(!(driver instanceof TakesScreenshot)) {
			throw new IllegalStateException("Driver must support screenshots");
		}
		if (!(driver instanceof JavascriptExecutor)) {
			throw new IllegalStateException("Driver must support Javascript execution");
		}
		
		this.driver = driver;
	}

	@Override
	public void targetUrl(String url) throws InvalidArgumentException {
		driver.get(url);
	}

	/**
	 * Screenshots the targeted page as soon as it finishes loading.
	 * 
	 * Times out if the page takes 4 seconds to render.
	 * 
	 * @param webdriver
	 */
	@Override
	public byte[] takeScreenshot() {
		return takeScreenshotAfterMs(0);
	}
	
	/**
	 * Waits for the WebDriver to render the page, delays by the input number 
	 * of milliseconds, then returns a screenshot of the page at that point.
	 * 
	 * Times out if the page takes 4 seconds to render.
	 * 
	 * @param webdriver
	 */
	public byte[] takeScreenshotAfterMs(long milliseconds) {
		WebDriverWait waitForRender = new WebDriverWait(driver, Duration.ofMillis(4000));
		waitForRender.until((driver) ->
			((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete") )
		;
		
		// ARTIFICIAL DELAY for animation element completion
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		
		return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
	}

	/**
	 * Cleans up the WebDriver.
	 * 
	 * This should be used to flush any hanging resources (like cookies & local storage items) 
	 * after the WebDriver is done interacting with a Web page.
	 */
	@Override
	public void flush() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.localStorage.clear()");
		js.executeScript("window.sessionStorage.clear()");
		
		driver.manage().deleteAllCookies();
		driver.get("about:blank");
		
		// clean up any other windows opened by the visited page
		String blankPage = driver.getWindowHandle();
		for (String handle : driver.getWindowHandles()) {
		    if (!handle.equals(blankPage)) {
		    	driver.switchTo().window(handle).close();
		    }
		}
		driver.switchTo().window(blankPage);
	}
	
	@Override
	public void quit() {
		driver.quit();
	}
}
