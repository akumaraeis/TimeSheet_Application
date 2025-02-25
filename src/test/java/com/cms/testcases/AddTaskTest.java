package com.cms.testcases;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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

		att.SendUserName();

		att.SendPassword();

		att.ClickonLoginBtn();

		String ActualProfileName=lp.GetProfileName();
		String ExpectedProfileName ="Welcome AutomationTesting | Timesheet Dashboard";

		sf.assertEquals(ActualProfileName, ExpectedProfileName);

		att.ClickonAddNewTimesheet();

		while(!isSuccessful)
		{

			clockInDate = att.generateRandomDate(); // Generate a date in February
			String clockOutDate = clockInDate; // Ensure both dates are the same
			att.SendClockinDate(clockInDate);

			att.SendClockinTime();

			att.SendClockoutDate(clockOutDate);

			atp.SendClockoutTime();

			atp.SelectBreakDuration();

			atp.ClickonSubmit();

			String FinalAlert = att.GetTaskAlert();
			if (FinalAlert.equals("Timesheet submitted successfully!")) {
				isSuccessful = true;  // Exit the loop if successful
			} else {
				// Optionally, you can add a delay or retry logic here
				Thread.sleep(1000); // Example: wait for 1 second before retrying
			}
		}	

		String enteredTimesheetDate = clockInDate ; // Example input date
        String formattedDate  = att.convertDateFormat(enteredTimesheetDate);
		
     List<WebElement> timesheetRows = driverR.findElements(By.xpath("//table/tbody/tr")); // Adjust XPath as needed

        boolean entryFound = false;

        for (WebElement row : timesheetRows) {
            // Find the column that contains date & time
            WebElement dateTimeColumn = row.findElement(By.xpath("./td[2]")); // Update index based on actual structure
            String rowDateTime = dateTimeColumn.getText().trim();

            // Check if rowDateTime contains the formatted date (ignoring time for now)
            if (rowDateTime.startsWith(formattedDate)) {  
                System.out.println("Matching Entry Found: " + rowDateTime);

                // Click "Add Task" button in the same row
                
                WebElement addTaskButton = row.findElement(By.xpath(".//button[contains(text(),'Add Task')]"));
                Utility.ExplicitWait(addTaskButton);
                JavascriptExecutor js = (JavascriptExecutor)driverR;
                js.executeScript("arguments[0].scrollIntoView()", addTaskButton);
                Thread.sleep(2000);
                addTaskButton.click();
                System.out.println("Clicked 'Add Task' button for entry: " + rowDateTime);

                entryFound = true;
                break; // Stop searching after the first match
            }
        }
//		att.ClickonAddTask();

		Thread.sleep(2000);

		att.SelectCoreProcess();

		att.SelectSubProcess();

		att.ClickonActivity();

		att.SendTaskDescription();

		att.SendTaskDuration();

		att.ClickonTaskSubmit();

		String ActualSuccessfulMsg = att.GetTaskSuccessfulNotification();

		String ExpectSuccessfulMsg = "Task added successfully!!";

		sf.assertEquals(ActualSuccessfulMsg, ExpectSuccessfulMsg);

		Thread.sleep(2000);

//		att.EditBreakDuration();
//
//		att.ClickonSelectDateRange();

		

		sf.assertAll();

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



