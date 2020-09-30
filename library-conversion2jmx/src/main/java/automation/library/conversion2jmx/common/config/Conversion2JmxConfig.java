package automation.library.conversion2jmx.common.config;

import org.apache.jmeter.util.JMeterUtils;

import java.io.File;

public class Conversion2JmxConfig
{
    public void setJMeterHome() throws Exception {
        String path = new File("").getAbsolutePath();
        path += "/jmeter";
        JMeterUtils.setJMeterHome(path);
    }
}
