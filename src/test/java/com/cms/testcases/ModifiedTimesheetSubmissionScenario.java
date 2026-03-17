package com.cms.testcases;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Locale;
import java.util.NoSuchElementException;


import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
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
import com.cms.utility.Log;
import com.cms.utility.Utility;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.util.List;

public class ModifiedTimesheetSubmissionScenario extends BaseTest {
	public SoftAssert sf;
	public SoftAssert sf2;
	public JavascriptExecutor js;
	public boolean isSuccessful = false;
	public String clockInDate;
	public boolean isTaskSuccessful = false ;
	public String FinalAlert;
	boolean keepProcessing = true;
	//	private int index=2;
	public String clockInTime;
	public String Timein;
	public String updatedTimeStr;
	public  String token;
	String Start_date ;
	String weekContainerXPath;
	int totalWeeks ;
	public WebElement MinimizeBtn;
	public String ActualUserName;
	public String ExpectTimesheetApproveMsg;
	public String dateStr;
	public WebDriverWait wait;

	@BeforeClass
	@Parameters("browser")
	public void openUrl(String browser) throws IOException
	{
		initBrowser(browser);
		creatingObject();	
		//		Utility.showTooltip("Browser is Launched using Selenium Automation Tool");

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
		System.out.println("🔐 Token fetched: " + token);
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
		LocalDate currentweekMonday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate previousMonday = currentweekMonday.minusWeeks(1);
		LocalDate previousFriday = previousMonday.plusDays(4);

		String formattedMonday = previousMonday.format(formatter);
		System.out.println("Loop start formatted Monday :->"+formattedMonday);
		String formattedFriday = previousFriday.format(formatter);
		System.out.println("Loop end formatted friday :->"+formattedFriday);
		String baseTime = "10:30:00";
		for (LocalDate date = previousMonday; !date.isAfter(previousFriday); date = date.plusDays(1)) {

			String date2 = date.format(formatter);
			System.out.println("Date Stamp entry for timestamp for send Attendance Method "+date2);
			System.out.println("🗓️ Submitting entries for:->" + date2);
			sendAttendanceData(token,"CLOCK_IN", date2, baseTime);
			sendAttendanceData(token,"BREAK_IN", date2,addHours(baseTime, 1));
			sendAttendanceData(token,"BREAK_OUT", date2,addHours(baseTime, 2));
			sendAttendanceData(token,"CLOCK_OUT", date2,addHours(baseTime, 9));
			Utility.waitForSeconds(2);
			Log.info("Timesheet entry Created Successfully for date :->"+date2);
		}

		driverR.navigate().refresh();
		Utility.waitForSeconds(1);
		Utility.showTooltip("Step 3 :->After Selecting Week,Timesheet Entry from (Mon-Fri) are creating in Background using API RestAssured ");
		String weekXPath = "(//div[contains(@class,'p-1 shadow mb-2 bg-gradient border-2 week-item  undefined card')])";
		WebElement weekElement = driverR.findElement(By.xpath(weekXPath));
		js = (JavascriptExecutor) driverR;
		js.executeScript("arguments[0].scrollIntoView(true);", weekElement);
		Utility.safeClick(driverR, js, weekElement);
		//

		List<WebElement> taskButtons = driverR.findElements(By.xpath("//*[contains(text(),'Add New Task')]"));
		System.out.println("Total Add Task buttons: " + taskButtons.size());

		//				Utility.showTooltip("Step 4:-> After Creating weekly TimesheetEntry,Now adding task to all entry using Automation Script");
		//				WebElement MinimizeBtn = driverR.findElement(By.xpath("(//*[contains(@class,'d-flex justify-content-end col-sm-1')])[1]"));
		//				Utility.scrollIntoView(driverR, js, MinimizeBtn);
		//				MinimizeBtn.click();

		for (int i = 1; i <= taskButtons.size(); i++) {
			try {
				WebElement MinimizeBtn = driverR.findElement(By.xpath("(//*[contains(@class,'d-flex justify-content-end col-sm-1')])["+i+"]"));
				Utility.scrollIntoView(driverR, js, MinimizeBtn);
				MinimizeBtn.click();
				WebElement addTaskBtn = driverR.findElement(By.xpath("(//*[contains(text(),'Add New Task')])["+i+"]"));
				Utility.scrollIntoView(driverR, js, addTaskBtn);
				wait = new WebDriverWait(driverR,Duration.ofSeconds(10));
				wait.until(ExpectedConditions.elementToBeClickable(addTaskBtn));
				Utility.safeClick(driverR, js, addTaskBtn);

				att.SelectSubProcess();
				att.ClickonActivity();
				att.SendTaskDescription();
				att.SendTaskDuration();
				att.ClickonTaskSubmit();
				Utility.waitForSeconds(1);

				WebElement SuccessfulMsg = driverR.findElement(By.xpath("//*[contains(text(),'Task created successfully!')]"));
				Utility.showCallout2("Validation Checks Applied on Task Submission Alert.", SuccessfulMsg);
				String ActualSuccessfulMsg = SuccessfulMsg.getText();
				String ExpectSuccessfulMsg = "Task created successfully!";
				sf.assertEquals(ActualSuccessfulMsg, ExpectSuccessfulMsg);
				Log.info("Task added Successfully to Timesheet for Respective date");

				//						WebElement MinimizeBtn3 = driverR.findElement(By.xpath("(//*[contains(@class,'accordion-button')])[1]"));
				//						Utility.scrollIntoView(driverR, js, MinimizeBtn3);
				//						MinimizeBtn3.click();
				//						Utility.waitForSeconds(2);
			} catch (ElementClickInterceptedException e) {
				System.out.println("Add Task Click Intercepted: Retrying via JS click.");
				js.executeScript("arguments[0].click();", driverR.findElement(By.xpath("(//*[contains(text(),'Add Task')])[1]")));
			}
		}

		Utility.showTooltip("Step 5:-> After adding Task, submitting this weekly Timesheet using Automation Script");
		// Submit timesheet
		try {
			WebElement submitBtn = Utility.waitForElementToBeClickable(driverR, By.xpath("(//*[contains(text(),'Submit Timesheet')])[5]"), 10);
			Utility.scrollIntoView(driverR, js, submitBtn);
			Utility.showCallout2("Click on Submit Button ", submitBtn);
			Utility.waitForSeconds(1);
			//               	Utility.highlightElement(submitBtn);
			Utility.safeClick(driverR, js, submitBtn);
			Utility.waitForSeconds(2);
			Log.info("Script click on Submit Button");

			WebElement confirmSubmit = Utility.waitForElementToBeClickable(driverR, By.xpath("//button[normalize-space()='Submit']"), 10);
			Utility.scrollIntoView(driverR, js, confirmSubmit);
			Utility.showCallout("Click on Confirm Button Using Automation Script", confirmSubmit);
			Utility.highlightElement(confirmSubmit);
			Utility.safeClick(driverR, js, confirmSubmit);
			Utility.waitForSeconds(2);
			System.out.println("✅ Timesheet submitted for week " + 2);

			WebElement confirmMsg = driverR.findElement(By.xpath("//*[contains(text(),'Timesheet submitted successfully!')]"));
			Utility.ExplicitWait(confirmMsg);
			Utility.highlightElement(confirmMsg);
			String ActualTimesheetSuccesful = confirmMsg.getText();
			System.out.println("Timesheet submission Succesful Message :-> " + ActualTimesheetSuccesful);
			String ExpectTimesheetSuccesful ="Timesheet submitted successfully!";
			sf.assertEquals(ActualTimesheetSuccesful, ExpectTimesheetSuccesful);
			Log.info("Timesheet Submitted for this Respective week Successfully");			
			// Check final status after submission
			Utility.waitForSeconds(2);
			lp.ClickonLogOutBtn(); 
	
			

			//					WebElement finalWeek = Utility.waitForElementToBeClickable(driverR, By.xpath(weekXPath), 10);
			//					Utility.scrollIntoView(driverR, js, finalWeek);
			//					Utility.safeClick(driverR, js, finalWeek);
			//					WebElement finalStatus = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[contains(@class,'bg-warning rounded text-black')]")));
			//					String newStatus = finalStatus.getText().replace("\u00A0", " ").trim();
			//					System.out.println("🟢 Post-Submission Status: '" + newStatus + "'");
			//
			//					if (newStat us.equalsIgnoreCase("SUBMITTED")) {
			//						System.out.println("✅ Submission confirmed. Stopping further processing.");
			//						// STOP the main loop
			//					}

		} catch (Exception e) {
			System.out.println("⚠️ Error during final submission: " + e.getMessage());

		}
		sf.assertAll();
	}



