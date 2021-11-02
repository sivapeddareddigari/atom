package automation.library.selenium.exec.driver.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import automation.library.selenium.exec.driver.factory.Capabilities;
import automation.library.selenium.exec.driver.factory.DriverManager;

public class SafariDriverManager extends DriverManager {

	protected Logger log = LogManager.getLogger(this.getClass().getName());

	@Override
	public void createDriver(){
		Capabilities cap = new Capabilities();
		SafariOptions options = new SafariOptions();
//		options.setUseCleanSession(true);			//TEMP
		driver = new SafariDriver(cap.getCap());
	}

	@Override
	public void updateResults(String result){
		//do nothing
	}
} 