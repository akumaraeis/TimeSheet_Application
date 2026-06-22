package com.cms.basetest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.cms.pageObjects.AddTaskPage;
import com.cms.pageObjects.AddTimesheetPage;
import com.cms.pageObjects.AddTimesheetPage2;
import com.cms.pageObjects.ApplyLeavePage;
import com.cms.pageObjects.ChangePasswordPage;
import com.cms.pageObjects.DashboardPage;
import com.cms.pageObjects.Gross_TaskHourValidationPage;
import com.cms.pageObjects.InValid_TimesheetEntryValidationPage;
import com.cms.pageObjects.LoginPage;
import com.cms.pageObjects.MultipleClockInScenarioPage;
import com.cms.pageObjects.MyTicketPage;
import com.cms.pageObjects.PreviousEntryClockOutScenarioPage;
import com.cms.pageObjects.RegularizationPage;
import com.cms.pageObjects.ShortLeavePage;
import com.cms.pageObjects.TimeOverlappingScenarioPage;
import com.cms.pageObjects.TimesheetSlotMinimumHourValidationPage;
import com.cms.pageObjects.TimesheetSubmissionPage;
import com.cms.pageObjects.TwentyFourhourValidationPage;
import com.cms.utility.Log;
import com.cms.utility.Utility;

public class BaseTest {

	// ── Config loaded once from config.properties ─────────────────────────────
	private static final Properties CONFIG = loadConfig();

	public static final String APP_USERNAME  = CONFIG.getProperty("app.username");
	public static final String APP_PASSWORD  = CONFIG.getProperty("app.password");
	public static final String APP_URL_UI    = CONFIG.getProperty("app.url.ui");
	public static final String APP_URL_API   = CONFIG.getProperty("app.url.api");
	public static final String ADMIN_USERNAME = CONFIG.getProperty("app.admin.username");
	public static final String ADMIN_PASSWORD = CONFIG.getProperty("app.admin.password");

	private static Properties loadConfig() {
		Properties props = new Properties();
		try (InputStream is = BaseTest.class.getClassLoader()
				.getResourceAsStream("config.properties")) {
			if (is != null) {
				props.load(is);
			} else {
				Log.warn("config.properties not found on classpath — using empty config");
			}
		} catch (IOException e) {
			Log.error("Failed to load config.properties", e);
		}
		return props;
	}

	// ── Driver and page object instances ──────────────────────────────────────
	public static RemoteWebDriver driverR;

	public static LoginPage lp;
	public static AddTimesheetPage atp;
	public static AddTimesheetPage2 atp2;
	public static AddTaskPage att;
	public static Gross_TaskHourValidationPage gtp;
	public static TimeOverlappingScenarioPage tol;
	public static MultipleClockInScenarioPage mcp;
	public static TwentyFourhourValidationPage tvp;
	public static InValid_TimesheetEntryValidationPage itp;
	public static TimesheetSlotMinimumHourValidationPage tmp;
	public static PreviousEntryClockOutScenarioPage pcp;
	public static TimesheetSubmissionPage tsp;
	public static ApplyLeavePage alp;
	public static DashboardPage tdp;
	public static RegularizationPage trp;
	public static MyTicketPage mtp;
	public static ChangePasswordPage tcp;
	public static ShortLeavePage slp;

	// ── Browser initialisation ────────────────────────────────────────────────
	public void initBrowser(String browserName) throws IOException {
		switch (browserName.toLowerCase()) {
			case "firefox":
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				firefoxOptions.addArguments("--incognito");
				firefoxOptions.addArguments("--disable-notifications");
				firefoxOptions.addArguments("--disable-blink-features=AutomationControlled");
				firefoxOptions.addArguments("--disable-extensions");
				driverR = new FirefoxDriver(firefoxOptions);
				break;
			case "chrome":
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--no-sandbox");
				options.addArguments("--disable-dev-shm-usage");
				options.addArguments("--disable-gpu");
				options.addArguments("--remote-allow-origins=*");
				options.addArguments("--disable-background-networking");
				options.addArguments("--disable-background-timer-throttling");
				options.addArguments("--disable-renderer-backgrounding");
				options.addArguments("--disable-sync");
				options.addArguments("--no-first-run");
				options.addArguments("--disable-default-apps");
				options.addArguments("--disable-crash-reporter");
				driverR = new ChromeDriver(options);
				break;
			case "edge":
				EdgeOptions edgeOptions = new EdgeOptions();
				edgeOptions.addArguments("--incognito");
				driverR = new EdgeDriver(edgeOptions);
				break;
			default:
				Log.warn("Unknown browser requested: " + browserName);
				break;
		}
		if (driverR != null) {
			driverR.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(300));
			driverR.manage().deleteAllCookies();
		}
	}

	// ── URL launch helpers ────────────────────────────────────────────────────
	public void launchUrl() throws IOException {
		driverR.manage().window().maximize();
		driverR.get(System.getProperty("appURL", APP_URL_UI));
	}

	public void launchLocalUrl() throws IOException {
		driverR.manage().window().maximize();
		driverR.get(APP_URL_UI);
	}

	// ── Config file reader (classpath-relative) ───────────────────────────────
	public static String getFile(String filename) throws IOException {
		Properties prop = new Properties();
		try (InputStream is = BaseTest.class.getClassLoader()
				.getResourceAsStream("config.properties")) {
			if (is != null) {
				prop.load(is);
			}
		}
		return prop.getProperty(filename);
	}

	// ── CSV cell reader ───────────────────────────────────────────────────────
	public String getCellData(String filePath, int row, int col) {
		String cellData = null;
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			int currentRow = 0;
			while ((line = br.readLine()) != null) {
				if (currentRow == row) {
					String[] columns = line.split(",");
					if (col < columns.length) {
						cellData = columns[col];
					}
					break;
				}
				currentRow++;
			}
		} catch (IOException e) {
			Log.error("Failed to read cell data from: " + filePath, e);
		}
		return cellData;
	}

	// ── Page object factory ───────────────────────────────────────────────────
	public void creatingObject() throws IOException {
		lp   = new LoginPage(driverR);
		atp  = new AddTimesheetPage(driverR);
		att  = new AddTaskPage(driverR);
		gtp  = new Gross_TaskHourValidationPage(driverR);
		atp2 = new AddTimesheetPage2(driverR);
		tol  = new TimeOverlappingScenarioPage(driverR);
		mcp  = new MultipleClockInScenarioPage(driverR);
		tvp  = new TwentyFourhourValidationPage(driverR);
		itp  = new InValid_TimesheetEntryValidationPage(driverR);
		tmp  = new TimesheetSlotMinimumHourValidationPage(driverR);
		pcp  = new PreviousEntryClockOutScenarioPage(driverR);
		tsp  = new TimesheetSubmissionPage(driverR);
		alp  = new ApplyLeavePage(driverR);
		tdp  = new DashboardPage(driverR);
		trp  = new RegularizationPage(driverR);
		mtp  = new MyTicketPage(driverR);
		tcp  = new ChangePasswordPage(driverR);
		slp  = new ShortLeavePage(driverR);
	}

	// ── Teardown ──────────────────────────────────────────────────────────────
	public void teardown() {
		if (driverR != null) {
			driverR.quit();
			driverR = null;
		}
	}
}
