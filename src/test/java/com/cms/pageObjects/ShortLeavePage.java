package com.cms.pageObjects;
import com.cms.utility.Log;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.cms.basetest.BaseTest;
import com.cms.utility.Utility;

public class ShortLeavePage extends BaseTest {

	public static WebDriver driver2;
	public static JavascriptExecutor js;

	// ─── Login Elements ───────────────────────────────────────────────────────

	@FindBy(xpath = "//input[@placeholder='Enter Username']")
	private WebElement UserName;

	@FindBy(xpath = "//input[@placeholder='Enter password']")
	private WebElement Password;

	@FindBy(xpath = "//button[@type='submit']")
	private WebElement Login;

	// ─── Apply Short Leave Button (on landing page after login) ──────────────

	@FindBy(xpath = "//button[normalize-space()='Apply Short Leave']")
	private WebElement ApplyShortLeaveBtn;

	// ─── Short Leave Popup Fields ─────────────────────────────────────────────

	@FindBy(xpath = "(//*[@class=\"react-datepicker__input-container\"]//input)[1]")
	private WebElement StartTime;

	@FindBy(xpath = "(//*[@class=\"react-datepicker__input-container\"]//input)[2]")
	private WebElement EndTime;

	@FindBy(xpath = "//textarea[@placeholder='Enter comment']")
	private WebElement Comment;

	@FindBy(xpath = "//button[normalize-space()='Submit']")
	private WebElement SubmitBtn;

	// ─── Success Validation ───────────────────────────────────────────────────

	@FindBy(xpath = "//*[contains(text(),'successfully') or contains(text(),'Successfully')]")
	private WebElement SuccessMsg;

	// ─── Constructor ──────────────────────────────────────────────────────────

	public ShortLeavePage(WebDriver driverR) {
		driver2 = driverR;
		PageFactory.initElements(driverR, this);
	}

	// ─── Utility ──────────────────────────────────────────────────────────────

	public static String timestamp() {
		LocalDateTime dt = LocalDateTime.now();
		DateTimeFormatter df = DateTimeFormatter.ofPattern("ddhhmm");
		return dt.format(df);
	}

	/**
	 * Computes a valid start time satisfying:
	 *   "Short leave start time must be later than current time."
	 *
	 * Primary  : walks the live DOM to find the "LOCAL TIME" clock element
	 *            (text node matching "HH:MM:SS AM/PM") — exactly what the
	 *            application displays to the user regardless of their timezone.
	 * Fallback : reads the browser's JS Date (same base as React's new Date()).
	 */
	public static String computeValidStartTime(WebDriver driver) {
		JavascriptExecutor jsExec = (JavascriptExecutor) driver;
		LocalTime appNow;

		// ── Step 1: read LOCAL TIME text node from the application UI ──────────
		String appTimeRaw = (String) jsExec.executeScript(
			"var walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, null, false);" +
			"var node;" +
			"var pat = /^\\s*\\d{1,2}:\\d{2}:\\d{2}\\s*(AM|PM)\\s*$/i;" +
			"while (node = walker.nextNode()) {" +
			"  if (pat.test(node.textContent)) { return node.textContent.trim(); }" +
			"}" +
			"return null;"
		);

		if (appTimeRaw != null && !appTimeRaw.isEmpty()) {
			Log.info("LOCAL TIME from application UI: " + appTimeRaw);
			// Parse "04:13:18 PM" — 12-hour format with seconds
			appNow = LocalTime.parse(appTimeRaw.toUpperCase(Locale.ENGLISH),
					DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.ENGLISH));
		} else {
			// ── Fallback: browser JS Date (same timezone as React app) ─────────
			String browserTime = (String) jsExec.executeScript(
				"var d = new Date();" +
				"return ('0'+d.getHours()).slice(-2)+':'+('0'+d.getMinutes()).slice(-2);"
			);
			Log.info("LOCAL TIME element not found — browser JS time (fallback): " + browserTime);
			appNow = LocalTime.parse(browserTime, DateTimeFormatter.ofPattern("HH:mm"));
		}

