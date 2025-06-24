package com.cms.testcases;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.NoSuchElementException;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.cms.basetest.BaseTest;
import com.cms.utility.Utility;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.List;

public class TimesheetSubmissionTest extends BaseTest {
	public SoftAssert sf;
	public JavascriptExecutor js;
	public boolean isSuccessful = false;
	public String clockInDate;
	public boolean isTaskSuccessful = false ;
	public String FinalAlert;
	boolean keepProcessing = true;
	private int index=2;
	public String clockInTime;
	public String Timein;
	public String updatedTimeStr;
	public  String token;
	String Start_date ;

	@BeforeClass
	@Parameters("browser")
	public void openUrl(String browser) throws IOException
	{
		initBrowser(browser);
		creatingObject();	
		//		Utility.showTooltip("Browser is Launched using Selenium Automation Tool");
		sf = new SoftAssert();
	}

	//	@BeforeMethod
	public void LaunchUrl() throws IOException, InterruptedException
	{
		//		Thread.sleep(2000);
		LaunchUrl();
	}

	@Test(priority=1)
	public void ValidateaddNewTimesheetFunctionality() throws InterruptedException, IOException
	{

		launchUrl();

		Thread.sleep(2000);

		lp.SendUserName();

		lp.SendPassword();

		lp.ClickonLoginBtn();

		String ActualProfileName=lp.GetProfileName();
		String ExpectedProfileName ="Welcome, AutomationTesting";

		sf.assertEquals(ActualProfileName, ExpectedProfileName);
		tsp.ClickonTimesheetSubmission();
		Thread.sleep(1000);
	

		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");



	List<WebElement> allWeeks = driverR.findElements(By.xpath("//div[contains(@class,'p-1 shadow mb-2 bg-gradient border-2')]"));
		System.out.println("Total week blocks found: " + allWeeks.size());
		while (keepProcessing) {
		    try {
		        if (index > allWeeks.size()) {
		            System.out.println("✅ All week blocks processed.");
		            break;
		        }

		        // 1. Click on week container
		        String weekContainerXPath = "(//div[contains(@class,'p-1 shadow mb-2 bg-gradient border-2')])[" + index + "]";
		        WebElement weekElement = driverR.findElement(By.xpath(weekContainerXPath));
		        weekElement.click();
		        Thread.sleep(1000); // optional delay

		        // 2. Wait for status span to appear
		        WebDriverWait wait = new WebDriverWait(driverR, Duration.ofSeconds(5));
		        String statusXPath = weekContainerXPath + "//span[contains(@class, 'text-white')]";
		        WebElement statusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(statusXPath)));

		        // 3. Clean and evaluate status text
		        String status = statusElement.getText().replace("\u00A0", " ").trim();
		        System.out.println("Week " + index + " Status: '" + status + "'");

		        if (status.equalsIgnoreCase("Not Submitted")) {
		            // 4. Extract start & end date
		            String startXPath = "(" + weekContainerXPath + "/div/div)[1]";
		            String endXPath = "(" + weekContainerXPath + "/div/div)[2]";
		            String startText = driverR.findElement(By.xpath(startXPath)).getText();
		            String endText = driverR.findElement(By.xpath(endXPath)).getText();

		            LocalDate startDate = LocalDate.parse(startText.substring(startText.indexOf(',') + 1).trim(), inputFormatter);
		            LocalDate endDate = LocalDate.parse(endText.substring(endText.indexOf(',') + 1).trim(), inputFormatter);

		            System.out.println("Start: " + startDate + ", End: " + endDate);

		            // 5. Authenticate API and fetch token
		            RestAssured.baseURI = "https://testbackend.ndtatlas.com";
		            Response loginResponse = RestAssured.given()
		                    .header("Content-Type", "application/json")
		                    .body("{ \"username\": \"AutomationTestUser\", \"password\": \"Test@123\" }")
		                    .when().post("/api/auth/login/")
		                    .then().statusCode(200)
		                    .extract().response();

		            String token = loginResponse.jsonPath().getString("data.token");
		            System.out.println("🔐 Token fetched.");

		            // 6. Submit attendance entries for each day
		            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
		                String dateStr = date.format(outputFormatter);
		                String baseTime = "09:00:00";

		                System.out.println("🗓️  Submitting entries for: " + dateStr);

		                sendAttendanceData(token, dateStr + "T" + baseTime + "Z", "clockin");
		                sendAttendanceData(token, dateStr + "T" + addHours(baseTime, 1) + "Z", "breakin");
		                sendAttendanceData(token, dateStr + "T" + addHours(baseTime, 2) + "Z", "breakout");
		                sendAttendanceData(token, dateStr + "T" + addHours(baseTime, 8) + "Z", "clockout");
		            }
		        }

		        index++; // move to next week

		    } catch (Exception e) {
		        System.err.println("❌ Error on week index " + index + ": " + e.getMessage());
		        e.printStackTrace();
		        index++; // continue with next week even on failure
		    }
		}
	}
		public String addHours(String baseTime, int hoursToAdd) {
		    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		    LocalTime time = LocalTime.parse(baseTime, timeFormatter);
		    return time.plusHours(hoursToAdd).format(timeFormatter);
		}

		public void sendAttendanceData(String token, String timestamp, String endpoint) {
		    HashMap<String, String> data = new HashMap<>();
		    data.put("test_timestamp", timestamp);

		    System.out.println("→ Sending to " + endpoint + ": " + timestamp);

		    given()
		        .contentType("application/json")
		        .header("Authorization", "Token " + token)
		        .body(data)
		        .when()
		        .patch("https://testbackend.ndtatlas.com/api/attendance-test/" + endpoint + "/")
		        .then()
		        .statusCode(201)
		        .log().all();
		}



		
	



	//	@AfterMethod
	public void closeURL()
	{
		driverR.navigate().to("about:blank");
	}

	@AfterClass
	public void closebrowser()
	{

		teardown();
	}

}



