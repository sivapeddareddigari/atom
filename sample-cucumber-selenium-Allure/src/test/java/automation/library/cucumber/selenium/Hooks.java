package automation.library.cucumber.selenium;

import automation.library.common.Property;
import automation.library.common.TestContext;
import automation.library.selenium.exec.driver.factory.DriverContext;
import automation.library.selenium.exec.driver.factory.DriverFactory;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java8.En;

import java.io.File;

import static automation.library.selenium.core.Screenshot.grabScreenshot;
import static automation.library.selenium.core.Screenshot.saveScreenshot;


public class Hooks implements En {

//	public Hooks(){
//		After(30, (Scenario scenario) -> {
//			if (DriverContext.getInstance().getTechStack() != null){
//				if (scenario.isFailed()){
//					String screenshotOnFailure = Property.getVariable("screenshotOnFailure");
//					if (screenshotOnFailure !=null && Boolean.parseBoolean(screenshotOnFailure)){
//						File file = saveScreenshot(grabScreenshot(DriverFactory.getInstance().getDriver()), getScreenshotPath());
//						String relativePath = "." + File.separator + "Screenshots" + File.separator + file.getName();
//						String absolutePath = file.getAbsolutePath();
//						TestContext.getInstance().putFwSpecificData("fw.screenshotRelativePath", relativePath);
//						TestContext.getInstance().putFwSpecificData("fw.screenshotAbsolutePath", absolutePath);
//					}
//				}

//				DriverFactory.getInstance().driverManager().updateResults(scenario.isFailed() ? "failed" : "passed");
//
//				if (!DriverContext.getInstance().getKeepBrowserOpen())
//					DriverFactory.getInstance().quit();
//			}
//		});
//	}

	@After
	public void afterHookTakeScreenShotAndCloseBrowser(Scenario scenario){
		if (DriverContext.getInstance().getTechStack() != null){
			if (scenario.isFailed()){
				String screenshotOnFailure = Property.getVariable("screenshotOnFailure");
				if (screenshotOnFailure !=null && Boolean.parseBoolean(screenshotOnFailure)){
					if(DriverFactory.getInstance().checkDriver() !=null){
						File file = saveScreenshot(grabScreenshot(DriverFactory.getInstance().getDriver()), getScreenshotPath());
						String relativePath = "." + File.separator + "Screenshots" + File.separator + file.getName();
						String absolutePath = file.getAbsolutePath();
						TestContext.getInstance().putFwSpecificData("fw.screenshotRelativePath", relativePath);
						TestContext.getInstance().putFwSpecificData("fw.screenshotAbsolutePath", absolutePath);
					}
				}
			}
			DriverFactory.getInstance().driverManager().updateResults(scenario.isFailed() ? "failed" : "passed");

			if (!DriverContext.getInstance().getKeepBrowserOpen())
				DriverFactory.getInstance().quit();
		}
	}

	public static String getReportPath(){
		String defaultReportPath = System.getProperty("user.dir")+File.separator+"RunReports"+File.separator;
		String reportPath = Property.getVariable("reportPath");
		return (reportPath==null?defaultReportPath:reportPath);
	}

	public static String getScreenshotPath(){
		return getReportPath()+"Screenshots"+File.separator;
	}

}
