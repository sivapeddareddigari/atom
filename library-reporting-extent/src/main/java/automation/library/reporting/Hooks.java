package automation.library.reporting;

import automation.library.common.Property;
import automation.library.common.TestContext;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static automation.library.reporting.Reporter.getCurrentStep;
import static automation.library.reporting.Reporter.*;

/**
 * Class to attach the screen shot to the report
 */
public class Hooks {

    protected static Logger log = LogManager.getLogger(PDFReport.class);

    @After(order = 10)
    public void hookSS(Scenario scenario) {
        if (Boolean.parseBoolean(Property.getVariable("extent.pdfReport"))){
            String featureName = (scenario.getId().split(";")[0].replace("-", " "));
            String scenarioName = scenario.getName();

            try {
                String techSuffix="";
                Map<String, Object> map = TestContext.getInstance().fwSpecificData();
                Reporter.addStepLog("PDF Updates:");
                if(map !=null){
                    if(map.containsKey("platformName"))
                    {
                        Reporter.addStepLog("<div id='platform'>Platform Name : " +  map.get("platformName").toString() + "</div>");
                        techSuffix=map.get("platformName").toString();
                    }

                    if(map.containsKey("platFormVersion"))
                    {
                        Reporter.addStepLog("<div id='platFormVersion'>Platform Version : " + map.get("platFormVersion").toString() + "</div>");
                        techSuffix = techSuffix+map.get("platFormVersion").toString();
                    }

                    if(map.containsKey("browser"))
                    {
                        Reporter.addStepLog("<div id='browser'>Browser : " + map.get("browser").toString() + "</div>");
                        techSuffix = techSuffix+map.get("browser").toString();
                    }

                    if(map.containsKey("version"))
                    {
                        Reporter.addStepLog("<div id='Version'>Version : " + map.get("version").toString() + "</div>");
                        techSuffix = techSuffix+map.get("version").toString();
                    }
                }

                Collection<String> tags = scenario.getSourceTagNames();

                String cucumberScenario = GetJiraRefs(tags, "CucumberScenario:").get(0);
                String zephyrId = Property.getVariable("TMSIssueId");
                if(zephyrId ==null){
                    List<String> ref = GetJiraRefs(tags, "TMSIssueId:");
                    if(ref !=null & ref.size()!=0) zephyrId=ref.get(0);
                }

                String pdfFileName = (zephyrId!=null?zephyrId:"") + "_" + cucumberScenario + "_" + techSuffix;

                Reporter.addStepLog("<div id='pdfReport'>PDF Name : " + pdfFileName + "</div>");
                Reporter.addStepLog("<div id='sceEndTime' style=\"display:none\">" + new Date() + "</div>");
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("Error while update report for PDF reporting:" + featureName + ":" + scenarioName);
            }
        }

        if (scenario.isFailed()) {
            getCurrentStep().fail("Scenario Failed");
            if (TestContext.getInstance().getFwSpecificData("fw.screenshotRelativePath") != null) {
                addScreenCaptureFromPath((String) TestContext.getInstance().getFwSpecificData("fw.screenshotRelativePath"));
            }
        } else {
            getCurrentStep().pass("Scenario Successful");
        }
    }

    private static List<String> GetJiraRefs(Collection<String> tags, String jiraPrefix) {
        // Get the JIRA refs for the feature and scenario
        String[] myStringArray = tags.toArray(new String[0]);
        List<String> jiraRefs = new ArrayList<String>();
        for (String s : myStringArray) {
            if (s.length() > jiraPrefix.length()) {
                if ((s.substring(1, jiraPrefix.length() + 1).equals(jiraPrefix))) {
                    jiraRefs.add(s.substring(jiraPrefix.length() + 1));
                }
            }
        }
        return jiraRefs;
    }
}
