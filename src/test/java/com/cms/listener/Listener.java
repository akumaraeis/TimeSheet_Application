package com.cms.listener;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.cms.utility.Utility;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Listener implements ITestListener {

	ExtentSparkReporter sparkReporter;
	ExtentReports extent;
	ExtentTest test;
	WebDriver driverR; // Assume driver is initialized in your Base class or Test class

	// Initialize the report
	@Override
	public void onStart(ITestContext context) {
		String timestamp2 = new SimpleDateFormat("yyyy.MM.dd.HH.mm").format(new Date());
		String repName = "ExtentReport-"+timestamp2+".html";
		String reportPath = System.getProperty("user.dir") + "/ExtentReport/"+repName;
		sparkReporter = new ExtentSparkReporter(reportPath);
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Machine1", "Testpc1");
		extent.setSystemInfo("Operating system", "Windows 11 pro");
		extent.setSystemInfo("Browser", "Chrome");
		extent.setSystemInfo("Environment", "QA Environment");
		extent.setSystemInfo("Host", "QA");
		extent.setSystemInfo("UserName", "Amit kumar");

	}

	@Override
	public void onTestStart(ITestResult result) {
		test = extent.createTest(result.getMethod().getMethodName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		test.log(Status.PASS, "Test Passed");
		test.addScreenCaptureFromPath(Utility.captureScreenshot(result.getMethod().getMethodName(), "PASS"));
	}

	@Override
	public void onTestFailure(ITestResult result) {
		test.log(Status.FAIL, "Test Failed: " + result.getThrowable());
		test.addScreenCaptureFromPath(Utility.captureScreenshot(result.getMethod().getMethodName(), "FAIL"));
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		test.log(Status.SKIP, "Test Skipped: " + result.getThrowable());
		test.addScreenCaptureFromPath(Utility.captureScreenshot(result.getMethod().getMethodName(), "SKIP"));
	}

	@Override
	public void onFinish(ITestContext context) {
		extent.flush(); // Write everything to the report
	}

	// Utility method to capture screenshots

}
