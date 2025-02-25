package com.cms.pageObjects;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cms.basetest.BaseTest;
import com.cms.utility.Utility;

public class AddTimesheetPage extends BaseTest{

	public static WebDriver driver2;
	public static JavascriptExecutor js ;

	@FindBy(xpath="//*[name()='path' and contains(@d,'M215.103 0')]")
	private WebElement GoogleLogin ;
	
	@FindBy(xpath="//*[@id=\"identifierId\"]")
	private WebElement Google_Id ;
	
	@FindBy(xpath="//*[contains(text(),\"Next\")]")
	private WebElement Next_btn ; 
	
	@FindBy(xpath="//*[@type=\"password\"]")
	private WebElement Google_Pd ;
	
//*****************************************************
//	@FindBy(xpath="//*[contains(text(),'Username')]")
//	private WebElement UserName ;

	@FindBy(xpath="//input[@class='form-control my-3']")
	private WebElement UserName ;
		
	@FindBy(xpath="//input[@type='password']")
	private WebElement Password ;

	@FindBy(xpath="//button[normalize-space()='Login']")
	private WebElement Login ;
	
	@FindBy(xpath="//p[@class='text-center p-2 rounded']")
	private WebElement ProfileName ;
	
	@FindBy(xpath="//input[@id='date']")
	private WebElement ClockinDate ;
	
	@FindBy(xpath="//input[@id='logout_date']")
	private WebElement ClockoutDate ;
	
	@FindBy(xpath="//input[@id='login_time']")
	private WebElement ClockinTime ; 
	
	@FindBy(xpath="//input[@id='logout_time']")
	private WebElement ClockoutTime ;
	
	@FindBy(xpath="//div[@id='break_duration']")
	private WebElement BreakDuration ;
	
	@FindBy(xpath="//li[normalize-space()='60 minutes']")
	private WebElement BreakDuration2 ;
	
	@FindBy(xpath="//a[normalize-space()='Add New Timesheet']")
	private WebElement AddNewTimesheet ;
	
	@FindBy(xpath="//input[@value='Submit']")
	private WebElement Submit ;
	
	@FindBy(xpath="(//*[contains(text(),'Add Task')])[1]")
	private WebElement Addtask ;
	
	@FindBy(xpath="//div[@id='core-process']")
	private WebElement CoreProcess ;
	
	@FindBy(xpath="//li[normalize-space()='Cross Functional Process']")
	private WebElement CrossFunctional ;
	
	@FindBy(xpath="//div[@id='mui-component-select-sub_process']")
	private WebElement SubProcess ;
	
	@FindBy(xpath="//li[normalize-space()='Operations Management Process']")
	private WebElement OperationalProcess ;
	
	@FindBy(xpath="//div[@id='activity']")
	private WebElement Activity ;
	
	@FindBy(xpath="//li[normalize-space()='Other']")
	private WebElement Other ;
	
	@FindBy(xpath="///input[@id='other']")
	private WebElement TaskDescription ;
	
	@FindBy(xpath="//li[normalize-space()='02:00']")
	private WebElement TaskDuration2 ;
	
	@FindBy(xpath="//div[@id='duration']")
	private WebElement TaskDuration ;
	
	
		// *********Construction Declaration to initialize Data Member********	
	public AddTimesheetPage(WebDriver driverR)
	{
		driver2 = driverR;
		PageFactory.initElements(driverR, this);
	}
	//********time stamp Creation *******************************************
	public static String timestamp()
	
	{
		//return new SimpleDateFormat("yyyyddHHmm").format(new Date(10));
		LocalDateTime dt=LocalDateTime.now();
		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String mydata=dt.format(df);
		return mydata;

	}
	
	public static String timestamp2()
	{
		//return new SimpleDateFormat("yyyyddHHmm").format(new Date(10));
		LocalDateTime dt=LocalDateTime.now();
		DateTimeFormatter df = DateTimeFormatter.ofPattern("hh-mm");
		String mydata=dt.format(df);
		return mydata;

	}
	//************** Method Declaration *************************************


