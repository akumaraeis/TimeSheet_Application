package com.cms.testcases;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
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
import com.cms.utility.Utility;


public class AddTaskTest extends BaseTest {
	public SoftAssert sf;
	public JavascriptExecutor js;
	public boolean isSuccessful = false;
	public String clockInDate;
	public String FinalAlert;
	
	
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
	public void ValidateUserAccessPermission() throws InterruptedException, IOException
	{

		launchUrl();

		Thread.sleep(2000);

		lp.SendUserName();

		lp.SendPassword();

		lp.ClickonLoginBtn();

		String ActualProfileName=lp.GetProfileName();
		String ExpectedProfileName ="Welcome, AutomationTesting (User)";

		sf.assertEquals(ActualProfileName, ExpectedProfileName);

		atp.ClickonAddNewTimesheet();

		while(!isSuccessful)
		{

			clockInDate = att.generateRandomDate(); // Generate a date in February
//			String clockOutDate = clockInDate; // Ensure both dates are the same
//			att.SendClockinDate(clockInDate);

			 atp.SelectClockinDate(clockInDate);
			
//			att.SendClockinTime();

	//		att.SendClockoutDate(clockOutDate);

	//		atp.SendClockoutTime();
			Thread.sleep(2000);
			atp.SelectClockOutDate(clockInDate);
			Thread.sleep(2000);
			atp.SelectBreakDuration();
			Thread.sleep(2000);
			atp.ClickonSubmit();

			 FinalAlert = atp.GetTaskAlert();
			
			if (FinalAlert.equals("Timesheet created successfully!")) {
				isSuccessful = true;  // Exit the loop if successful
			} else {
				// Optionally, you can add a delay or retry logic here
				Thread.sleep(1000); // Example: wait for 1 second before retrying
			}
		}	
		
		String ActMsg = FinalAlert;
		String ExpMsg ="Timesheet created successfully!";
		sf.assertEquals(ActMsg, ExpMsg);

		String enteredTimesheetDate = clockInDate ; // Example input date
		String formattedDate = att.convertDateFormat(enteredTimesheetDate);
		
		att.SendDateFilter(formattedDate);
		Thread.sleep(2000);
		

		List<WebElement> timesheetRows = driverR.findElements(By.xpath("//*[@class='shadow table table-light table-sm table-striped table-bordered table-hover']//tbody//tr"));

		
		boolean entryFound = false;

		for (WebElement row : timesheetRows) {
		    try {
		        // 1. Get the 3rd column (assuming that's where date lives)
		        WebElement dateTimeColumn = row.findElement(By.xpath(".//td[3]"));
		        String rowDateTime = dateTimeColumn.getText().trim();
		        
		        System.out.println("Checking row with date: " + rowDateTime);

		        // 2. Match the date
		        if (rowDateTime.startsWith(formattedDate)) {
		            System.out.println("✅ Matching Entry Found: " + rowDateTime);

		            // 3. Try to find "ADD TASK" inside this same row
		            List<WebElement> addTaskButtons = row.findElements(By.xpath(".//*[contains(translate(text(), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), 'ADD TASK')]"));
		            
		            if (!addTaskButtons.isEmpty()) {
		                WebElement addTaskButton = addTaskButtons.get(0);

		                Utility.ExplicitWait(addTaskButton);
		                JavascriptExecutor js = (JavascriptExecutor)driverR;
		                js.executeScript("arguments[0].scrollIntoView(true);", addTaskButton);

		                Thread.sleep(1000);
		                addTaskButton.click();

		                System.out.println("🟢 Clicked 'ADD TASK' for date: " + formattedDate);
		                entryFound = true;
		                break;
		            } else {
		                System.out.println("⚠️ 'ADD TASK' button not found in matched row.");
		            }
		        }

		    } catch (NoSuchElementException e) {
		        System.out.println("❌ Error in row: Element not found - " + e.getMessage());
		    }
		}

		if (!entryFound) {
		    System.out.println("❌ No matching entry found with 'ADD TASK' for date: " + formattedDate);
		}

//				att.SelectCoreProcess();

				att.SelectSubProcess();

				att.ClickonActivity();

				att.SendTaskDescription();

				att.SendTaskDuration();

				att.ClickonTaskSubmit();

				String ActualSuccessfulMsg = att.GetTaskSuccessfulNotification();

				String ExpectSuccessfulMsg = "Task created successfully!";

				sf.assertEquals(ActualSuccessfulMsg, ExpectSuccessfulMsg);

				Thread.sleep(2000);

				sf.assertAll();
			}
		
		//		att.ClickonAddTask();


	


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



