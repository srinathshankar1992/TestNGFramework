package Resources;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReporterClass {
 
   public  static ExtentReports extent;
	
	public static ExtentReports getReportobject()
	{
		String path = System.getProperty("user.dir")+"\\ExtentReport\\index.html";
		ExtentSparkReporter reporter = new ExtentSparkReporter(path);
		reporter.config().setReportName("Manufacture Advisor");
		reporter.config().setDocumentTitle("Test Result");
		
		extent = new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("Tester","Srinath shankar");
		return extent;
		
	}
}
