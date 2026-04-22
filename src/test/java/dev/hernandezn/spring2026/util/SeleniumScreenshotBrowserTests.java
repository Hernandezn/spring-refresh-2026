package dev.hernandezn.spring2026.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.Set;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.TargetLocator;

@ExtendWith(MockitoExtension.class)
class SeleniumScreenshotBrowserTests {
	
	@Mock
	WebDriver driver;
	
	@Mock
	TakesScreenshot screenshotter;
	
	@Mock
	JavascriptExecutor js;
	
	@Mock
	Consumer<Long> delayFunction;
	
	@InjectMocks
	SeleniumScreenshotBrowser browser;
	
	@Test
	void create_shouldThrowIfDriverCantTakeScreenshot() {
		WebDriver driverWithoutTakesScreenshotInterface = mock(WebDriver.class);
		
		assertThrows(
			IllegalStateException.class, 
			() -> SeleniumScreenshotBrowser.create(driverWithoutTakesScreenshotInterface),
			"create() shold throw an IllegalStateException if the input WebDriver is not a TakesScreenshot instance"
		);
	}
	
	@Test
	void create_shouldThrowIfDriverIsntJavascriptExecutor() {
		WebDriver driverWithoutJavascriptExecutorInterface = 
			mock(
				WebDriver.class,
				withSettings().extraInterfaces(TakesScreenshot.class)
			)
		;
		
		assertThrows(
			IllegalStateException.class, 
			() -> SeleniumScreenshotBrowser.create(driverWithoutJavascriptExecutorInterface),
			"create() shold throw an IllegalStateException if the input WebDriver is not a JavascriptExecutor instance"
		);
	}
	
	@Test
	void create_shouldNotThrowIfItImplementsBothInterfaces() {
		WebDriver driverWithBothInterfaces = 
			mock(
				WebDriver.class,
				withSettings().extraInterfaces(TakesScreenshot.class, JavascriptExecutor.class)
			)
		;
		
		assertDoesNotThrow(() -> SeleniumScreenshotBrowser.create(driverWithBothInterfaces));
	}
	
	@Test
	void targetUrl_shouldGetUrlFromDriver() {
		String inputUrl = "https://www.example.com";
		
		browser.targetUrl(inputUrl);
		
		verify(
			driver,
			description("Targetting a URL should direct the WebDriver to get from the URL")
		).get(inputUrl);
	}
	
	@Test
	void takeScreenshotAfterMs_shouldReturnBytesWithGivenDelay() {
		byte[] expected = new byte[] {1, 2, 3, 4, 5};
		long delayInput = 100L;
		
		when(js.executeScript("return document.readyState")).thenReturn("complete");
		when(screenshotter.getScreenshotAs(OutputType.BYTES)).thenReturn(expected);
		
		ArgumentCaptor<Long> delayDurationCaptor = ArgumentCaptor.forClass(Long.class);
		
		assertArrayEquals(
			expected,
			browser.takeScreenshotAfterMs(delayInput)
		);
		verify(delayFunction).accept(delayDurationCaptor.capture());
		assertEquals(
			delayInput,
			delayDurationCaptor.getValue()
		);
	}
	
	@Test
	void takeScreenshot_shouldReturnBytesWithNoDelay() {
		byte[] expected = new byte[] {1, 2, 3, 4, 5};
		
		when(js.executeScript("return document.readyState")).thenReturn("complete");
		when(screenshotter.getScreenshotAs(OutputType.BYTES)).thenReturn(expected);
		
		ArgumentCaptor<Long> delayDurationCaptor = ArgumentCaptor.forClass(Long.class);
		
		assertArrayEquals(
			expected,
			browser.takeScreenshot()
		);
		verify(delayFunction).accept(delayDurationCaptor.capture());
		assertEquals(
			0L,
			delayDurationCaptor.getValue()
		);
	}
	
	@Test
	void flush_shouldCleanWebDriver() {
		Options driverManageOptions = Mockito.mock(Options.class);
		TargetLocator switchToLocator = Mockito.mock(TargetLocator.class);
		WebDriver closingWindowDriver = Mockito.mock(WebDriver.class);
		
		when(driver.manage()).thenReturn(driverManageOptions);
		when(driver.getWindowHandle()).thenReturn("main");
		when(driver.getWindowHandles()).thenReturn(Set.of("not main", "main", "also not main"));
		when(driver.switchTo()).thenReturn(switchToLocator);
		when(switchToLocator.window(anyString())).thenReturn(closingWindowDriver);
		when(switchToLocator.window("main")).thenReturn(driver);
		
		browser.flush();
		
		verify(js).executeScript("window.localStorage.clear()");
		verify(js).executeScript("window.sessionStorage.clear()");
		verify(driverManageOptions).deleteAllCookies();
		verify(driver).get("about:blank");
		verify(switchToLocator, times(1)).window("not main");
		verify(switchToLocator, times(1)).window("also not main");
		verify(closingWindowDriver, times(2)).close();
		verify(switchToLocator, times(1)).window("main");
	}
	
	@Test
	void quit_shouldCallDriverQuit() {
		browser.quit();
		
		verify(driver).quit();
	}
}
