package dev.hernandezn.spring2026.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.InvalidArgumentException;

import dev.hernandezn.spring2026.port.WebScreenshotBrowser;

@ExtendWith(MockitoExtension.class)
class ScreenshotExecutorTests {
	
	@Mock
	WebScreenshotBrowser browser;
	
	@Mock
	ExecutorService singleThreadExecutor;
	
	@InjectMocks
	ScreenshotExecutor screenshotExecutor;
	
	@Test
	void createScreenshot_shouldReturnScreenshotBytes() throws Exception {
		String inputUrl = "https://www.example.com";
		byte[] expected = new byte[] {1, 2, 3, 4, 5};
		
		Callable<byte[]> executedLambda = extractLambdaFromExecutorServiceSubmit(inputUrl);
		
		when(browser.takeScreenshotAfterMs(0)).thenReturn(expected);
		
		assertArrayEquals(
			expected,
			executedLambda.call(),
			"The createScreenshot method should return the screenshot bytes returned from the browser"
		);
		
		verify(
			browser,
			times(1).description("Browser should use exactly 1 attempt to get from the input URL")
		).targetUrl(inputUrl);
		verify(
			browser,
			description("Browser should be called to take a screenshot without delay")
		).takeScreenshotAfterMs(0);
		verify(
			browser,
			description("Browser should be flushed")
		).flush();
	}
	
	@Test
	void createScreenshot_shouldSwapURISchemeFromHttps() throws Exception {
		String inputUrl = "https://www.example.com";
		byte[] expected = new byte[] {1, 2, 3, 4, 5};
		
		Callable<byte[]> executedLambda = extractLambdaFromExecutorServiceSubmit(inputUrl);
		
		doThrow(new InvalidArgumentException(inputUrl))
			.when(browser)
			.targetUrl(inputUrl)
		;
		when(browser.takeScreenshotAfterMs(0)).thenReturn(expected);
		
		executedLambda.call();
		
		verify(
			browser,
			times(2).description("Browser should use exactly 2 attempts to target URLs")
		).targetUrl(anyString());
		verify(
			browser,
			description("Browser should target the supplied URL with \"http\" instead of \"https\"")
		).targetUrl("http://www.example.com");
		verify(
			browser,
			description("Browser should be called to take a screenshot without delay")
		).takeScreenshotAfterMs(0);
		verify(
			browser,
			description("Browser should be flushed")
		).flush();
	}
	
	@Test
	void createScreenshot_shouldSwapURISchemeFromHttp() throws Exception {
		String inputUrl = "http://www.example.com";
		String swappedUrl = "https://www.example.com";
		byte[] expected = new byte[] {1, 2, 3, 4, 5};
		
		Callable<byte[]> executedLambda = extractLambdaFromExecutorServiceSubmit(inputUrl);
		
		doThrow(new InvalidArgumentException(inputUrl))
			.when(browser)
			.targetUrl(inputUrl)
		;
		when(browser.takeScreenshotAfterMs(0)).thenReturn(expected);
		
		executedLambda.call();
		
		verify(
			browser,
			times(2).description("Browser should use exactly 2 attempts to target URLs")
		).targetUrl(anyString());
		verify(
			browser,
			description("Browser should target the supplied URL with \"https\" instead of \"http\"")
		).targetUrl(swappedUrl);
		verify(
			browser,
			description("Browser should be called to take a screenshot without delay")
		).takeScreenshotAfterMs(0);
		verify(
			browser,
			description("Browser should be flushed")
		).flush();
	}
	
	@Test
	void createScreenshot_shouldThrowExceptionForUnreachableUrls() throws Exception {
		String inputUrl = "https://www.example.com";
		String swappedUrl = "http://www.example.com";
		InvalidArgumentException expected = new InvalidArgumentException(inputUrl);
		
		Callable<byte[]> executedLambda = extractLambdaFromExecutorServiceSubmit(inputUrl);
		
		doThrow(expected)
			.when(browser)
			.targetUrl(inputUrl)
		;
		doThrow(new InvalidArgumentException(swappedUrl))
			.when(browser)
			.targetUrl(swappedUrl)
		;
		assertThrows(
			InvalidArgumentException.class, 
			executedLambda::call
		);
		
		verify(
			browser,
			description("Browser should attempt to target the input URL")
		).targetUrl(inputUrl);
		verify(
			browser,
			description("Browser should attempt to target the input URL with the URI scheme swapped")
		).targetUrl(swappedUrl);
		verify(
			browser,
			never().description("Browser that never reaches its target should NOT be called to take a screenshot")
		).takeScreenshotAfterMs(anyLong());
	}
	
	private Callable<byte[]> extractLambdaFromExecutorServiceSubmit(String inputUrl) {
		ArgumentCaptor<Callable<byte[]>> submitArgCaptor = ArgumentCaptor.forClass(Callable.class);
		when(singleThreadExecutor.submit(submitArgCaptor.capture())).thenReturn(mock(Future.class));
		
		// runs the executor submit so its argument can be captured
		screenshotExecutor.createScreenshot(inputUrl);
		
		return submitArgCaptor.getValue();
	}
}
