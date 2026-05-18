package com.cms.pageObjects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.cms.basetest.BaseTest;
import com.cms.utility.Utility;

public class MyTicketPage extends BaseTest {

	public static WebDriver driver2;
	public static JavascriptExecutor js;

	// ─── Login Elements ───────────────────────────────────────────────────────

	@FindBy(xpath = "//input[@placeholder='Enter Username']")
	private WebElement UserName;

	@FindBy(xpath = "//input[@placeholder='Enter password']")
	private WebElement Password;

	@FindBy(xpath = "//button[@type='submit']")
	private WebElement Login;

	// ─── Sidebar Navigation ───────────────────────────────────────────────────

	@FindBy(xpath = "//span[@class='d-flex align-items-center gap-2']//span[contains(text(),'My Leaves')]")
	private WebElement MyLeaves;

	@FindBy(xpath = "//span[@class='d-flex align-items-center gap-2']//span[contains(text(),'My Ticket')]")
	private WebElement MyTicketNav;

	// ─── My Ticket Page Controls ──────────────────────────────────────────────

	@FindBy(xpath = "//button[normalize-space()='New Ticket']")
	private WebElement NewTicketBtn;

	@FindBy(xpath = "//a[contains(normalize-space(),'New Ticket')]")
	private WebElement NewTicketLink;

	// ─── Ticket Form Fields ───────────────────────────────────────────────────

	@FindBy(xpath = "//select[@aria-label='Select Category']")
	private WebElement Category;

	@FindBy(xpath = "//select[@aria-label='Select Priority']")
	private WebElement Priority;

	@FindBy(xpath = "//select[@aria-label='Select Type']")
	private WebElement TicketType;

	@FindBy(xpath = "//input[@placeholder='Enter Subject']")
	private WebElement Subject;

	@FindBy(xpath = "//textarea[@placeholder='Enter Description']")
	private WebElement Description;

	@FindBy(xpath = "//textarea[@placeholder='Enter comment']")
	private WebElement Comment;

	@FindBy(xpath = "//button[normalize-space()='Submit']")
	private WebElement SubmitBtn;

	// ─── Success / Validation ─────────────────────────────────────────────────

	@FindBy(xpath = "//div[contains(text(),'Ticket created successfully')]")
	private WebElement TicketSuccessMsg;

	@FindBy(xpath = "//div[contains(text(),'successfully')]")
	private WebElement GenericSuccessMsg;

	// ─── Constructor ──────────────────────────────────────────────────────────

	public MyTicketPage(WebDriver driverR) {
		driver2 = driverR;
		PageFactory.initElements(driverR, this);
	}

	// ─── Utility ──────────────────────────────────────────────────────────────

	public static String timestamp() {
		LocalDateTime dt = LocalDateTime.now();
		DateTimeFormatter df = DateTimeFormatter.ofPattern("ddhhmm");
		return dt.format(df);
	}

	// ─── Navigation Methods ───────────────────────────────────────────────────

	public void ClickonMyLeaves() throws InterruptedException {
		Utility.ExplicitWait(MyLeaves);
		Utility.showCallout("Navigating to My Leaves", MyLeaves);
		js = (JavascriptExecutor) driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:4px solid green;')", MyLeaves);
		MyLeaves.click();
		Utility.waitForSeconds(2);
	}

	public void ClickonMyTicket() throws InterruptedException {
		Utility.ExplicitWait(MyTicketNav);
		Utility.showCallout("Navigating to My Ticket", MyTicketNav);
		js = (JavascriptExecutor) driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:4px solid green;')", MyTicketNav);
		MyTicketNav.click();
		Utility.waitForSeconds(2);
	}

	// ─── New Ticket Button ────────────────────────────────────────────────────

	public void ClickonNewTicketBtn() throws InterruptedException {
		try {
			Utility.ExplicitWait(NewTicketBtn);
			Utility.showCallout("Clicking New Ticket Button", NewTicketBtn);
			js = (JavascriptExecutor) driver2;
			js.executeScript("arguments[0].setAttribute('style','background:yellow;border:4px solid green;')", NewTicketBtn);
			NewTicketBtn.click();
		} catch (Exception e) {
			System.out.println("Button not found, trying link: " + e.getMessage());
			Utility.ExplicitWait(NewTicketLink);
			NewTicketLink.click();
		}
		Utility.waitForSeconds(2);
	}

