package dev.hernandezn.spring2026.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumConfig {

	// A single WebDriver instance IS NOT THREAD SAFE, so a single-thread executor is used to serialize requests
	// Typically wouldn't use a browser session per service, and it should be occasionally flushed/restarted...
	@Bean(destroyMethod="quit")
	public WebDriver screenshotExecutor() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless=new");
		options.addArguments("--window-size=1920,1080");
		return new ChromeDriver(options);
	}
}
