package automation.library.cucumber.selenium;

import automation.library.common.Property;
import automation.library.common.TestContext;
import automation.library.cucumber.core.Constants;

@SuppressWarnings("deprecation")
public class CommonSteps extends BaseSteps {

	public CommonSteps() {
		When("^the browser is opened$",() -> getDriver().manage().window().maximize());

		When("^the application \"(.*)\"$",(String app) -> {
			getDriver().manage().window().maximize();
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
			log.debug("Navigating to url: "+url);
			getDriver().get(url);
		});

		When("^browser is navigated back$",() -> getDriver().navigate().back());

		When("^browser is navigated forward$",() -> getDriver().navigate().forward());
	}
}