	// ─── Form Fill Methods ────────────────────────────────────────────────────

	public void SelectCategory() throws InterruptedException {
		Utility.ExplicitWait(Category);
		Utility.showCallout("Selecting Ticket Category", Category);
		js = (JavascriptExecutor) driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:2px solid green;')", Category);
		Select s = new Select(Category);
		s.selectByIndex(1);
		Utility.waitForSeconds(1);
	}

	public void SelectPriority() throws InterruptedException {
		Utility.ExplicitWait(Priority);
		Utility.showCallout("Selecting Ticket Priority", Priority);
		js = (JavascriptExecutor) driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:2px solid green;')", Priority);
		Select s = new Select(Priority);
		s.selectByIndex(1);
		Utility.waitForSeconds(1);
	}

	public void SelectTicketType() throws InterruptedException {
		try {
			Utility.ExplicitWait(TicketType);
			Utility.showCallout("Selecting Ticket Type", TicketType);
			js = (JavascriptExecutor) driver2;
			js.executeScript("arguments[0].setAttribute('style','background:yellow;border:2px solid green;')", TicketType);
			Select s = new Select(TicketType);
			s.selectByIndex(1);
			Utility.waitForSeconds(1);
		} catch (Exception e) {
			System.out.println("Ticket Type dropdown not present, skipping: " + e.getMessage());
		}
	}

	public void EnterSubject() throws InterruptedException {
		Utility.ExplicitWait(Subject);
		Utility.showCallout("Entering Ticket Subject", Subject);
		js = (JavascriptExecutor) driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:2px solid green;')", Subject);
		Subject.clear();
		Subject.sendKeys("Automation Test Ticket - " + timestamp());
		Utility.waitForSeconds(1);
	}

	public void EnterDescription() throws InterruptedException {
		Utility.ExplicitWait(Description);
		Utility.showCallout("Entering Ticket Description", Description);
		js = (JavascriptExecutor) driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:2px solid green;')", Description);
		Description.sendKeys("This is an automated test ticket created by Selenium automation script for regression testing.");
		Utility.waitForSeconds(1);
	}

	public void EnterComment() throws InterruptedException {
		try {
			Utility.ExplicitWait(Comment);
			Utility.showCallout("Entering Comment", Comment);
			js = (JavascriptExecutor) driver2;
			js.executeScript("arguments[0].setAttribute('style','background:yellow;border:2px solid green;')", Comment);
			Comment.sendKeys("Automation Test Comment");
			Utility.waitForSeconds(1);
		} catch (Exception e) {
			System.out.println("Comment field not present, skipping: " + e.getMessage());
		}
	}

	public void ClickonSubmitBtn() throws InterruptedException {
		Utility.ExplicitWait(SubmitBtn);
		Utility.showCallout("Clicking Submit Button", SubmitBtn);
		js = (JavascriptExecutor) driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:4px solid green;')", SubmitBtn);
		SubmitBtn.click();
		Utility.waitForSeconds(2);
	}

	// ─── Validation Methods ───────────────────────────────────────────────────

	public String GetTicketSuccessMessage() throws InterruptedException {
		try {
			Utility.ExplicitWait(TicketSuccessMsg);
			String msg = TicketSuccessMsg.getText();
			System.out.println("Ticket Success Message: " + msg);
			return msg;
		} catch (Exception e) {
			WebElement fallback = driver2.findElement(By.xpath("//div[contains(@class,'alert') and contains(text(),'success')]"));
			Utility.ExplicitWait(fallback);
			return fallback.getText();
		}
	}

	public String GetGenericSuccessMessage() throws InterruptedException {
		Utility.ExplicitWait(GenericSuccessMsg);
		String msg = GenericSuccessMsg.getText();
		System.out.println("Success Message: " + msg);
		return msg;
	}
}