	@Test(priority=3)
	public void ValidateApproveTimesheetReportFunctionality() throws InterruptedException, IOException
	{
		sf = new SoftAssert();
		Utility.showTooltip("Executing Automation Script to Check Timesheet Submission by User and Approval Functionality by Line Manager.");

		Utility.waitForSeconds(5);

		launchUrl();
		Log.info("Url is launched");

		Utility.showTooltip("Step 1:->After Launching Timesheet Application, Login as User to submit Weekly Timesheet using Automation Script");

		Utility.waitForSeconds(2);

		lp.SendAdminUserName2();
		Log.info("input user name");

		lp.SendPassword();
		Log.info("input user Password");

		lp.ClickonLoginBtn();
		Log.info("Click on Login Button");

//		String ActualProfileName=lp.GetProfileName();
//		String ExpectedProfileName ="Welcome, AutomationTesting";
//		Log.info("Profile Name is verified");
//
//		String str =  "Welcome, AutomationTesting";
//		ActualUserName = str.replace("Welcome, ", "");
//		System.out.println(ActualUserName);
//
//
//		sf.assertEquals(ActualProfileName, ExpectedProfileName);
		tsp.ClickonTimesheetApprovals();

		Utility.waitForSeconds(3);

		WebElement SelectedWeek = driverR.findElement(By.xpath("(//*[contains(@class,\"p-1 shadow mb-2 border-secondary bg-gradient week-item card\")])[1]"));
		Utility.safeClick(driverR, js, SelectedWeek);
        WebElement SelectedUser = driverR.findElement(By.xpath("//*[contains(@class,\"px-1 py-0 shadow mb-2 bg-gradient border-secondary bg\")]"));
	    Utility.safeClick(driverR, js, SelectedUser);		
				// Add task and submit timesheet

//				Utility.showTooltip("Step 5:-> After adding Task, submitting this weekly Timesheet using Automation Script");
				// Submit timesheet
				try {
					WebElement actionsBtn = Utility.waitForElementToBeClickable(driverR, By.xpath("//*[contains(text(),'ACTIONS')]"), 10);
					Utility.scrollIntoView(driverR, js, actionsBtn);
					Utility.showCallout2("Click on actions Button ", actionsBtn);
					Utility.waitForSeconds(1);
					Utility.safeClick(driverR, js, actionsBtn);
					Utility.waitForSeconds(2);
					Log.info("Script click on Action Button");
					
					WebElement approveTimesheet = Utility.waitForElementToBeClickable(driverR, By.xpath("//*[contains(text(),'Approve Timesheet')]"), 10);
					Utility.scrollIntoView(driverR, js, approveTimesheet);
					Utility.showCallout2("Click on Submit Button ", approveTimesheet);
					Utility.waitForSeconds(1);
					//           	Utility.highlightElement(submitBtn);
					Utility.safeClick(driverR, js, approveTimesheet);
					Utility.waitForSeconds(2);
					Log.info("Script click on approveTimesheet Button");

					tsp.SendComment();
					WebElement submitBtn = Utility.waitForElementToBeClickable(driverR, By.xpath("//button[normalize-space()='Submit']"), 10);
					Utility.scrollIntoView(driverR, js, submitBtn);
					Utility.showCallout2("Click on Submit Button ", submitBtn);
					Utility.waitForSeconds(1);
					//           	Utility.highlightElement(submitBtn);
					Utility.safeClick(driverR, js, submitBtn);
					Utility.waitForSeconds(2);
					Log.info("Script click on Submit Button");


					WebElement confirmMsg = driverR.findElement(By.xpath("//*[contains(text(),'Approved successfully!')]"));
					Utility.ExplicitWait(confirmMsg);
					Utility.highlightElement(confirmMsg);
					String ActualTimesheetSuccesful = confirmMsg.getText();
					System.out.println("Timesheet submission Succesful Message :-> " + ActualTimesheetSuccesful);
					String ExpectTimesheetSuccesful ="Approved successfully!";
					sf.assertEquals(ActualTimesheetSuccesful, ExpectTimesheetSuccesful);
					Log.info("Timesheet Approved successfully! for this Respective week");
					// Check final status after submission
					driverR.navigate().refresh();
					Utility.waitForSeconds(2);
					

				}
				catch(Exception e)
				{
					throw new RuntimeException("Exception failed due to error"+e);
				}
		sf.assertAll();
	}


	//	}

	
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

		System.out.println("→ Sending to " + ": " + timestamp);

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

		System.out.println("→ Sending to " + ": " + timestamp);

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
		//	    HashMap<String, String> data = new HashMap<>();
		//	    data.put("test_timestamp", timestamp);

		//	    System.out.println("→ Sending to " + endpoint + ": " + timestamp);

		given()
		.contentType("application/json")
		.header("Authorization", "Token " + token)
		//	        .body(data)
		.when()
		.post("https://timesheet.ndtatlas.com/api/utils/remove-automation-test-data/")
		.then()
		.statusCode(200)
		.log().all();
	}

		@AfterMethod
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



