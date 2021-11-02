package automation.library.conversion2jmx.har2jmx;

import automation.library.common.Property;
import automation.library.conversion2jmx.har2jmx.service.HarParser;
import automation.library.conversion2jmx.har2jmx.service.IJmxFileCreator;
import automation.library.conversion2jmx.har2jmx.service.IParser;
import automation.library.conversion2jmx.har2jmx.service.JmxFileCreator;
import automation.library.selenium.exec.BaseTest;
import automation.library.selenium.exec.Constants;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;
import org.apache.commons.io.FilenameUtils;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class HarHelper {

    private static HarHelper harHelper = new HarHelper();
    private BrowserMobProxy proxy = new BrowserMobProxyServer();

    /**
     * Returns the current instance
     */
    public static HarHelper getInstance() {
        return harHelper;
    }

    /**
     * Returns the current proxy
     */
    public BrowserMobProxy getProxy() {
        return proxy;
    }

    /**
     * Sets the proxy to use.
     * Enables the types of content to capture.
     * Begins creation of a new HAR.
     */
    public void setProxy(BrowserMobProxy proxy) {
        this.proxy = proxy;
        this.proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
        this.proxy.newHar();
    }

    /**
     * Writes all HAR data which has been recorded to a new HAR file in a specified location. After writing the HAR
     * file the proxy is stopped. This HAR file is then used to build and create a new JMX file with the same filename
     * as the HAR file in a specified location.
     *
     * @param locationToSaveHar Location in which to save the HAR file.
     * @param locationToSaveJmx Location in which to save the JMX file.
     * @param filename The filename which is to be used for the new HAR and JMX files. Passing a null value for this
     *                 parameter caused a timestamp to be generated and used for the naming of both files.
     */
    public void saveHarAndGenerateJmx(String locationToSaveHar, String locationToSaveJmx, String filename) throws Exception {
        if(Property.getVariable("cukes.enableHar2Jmx") != null && Property.getVariable("cukes.enableHar2Jmx").equalsIgnoreCase("true")) {
            Har har = proxy.getHar();

            if (filename == null) {
                DateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
                Calendar cal = Calendar.getInstance();
                filename = sdf.format(cal.getTime());
            }

            String harPath = locationToSaveHar  + filename + ".har";
            Writer writer = new FileWriter(harPath);
            har.writeTo(writer);
            writer.close();

            try{
                proxy.stop();
            }
            catch (Exception e){

            }

            String jmxPath = locationToSaveJmx + filename + ".jmx";
            createJmx(harPath, jmxPath);
        }
    }

    /**
     * Reads a HAR file from a given path so it can be parsed and used for creating a JMX file.
     *
     * @param pathToHar Location to where the HAR file is saved.
     * @param locationToSaveJmx Location to where the JMX file should be created.
     */
    private void createJmx(String pathToHar, String locationToSaveJmx) throws Exception {
        IParser parser = new HarParser();
        de.sstoehr.harreader.model.Har har = parser.parse(new File(pathToHar));
        IJmxFileCreator jmxFileCreator = new JmxFileCreator();
        jmxFileCreator.create(har, locationToSaveJmx);
    }

    /**
     * Reads the /jmeter/harfiles/ directory for all files which match the filenames given as a parameter. Each file
     * is then parsed and used to create a JMX file in the /jmeter/jmxfiles/ directory which has the same filename
     * as the original HAR file.
     *
     * @param harFiles A List&lt;String\&gt; of all the HAR filenames which are to be converted into JMX format.
     */
    public void createJmx(List<String> harFiles) throws Exception {

        // Get all files in harFiles directory
        File folder = new File(Property.getProperties(Constants.SELENIUMRUNTIMEPATH).getString("harPath"));
        File[] listOfFiles = folder.listFiles();

        // Loop through all files and create a JMX
        for (File file : listOfFiles) {
            if (file.isFile() && harFiles.contains(file.getName())) {
                IParser parser = new HarParser();
                de.sstoehr.harreader.model.Har har = parser.parse(file);

                String jmxOutputFile = Property.getProperties(Constants.SELENIUMRUNTIMEPATH).getString("jmxPath") + FilenameUtils.removeExtension(file.getName()) + ".jmx";

                IJmxFileCreator jmxFileCreator = new JmxFileCreator();
                jmxFileCreator.create(har, jmxOutputFile);
            }
        }
    }

}
