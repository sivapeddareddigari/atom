package automation.library.jira.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import automation.library.common.Property;
import automation.library.reporting.Reporter;
import io.cucumber.core.api.Scenario;
import io.cucumber.java8.En;

public class CucumberHooks implements En {

    public CucumberHooks() {
        After(20, (Scenario scenario) -> {
            String featureName = (scenario.getId().split(";")[0].replace("-", " "));
            String scenarioName = scenario.getName();
            if (Boolean.parseBoolean(Property.getVariable("cukes.jira"))) {
                Map<String, Object> map = new HashMap<String, Object>();
                Collection<String> tags = scenario.getSourceTagNames();
                map.put("jiraStory", GetJiraRefs(tags, "Story:").get(0));
                map.put("jiraFeature", GetJiraRefs(tags, "Feature:").get(0));
                map.put("jiraScenario", GetJiraRefs(tags, "Scenario:").get(0));

                StringBuilder builder = new StringBuilder();
                builder.append("---------------------------\n");
                builder.append("Feature: " + featureName + " \n");
                builder.append("Scenario: " + scenarioName + " \n");

//                if (ThreadContext.getInstance().getSelenium()) {
//                    String browser = ThreadContext.getInstance().getBrowserName();
//                    String browserVersion = ThreadContext.getInstance().getBrowserVersion();
//                    String platform = ThreadContext.getInstance().getPlatform();
//                    builder.append("Browser: " + browser
//                            + (browserVersion != null ? " " + browserVersion : "")
//                            + ((platform != null && !platform.equals("null_null")) ? " " + platform : "")
//                            + "\n");
//                }

                builder.append("Environment: " + Property.getVariable("cukes.env"));
                builder.append("Build: " + Property.getVariable("JOB_NAME") + "_" + Property.getVariable("BUILD_NUMBER"));
                builder.append("Status: " + (scenario.isFailed() ? "Failed" : "Passed") + " \n");
                builder.append("---------------------------\n");

                String comment = builder.toString();
                map.put("comment", comment);
                map.put("failed", scenario.isFailed());

                int bugLevel = Integer.parseInt(Property.getVariable("cukes.jira.bugLevel"));
                if (scenario.isFailed()) {
                    map.put("bugLevel", bugLevel);
                    if (bugLevel == 2) {
                        builder.append("Error: " + Reporter.getErrorMsg() + " \n");
                        String bugSummary = "Automated Test Failure for Scenario : " + scenario.getName();
                        String bugDescription = builder.toString();
                        map.put("bugSummary", bugSummary);
                        map.put("bugDescription", bugDescription);
                    }

//                    if (ThreadContext.getInstance().getSelenium()) {
//                        String screenshotPath = ThreadContext.getInstance().testdata().get("screenshotPath").toString();
//                        map.put("screenshotPath", screenshotPath);
//                    }
                }
                JIRAContext.addIssue(map.get("jiraScenario").toString(), map);
            }
        });
    }

    private static String GetAllScenarioTags(Collection<String> tags) {
        // Get All tags from the scenario as a string
        String[] myStringArray = tags.toArray(new String[0]);

        int i = 0;
        String allTags = "";
        if (myStringArray.length == 0) {
            allTags = "Unknown";
        } else {
            while (i < myStringArray.length) {
                allTags = allTags + " " + myStringArray[i];
                i++;
            }
        }
        return allTags;
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
