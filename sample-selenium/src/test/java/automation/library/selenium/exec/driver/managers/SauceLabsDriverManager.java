package automation.library.selenium.exec.driver.managers;

import automation.library.common.Property;
import automation.library.common.TestContext;
import automation.library.selenium.exec.driver.factory.Capabilities;
import automation.library.selenium.exec.driver.factory.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class SauceLabsDriverManager extends DriverManager {

	protected Logger log = LogManager.getLogger(this.getClass().getName());

	@Override
	public void createDriver(){
		Capabilities cap = new Capabilities();
		String sauceUserName = Property.getVariable("cukes.sauceUserName");
		String sauceAccessKey = Property.getVariable("cukes.sauceAccessKey");
		String sauceEndPoint = Property.getVariable("cukes.sauceEndPoint");
		String sauceServerAddress = "http://"+sauceUserName + ":" + sauceAccessKey + sauceEndPoint;
		try {
			if (cap.getCap().getCapability("platformName").toString().equalsIgnoreCase("Android")) {
				cap.getCap().setCapability("automationName","uiautomator2");
				cap.getCap().setCapability("app","sauce-storage:"+TestContext.getInstance().getFwSpecificData("fw.appName"));
				driver = new AndroidDriver(new URL(sauceServerAddress), cap.getCap());
			}else if (cap.getCap().getCapability("platformName").toString().equalsIgnoreCase("iOS")) {
				cap.getCap().setCapability("automationName","XCUITest");
				driver = new IOSDriver(new URL(sauceServerAddress), cap.getCap());
			}else {
				driver = new RemoteWebDriver(new URL(sauceServerAddress), cap.getCap());
				((RemoteWebDriver) driver).setFileDetector(new LocalFileDetector());
			}
		} catch (MalformedURLException e) {
			log.debug("Could not connect to SauceLabs: url invalid");
		}
	}

	@Override
	public void updateResults(String result){
			((JavascriptExecutor) driver).executeScript("sauce:job-result=" + result);
	}
} 
