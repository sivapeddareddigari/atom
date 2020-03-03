package automation.library.selenium.exec.driver.managers;

import automation.library.common.Property;
import automation.library.selenium.exec.driver.factory.Capabilities;
import automation.library.selenium.exec.driver.factory.DriverContext;
import automation.library.selenium.exec.driver.factory.DriverManager;
import automation.library.selenium.exec.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxDriverManager extends DriverManager {
	protected Logger log = LogManager.getLogger(this.getClass().getName());
	
	@Override
	public void createDriver(){
		Capabilities cap = new Capabilities();
    	PropertiesConfiguration props = Property.getProperties(Constants.SELENIUMRUNTIMEPATH);
    	if (Property.getVariable("cukes.webdrivermanager") != null && Property.getVariable("cukes.webdrivermanager").equalsIgnoreCase("true")) {
    		if (Property.getVariable("cukes.firefoxDriver")!=null) {
				WebDriverManager.firefoxdriver().version(Property.getVariable("cukes.firefoxDriver")).setup();
			}else {
				WebDriverManager.firefoxdriver().setup();
			}
    	}else {
    		System.setProperty("webdriver.gecko.driver",getDriverPath("geckodriver")); 			
    	}
    	
		FirefoxOptions options = new FirefoxOptions();
		for (String variable : props.getStringArray("options."+DriverContext.getInstance().getBrowserName().replaceAll("\\s",""))){
    		options.addArguments(variable);
		}									
//		options.addCapabilities(cap.getCap()); 	//TEMP
		driver = new FirefoxDriver(options);
	}

	@Override
	public void updateResults(String result){
		//do nothing
	}
} 