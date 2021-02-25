package steps;

import automation.library.common.TestContext;
import automation.library.cucumber.selenium.BaseSteps;
import automation.library.reporting.Reporter;
import org.testng.asserts.SoftAssert;

public class BaseSteps1 extends BaseSteps {


    public SoftAssert sa(String message) {
        Reporter.addStepLog(message);
        Reporter.getCurrentStep().fail(message);
        TestContext.getInstance().sa().onAssertFailure();
        return TestContext.getInstance().sa().;
    }
}
