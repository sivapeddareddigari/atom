package automation.library.cucumber.api;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.TestNGCucumberRunner;
import org.apiguardian.api.API;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        strict = true,
        glue = {"automation.library"}
)

/**
 * Runs each cucumber scenario found in the features as separated test
 */
@API(status = API.Status.STABLE)
public abstract class RunnerClassParallel extends AbstractTestNGCucumberTests {

    /**
     * Returns two dimensional array of PickleEventWrapper scenarios
     * with their associated CucumberFeatureWrapper feature.
     *
     * @return a two dimensional array of scenarios features.
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
