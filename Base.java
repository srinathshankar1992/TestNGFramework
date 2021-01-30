package Resources;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Assert;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.paulhammant.ngwebdriver.NgWebDriver;
import jdk.internal.org.jline.utils.Log;
import pageobjects_Publisher.AddnewDataObject;
import pageobjects_Publisher.Publisher_Login;
import pageobjects_Publisher.Publisher_dashboard;
import pageobjects_Publisher.SubscribersRequest;
import pageobjects_Subscriber.My_Applications;
import pageobjects_Subscriber.Subscriber_Dashboard;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

public class Base {
	public WebDriver driver;
	public Properties prop;
	public static ExtentReports extent;
	public static Logger log = LogManager.getLogger(Base.class.getName());
	public Robot rb;
	public Publisher_Login pbl;
	public Publisher_dashboard sbd;
	public Subscriber_Dashboard sd;

	public NgWebDriver ngDriver;
	public My_Applications Ma;
	public String Application_name = getExcelData(8, 4);
	public WebDriverWait wait;
	public AddnewDataObject ano;
	public String Applicationid = getExcelData(7, 4);
	public String DON = getExcelData(7, 1);
	public String SESAID = getExcelData(1, 0);

	public WebDriver initializedriver() throws IOException {

		prop = new Properties();
       FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\java\\Resources\\globalVariables.properties");
		prop.load(fis);
		String browsername = prop.getProperty("browser");
		System.out.println(browsername);

		if (browsername.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\src\\main\\resources\\chromedriver.exe");
			//ChromeOptions options = new ChromeOptions();
			//options.addArguments("headless");
			driver = new ChromeDriver();

		}

		else if (browsername.equals("firefox")) {
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"\\src\\main\\resources\\geckodriver.exe");
			driver = new FirefoxDriver();
			System.out.println("mozilacode here");
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		return driver;
	}

	public void getScreenShotPath(String destination) throws IOException {
		TakesScreenshot screenshot = (TakesScreenshot) driver;
		File source = screenshot.getScreenshotAs(OutputType.FILE);
		destination = System.getProperty("user.dir") + "\\reports\\" + destination + ".png";
		FileUtils.copyFile(source, new File(destination));
	}

