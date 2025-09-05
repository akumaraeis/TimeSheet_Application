package com.cms.testcases;

import static io.restassured.RestAssured.given;

import java.io.IOException;
<<<<<<< HEAD
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
=======
import java.util.List;

import org.openqa.selenium.By;
>>>>>>> 5c076efa1cad2d354015a96482539401643c64e0
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
<<<<<<< HEAD
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
	
	
=======
import com.cms.utility.Log;
import com.cms.utility.Utility;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class RegularizationTest extends BaseTest {
	public SoftAssert sf;
	public JavascriptExecutor js;
	public String token;


>>>>>>> 5c076efa1cad2d354015a96482539401643c64e0
	@BeforeClass
	@Parameters("browser")
	public void openUrl(String browser) throws IOException
	{
		initBrowser(browser);
		creatingObject();	
		//		Utility.showTooltip("Browser is Launched using Selenium Automation Tool");
<<<<<<< HEAD
		sf = new SoftAssert();
=======
		
>>>>>>> 5c076efa1cad2d354015a96482539401643c64e0
	}

	//	@BeforeMethod
	public void LaunchUrl() throws IOException, InterruptedException
	{
		//		Thread.sleep(2000);
		LaunchUrl();
	}
<<<<<<< HEAD
	
	@Test(priority=1)
	public void ValidateRegularizationFunctionality() throws InterruptedException, IOException
	{

		launchUrl();
		
		Thread.sleep(2000);
		
		lp.SendUserName();

		lp.SendPassword();

		lp.ClickonLoginBtn();
		
		Thread.sleep(2000);
=======
	@Test(priority=1)
	public void DeleteTestUserRecord() throws InterruptedException, IOException
	{

        RestAssured.baseURI = "http://192.168.1.10:8085";
        Response loginResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{ \"username\": \"AutomationTestUser\", \"password\": \"Test@123\" }")
                .when().post("/api/auth/login/")
                .then().statusCode(200)
                .extract().response();
        token = loginResponse.jsonPath().getString("data.token");
        System.out.println("🔐 Token fetched: " + token);
//        Utility.waitForSeconds(2);
        DeleteAutomationTestUserRecords(token);
//        Utility.waitForSeconds(1);

	}

	@Test(priority=2)
	public void ValidateRegularizationFunctionality() throws InterruptedException, IOException
	{

		sf = new SoftAssert();
		Utility.showTooltip("Executing Automation Script to Validate Regularization Send by User.");
		
		launchLocalUrl();
		
		Utility.waitForSeconds(2);
		
		lp.SendUserName();
        Log.info("Input User Name");
        
		lp.SendPassword();
		Log.info("Input User Password");
		
		lp.ClickonLoginBtn();
		Log.info("login submitted with  user credential ");
		
		Utility.waitForSeconds(2);
		Utility.showTooltip("User Login Successfully");
>>>>>>> 5c076efa1cad2d354015a96482539401643c64e0
		
	   String ActualProfileName=lp.GetProfileName();
	   String ExpectedProfileName ="Welcome, AutomationTesting";
	   
	   sf.assertEquals(ActualProfileName, ExpectedProfileName);
<<<<<<< HEAD
	   
       trp.ClickonRegularizationsBtn();
=======
	   Log.info("Profile Name is verified.");
	   
       trp.ClickonRegularizationsBtn();
       Log.info("User Navigate to Regularization page.");
>>>>>>> 5c076efa1cad2d354015a96482539401643c64e0
       
       List<WebElement> table = driverR.findElements(By.xpath("//table[@class=\"shadow table table-light table-sm table-bordered table-hover\"]//tbody//tr"));
       int tablesize = table.size();
       
       for(int i=2 ;i<=tablesize ;i++)
       {
           String ActionStatusText = driverR.findElement(By.xpath("(//table[@class=\"shadow table table-light table-sm table-bordered table-hover\"]//tbody//tr)["+i+"]//td[14]//button")).getText();
           boolean ActionButtonStatus = driverR.findElement(By.xpath("(//table[@class=\"shadow table table-light table-sm table-bordered table-hover\"]//tbody//tr)["+i+"]//td[14]//button")).isEnabled();
           System.out.println("status Text found on Action Button :->"+ActionStatusText);
           System.out.println("Boolean Status found on Action Button :->"+ActionButtonStatus);
           String RegularizeDate = driverR.findElement(By.xpath("(//table[@class=\"shadow table table-light table-sm table-bordered table-hover\"]//tbody//tr)["+i+"]//td[2]")).getText();
           System.out.println("Selected Regularize Date :->"+RegularizeDate);
<<<<<<< HEAD
           
=======
           Utility.showTooltip("New Regularization Request details Submitted using AutomationScript.");
>>>>>>> 5c076efa1cad2d354015a96482539401643c64e0
           if((ActionStatusText.equalsIgnoreCase("Regularize"))&& (ActionButtonStatus == true) )      	   
           {
        	   WebElement ActionButton = driverR.findElement(By.xpath("(//table[@class=\"shadow table table-light table-sm table-bordered table-hover\"]//tbody//tr)["+i+"]//td[14]//button"));
        	   js =(JavascriptExecutor)driverR;
        	   Utility.safeClick(driverR, js, ActionButton);
        	   Thread.sleep(2000);
        	   trp.SendRegularizationReason();
        	   trp.SendClockIn();
        	   trp.SendBreakIn();
        	   trp.SendBreakBreakOut();
        	   trp.SendClockOut();
        	   trp.ClickonSubmitBtn();
        	   
        	   String ActualMsg = trp.GetSuccessfulMsg();
        	   String ExpectMsg ="Regularization updated successfully!";
        	   sf.assertEquals(ActualMsg, ExpectMsg);
<<<<<<< HEAD
=======
        	   Log.info("Regularization Request Sent successfully by User.");
        	   Utility.showTooltip("Regularization Request Sent successfully by User.");
        	   
>>>>>>> 5c076efa1cad2d354015a96482539401643c64e0
        	    if(ActualMsg.equalsIgnoreCase("Regularization updated successfully!"))
        	    {
        	    	System.out.println("Loop Breaks");
        	    	break;
        	    }
        	    else
        	    {
        	    	System.out.println("Otherwise Loop Continues");
        	    }
           }
           else
           {
        	 System.out.println("Either Regularize or Action Button is not enabled");  
           }
           
           }
    	   sf.assertAll();
       }
<<<<<<< HEAD
       
=======
	public void DeleteAutomationTestUserRecords(String token) {
		//		    HashMap<String, String> data = new HashMap<>();
		//		    data.put("test_timestamp", timestamp);

		//		    System.out.println("→ Sending to " + endpoint + ": " + timestamp);

		given()
		.contentType("application/json")
		.header("Authorization", "Token " + token)
		//		        .body(data)
		.when()
		.post("http://192.168.1.10:8085/api/utils/remove-automation-test-data/")
		.then()
		.statusCode(200)
		.log().all();
	}

>>>>>>> 5c076efa1cad2d354015a96482539401643c64e0
	   
	   
		
	
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



