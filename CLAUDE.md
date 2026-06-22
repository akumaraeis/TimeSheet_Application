# AEIS Timesheet Automation Framework

## Project identity
- **Artifact**: IntelleTime (v1.0)
- **Application**: IntelleTime — Timesheet module
- **Git repo**: https://github.com/akumaraeis/TimeSheet_Application.git
- **Java version**: 21
- **Base package**: `com.cms`

---

## Exact dependency versions (do not suggest upgrades unless asked)
| Library | Version |
|---|---|
| selenium-java | 4.11.0 |
| webdrivermanager | 5.6.3 |
| testng | 7.4.0 |
| extentreports | 5.0.9 |
| lombok | 1.18.32 |
| apache poi + poi-ooxml | 4.1.2 |
| rest-assured | 5.4.0 |
| log4j-core + log4j-api | 2.23.1 |
| gson | 2.10.1 |
| tess4j (OCR) | 5.7.0 |
| scribejava-apis (OAuth) | 8.3.1 |

---

## Project structure
```
src/
  test/
    java/
      com/cms/
        base/
          BasePage.java          ← All page classes extend this
          BaseTest.java          ← All test classes extend this
        pages/                   ← Page Object classes (one per page/component)
        testcases/               ← TestNG test classes
        listener/
          Listener.java          ← ITestListener (registered in all TestNG XMLs)
        utils/
          DriverFactory.java     ← WebDriver setup via WebDriverManager
          ExcelUtils.java        ← Apache POI — reads test data from Excel
          ExtentManager.java     ← ExtentReports 5 singleton
          ScreenshotUtil.java    ← Screenshot capture on failure
          Log4jUtils.java        ← Log4j2 logging wrapper
    resources/
      RegressionSuiteForRegularization.xml      ← DEFAULT suite (pom.xml default)
      RegressionSuiteFor_Dashboard.xml          → DashboardTest
      RegressionSiteForCopyTimesheetSubmission.xml → CopyTimesheetSubmissionTest
      RegressionSuiteFor_NewAddTask.xml         → NewTimesheetSubmission
      DemoAddTask.xml                           → AddTaskTest2
      testdata/                  ← Excel files for data-driven tests
```

---

## Known test classes (existing — do not rename or restructure)
| TestNG XML | Test class |
|---|---|
| RegressionSuiteForRegularization.xml | (default — check existing class) |
| RegressionSuiteFor_Dashboard.xml | `com.cms.testcases.DashboardTest` |
| RegressionSiteForCopyTimesheetSubmission.xml | `com.cms.testcases.CopyTimesheetSubmissionTest` |
| RegressionSuiteFor_NewAddTask.xml | `com.cms.testcases.NewTimesheetSubmission` |
| DemoAddTask.xml | `com.cms.testcases.AddTaskTest2` |

---

## TestNG XML pattern — use this for every new suite
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Suite">
  <listeners>
    <listener class-name="com.cms.listener.Listener"/>
  </listeners>
  <test thread-count="5" name="Test">
    <parameter name="browser" value="chrome"/>
    <classes>
      <class name="com.cms.testcases.YourTestClass"/>
    </classes>
  </test>
</suite>
```
- Always register `com.ITA.listener.Listener` in every XML
- Always pass `browser` as a parameter
- `thread-count="5"` is the project standard

---

## How to run

```bash
# Default regression suite (RegressionSuiteForRegularization.xml)
mvn clean test -DappURL=https://your-app-url.com

# Specific suite
mvn clean test -DappURL=https://your-app-url.com \
  -Dsurefire.suiteXmlFiles=src/test/resources/RegressionSuiteFor_Dashboard.xml

# CI (Jenkins uses bat, not sh)
mvn clean install -DskipTests
mvn test

# Skip compilation, run only tests
mvn test -DskipTests=false
```

- App URL is always passed as `-DappURL` system property — never hardcode URLs
- Read URL in code as: `System.getProperty("appURL")`

---

## CI/CD — Jenkins pipeline (Jenkinsfile)
- Source: `main` branch of GitHub repo
- Stages: Checkout → `mvn clean install -DskipTests` → `mvn test` → Publish TestNG report
- Reports archived from: `**/test-output/*.html`
- Failure notification: email to team
- Uses `bat` (Windows Jenkins agent) — never suggest `sh` for build commands

---

## Coding standards — always follow

### Page Objects
- Extend `BasePage`, call `super(driver)` and `PageFactory.initElements(driver, this)`
- `@FindBy` locators declared at top of class
- Use **Lombok** (`@Getter`, `@Slf4j`) where appropriate — it is on the classpath
- Methods return `void` or next page object (fluent)
- No assertions inside page classes

### Waits
- **Never `Thread.sleep()`** — use explicit waits only
- Use `WebDriverWait` with `ExpectedConditions`
- Wrap in `WaitHelper` utility for reuse

### Locator priority
1. `data-testid` → `By.cssSelector("[data-testid='...']")`
2. `id` → `By.id("...")`
3. `name` → `By.name("...")`
4. CSS → `By.cssSelector("...")`
5. XPath — last resort only, never by class attribute alone

### Test data
- External data → Excel files via **Apache POI** (`ExcelUtils`)
- Use `@DataProvider` to feed data into `@Test` methods
- Never hardcode test data strings inside test methods

### Logging
- Use **Log4j2** for all logging — never `System.out.println()`
- Use `@Slf4j` (Lombok) on any class that needs logging

### API tests (REST Assured)
- REST Assured is on the classpath — use it for any API-layer test or setup
- API utility/helper classes go in `com.cms.utils`
- Use Gson or org.json for JSON serialization

### OCR (Tess4J)
- Tess4J is available for reading text from images (e.g., CAPTCHA, image-based fields)
- OCR utility goes in `com.cms.utils.OcrUtil`

### OAuth (ScribeJava)
- ScribeJava is available for OAuth-based authentication flows
- Auth utilities go in `com.cms.utils`

---

## Things Claude must NEVER do
- Never use `Thread.sleep()` — use explicit waits
- Never hardcode app URLs — always use `System.getProperty("appURL")`
- Never write assertions inside page object classes
- Never use XPath by class attribute (e.g., `//div[@class='btn']`)
- Never create a `WebDriver` instance inside a page class — always receive via constructor
- Never call `driver.quit()` inside a test method — only in `BaseTest.@AfterMethod`
- Never suggest `sh` for Jenkins steps — this project runs on a Windows agent (use `bat`)
- Never change existing TestNG XML suite names or listener registration

---

## When generating new files

### New page object
- Location: `src/test/java/com/cms/pages/`
- Name: `<PageName>Page.java`
- Package: `com.cms.pages`
- Extends `BasePage`, uses `@FindBy` + `PageFactory`

### New test class
- Location: `src/test/java/com/cms/testcases/`
- Name: `<Feature>Test.java`
- Package: `com.cms.testcases`
- Extends `BaseTest`
- Uses `@Test(groups={"smoke"})` or `{"regression"}`

### New TestNG XML suite
- Location: `src/test/resources/`
- Follow the XML pattern above exactly
- Register `com.cms.listener.Listener`

### New utility
- Location: `src/test/java/com/cms/utils/`
- Package: `com.cms.utils`
- Stateless or singleton pattern
- Javadoc on all public methods