	public void ClickonContinueSite() throws InterruptedException
	{
		WebElement SecureSite = driver2.findElement(By.xpath("//*[contains(text(),\"Continue to site\")]"));
		Utility.ExplicitWait(SecureSite);
		SecureSite.click();
	    Thread.sleep(5000);
	}
	

	public void SendUserName() throws InterruptedException
	{
		Utility.ExplicitWait(UserName);
		js = (JavascriptExecutor)driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:4px solid green;')",UserName );
		UserName.sendKeys("testuser");
		Thread.sleep(2000);
	}
	
	public void SendPassword() throws InterruptedException
	{
		Utility.ExplicitWait(Password);
		js = (JavascriptExecutor)driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:4px solid green;')",Password );
		Password.sendKeys("Test@123");
		Thread.sleep(2000); 
	}

	public void ClickonLoginBtn() throws InterruptedException
	{
		Utility.ExplicitWait(Login);
		js = (JavascriptExecutor)driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:4px solid green;')",Login );
		Login.click();
	}

	
	public String GetProfileName() throws InterruptedException
	{
		Utility.ExplicitWait(ProfileName);
		js = (JavascriptExecutor)driver2;
		js.executeScript("arguments[0].setAttribute('style','background:yellow;border:4px solid green;')",ProfileName );
		Thread.sleep(2000);
		
		String Pname = ProfileName.getText();
		return Pname;
		}

	public void ClickonAddNewTimesheet() throws InterruptedException
	{
		Utility.ExplicitWait(AddNewTimesheet);
		AddNewTimesheet.click();
	    Thread.sleep(2000);
	}

	public void SendClockinDate() throws InterruptedException
	{
		Utility.ExplicitWait(ClockinDate);
//		ClockinDate.click();
		ClockinDate.sendKeys(timestamp());
	    Thread.sleep(1000);
	}

	public void SendClockoutDate() throws InterruptedException
	{
		Utility.ExplicitWait(ClockoutDate);
//		ClockoutDate.click();
		ClockoutDate.sendKeys(timestamp());
	    Thread.sleep(1000);
	}

	public void SendClockinTime() throws InterruptedException
	{
		Utility.ExplicitWait(ClockinTime);
//		ClockinTime.click();
		ClockinTime.sendKeys(timestamp2());
	    Thread.sleep(1000);
	}

	public void SendClockoutTime() throws InterruptedException
	{
		Utility.ExplicitWait(ClockoutTime);
//		ClockoutTime.click();
		ClockoutTime.sendKeys("19:30");
	    Thread.sleep(1000);
	}
	
	public void SelectBreakDuration() throws InterruptedException
	{
		Utility.ExplicitWait(BreakDuration);
		BreakDuration.click();
		Thread.sleep(2000);
		BreakDuration2.click();
	}
	
	public void ClickonSubmit() throws InterruptedException
	{
		Utility.ExplicitWait(Submit);
		Submit.click();
	    Thread.sleep(2000);
	}

	public void ClickonAddTask() throws InterruptedException
	{
		Utility.ExplicitWait(Submit);
		Submit.click();
	    Thread.sleep(2000);
	}
	
	public void SelectCoreProcess() throws InterruptedException
	{
		Utility.ExplicitWait(CoreProcess);
		CoreProcess.click();
	    Thread.sleep(2000);
	    
	    
	}

	public void SelectSubProcess() throws InterruptedException
	{
		Utility.ExplicitWait(Submit);
		Submit.click();
	    Thread.sleep(2000);
	    CrossFunctional.click();
	}

	public void ClickonSubProcess() throws InterruptedException
	{
		Utility.ExplicitWait(SubProcess);
		SubProcess.click();
	    Thread.sleep(2000);
	    OperationalProcess.click();
	}

	public void ClickonActivity() throws InterruptedException
	{
		Utility.ExplicitWait(Activity);
		Activity.click();
	    Thread.sleep(2000);
	    Other.click();
	}
	
	public void SendTaskDescription() throws InterruptedException
	{
		Utility.ExplicitWait(TaskDescription);
		TaskDescription.sendKeys("Test Description");
		Thread.sleep(2000);
	}
}






