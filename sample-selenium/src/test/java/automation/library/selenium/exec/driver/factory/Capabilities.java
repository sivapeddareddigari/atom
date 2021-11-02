package automation.library.selenium.exec.driver.factory;

import automation.library.common.Property;
import automation.library.common.TestContext;
import automation.library.selenium.exec.Constants;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Map;

/**
 * Class to set the desired capabilities
 */
public class Capabilities {

    private DesiredCapabilities dc;

    /**
     * constructor to set the desired capabilities
     */
    public Capabilities() {
        Map<String, String> map = DriverContext.getInstance().getTechStack();

        dc = new DesiredCapabilities();

        if (!map.get("seleniumServer").equalsIgnoreCase("grid")) {
            dc.setCapability("name", TestContext.getInstance().getFwSpecificData("fw.testDescription"));
        }

        if (TestContext.getInstance().fwSpecificData().containsKey("fw.projectName"))
            dc.setCapability("project", TestContext.getInstance().getFwSpecificData("fw.projectName"));

        if (TestContext.getInstance().fwSpecificData().containsKey("fw.buildNumber"))
            dc.setCapability("build", TestContext.getInstance().getFwSpecificData("fw.buildNumber"));

        if (TestContext.getInstance().getFwSpecificData("fw.appPackage") != null)
            dc.setCapability("appPackage", TestContext.getInstance().getFwSpecificData("fw.appPackage"));

        if (TestContext.getInstance().getFwSpecificData("fw.appActivity") != null)
            dc.setCapability("appActivity", TestContext.getInstance().getFwSpecificData("fw.appActivity"));

        for (Map.Entry<String, String> pair : map.entrySet()) {
            if (!pair.getKey().equalsIgnoreCase("seleniumServer") && !pair.getKey().equalsIgnoreCase("description"))
                dc.setCapability(pair.getKey(), pair.getValue());
        }

        //TEMP
        PropertiesConfiguration props = Property.getProperties(Constants.SELENIUMRUNTIMEPATH);
        //setting any project specific capabilities
        for (String variable : props.getStringArray("desiredCapabilities." + DriverContext.getInstance().getBrowserName().replaceAll("\\s", ""))) {
            String[] par = variable.split("==");
            if (par[1].trim().equalsIgnoreCase("true") || par[1].trim().equalsIgnoreCase("false"))
                dc.setCapability(par[0].trim(), Boolean.parseBoolean(par[1].trim()));
            else
                dc.setCapability(par[0].trim(), par[1].trim());
        }
    }

    public DesiredCapabilities getCap() {
        return dc;
    }

}
