package automation.library.jira.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import automation.library.common.Property;
import automation.library.common.ZipHelper;
import org.apache.commons.collections4.MultiMap;
import org.testng.annotations.Test;

import com.atlassian.jira.rest.client.api.domain.Issue;

public class JIRAUpdate {

	@Test
	public void jiraIssues() throws Exception {

		MultiMap<String, ?> mmap = JIRAContext.getIssues();
		Set<String> set = mmap.keySet();
		Boolean testRunFail = false;
		int bugCount = 0;
		int bugLevel = Integer.parseInt(Property.getVariable("cukes.jira.bugLevel"));
		Map<String, Boolean> featureFail = new HashMap<String, Boolean>();
		Issue bug = null;
		JIRAHelper jira = new JIRAHelper("","","","");
		
		for (String s : set) {
			List<?> maps = (List<?>) mmap.get(s);
			System.out.println("UPDATING JIRA FOR Scenario: " + s);
			String jiraScenario = null;
			String jiraFeature = null;
			Boolean scenarioFail = false;
			StringBuilder bugSummary = new StringBuilder();
			StringBuilder bugDescription = new StringBuilder();
			StringBuilder comment = new StringBuilder();
			String screenshotPath = null;

			for (int i = 0; i< maps.size(); i++) {
				Map<String, Object> map = (Map<String, Object>) maps.get(i);
				jiraScenario = map.get("jiraScenario").toString();
				jiraFeature = map.get("jiraFeature").toString();
				comment.append(map.get("comment"));
				if (Boolean.valueOf(map.get("failed").toString())) {
					scenarioFail = true;
					testRunFail = true;
					if (bugLevel == 2) {
						bugSummary.append(map.get("bugSummary"));
						bugDescription.append(map.get("bugDescription"));
						screenshotPath = map.get("screenshotPath") != null?map.get("screenshotPath").toString():null;
					}
				}
			}

			if (featureFail.containsKey(jiraFeature)) {
				if (scenarioFail) {
					featureFail.replace(jiraFeature, scenarioFail);
				}
			}else {
				featureFail.put(jiraFeature, scenarioFail);
			}
			
			jira.addRunResult(jiraScenario, scenarioFail, comment.toString());
			
			if (bugLevel == 2 && scenarioFail) {
				bug = jira.addScenarioBug(jiraScenario, bugSummary.toString(), bugDescription.toString(), screenshotPath);
				bugCount++;
			}else if (bugLevel == 1 && testRunFail && bugCount == 0) {
				bug = jira.addRunBug("Automated Test Run Failure", "Please see attached run report for details of test failures");
				ZipHelper.zipit("RunReports","RunReport.zip");
				File file = new File("RunReport.zip");
				jira.addIssueAttachment(bug, file, file.getName());
				bugCount++;
			}
			
			if (scenarioFail && bugLevel > 0) {
				jira.linkIssues(bug.getKey(), jiraScenario, "Blocks");
			}
		}	
		
		for (Map.Entry<String, Boolean> feature : featureFail.entrySet()){
			System.out.println("UPDATING JIRA FOR Feature: " + feature.getKey());
			jira.addRunResult(feature.getKey(), feature.getValue(), null);
		}
	}


}