		LocalTime start  = appNow.plusMinutes(15);
		String formatted = start.format(DateTimeFormatter.ofPattern("HH:mm"));
		Log.info("Computed Start Time: " + formatted + " (app time + 15 min)");
		return formatted;
	}

	/**
	 * Computes a valid end time satisfying:
	 *   "Duration must be between 30 minutes and 2 hours."
	 * Strategy: start time + 1 hour → 1 hr satisfies 30 min ≤ duration ≤ 2 hrs.
	 */
	public static String computeValidEndTime(String startTimeStr) {
		LocalTime start = LocalTime.parse(startTimeStr, DateTimeFormatter.ofPattern("HH:mm"));
		LocalTime end = start.plusHours(1);
		String formatted = end.format(DateTimeFormatter.ofPattern("HH:mm"));
		Log.info("Computed End Time:   " + formatted + " (start + 1 hr — duration: 1 hr)");
		return formatted;
	}

	// ─── Login Methods ────────────────────────────────────────────────────────

	public void SendUserName() throws InterruptedException {
		Utility.ExplicitWait(UserName);
		js = (JavascriptExecutor) driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:2px solid green;')", UserName);
		UserName.sendKeys("AutomationTestUser");
		Utility.waitForSeconds(2);
	}

	public void SendPassword() throws InterruptedException {
		Utility.ExplicitWait(Password);
		js = (JavascriptExecutor) driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:2px solid green;')", Password);
		Password.sendKeys("A425534k@");
		Utility.waitForSeconds(2);
	}

	public void ClickonLoginBtn() throws InterruptedException {
		Utility.ExplicitWait(Login);
		js = (JavascriptExecutor) driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:4px solid green;')", Login);
		Login.click();
		Utility.waitForSeconds(2);
	}

	// ─── Apply Short Leave Button (on landing page) ───────────────────────────

	public void ClickonApplyShortLeaveBtn() throws InterruptedException {
		Utility.ExplicitClickWait(ApplyShortLeaveBtn);
		Utility.showCallout("Clicking Apply Short Leave Button", ApplyShortLeaveBtn);
		js = (JavascriptExecutor) driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:4px solid green;')", ApplyShortLeaveBtn);
		// Scroll the button into the centre of the viewport to avoid card-body overlay
		js.executeScript("arguments[0].scrollIntoView({block:'center',inline:'nearest'});", ApplyShortLeaveBtn);
		Utility.waitForSeconds(1);
		try {
			ApplyShortLeaveBtn.click();
			Log.info("Apply Short Leave button clicked (regular click)");
		} catch (ElementClickInterceptedException e) {
			// Only fall back to JS click when another element overlays the button,
			// not when the button itself is disabled (disabled buttons should fail fast).
			Log.info("Click intercepted by overlay — using JS click");
			js.executeScript("arguments[0].click();", ApplyShortLeaveBtn);
		}
		Utility.waitForSeconds(2);
	}

	// ─── Popup — Start Time ───────────────────────────────────────────────────

	public void EnterStartTime(String time) throws InterruptedException {
		Utility.ExplicitWait(StartTime);
		Utility.showCallout("Entering Start Time: " + time, StartTime);
		js = (JavascriptExecutor) driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:2px solid green;')", StartTime);
		StartTime.click();
		StartTime.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		StartTime.sendKeys(time);
		Log.info("Start Time entered: " + time);
		Utility.waitForSeconds(1);
		// Close the React time-picker dropdown so it doesn't intercept the next field
		StartTime.sendKeys(Keys.ESCAPE);
		Utility.waitForSeconds(1);
	}

	// ─── Popup — End Time ─────────────────────────────────────────────────────

	public void EnterEndTime(String time) throws InterruptedException {
		Utility.ExplicitWait(EndTime);
		Utility.showCallout("Entering End Time: " + time, EndTime);
		js = (JavascriptExecutor) driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:2px solid green;')", EndTime);
		EndTime.click();
		EndTime.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		EndTime.sendKeys(time);
		Log.info("End Time entered: " + time);
		Utility.waitForSeconds(1);
		// Close the React time-picker dropdown before moving to next field
		EndTime.sendKeys(Keys.ESCAPE);
		Utility.waitForSeconds(1);
	}

	// ─── Popup — Comment ──────────────────────────────────────────────────────

	public void SendComment() throws InterruptedException {
		Utility.ExplicitWait(Comment);
		Utility.showCallout("Entering Comment", Comment);
		js = (JavascriptExecutor) driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:2px solid green;')", Comment);
		Comment.sendKeys("Short Leave - Automated Test Comment");
		Utility.waitForSeconds(1);
	}

	// ─── Popup — Submit ───────────────────────────────────────────────────────

	public void ClickonSubmitBtn() throws InterruptedException {
		Utility.ExplicitWait(SubmitBtn);
		Utility.showCallout("Clicking Submit Button", SubmitBtn);
		js = (JavascriptExecutor) driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:4px solid green;')", SubmitBtn);
		SubmitBtn.click();
		Utility.waitForSeconds(2);
	}

	// ─── Validation ───────────────────────────────────────────────────────────

	public String GetSuccessMessage() throws InterruptedException {
		Utility.ExplicitWait(SuccessMsg);
		String msg = SuccessMsg.getText();
		Log.info("Success Message: " + msg);
		return msg;
	}

}
