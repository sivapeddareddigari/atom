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
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverManager extends DriverManager {

    protected Logger log = LogManager.getLogger(this.getClass().getName());


    @Override
    public void createDriver() {
        Capabilities cap = new Capabilities();
        PropertiesConfiguration props = Property.getProperties(Constants.SELENIUMRUNTIMEPATH);
        if (Property.getVariable("cukes.webdrivermanager") != null && Property.getVariable("cukes.webdrivermanager").equalsIgnoreCase("true")) {
            if (Property.getVariable("cukes.chromeDriver") != null) {
                WebDriverManager.chromedriver().version(Property.getVariable("cukes.chromeDriver")).setup();
            } else {
                WebDriverManager.chromedriver().setup();
            }
        } else {
            System.setProperty("webdriver.chrome.driver", getDriverPath("chromedriver"));
        }
        System.setProperty("webdriver.chrome.silentOutput", "true");
        ChromeOptions options = new ChromeOptions();

        // If enableHar2Jmx is true then an extra capability will be added to allow for 'untrusted' certificates.
        // This is needed for when using a proxy to capture network traffic when recording HAR data.
        if(Property.getVariable("cukes.enableHar2Jmx") != null && Property.getVariable("cukes.enableHar2Jmx").equalsIgnoreCase("true")) {
            options.addArguments("--ignore-ssl-errors=yes");
            options.addArguments("--ignore-certificate-errors");
        }


        for (String variable : props.getStringArray("options." + DriverContext.getInstance().getBrowserName().replaceAll("\\s", ""))) {
            options.addArguments(variable);
        }

        log.debug("chrome.options="+ Property.getVariable("chrome.options"));
        if (Property.getVariable("chrome.options")!=null){
            options.addArguments(Property.getVariable("chrome.options"));
        }

        if (DriverContext.getInstance().getBrowserName().contains("kiosk")) {
            options.addArguments("--kiosk");
        }

        cap.getCap().setCapability(ChromeOptions.CAPABILITY, options);
        driver = new ChromeDriver(cap.getCap());
    }

    @Override
    public void updateResults(String result) {
        //do nothing
    }
} 