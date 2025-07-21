package com.cms.testcases;

import java.io.IOException;

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
import com.cms.utility.Utility;

public class ApplyLeaveTest extends BaseTest {
	public SoftAssert sf;
	public JavascriptExecutor js;
	public boolean isSuccessful = false;
	public String clockInDate;
	public boolean isTaskSuccessful = false ;
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
	public void ValidateapplyLeaveFunctionality() throws InterruptedException, IOException
	{

		launchUrl();

		Thread.sleep(2000);

		lp.SendUserName();

		lp.SendPassword();

		lp.ClickonLoginBtn();
		
		Thread.sleep(2000);

//		String ActualProfileName=lp.GetProfileName();
//		String ExpectedProfileName ="Welcome, AutomationTesting (User)";
//
//		sf.assertEquals(ActualProfileName, ExpectedProfileName);
		alp.ClickonApplyLeave();
		
		Thread.sleep(2000);
		
		alp.ClickonApplyLeaveBtn();
		
		Thread.sleep(2000);
		
		alp.SelectLeaveType();
		
		Thread.sleep(2000);
		
		alp.SendComment();
		
		alp.ClickonSubmitBtn();
		
		Thread.sleep(2000);
		
		WebElement Toaster = driverR.findElement(By.xpath("//*[contains(@class,'Toastify__toast Toastify')]"));
		String Toaster_Msg = Toaster.getText();
		System.out.println("Message Received on Submitting Apply Leave:->"+Toaster_Msg);
		
		alp.ClickonUserList();

		// old Timesheet for dummy purpose
	/*	
		driverR.findElement(By.xpath("//a[normalize-space()='Add Timesheet']"));
		
		driverR.findElement(By.xpath("//tbody/tr[1]/td[8]/button[1]"));
		
		driverR.findElement(By.xpath("//tbody/tr[1]/td[8]/button[1]"));
		
		driverR.findElement(By.xpath("//tbody/tr[1]/td[8]/button[1]"));
		
		driverR.findElement(By.xpath("//tbody/tr[1]/td[8]/button[1]"));
		
		driverR.findElement(By.xpath("//tbody/tr[1]/td[8]/button[2]"));
		
		driverR.findElement(By.xpath("//tbody/tr[1]/td[8]/button[2]"));
		
		driverR.findElement(By.xpath("//button[normalize-space()='Last Week']"));
		
		driverR.findElement(By.xpath("//button[normalize-space()='Last Month']"));
		
		driverR.findElement(By.xpath("//tbody/tr[1]/td[8]/button[2]"));
		
		// old Timesheet for dummy purpose
		/*
		while(!isSuccessful)
		{

			clockInDate = att.generateRandomDate(); // Generate a date in February
			atp.SelectClockinDate(clockInDate);
			Thread.sleep(2000);
			atp.SelectClockOutDate(clockInDate);
			Thread.sleep(2000);
//			atp.SelectBreakDuration();
//			Thread.sleep(2000);
//			atp.ClickonSubmit();

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
*/

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



