package automation.library.cucumber.selenium;


import io.cucumber.testng.CucumberFeatureWrapper;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleEventWrapper;
import org.apiguardian.api.API;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

@CucumberOptions(
        strict = true,
        glue = {"automation.library"}
)

@Deprecated //TODO - to be removed after merge and test
@API(status = API.Status.STABLE)
public class BaseTest extends automation.library.selenium.exec.BaseTest {

/*    //Invoke Cucumber tests
    private io.cucumber.testng.TestNGCucumberRunner testNGCucumberRunner;

    @BeforeClass(alwaysRun = true)
    public void setUpClass() throws Exception {
        testNGCucumberRunner = new io.cucumber.testng.TestNGCucumberRunner(this.getClass());
    }

    @Test(dataProvider = "techStackJSON", dataProviderClass = BaseTest.class)
    public void scenario(PickleEventWrapper pickleEvent, CucumberFeatureWrapper cucumberFeature) throws Throwable {
        testNGCucumberRunner.runScenario(pickleEvent.getPickleEvent());
    }

    @DataProvider
    public Object[][] scenarios() {
        return testNGCucumberRunner.provideScenarios();
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() throws Exception {
        testNGCucumberRunner.finish();
    }*/


}
