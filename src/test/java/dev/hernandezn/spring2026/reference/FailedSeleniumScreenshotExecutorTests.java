package dev.hernandezn.spring2026.reference;

//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.withSettings;
//
//import java.util.concurrent.Callable;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutorService;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebDriver;
//import org.springframework.test.util.ReflectionTestUtils;

/**
 * 
 * 
 * FAILED testing class for selenium screenshot executor.
 * 
 * 
 * Kept here for future reference, as the feature needed to be refactored for 
 * testability.
 * 
 * 
 * Failed due to the screenshot executor running on the assumption that the 
 * injected WebDriver would always be castable as an instance of 
 * JavascriptExecutor (needed to ensure the browser's DOM is loaded before 
 * taking a screenshot) AND as an instance of TakesScreenshot (needed to 
 * actually take screenshots). This resulted in the mocked WebDriver being 
 * incapable of running these two processes when injected into the class being 
 * tested, unless there was additional setup that would operate outside of 
 * injecting the WebDriver dependency. Any way I sliced it, I was fighting 
 * my design to make its testing work. So, I opted to redesign the class 
 * being tested.
 * 
 * 
 * 
 */

//@ExtendWith(MockitoExtension.class)
public class FailedSeleniumScreenshotExecutorTests {
//	
//	WebDriver mockDriver;
//	
//	@Mock
//	ExecutorService mockExecutorService;
//	
//	@InjectMocks
//	SeleniumScreenshotExecutor executor;
//	
//	
//	
//	@Mock
//	TakesScreenshot mockScreenshotTaker;
//	
//	@Mock
//	JavascriptExecutor mockJsExecutor;
//	
////	@Mock
////	WebDriver.Options mockOptions;
////	
////	@Mock
////	WebDriver.TargetLocator mockTargetLocator;
//	
//	@BeforeEach
//	void setupMockDefinitions() {
//		mockDriver = mock(
//			WebDriver.class,
//			withSettings().extraInterfaces(TakesScreenshot.class, JavascriptExecutor.class)
//		);
//		// injected via reflection
//		ReflectionTestUtils.setField(executor, "seleniumWebDriver", mockDriver);
//		
//		
//		
//		/*
//		 * When submit is called on any Callable<byte[]>-type functional 
//		 * interface implementation / lambda, return the Future<byte[]> that 
//		 * resolves to that Callable's call() return.
//		 * 
//		 * This mimics ExecutorService behavior but with a synchronous 
//		 * workflow & without internal overhead, allowing for a pure 
//		 * deterministic unit test.
//		 */
//		when(mockExecutorService.submit(Mockito.<Callable<byte[]>>any()))
//			.thenAnswer(executorServiceMockInvocation -> {
//				Callable<byte[]> task = executorServiceMockInvocation.getArgument(0);
//				return CompletableFuture.completedFuture(task.call());
//			})
//		;
//		
//		// tested class gives ClassCastExceptions when it tries to cast the 
//		// mock, but I can define the cast behavior to return other mocks
//		when((TakesScreenshot) mockDriver).thenReturn(mockScreenshotTaker);
//		when((JavascriptExecutor) mockDriver).thenReturn(mockJsExecutor);
//	}
//	
//	@Test
//	void createScreenshot_shouldReturnScreenshotBytes() throws Exception {
//		String inputUrl = "https://www.example.com";
//		byte[] expected = new byte[] {1, 2, 3, 4, 5};
//		
//		when(mockJsExecutor.executeScript("return document.readyState")).thenReturn("complete");
//		when(mockScreenshotTaker.getScreenshotAs(OutputType.BYTES)).thenReturn(expected);
////		when()
//		
//		byte[] actual = executor.createScreenshot(inputUrl).get();
//		
//		verify(
//			mockDriver, 
//			times(1).description("Webdriver used exactly 1 attempt to get from the input URL")
//		).get(anyString());
////		verify(
////			(JavascriptExecutor) mockDriver,
////			description("The driver was cast to a JavascriptExecutor, and that cast found that the ")
////		).executeScript("return document.readyState").equals("complete");
//		
//		
//	}
}
