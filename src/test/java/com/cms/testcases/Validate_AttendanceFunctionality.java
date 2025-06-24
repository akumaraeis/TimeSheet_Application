package com.cms.testcases;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

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

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Validate_AttendanceFunctionality extends BaseTest {
	public SoftAssert sf;
	public JavascriptExecutor js;
	public boolean isSuccessful = false;
	public String clockInDate;
	public boolean isTaskSuccessful = false ;
	public String FinalAlert;
	public String Timein ;
	public String clockInTime;
	public String updatedTimeStr;
	public String updatedTime2;
	
/*	@BeforeClass
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
*/

	
	@Test(priority=0)
	    public static  String getToken() {
	        RestAssured.baseURI = "https://testbackend.ndtatlas.com";

	        Response response = given()
	            .header("Content-Type", "application/json")
	            .body("{ \"username\": \"AutomationTestUser\", \"password\": \"Test@123\" }")
	        .when()
	            .post("/api/auth/login/")
	        .then()
	            .statusCode(200)
	            .extract().response();

	        // Extract token from response
	        JsonPath jsonPath = response.jsonPath();
	        String token = response.jsonPath().getString("data.token");  // Change key if API returns it with a different name

	        System.out.println("Token: " + token);
	        return token;
	        
	    }
	
	@Test(priority=1)
	public void ValidateClockIn() throws InterruptedException, IOException
	{

           System.out.println("Final Token "+ (getToken()));
			clockInDate = att.generateRandomDate(); // Generate a date in February
			clockInTime = att.timestamp();
			Timein = clockInDate+"T"+clockInTime +"Z";
			System.out.println("Final Time-In given :->"+Timein);

			HashMap data = new HashMap();
			data.put("test_timestamp", Timein );
			System.out.println("found accountId detail for delete functions : "+Timein);
			given()
			.contentType("application/json")
			.headers("Authorization",("Token "+getToken()))
			.body(data)

			.when()
			.post("https://testbackend.ndtatlas.com/api/attendance-test/clockin/")

			.then()
			.statusCode(201)
			.log().all();
			System.out.println("******Clock-in Functionality Verified************");

			
		}	
		
	@Test(priority=2,dependsOnMethods="ValidateClockIn")
	public void ValidateBreakIn() throws InterruptedException, IOException
	{

           System.out.println("Final Token "+ (getToken()));
//			clockInDate = att.generateRandomDate(); // Generate a date in February
//			String clockInTime = att.timestamp();
           DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
           LocalTime originalTime = LocalTime.parse(clockInTime, timeFormatter);
           LocalTime updatedTime = originalTime.plusHours(1);
            updatedTimeStr = updatedTime.format(timeFormatter);
			 Timein  = clockInDate+"T"+updatedTimeStr +"Z";
			System.out.println("Final Time-In given :->"+updatedTimeStr);

			HashMap data = new HashMap();
			data.put("test_timestamp", Timein );
			System.out.println("found accountId detail for delete functions : "+Timein);
			given()
			.contentType("application/json")
			.headers("Authorization",("Token "+getToken()))
			.body(data)

			.when()
			.patch("https://testbackend.ndtatlas.com/api/attendance-test/breakin/")

			.then()
			.statusCode(201)
			.log().all();
			System.out.println("******BreakIn Functionality is  Verified************");
	}
	
	@Test(priority=3,dependsOnMethods="ValidateBreakIn")
	public void ValidateBreakOut() throws InterruptedException, IOException
	{

           System.out.println("Final Token "+ (getToken()));
//			clockInDate = att.generateRandomDate(); // Generate a date in February
//			String clockInTime = att.timestamp();
	           DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	           LocalTime originalTime = LocalTime.parse(updatedTimeStr, timeFormatter);
	           LocalTime updatedTime2 = originalTime.plusHours(1);
	           String updatedTimeStr = updatedTime2.format(timeFormatter);

			String Timein = clockInDate+"T"+updatedTimeStr +"Z";
			System.out.println("Final Time-In given :->"+Timein);

			HashMap data = new HashMap();
			data.put("test_timestamp", Timein );
			System.out.println("found accountId detail for delete functions : "+Timein);
			given()
			.contentType("application/json")
			.headers("Authorization",("Token "+getToken()))
			.body(data)

			.when()
			.patch("https://testbackend.ndtatlas.com/api/attendance-test/breakout/")

			.then()
			.statusCode(201)
			.log().all();
			System.out.println("******BreakOut Functionality is Verified************");
	}
	
	@Test(priority=4,dependsOnMethods="ValidateBreakOut")
	public void ValidateClockOut() throws InterruptedException, IOException
	{

           System.out.println("Final Token "+ (getToken()));
//			clockInDate = att.generateRandomDate(); // Generate a date in February
//			String clockInTime = att.timestamp();
	           DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	           LocalTime originalTime2 = LocalTime.parse(updatedTimeStr, timeFormatter);
	           LocalTime updatedTime3 = originalTime2.plusHours(8);
	           String updatedTimeStr = updatedTime3.format(timeFormatter);

			String Timein = clockInDate+"T"+updatedTime3 +"Z";
			System.out.println("Final Time-In given :->"+Timein);

			HashMap data = new HashMap();
			data.put("test_timestamp", Timein );
			System.out.println("found accountId detail for delete functions : "+Timein);
			given()
			.contentType("application/json")
			.headers("Authorization",("Token "+getToken()))
			.body(data)

			.when()
			.patch("https://testbackend.ndtatlas.com/api/attendance-test/clockout/")

			.then()
			.statusCode(201)
			.log().all();
			System.out.println("******Clock-Out Functionality is Verified************");
	}
//		String ActMsg = FinalAlert;
//		String ExpMsg ="Timesheet created successfully!";
//		sf.assertEquals(ActMsg, ExpMsg);
//
//
//		sf.assertAll();

//	}
	
/*	
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

*/
}



