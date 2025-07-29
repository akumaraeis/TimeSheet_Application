package com.cms.utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cms.basetest.BaseTest;

public class Utility extends BaseTest {
	
	
	 public static String captureScreenshot(String testName, String status) {
	        TakesScreenshot ts = (TakesScreenshot) driverR;
	        File source = ts.getScreenshotAs(OutputType.FILE);
	        
	        // Dynamic path for screenshot storage
	        String baseDir = System.getProperty("user.dir") + "\\AEIS-CMS-Screenshots\\";
	        File dir = new File(baseDir);
	        if (!dir.exists()) {
	            dir.mkdirs(); // Ensure the directory exists
	        }
	        
	        String destination = baseDir + "ContactCenter_" + testName + "_" + status + ".jpg";
	        File finalDestination = new File(destination);
	        
	        try {
	            // Convert and save as JPG if necessary
	            BufferedImage img = ImageIO.read(source);
	            ImageIO.write(img, "jpg", finalDestination);
	        } catch (IOException e) {
	            e.printStackTrace(); // Log exception or handle it as needed
	        }
	        
	        return destination; // Return path for reporting
	    }
	
    
    public static String getToken() {
       String token ="Bearer "+"Z4uzBKvHpTJlh5QCf9SDzQyHJoKQJpb6";
//         String token ="Bearer "+"07DphO7pAhaC7jcUkQBsiVaK4aZv8h3f";
		return token ;
    }
    
    
    // Method to wait for visibility of WebElement
    public static WebElement ExplicitWait(WebElement element) {
    	WebDriverWait wait = new WebDriverWait(driverR,Duration.ofSeconds(300));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    public static WebElement ExplicitClickWait(WebElement element) {
    	WebDriverWait wait = new WebDriverWait(driverR,Duration.ofSeconds(300));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    public static void switchToTab(int index) {
        ArrayList<String> tabs = new ArrayList<>(driverR.getWindowHandles());
        driverR.switchTo().window(tabs.get(index));
    }
//	public static void ExplicitWait(WebElement ele)
//	{
//		WebDriverWait wait = new WebDriverWait(driverR,Duration.ofSeconds(50));
//		wait.until(ExpectedConditions.visibilityOf(ele));
//		
//	}
	
//	  public static void ExplicitClickWait(WebElement ele) {
//	        WebDriverWait wait = new WebDriverWait(driverR, Duration.ofSeconds(10));
//	        wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(ele)));
//	        loginButton.click();
//	    }
	  
//	public static void ExplicitWait2(WebElement ele)
//{
//	WebDriverWait wait = new WebDriverWait(driverR,Duration.ofSeconds(50));
//	wait.until(ExpectedConditions.elementToBeClickable(ele));
//}

	 public static void showTooltip(String message) {
	        String script = "var tooltip = document.createElement('div');" +
	                        "tooltip.innerHTML = '" + message + "';" +
	                        "tooltip.style.position = 'fixed';" +
	                        "tooltip.style.top = '80px';" +
	                        "tooltip.style.left = '50%';" +
	                        "tooltip.style.padding = '25px';" +
	                        "tooltip.style.backgroundColor = 'green';" +
	                        "tooltip.style.color = 'white';" +
	                        "tooltip.style.border = '1px solid black';" +
	                        "tooltip.style.zIndex = '10000';" +
	                        "document.body.appendChild(tooltip);" +
	                        "setTimeout(function() { document.body.removeChild(tooltip); }, 2000);";

	        ((JavascriptExecutor) driverR).executeScript(script);
	    }
	 public static void showCallout(String message , WebElement element) {
		  int x = element.getLocation().getX();
	        int y = element.getLocation().getY();

	        // Adjust position for the callout
	        int calloutX = x;
	        int calloutY = y-60;  // Place the callout above the element

/*	        String script2 = "var callout = document.createElement('div');" +
                 "callout.innerHTML = '" + message + "';" +
                 "callout.style.position = 'absolute';" +
                 "callout.style.left = '" + calloutX + "px';" +
                 "callout.style.top = '" + calloutY + "px';" +
                 "callout.style.padding = '10px';" +
                 "callout.style.backgroundColor = 'red';" + // Light blue color
                 "callout.style.border = '1px solid #000';" +
                 "callout.style.borderRadius = '5px';" +
                 "callout.style.boxShadow = '0 0 10px rgba(0, 0, 0, 0.1)';" +
                 "callout.style.zIndex = '10000';" +
                 "callout.style.display = 'flex';" +
                 "callout.style.alignItems = 'center';" +
                 "var arrow = document.createElement('div');" +
                 "arrow.style.width = '6px';" + // Bold arrow
                 "arrow.style.height = '100px';" + // Long arrow
                 "arrow.style.backgroundColor = '#000';" + // Black color
                 "arrow.style.position = 'absolute';" +
                 "arrow.style.left = '-20px';" +  // Adjust to position the arrow to the left of the callout
                 "arrow.style.top = '5px';" + // Adjust to position the arrow down from the top of the callout
               //  "arrow.style.transform = 'rotate(-45deg)';" +  // Incline the arrow
//                 "arrow.style.transformOrigin = 'top left';" +
//                 "callout.appendChild(arrow);" +
//                 "var arrowHead = document.createElement('div');" +
//                 "arrowHead.style.position = 'absolute';" +
//                 "arrowHead.style.fontSize = '60px';" +
//                 "arrowHead.style.color = '#000';" + // Black color
//                 "arrowHead.style.left = '0px';" +  // Adjust to position the arrowhead at the end of the arrow
//                 "arrowHead.style.top = '0px';" + // Adjust to position the arrowhead down from the top of the callout
//                 "arrowHead.innerHTML = '&#x279E;';" + // Unicode for ➤
//                 "callout.appendChild(arrowHead);" +
//                 "document.body.appendChild(callout);" +
                 "setTimeout(function() { document.body.removeChild(callout); }, 3000);";

	        ((JavascriptExecutor) driverR).executeScript(script2);
	*/
	        String script2 =  "var callout = document.createElement('div');" +
         "callout.innerHTML = '" + message + "';" +
         "callout.style.position = 'absolute';" +
         "callout.style.left = '" + calloutX + "px';" +
         "callout.style.top = '" + calloutY + "px';" +
         "callout.style.padding = '10px';" +
         "callout.style.backgroundColor = 'green';" + 
         "callout.style.color = 'white';" + 
         "callout.style.border = '1px white';" +
         "callout.style.borderRadius = '5px';" +
         "callout.style.boxShadow = '0 0 10px rgba(0, 0, 0, 0.1)';" +
         "callout.style.zIndex = '10000';" +
         "callout.style.display = 'flex';" +
         "callout.style.alignItems = 'center';" +
         "var arrow = document.createElement('div');" +
         "arrow.style.width = '0';" +
         "arrow.style.height = '0';" +
         "arrow.style.borderLeft =  '10px solid transparent';" +
         "arrow.style.borderRight = '10px solid transparent';" +
         "arrow.style.borderTop = 'white';" + // Same light blue color
         "arrow.style.position = 'absolute';" +
         "arrow.style.left = '10px';" +
         "arrow.style.top = '100%';" +
         "callout.appendChild(arrow);" +
         "document.body.appendChild(callout);" +
         "setTimeout(function() { document.body.removeChild(callout); }, 1000);";
	        ((JavascriptExecutor) driverR).executeScript(script2);
	    }
	
public static void safeClick(WebDriver driverR, JavascriptExecutor js, WebElement element) {
    WebDriverWait wait = new WebDriverWait(driverR, Duration.ofSeconds(10));
    try {
        // Scroll element into center view
//    	JavascriptExecutor js1 = (JavascriptExecutor)driverR;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        // Wait until it is clickable
        wait.until(ExpectedConditions.elementToBeClickable(element));
        
        try {
            element.click();  // normal click
        } catch (ElementClickInterceptedException e) {
            System.out.println("Click intercepted, using JS fallback.");
            js.executeScript("arguments[0].click();", element);  // fallback click
        }
    } catch (Exception e) {
        System.out.println("safeClick failed: " + e.getMessage());
    }
}
public static WebElement ExpectTimesheetSuccesful(WebDriver driver, By locator, int timeoutSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
}

public static WebElement waitForElementToBeClickable(WebDriver driver, By locator, int timeoutSeconds) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    return wait.until(ExpectedConditions.elementToBeClickable(locator));
}

public static void scrollIntoView(WebDriver driver, JavascriptExecutor js, WebElement element) {
    js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
}

public static void waitForSeconds(double seconds) {
    try {
        Thread.sleep((long) (seconds * 1000));
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
}

public static void highlightElement(WebElement Element)
{
	JavascriptExecutor js = (JavascriptExecutor)driverR;
	js.executeScript("arguments[0].setAttribute('style','background:yellow;border:4px solid green;')",Element );

}

}


