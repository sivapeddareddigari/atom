package automation.library.conversion2jmx.selenium;

import automation.library.conversion2jmx.har2jmx.HarHelper;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import org.openqa.selenium.Proxy;

/**
 * Class to set the desired capabilities
 */
public class Har2JmxCapabilities{

    /**
     * if enableHar2Jmx is true then a new proxy server will be set and HAR recording started. This also involves a
     * proxy being added to the driver's DesiredCapabilities.
     */
    public Proxy setProxyCap() {
        HarHelper.getInstance().setProxy(new BrowserMobProxyServer());
        HarHelper.getInstance().getProxy().start(0);

        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(HarHelper.getInstance().getProxy());
        return seleniumProxy;
    }

}
