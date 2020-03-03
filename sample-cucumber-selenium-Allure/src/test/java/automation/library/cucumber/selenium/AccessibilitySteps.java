package automation.library.cucumber.selenium;

import automation.library.common.Property;
import com.deque.axe.AXE;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

public class AccessibilitySteps extends BaseSteps {

    private URL scriptUrl = getClass().getClassLoader().getResource("accessibility/axe.min.js");


    public String getAccessibilityReportPath() {
        String defaultReportPath = System.getProperty("user.dir") + File.separator + "AccessibilityReports" + File.separator;
        String reportPath = Property.getVariable("accessibilityReportPath");
        return (reportPath == null ? defaultReportPath : reportPath);
    }

    public File generateReportFile() {
        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new java.util.Date());

        File file = new File(getAccessibilityReportPath() + "AccessibilityViolations_ " + timeStamp + ".txt");

        try {
            new File(getAccessibilityReportPath()).mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public void determineViolations(JSONArray violations) {
        if (violations.length() == 0) {
            Assert.assertTrue(true, "No violations found");
        } else {

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(generateReportFile()))) {
                bw.write(AXE.report(violations));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void determineViolations(String pageUrl, JSONArray violations) {
        if (violations.length() == 0) {
            Assert.assertTrue(true, "No violations found");
        } else {

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(generateReportFile()))) {
                bw.write(pageUrl);
                bw.write("\n\n");
                bw.write(AXE.report(violations));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Basic test
     *
     * @param pageUrl
     */
    public void testAccessibility(String pageUrl) {
        getDriver().get(pageUrl);
        JSONObject responseJSON = new AXE.Builder(getDriver(), scriptUrl).analyze();

        JSONArray violations = responseJSON.getJSONArray("violations");

        determineViolations(pageUrl, violations);
    }

    /**
     * Test with options
     *
     * @param pageUrl
     */
    public void testAccessibilityWithOptions(String pageUrl) {
        getDriver().get(pageUrl);
        JSONObject responseJSON = new AXE.Builder(getDriver(), scriptUrl)
                .options("{ rules: { 'accesskeys': { enabled: false } } }")
                .analyze();

        JSONArray violations = responseJSON.getJSONArray("violations");

        determineViolations(violations);
    }

    /**
     * Test includes and excludes
     */
    public void testAccessibilityWithIncludesAndExcludes(String pageUrl, String includedElem, String excludedElem) {
        getDriver().get(pageUrl);
        JSONObject responseJSON = new AXE.Builder(getDriver(), scriptUrl)
                .include(includedElem)
                .exclude(excludedElem)
                .analyze();

        JSONArray violations = responseJSON.getJSONArray("violations");

        determineViolations(violations);
    }

    /**
     * Test a WebElement
     *
     * @param pageUrl
     * @param tag
     */
    public void testAccessibilityWithWebElement(String pageUrl, String tag) {
        getDriver().get(pageUrl);
        JSONObject responseJSON = new AXE.Builder(getDriver(), scriptUrl)
                .analyze(getDriver().findElement(By.tagName(tag)));

        JSONArray violations = responseJSON.getJSONArray("violations");

        determineViolations(violations);
    }
}