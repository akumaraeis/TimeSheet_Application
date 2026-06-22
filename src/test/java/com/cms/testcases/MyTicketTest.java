package com.cms.testcases;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.cms.basetest.BaseTest;
import com.cms.utility.Log;
import com.cms.utility.Utility;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test class for My Ticket module.
 *
 * Flow:
 *   1. Launch local URL  →  http://192.168.1.10:8086/login
 *   2. Login as AutomationTestUser
 *   3. Navigate to My Leaves → My Ticket
 *   4. Click New Ticket and fill all input fields
 *   5. Submit and assert success message
 *
 * Inheritance: extends BaseTest (provides driverR, page objects, browser init)
 */
public class MyTicketTest extends BaseTest {

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
	 * TC_MyTicket_01 : Validate creating a new ticket under My Ticket module.
	 *
	 * Steps:
	 *   1. Launch local application URL
	 *   2. Login with AutomationTestUser / Test@123
	 *   3. Navigate to My Leaves section in sidebar
	 *   4. Click My Ticket navigation item
	 *   5. Click New Ticket button
	 *   6. Fill Category, Priority, Subject, Description, and Comment fields
	 *   7. Submit the ticket form
	 *   8. Assert success toaster message
	 */
	@Test(priority = 1)
	public void ValidateMyTicketCreationFunctionality() throws InterruptedException, IOException {

		launchLocalUrl();
		Log.info("Application launched: http://192.168.1.10:8086/login");

		// ── Step 1: Login ─────────────────────────────────────────────────────
		lp.SendUserName();
		lp.SendPassword();
		lp.ClickonLoginBtn();
		Utility.waitForSeconds(2);
		Log.info("User logged in as AutomationTestUser");

		Utility.showTooltip("Step 1 → Login successful as AutomationTestUser");
		Utility.waitForSeconds(1);

		// ── Step 2: Navigate to My Ticket ────────────────────────────────────

		mtp.ClickonMyTicket();
		Log.info("Navigated to My Ticket module");

		Utility.showTooltip("Step 2 → Navigated to My Ticket Module");
		Utility.waitForSeconds(1);

		// ── Step 3: Open New Ticket Form ─────────────────────────────────────
		mtp.ClickonNewTicketBtn();
		Log.info("Clicked New Ticket button — form is open");

		Utility.showTooltip("Step 3 → New Ticket form opened");
		Utility.waitForSeconds(1);

		// ── Step 4: Fill all Input Fields ────────────────────────────────────
		mtp.SelectCategory();
		Log.info("Category selected");

		mtp.SelectSubCategory();
		Log.info("SubCategory selected");

		mtp.SelectPriority();
		Log.info("Priority selected");

		// mtp.SelectTicketType();
		// Log.info("Ticket type selected (if present)");

		mtp.EnterSubject();
		Log.info("Subject entered");

		mtp.EnterDescription();
		Log.info("Description entered");

		// mtp.EnterComment();
		// Log.info("Comment entered (if present)");

		Utility.showTooltip("Step 4 → All input fields filled for the new ticket");
		Utility.waitForSeconds(1);

		// ── Step 5: Submit Ticket ────────────────────────────────────────────
		mtp.ClickonSubmitBtn();
		Log.info("Ticket submission initiated");

		Utility.showTooltip("Step 5 → Ticket Submitted via Automation Script");

		// ── Step 6: Assert Success Toaster ───────────────────────────────────
		WebElement Toaster = driverR.findElement(
				By.xpath("//*[contains(text(),'successfully') or contains(text(),'Successfully')]"));
		Utility.ExplicitWait(Toaster);
		String ActToaster_Msg = Toaster.getText();
		Log.info("Message Received on Submitting Ticket: " + ActToaster_Msg);
		Log.info("Ticket Created Successfully — Message: " + ActToaster_Msg);

		sf.assertTrue(ActToaster_Msg != null && !ActToaster_Msg.isEmpty(),
				"Success message should not be empty after ticket creation");

		Utility.showTooltip("Step 6 → Ticket Created Successfully! Message: " + ActToaster_Msg);

		sf.assertAll();
	}

	// ─── Class Teardown ───────────────────────────────────────────────────────

	@AfterClass
	public void closebrowser() {
		teardown();
	}
}
