package automation.library.conversion2jmx.selenium;

import automation.library.common.Property;
import automation.library.conversion2jmx.har2jmx.HarHelper;
import automation.library.selenium.exec.Constants;

public class Har2JmxBaseTest {
    /**
     * if enableHar2Jmx is true then a HAR and JMX will be created
     */
    public void finaliseRecording() throws Exception {
        HarHelper.getInstance().saveHarAndGenerateJmx(Property.getProperties(Constants.SELENIUMRUNTIMEPATH).getString("harPath"), Property.getProperties(Constants.SELENIUMRUNTIMEPATH).getString("jmxPath"), null);
    }
}
