package com.cms.testcases;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.cms.basetest.BaseTest;
import com.cms.pageObjects.ShortLeavePage;
import com.cms.utility.Log;
import com.cms.utility.Utility;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;


import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test class for Short Leave on Clock & Attendance (landing page).
 *
 * Flow:
 *   1. Launch http://192.168.1.10:8086/login
 *   2. Login as AutomationTestUser / A425534k@
 *   3. On the landing page click "Apply Short Leave" button
 *   4. Popup opens — enter Start Time, End Time, Comment
 *   5. Click Submit and assert success message
 */
public class ShortLeaveTest extends BaseTest {

	public SoftAssert sf;
	public JavascriptExecutor js;

	// ─── Suite Setup ──────────────────────────────────────────────────────────

	@BeforeSuite
	public void setupLogging() {
		Logger.getLogger("org.openqa.selenium.remote.http.WebSocket")
			  .setLevel(Level.SEVERE);
	}

	// ─── Class Setup ─────────────────────────────────────────────────────────

	@BeforeClass
	@Parameters("browser")
	public void openUrl(String browser) throws IOException {
		initBrowser(browser);
		creatingObject();
		sf = new SoftAssert();
	}

	// ─── Test Cases ───────────────────────────────────────────────────────────

	/**
	 * TC_ShortLeave_01: Validate Short Leave submission from the landing page.
	 *
	 * Steps:
	 *   1. Launch local application URL
	 *   2. Login with AutomationTestUser / A425534k@
	 *   3. On the landed page click "Apply Short Leave" button
	 *   4. Compute valid Start Time (now + 15 min — satisfies: later than current time)
	 *   5. Compute valid End Time (start + 1 hr — satisfies: 30 min <= duration <= 2 hrs)
	 *   6. Enter comment
	 *   7. Click Submit
	 *   8. Assert success toaster message
	 */

		@Test(priority=0)
    public static  String getToken() {
        RestAssured.baseURI = "http://192.168.1.10:8085";

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

        Log.info("Token: " + token);
        return token;
        
    }
	
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
        String token = loginResponse.jsonPath().getString("data.token");
        Log.info("🔐 Token fetched: " + token);
//        Utility.waitForSeconds(2);
        DeleteAutomationTestUserRecords(token);
//        Utility.waitForSeconds(1);
//        driverR.navigate().refresh();
//        Utility.waitForSeconds(1);		        
		
	}


	@Test(priority = 2)
	public void ValidateShortLeaveFunctionality() throws InterruptedException, IOException {

		launchLocalUrl();
		Log.info("Application launched: http://192.168.1.10:8086/login");

		// ── Step 1: Login ─────────────────────────────────────────────────────
		lp.SendUserName();
		lp.SendPassword();
		lp.ClickonLoginBtn();
		Utility.waitForSeconds(2);
		Log.info("User logged in as AutomationTestUser");

		Utility.showTooltip("Step 1 → Login successful as AutomationTestUser");

		// Refresh to ensure the landing page reflects the latest short-leave state
		// (e.g. after API cleanup the button may still appear disabled without a reload)
		driverR.navigate().refresh();
		Utility.waitForSeconds(2);

		// ── Step 2: Click Apply Short Leave on landing page ───────────────────
		slp.ClickonApplyShortLeaveBtn();
		Log.info("Clicked Apply Short Leave — popup is open");
		Utility.showTooltip("Step 2 -> Apply Short Leave popup opened");
		Utility.waitForSeconds(1);

		// ── Step 3: Compute valid times satisfying business validations ────────
		// Rule 1 — Start time must be later than current time  → now + 15 min
		// Rule 2 — Duration must be between 30 min and 2 hrs  → 1 hr duration
		String startTime = ShortLeavePage.computeValidStartTime(driverR);
		String endTime   = ShortLeavePage.computeValidEndTime(startTime);
		Log.info("Validation — Start: " + startTime + " (now+15min) | End: " + endTime + " (duration: 1hr)");

		// ── Step 4: Enter Start Time ──────────────────────────────────────────
		slp.EnterStartTime(startTime);
		Log.info("Start Time entered: " + startTime);
		Utility.showTooltip("Step 3 -> Start Time entered: " + startTime + " (now + 15 min)");
		Utility.waitForSeconds(1);

		// ── Step 5: Enter End Time ────────────────────────────────────────────
		slp.EnterEndTime(endTime);
		Log.info("End Time entered: " + endTime);
		Utility.showTooltip("Step 4 -> End Time entered: " + endTime + " (1 hr duration)");
		Utility.waitForSeconds(1);

		// ── Step 5: Enter Comment ─────────────────────────────────────────────
		slp.SendComment();
		Log.info("Comment entered");
		Utility.showTooltip("Step 5 -> Comment entered");
		Utility.waitForSeconds(1);

		// ── Step 6: Submit ────────────────────────────────────────────────────
		slp.ClickonSubmitBtn();
		Log.info("Short Leave submission initiated");
		Utility.showTooltip("Step 6 -> Short Leave Submitted via Automation Script");

		// ── Step 7: Assert success toaster ────────────────────────────────────
		// Use WebDriverWait + visibilityOfElementLocated so it polls for the toast
		// instead of failing instantly via implicit-wait findElement
		WebDriverWait wait = new WebDriverWait(driverR, Duration.ofSeconds(15));
		WebElement Toaster = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("//*[contains(text(),'successfully') or contains(text(),'Successfully') " +
						 "or contains(text(),'Short leave') or contains(text(),'leave applied')]")));

		String ActToaster_Msg = Toaster.getText();
		Log.info("Message Received on Submitting Short Leave: " + ActToaster_Msg);
		Log.info("Short Leave Applied Successfully — Message: " + ActToaster_Msg);

		sf.assertTrue(ActToaster_Msg != null && !ActToaster_Msg.isEmpty(),
				"Success message should not be empty after Short Leave submission");

		Utility.showTooltip("Step 7 -> Short Leave Applied Successfully! Message: " + ActToaster_Msg);

		sf.assertAll();
	}
		public void DeleteAutomationTestUserRecords(String token) {
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


	// ─── Class Teardown ───────────────────────────────────────────────────────

	@AfterClass
	public void closebrowser() {
		teardown();
	}
}
