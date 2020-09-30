package automation.library.cucumber.selenium;

import automation.library.common.TestContext;
import automation.library.common.Property;
import automation.library.selenium.exec.Constants;
import automation.library.selenium.exec.driver.factory.DriverContext;
import automation.library.selenium.exec.driver.factory.DriverFactory;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java8.En;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static automation.library.selenium.core.Screenshot.*;

import java.io.File;

/**
 * Hooks class for the screenshot in case of failure
 */
public class Hooks {

    protected Logger log = LogManager.getLogger(this.getClass().getName());

    @After(order = 30)
    public void checkScenarioStatus(Scenario scenario) {
        if (DriverContext.getInstance().getTechStack() != null) {
            if (scenario.isFailed()) {
                boolean errorScreenShotTaken = false;
                String screenshotOnFailure = Property.getVariable("screenshotOnFailure");
                if(screenshotOnFailure == null) screenshotOnFailure = Property.getProperty(Constants.SELENIUMRUNTIMEPATH,"screenshotOnFailure");

                if (screenshotOnFailure != null && Boolean.parseBoolean(screenshotOnFailure)) {
                    if(DriverFactory.getInstance().returnDriver() !=null){
                        File img = grabScreenshot(DriverFactory.getInstance().returnDriver());
                        if(img!=null){
                            File file = saveScreenshot(img, getScreenshotPath());
                            String relativePath = "." + File.separator + "Screenshots" + File.separator + file.getName();
                            String absolutePath = file.getAbsolutePath();
                            TestContext.getInstance().putFwSpecificData("fw.screenshotRelativePath", relativePath);
                            TestContext.getInstance().putFwSpecificData("fw.screenshotAbsolutePath", absolutePath);
                            errorScreenShotTaken = true;
                        }
                    }
                    if(!errorScreenShotTaken) log.debug("no screenshot taken");
                }
            }

            DriverFactory.getInstance().driverManager().updateResults(scenario.isFailed() ? "failed" : "passed");

            if (!DriverContext.getInstance().getKeepBrowserOpen())
                DriverFactory.getInstance().quit();
        }
    }

    public static String getReportPath() {
        String defaultReportPath = System.getProperty("user.dir") + File.separator + "RunReports" + File.separator;
        String reportPath = Property.getVariable("reportPath");
        return (reportPath == null ? defaultReportPath : reportPath);
    }

    public static String getScreenshotPath() {
        return getReportPath() + "Screenshots" + File.separator;
    }

}
