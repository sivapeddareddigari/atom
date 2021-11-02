package automation.library.reporting;

import automation.library.common.Property;
import automation.library.selenium.exec.Constants;
import automation.library.selenium.exec.driver.factory.DriverFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;

/**
 * Screenshot publisher implementing io.qameta.allure.listener.StepLifecycleListener to update the step in case of FAILED or BROKEN
 */
public class AllureScreenShotPub implements StepLifecycleListener {

    @Override
    public void afterStepUpdate(final StepResult result) {
        if ((result.getStatus() == Status.FAILED) || (result.getStatus() == Status.BROKEN)) {

            String screenshotOnFailure = Property.getVariable("screenshotOnFailure");
            if (screenshotOnFailure == null)
                screenshotOnFailure = Property.getProperty(Constants.SELENIUMRUNTIMEPATH, "screenshotOnFailure");

            if (screenshotOnFailure != null && Boolean.parseBoolean(screenshotOnFailure)) {
                if (DriverFactory.getInstance().returnDriver() != null) {
                    AllureScreenShot.attachScreenShot(DriverFactory.getInstance().returnDriver(), "error");
                }
            }
        }
    }
}
