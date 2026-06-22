package com.cms.testcases;

import static io.restassured.RestAssured.given;

import java.io.IOException;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

import java.util.List;

import org.openqa.selenium.By;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.cms.basetest.BaseTest;
import com.cms.utility.Log;
import com.cms.utility.Utility;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class RegularizationTest extends BaseTest {
	public SoftAssert sf;
	public JavascriptExecutor js;
	public boolean isSuccessful = false;
	public String clockInDate;
	public String FinalAlert;
	public String Timein ;
	public String clockInTime;
	public String updatedTimeStr;
	public String updatedTime2;
	public String token ;
	
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
	public void DeleteTestUserRecord() throws InterruptedException, IOException
	{
		RestAssured.baseURI = "https://timesheet.ndtatlas.com/";
		Response loginResponse = RestAssured.given()
				.header("Content-Type", "application/json")
				.body("{ \"username\": \"AutomationTestUser\", \"password\": \"Test@123\" }")
				.when().post("/api/auth/login/")
				.then().statusCode(200)
				.extract().response();
		token = loginResponse.jsonPath().getString("data.token");
		Log.info("🔐 Token fetched: " + token);
		DeleteAutomationTestUserRecords(token);
	}
	@Test(priority=2)
	public void ValidateTimesheetSubmissionFunctionality() throws InterruptedException, IOException
	{
		sf = new SoftAssert();
		Utility.showTooltip("Executing Automation Script to Check Timesheet Submission by User and Approval Functionality by Line Manager.");

		Utility.waitForSeconds(5);

		launchUrl();
		Log.info("Url is launched");

		Utility.showTooltip("Step 1:->After Launching Timesheet Application, Login as User to submit Weekly Timesheet using Automation Script");

		Utility.waitForSeconds(2);

		lp.SendUserName();
		Log.info("input user name");

		lp.SendPassword();
		Log.info("input user Password");

		lp.ClickonLoginBtn();
		Log.info("Click on Login Button");

		tsp.ClickonTimesheetSubmission();

		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		LocalDate currentweekMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
//		LocalDate previousMonday = currentweekMonday.minusWeeks(1);
//		LocalDate previousFriday = previousMonday.plusDays(4);
//
//		String formattedMonday = previousMonday.format(formatter);
//		Log.info("Loop start formatted Monday :->"+formattedMonday);
//		String formattedFriday = previousFriday.format(formatter);
//		Log.info("Loop end formatted friday :->"+formattedFriday);
		String baseTime = "11:45:00";
//		LocalDate date = previousMonday; 

			String date2 = today.format(formatter);
			Log.info("Date Stamp entry for timestamp for send Attendance Method "+date2);
			Log.info("🗓️ Submitting entries for:->" + date2);
			sendAttendanceData(token,"CLOCK_IN", date2, baseTime);
			sendAttendanceData(token,"BREAK_IN", date2,addHours(baseTime, 1));
			sendAttendanceData(token,"BREAK_OUT", date2,addHours(baseTime, 2));
			sendAttendanceData(token,"CLOCK_OUT", date2,addHours(baseTime, 4));
			Utility.waitForSeconds(2);
			Log.info("Timesheet entry Created Successfully for date :->"+date2);
			
		       trp.ClickonRegularizationsBtn();
		       Log.info("User Navigate to Regularization page.");
		       trp.ActionOnRegularizationBtn();   
			   trp.SendRegularizationReason();
			   trp.SendClockIn();
			   trp.SendBreakIn();
			   trp.SendBreakBreakOut();
			   trp.SendClockOut();
			   trp.ClickonSubmitBtn();
		}
	
		public String addHours(String baseTime, int hoursToAdd) {

			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			LocalTime time = LocalTime.parse(baseTime, timeFormatter);
			return time.plusHours(hoursToAdd).format(timeFormatter);
		}

		public void sendAttendanceData(String token,String action, String timestamp, String time) {
			HashMap<String, String> data = new HashMap<>();
			data.put("action", action);
			data.put("action_date", timestamp);
			data.put("action_time", time);

			Log.info("→ Sending to " + ": " + timestamp);

			given()
			.contentType("application/json")
			.header("Authorization", "Token " + token)
			.body(data)
			.when()
			.post("https://timesheet.ndtatlas.com/api/clock-entries/action-test/")
			.then()
			.statusCode(200)
			.log().all();
		}
		public void sendAttendanceData2(String token,String action, String timestamp, String time) {
			HashMap<String, String> data = new HashMap<>();
			data.put("action", action);
			data.put("action_date", timestamp);
			data.put("action_time", time);

			Log.info("→ Sending to " + ": " + timestamp);

			given()
			.contentType("application/json")
			.header("Authorization", "Token " + token)
			.body(data)
			.when()
			.post("https://timesheet.ndtatlas.com/api/clock-entries/action/")
			.then()
			.statusCode(200)
			.log().all();
		}

	public void DeleteAutomationTestUserRecords(String token) {
		given()
		.contentType("application/json")
		.header("Authorization", "Token " + token)
		//		        .body(data)
		.when()
		.post("https://timesheet.ndtatlas.com/api/utils/remove-automation-test-data/")
		.then()
		.statusCode(200)
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



