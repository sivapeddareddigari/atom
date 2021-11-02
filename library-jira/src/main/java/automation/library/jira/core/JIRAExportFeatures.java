package automation.library.jira.core;

import org.testng.annotations.Test;

public class JIRAExportFeatures {
	
	@Test
	public void jiraFeatures() throws Exception {

		//provide valid url, credential and project
		String uri="";
		String username="";
		String password="";
		String project = "";

		JIRAHelper jira = new JIRAHelper(uri,project,username,password);
		
		jira.buildFeatureFiles();
	}
}