	public static String getExcelData(int rowindex, int cellindex) {
		String data = null;
		try {
			File f = new File(System.getProperty("user.dir")+"\\userCredentialsSESA.xlsx");
			FileInputStream fis = new FileInputStream(f);
			Workbook wb = WorkbookFactory.create(fis);
			org.apache.poi.ss.usermodel.Sheet st = wb.getSheet("Sheet1");
			Row rr = st.getRow(rowindex);
			Cell cc = rr.getCell(cellindex);
			DataFormatter df = new DataFormatter();
			data = df.formatCellValue(cc);

			// data= cc.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;

	}

	public void beforeTest() throws IOException {
		driver = initializedriver();

	}

	public void PublisherLogin() throws InterruptedException, AWTException, IOException {

		driver.get(prop.getProperty("url"));
		log.info("browser initiated");
		Thread.sleep(2000);
		rb = new Robot();
		rb.keyPress(KeyEvent.VK_TAB);
		rb.keyPress(KeyEvent.VK_TAB);
		rb.keyPress(KeyEvent.VK_TAB);
		rb.keyPress(KeyEvent.VK_ENTER);
		rb.keyRelease(KeyEvent.VK_ENTER);
		pbl = new Publisher_Login(driver);
		log.info("skipped login here");

		pbl.username().sendKeys(SESAID);
		String password = getExcelData(1, 1);
		pbl.password().sendKeys(password);
		pbl.loginClick().click();
		log.info("Logged in");
		Thread.sleep(2000);
	}

	public void subscriberlogin() throws InterruptedException, AWTException {

		driver.get(prop.getProperty("url"));
		JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
		ngDriver = new NgWebDriver(jsDriver);
		ngDriver.waitForAngularRequestsToFinish();
		log.info("browser initiated");
		Thread.sleep(2000);
		rb = new Robot();
		rb.keyPress(KeyEvent.VK_TAB);
		rb.keyPress(KeyEvent.VK_TAB);
		rb.keyPress(KeyEvent.VK_TAB);
		rb.keyPress(KeyEvent.VK_ENTER);
		rb.keyRelease(KeyEvent.VK_ENTER);
		log.info("skipped login here");
		String subscriberSESA = getExcelData(1, 3);
		String subscriberPassword = getExcelData(1, 4);
		Publisher_Login pl = new Publisher_Login(driver);
		pl.username().sendKeys(subscriberSESA);
		pl.password().sendKeys(subscriberPassword);
		pl.loginClick().click();

	}
        public void addapplicationBySubscriber() throws InterruptedException, AWTException, IOException {
		rb = new Robot();
		sd = new Subscriber_Dashboard(driver);
		wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.elementToBeClickable(sd.myapplicationLink()));
		sd.myapplicationLink().click();
		Thread.sleep(5000);
		rb.keyPress(KeyEvent.VK_TAB);
		rb.keyPress(KeyEvent.VK_ENTER);
		rb.keyRelease(KeyEvent.VK_ENTER);
		Ma = new My_Applications(driver);
		String Application_owner = getExcelData(9, 4);
		String Business_entity = getExcelData(10, 4);
		Ma.applicationStacID().sendKeys(Applicationid);
		Ma.applicationame().sendKeys(Application_name);
		Ma.Applicationowner().sendKeys(Application_owner);
		Ma.BusinessEntity().sendKeys(Business_entity);
		Ma.AddApplicationButton().click();
		Thread.sleep(4000);

		try {
			WebElement error = Ma.duplicateStacIdErrorMessage();
			if (!error.isDisplayed()) {
				Ma.OK_Button().click();
				rb.keyPress(KeyEvent.VK_TAB);
				rb.keyPress(KeyEvent.VK_TAB);
				rb.keyPress(KeyEvent.VK_ENTER);
				rb.keyRelease(KeyEvent.VK_ENTER);	
				getScreenShotPath("Application is added successfully");
				Thread.sleep(2000);
			} else if(error.isDisplayed())
			{

				Thread.sleep(5000);
				getScreenShotPath("Application is already added with same stacid");
				log.info("application is already  added with same stacid");
				driver.close();
			}
		} catch (Exception e) {
driver.close();
		}
	}

	public void adddataobjectsByPublisher() throws InterruptedException, IOException {
		ano = new AddnewDataObject(driver);
		ano.adddataobjectButton().click();
		String DL = getExcelData(8, 1);
		String DP = getExcelData(9, 1);
		String Tp = getExcelData(10, 1);
		String Pe = getExcelData(11, 1);
		String Pm = getExcelData(12, 1);
		String Overview = getExcelData(13, 1);
		String description = getExcelData(14, 1);
		ano.DataObjectName().sendKeys(DON);
		ano.Datalayer().sendKeys(DL);
		ano.DataProvider().sendKeys(DP);
		Select Approval = new Select(ano.Approval());
		Approval.selectByVisibleText("Manual");
		Select Status = new Select(ano.status());
		Status.selectByVisibleText("Active");
		ano.Topicname().sendKeys(Tp);
		ano.PublishingEndPoint().sendKeys(Pe);
		ano.overview().sendKeys(Overview);
		ano.publishingmode().sendKeys(Pm);
		ano.DetailedDescription().sendKeys(description);
		ano.AddNewDataObjectButton().click();
		Thread.sleep(4000);
		getScreenShotPath("SameDataObject");

		if (ano.Okbutton().isDisplayed()) {
			ano.Okbutton().click();
			getScreenShotPath("Data objects are added successfully");
		} else {
			log.info("data objects are already added");
			driver.close();
		}
		driver.navigate().refresh();
		log.info("Data Object is added successfully");
		sbd = new Publisher_dashboard(driver);
		sbd.searchText().sendKeys(DON);
		getScreenShotPath("searched successfully");
		Thread.sleep(2000);
		log.info("searched successfully");
		
	}

}
