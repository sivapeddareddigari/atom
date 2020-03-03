package automation.library.selenium.exec.driver.managers;

import automation.library.common.Property;
import automation.library.selenium.exec.driver.factory.Capabilities;
import automation.library.selenium.exec.driver.factory.DriverManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class IEDriverManager extends DriverManager {

	protected Logger log = LogManager.getLogger(this.getClass().getName());

	@Override
	public void createDriver(){
		Capabilities cap = new Capabilities();
		if (Property.getVariable("cukes.webdrivermanager") != null && Property.getVariable("cukes.webdrivermanager").equalsIgnoreCase("true")) {
			if (Property.getVariable("cukes.ieDriver")!=null) {
				WebDriverManager.iedriver().version(Property.getVariable("cukes.ieDriver")).setup();
			}else {
				WebDriverManager.iedriver().setup();
			}
    	}else {
    		System.setProperty("webdriver.ie.driver", getDriverPath("IEDriverServer"));
    	}
    	
		System.setProperty("webdriver.ie.driver.silent", "true");
		System.setProperty("ie.ensureCleanSession", "true");
		driver = new InternetExplorerDriver(cap.getCap());
	}

	@Override
	public void updateResults(String result){
		//do nothing
	}
} 