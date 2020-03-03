package automation.library.selenium.exec.driver.managers;

import automation.library.common.Property;
import automation.library.selenium.exec.driver.factory.Capabilities;
import automation.library.selenium.exec.driver.factory.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserStackDriverManager extends DriverManager {

	protected Logger log = LogManager.getLogger(this.getClass().getName());
	
	@Override
	public void createDriver(){
		Capabilities cap = new Capabilities();
		String browserstackUserName = Property.getVariable("cukes.browserstackUserName");
		String browserstackAccessKey = Property.getVariable("cukes.browserstackAccessKey");
		String browserstackEndPoint = Property.getVariable("cukes.browserstackEndPoint");
		String browserstackServerAddress = "https://"+browserstackUserName + ":" + browserstackAccessKey + browserstackEndPoint;
		try {
			if (cap.getCap().getCapability("platformName").toString().equalsIgnoreCase("Android")) {
				cap.getCap().setCapability("automationName","uiautomator2");
				driver = new AndroidDriver(new URL(browserstackServerAddress), cap.getCap());
			}else if (cap.getCap().getCapability("platformName").toString().equalsIgnoreCase("iOS")) {
				cap.getCap().setCapability("automationName","XCUITest");
				driver = new IOSDriver(new URL(browserstackServerAddress), cap.getCap());
			}else {
				driver = new RemoteWebDriver(new URL(browserstackServerAddress), cap.getCap());
			}
		} catch (MalformedURLException e) {
			log.debug("Could not connect to BrowserStack: url invalid");
		}
	}

	@Override
	public void updateResults(String result){
		//to be completed
	}
} 