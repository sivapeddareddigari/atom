package runners;

import automation.library.cucumber.api.RunnerClass;
import automation.library.cucumber.selenium.RunnerClassParallel;
import automation.library.reporting.ExtentProperties;
import automation.library.reporting.Reporter;
import automation.library.reporting.TextReport;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

@CucumberOptions(
        plugin = {"automation.library.reporting.adapter.ExtentCucumberAdapter:"},
        features = {"classpath:features"},
        glue = {"steps", "hooks"}
)

public class BaseRunner extends RunnerClass {

    //TestNG after hook
    @AfterSuite
    public void teardown() {
        TextReport tr = new TextReport();
        tr.createReport(true);
    }
}