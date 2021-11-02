package automation.library.cucumber.selenium;

import automation.library.common.Property;
import automation.library.common.TestContext;
import automation.library.cucumber.core.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Common Step definition class. This has some basic steps defs to launch the open browser, launch rul navigate forward
 * and back and launch mobile application
 */
@SuppressWarnings("deprecation")
public class CommonSteps extends BaseSteps {
	protected Logger log = LogManager.getLogger(this.getClass().getName());

	public CommonSteps() {
		When("^the browser is opened$",() -> getDriver().manage().window().maximize());

		When("^the application \"(.*)\"$",(String app) -> {
			getDriver().manage().window().maximize();
			log.debug("browser launched");
			String url = Property.getProperty(Constants.ENVIRONMENTPATH+Property.getVariable("cukes.env")+".properties",app);
			log.debug("Navigating to url: "+url);
			getDriver().get(url);
		});

		When("^the mobile application \"(.*)\"$",(String app) -> {
			String[] target = (Property.getProperty(Constants.ENVIRONMENTPATH+Property.getVariable("cukes.env")+".properties",app)).split("::");
			TestContext.getInstance().putFwSpecificData("fw.appName", target[0]);
			TestContext.getInstance().putFwSpecificData("fw.appPackage", target[1]);
			TestContext.getInstance().putFwSpecificData("fw.appActivity", target[2]);
			getDriver();
		});

		When("^the url \"(.*)\"$", (String url) -> {
			getDriver().manage().window().maximize();
			log.debug("browser launched");
			log.debug("Navigating to url: "+url);
			getDriver().get(url);
		});

		When("^browser is navigated back$",() -> getDriver().navigate().back());

		When("^browser is navigated forward$",() -> getDriver().navigate().forward());
	}
}