package automation.library.selenium.exec.driver.managers;

import automation.library.common.Property;
import automation.library.selenium.exec.driver.factory.Capabilities;
import automation.library.selenium.exec.driver.factory.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class GridDriverManager extends DriverManager {

	protected Logger log = LogManager.getLogger(this.getClass().getName());


	@Override
	public void createDriver(){
		Capabilities cap = new Capabilities();
		try {
			if (cap.getCap().getCapability("platformName").toString().equalsIgnoreCase("Android")) {
				driver = new AndroidDriver(new URL(Property.getVariable("cukes.seleniumGrid")), cap.getCap());
			}else if (cap.getCap().getCapability("platformName").toString().equalsIgnoreCase("iOS")) {
				driver = new IOSDriver(new URL(Property.getVariable("cukes.seleniumGrid")), cap.getCap());
			}else if (cap.getCap().getCapability("platformName").toString().equalsIgnoreCase("Windows")) {
				driver = new WindowsDriver<WindowsElement>(new URL(Property.getVariable("cukes.seleniumGrid")), cap.getCap());
			}else {
				driver = new RemoteWebDriver(new URL(Property.getVariable("cukes.seleniumGrid")), cap.getCap());
			}
		} catch (MalformedURLException e) {
			log.debug("Could not connect to SauceLabs: url invalid");
		}
	}

	@Override
	public void updateResults(String result){
		//do nothing
	}
} 