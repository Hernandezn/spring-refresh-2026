package dev.hernandezn.spring2026.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.hernandezn.spring2026.port.WebScreenshotBrowser;
import dev.hernandezn.spring2026.util.SeleniumScreenshotBrowser;

@Configuration
public class SeleniumConfig {
	
	// A single WebDriver instance IS NOT THREAD SAFE; requests should be serialized, like with a single-thread ExecutorService
	// Typically wouldn't use a browser session per service, and it should be occasionally flushed/restarted...
	@Bean(
		name="seleniumScreenshotter",
		destroyMethod="quit"
	)
	public WebScreenshotBrowser screenshotBrowser() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless=new");
		options.addArguments("--window-size=1920,1080");
		return new SeleniumScreenshotBrowser(new ChromeDriver(options));
	}
	
	/**
	 * This ExecutorService Bean is specifically for the Selenium-powered 
	 * screenshot API.
	 * 
	 * It's given a name to distinguish it from other screenshotExecutors.
	 * 
	 * When using the Autowired annotation to use this Bean, also address it 
	 * using the Qualifier("screenshotExecutor") annotation
	 * 
	 * @return
	 */
	@Bean(
		name="screenshotTaskExecutor",
		destroyMethod="shutdown"
	)
	public ExecutorService screenshotExecutor() {
		return Executors.newSingleThreadExecutor();
	}
}
