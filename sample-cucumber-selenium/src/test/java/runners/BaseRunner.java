package runners;

import automation.library.cucumber.selenium.RunnerClass;
import automation.library.cucumber.selenium.RunnerClassParallel;
import automation.library.cucumber.selenium.RunnerClassSequential;
import automation.library.reporting.ExtentProperties;
import automation.library.reporting.Reporter;
import automation.library.reporting.TextReport;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;


@CucumberOptions(
        plugin = {"automation.library.reporting.adapter.ExtentCucumberAdapter:"},
        features = {"classpath:features"},
        glue = {"steps", "hooks"}

        )

public class BaseRunner extends RunnerClassSequential {

    //TestNG before hook
//    @BeforeTest
//    public static void setup() {
//        ExtentProperties extentProperties = ExtentProperties.INSTANCE;
//        extentProperties.setReportPath(Reporter.getReportPath() + Reporter.getReportName());
//    }

    //TestNG after hook
    @AfterTest
    public void teardown() {
        TextReport tr = new TextReport();
        tr.createReport(true);
    }
}