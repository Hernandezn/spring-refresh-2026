package dev.hernandezn.spring2026.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hernandezn.spring2026.util.ScreenshotExecutor;

@ExtendWith(MockitoExtension.class)
public class ScreenshotServiceTests {
	@Mock
	ScreenshotExecutor screenshotExecutor;
	
	@InjectMocks
	ScreenshotService screenshotService;
	
	@Test
	void fetchScreenshot_shouldReturnScreenshotBytes() {
		String inputUrl = "https://example.com";
		
		byte[] expectedBytes = new byte[] {1, 2, 3, 4, 5};
		
		when(screenshotExecutor.createScreenshot(anyString()))
			.thenReturn(CompletableFuture.completedFuture(expectedBytes))
		;
		
		assertArrayEquals(
			expectedBytes, 
			screenshotService.fetchScreenshot(inputUrl),
			"Should return screenshot byte data from the screenshotExecutor's completed Future"
		);
	}
	
	@Test
	void fetchScreenshot_shouldUseHttpsForMissingScheme() {
		String inputUrl = "example.com";
		
		when(screenshotExecutor.createScreenshot(anyString()))
			.thenReturn(CompletableFuture.completedFuture(new byte[0]))
		;
		
		screenshotService.fetchScreenshot(inputUrl);
		
		verify(
			screenshotExecutor, 
			description("Should prepend \"https://\" when the scheme is missing from the input URL")
		).createScreenshot("https://example.com");
	}
	
	@Test
	void fetchScreenshot_shouldUseFallbackUrlForNullOrBlankOrEmptyInput() throws Exception {
		byte[] expectedBytes = new byte[] {1, 2, 3, 4, 5};
		
		when(screenshotExecutor.createScreenshot(anyString())).thenReturn(
			CompletableFuture.completedFuture(expectedBytes)
		);
		
		screenshotService.fetchScreenshot(null);
		screenshotService.fetchScreenshot("   ");
		screenshotService.fetchScreenshot("");
		
		verify(
			screenshotExecutor, 
			times(3).description("3 missing URLs should request the screenshot of a default URL exactly 3 times")
		).createScreenshot("https://www.google.com");
	}
	
	@Test
	void fetchScreenshot_shouldReturnEmptyForUnparseableUris() throws Exception {
		String inputUrl = "ht tps://ww w.goo gle.c om";
		
		// no cover needed for takeScreenshot, since that method is never reached here
		
		assertArrayEquals(
			new byte[0], 
			screenshotService.fetchScreenshot(inputUrl),
			"Should return empty byte data when the input URL can't be parsed as a URI"
		);
	}
	
	@Test
	void fetchScreenshot_shouldReturnEmptyUponTimeout() throws Exception {
		Future<byte[]> mockFuture = mock(Future.class);
		
		when(screenshotExecutor.createScreenshot(anyString())).thenReturn(
			mockFuture
		);
		when(mockFuture.get(anyLong(), any())).thenThrow(new TimeoutException());
		
		assertEquals(
			0,
			screenshotService.fetchScreenshot("https://www.google.com").length,
			"Should return empty byte data when the screenshotter times out"
		);
	}
	
	@Test
	void fetchScreenshot_shouldReturnEmptyWhenExecutionFails() throws Exception {
		Future<byte[]> mockFuture = mock(Future.class);
		
		when(screenshotExecutor.createScreenshot(anyString())).thenReturn(
			mockFuture
		);
		when(mockFuture.get(anyLong(), any())).thenThrow(new ExecutionException(new RuntimeException()));
		
		assertEquals(
			0,
			screenshotService.fetchScreenshot("https://www.google.com").length,
			"Should return empty byte data when an internal task aborted, causing the Selenium screenshot executor to fail"
		);
	}
	
	@Test
	void fetchScreenshot_shouldReturnEmptyWhenInterrupted() throws Exception {
		Future<byte[]> mockFuture = mock(Future.class);
		
		when(screenshotExecutor.createScreenshot(anyString())).thenReturn(
			mockFuture
		);
		when(mockFuture.get(anyLong(), any())).thenThrow(new InterruptedException());
		
		assertEquals(
			0,
			screenshotService.fetchScreenshot("https://www.google.com").length,
			"Should return empty byte data when the screenshotter's thread is interrupted"
		);
	}
}
