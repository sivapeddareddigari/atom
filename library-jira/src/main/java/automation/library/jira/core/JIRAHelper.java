package automation.library.jira.core;

import automation.library.common.JsonHelper;
import automation.library.common.Property;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.*;
import com.atlassian.jira.rest.client.api.domain.input.*;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.google.common.collect.Iterables;
import io.atlassian.util.concurrent.Promise;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class JIRAHelper {

	String uri;
	String username;
	String password;
	String comment;
	String project;
	PropertiesConfiguration props;
	URI attachmentsURI = null;
	JiraRestClientFactory restClientFactory;
	JiraRestClient restClient;
	IssueRestClient issueClient;
	IssueInputBuilder issueBuilder;


	public JIRAHelper(String uri, String project, String username, String password) {

		try {
//			props = Property.getProperties(System.getProperty("user.dir")+Property.getProperty("jiraPath")+"jira.properties");
//			uri = props.getString("uri");
//			username = props.getString("username");
//			password = props.getString("password");
			this.uri = uri;
			this.project = project;
			this.username = username;
			this.password = password;
			restClientFactory = new AsynchronousJiraRestClientFactory();
			restClient = restClientFactory.createWithBasicHttpAuthentication(new URI(this.uri), username, password);
			issueClient = restClient.getIssueClient();
			issueBuilder = new IssueInputBuilder();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	// Executes after each scenario is run (passed or failed)
	public void addRunResult(String jiraRef, Boolean failed, String comment) throws Exception {
		String transition = null;
		this.comment = comment;

		Issue issue = restClient.getIssueClient().getIssue(jiraRef).claim();

		if (failed) {
			transition = "Test Failed";
		}else {          
			transition = "Test Passed";
		}

		transitionIssue(issue, transition, comment);

	}

	// Executes after each scenario is run (passed or failed)
	public Issue addScenarioBug(String jiraScenario, String bugSummary, String bugDescription, String screenshotPath) throws Exception {
		Issue issue = getExistingBug(bugSummary, bugDescription);
		if (issue == null) {
			issue = createBug(bugSummary, bugDescription);
		}

		if (screenshotPath != null){
			File file = new File(screenshotPath);		
			addIssueAttachment(issue, file, file.getName());
		}

		return issue;
	}

	public Issue addRunBug(String bugSummary, String bugDescription) throws Exception {
		Issue issue = createBug(bugSummary, bugDescription);
		return issue;
	}

	public List<Issue> getJIRAIssues(String issueType) {
		List<Issue> issues = new ArrayList<Issue>();
		Promise<SearchResult> searchJqlPromise = restClient.getSearchClient().searchJql("project = " + props.getString("project") + " AND issuetype in (issueType) ORDER BY Rank ASC");
		for (BasicIssue issue : searchJqlPromise.claim().getIssues()) {
			issues.add(restClient.getIssueClient().getIssue(issue.getKey()).claim());
		}
		return issues;
	}

	public void transitionIssue(Issue issue, String transitionName, String comment) throws Exception {
		final Iterable<Transition> transitions = restClient.getIssueClient().getTransitions(issue.getTransitionsUri()).claim();
		final Transition resolveIssueTransition = getTransitionByName(transitions, transitionName);
		final TransitionInput transitionInput = new TransitionInput(resolveIssueTransition.getId());
		restClient.getIssueClient().transition(issue.getTransitionsUri(),  transitionInput);
		if (comment != null){
			issueClient.addComment(issue.getCommentsUri(), Comment.valueOf(comment));
		}
	}

	private static Transition getTransitionByName(Iterable<Transition> transitions,	String transitionName) {
		for (Transition transition : transitions) {
			if (transition.getName().equals(transitionName)) {
				return transition;
			}
		}
		return null;
	}

	public void linkIssues(String issueOne, String issueTwo, String linkName) throws Exception {
		issueClient.linkIssue(new LinkIssuesInput(issueOne, issueTwo, linkName, null)).claim();
		final Issue originalIssue = issueClient.getIssue(issueTwo).claim();
		Iterator<IssueLink> links = (originalIssue.getIssueLinks().iterator());
		while ((links.hasNext()) ) {
			IssueLink link = links.next();
			String transitiveLinkName = link.getIssueLinkType().getName();
			String transitiveLinkDescription = link.getIssueLinkType().getDescription();
			if (transitiveLinkName.equals("Test Coverage") && transitiveLinkDescription.equals("is a test for")) {
				issueClient.linkIssue(new LinkIssuesInput(issueOne, link.getTargetIssueKey(), linkName, null)).claim();
			}
		}
	}

	public void addIssueAttachment(Issue issue, File file, String name) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			issueClient.addAttachment(issue.getAttachmentsUri(), fis, name).claim();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	public Issue getExistingBug(String bugSummary, String bugDescription) {
		Promise<SearchResult> searchJqlPromise = restClient.getSearchClient().searchJql("project = " + props.getString("project") + " AND status != Done AND issuetype = Bug " + " AND summary ~ \"" + bugSummary + "\" ORDER BY Rank ASC");
		Issue issue = null;

		if (searchJqlPromise.claim().getTotal() > 0) {
			existing:
				for (BasicIssue bi : searchJqlPromise.claim().getIssues()) {
					Issue fullIssue = restClient.getIssueClient().getIssue(bi.getKey()).claim();
					//if (fullIssue.getDescription().equals(description)) {
					issue = fullIssue;
					break existing;
					//}            
				}
		}
		return issue;
	}

	public Issue createBugFromProperties(String bugSummary, String bugDescription) {
		issueBuilder.setProjectKey(props.getString("project"));
		issueBuilder.setIssueTypeId(Long.valueOf(props.getString("bugId")));
		issueBuilder.setFieldValue("labels",Arrays.asList("AutomatedTest", "Cucumber"));
		issueBuilder.setSummary(bugSummary); 
		issueBuilder.setDescription(bugDescription);
		Iterator<String> keys = props.getKeys("field");
		while(keys.hasNext()) {
			String key = keys.next();
			String value = props.getString(key);
			if (value.startsWith("<")) {
				value = System.getProperty(value.replace("<","").replace(">",""), null);
			}
			String field = key.split("field\\.")[1];
			if (field.startsWith("customfield_")) {
				if (value.startsWith("[")){
					issueBuilder.setFieldValue(field, ComplexIssueInputFieldValue.with("value", (Object) value.replace("[","").replace("]","")));
				}else {
					issueBuilder.setFieldValue(field, value);
				}
			}else {
				switch(field) {
				case "priority": issueBuilder.setPriorityId(Long.valueOf(value)); break;
				case "affectsVersions": issueBuilder.setAffectedVersionsNames(new ArrayList<String>(Arrays.asList(value.split(",")))); break;
				case "components": issueBuilder.setComponentsNames(new ArrayList<String>(Arrays.asList(value.split(",")))); break;
				case "environment": issueBuilder.setFieldValue("environment", value);
				case "assignee": issueBuilder.setAssigneeName(value); break;
				case "reporter": issueBuilder.setReporterName(value); break;
				default: issueBuilder.setFieldValue(field, value);
				}
			}
		}
		IssueInput issueInput = issueBuilder.build();
		Promise<BasicIssue> promise = issueClient.createIssue(issueInput);
		BasicIssue basicIssue = promise.claim();
		Issue newIssue = issueClient.getIssue(basicIssue.getKey()).claim();
		return newIssue;
	}

	public Issue createBugFromJSON(String bugSummary, String bugDescription) {
		String issueKey = null;
		Issue issue = null;
		try {
			JSONObject json = JsonHelper.getJSONData(Constants.JIRAPATH+"bug.json");
			json.getJSONObject("fields").put("summary", bugSummary);
			json.getJSONObject("fields").put("description", bugDescription);
			String fields = json.toString();


			URL url = new URL(uri+"/rest/api/2/issue/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");

			byte[] auth = (username+":"+password).getBytes();
			String encodedAuth = Base64.getEncoder().encodeToString(auth);

			conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.getOutputStream().write(fields.getBytes());

			int responseCode = conn.getResponseCode();

			if (responseCode == 201) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				JSONObject resp = new JSONObject(response.toString());
				issueKey = resp.getString("key");
				//attachmentsURI = new URI(resp.getString("self")+"/attachments");
				issue = restClient.getIssueClient().getIssue(issueKey).claim();
			}
		} catch (JSONException|IOException e) {
			e.printStackTrace();
		} 
		return issue;
	}

	public Issue createBug(String bugSummary, String bugDescription) {
		Issue issue;
		Iterator<String> keys = props.getKeys("field");
		if (keys.hasNext()) {
			issue = createBugFromProperties(bugSummary, bugDescription);
		}else {
			issue = createBugFromJSON(bugSummary, bugDescription);
		}
		return issue;
	}

	public void buildFeatureFiles() throws Exception {
//		String projectKey = props.getString("project");
		String issueType = "Feature";
		String startPath = Property.getVariable("cukes.jira.featuresPath");

		//  retreive all features from jira
		Map<Map<String,String>,List<Map<String,String>>> allIssuesAndSubTasks = getAllIssuesAndSubTasks(project, issueType);
		Set<Map<String,String>> features = allIssuesAndSubTasks.keySet();


		for (Iterator<Map<String,String>> featureIt = features.iterator(); featureIt.hasNext(); ) {
			Map<String,String> feature = featureIt.next();

//			if (feature.get("Automated").equals("Yes")) {

				String featureName = feature.get("FeatureKey")+"_"+feature.get("Summary").replaceAll(" ", "");
				String[] tags = feature.get("Labels").split(",");

				String outfile = featureName+".feature";
				// create the new file        
				File file = new File(startPath + outfile);
				file.getParentFile().mkdirs();
				FileWriter fw = new FileWriter(file, false);
				// creater the print writer
				PrintWriter pw = new PrintWriter(fw, true);

				int tagsCounter=0;

				pw.write("@Story:"+feature.get("StoryKey"));
				pw.write(" ");
				pw.write("@Feature:"+feature.get("FeatureKey"));
				tags = feature.get("Labels").split(",");
				// loop through each label
				while (tagsCounter<tags.length) {
					// remove unwanted characters from the jira labels
					// for use in @tags
					tags[tagsCounter]=tags[tagsCounter].replace("[", "");
					tags[tagsCounter]=tags[tagsCounter].replace("]", "");
					tags[tagsCounter]=tags[tagsCounter].replace(" ", "");
					if(tags[tagsCounter].length()>2) {
						pw.write(" @"+tags[tagsCounter]);
					}
					tagsCounter++;
				}

				pw.println();
				// write the feature name
				pw.write("Feature: " + feature.get("Summary"));
				pw.println();
				// write the featue description
				if (Boolean.parseBoolean(Property.getVariable("cukes.jira.useStoryDesc"))){
					if (feature.get("StoryDescription") != null){
						pw.write(feature.get("StoryDescription"));
						pw.println();
					}
				}

				if (feature.get("FeatureDescription") != null){
					pw.println();
					pw.write(feature.get("FeatureDescription"));
					pw.println();
				}

				// get all the scenarios for the feature
				List<Map<String,String>> featureScenarios = allIssuesAndSubTasks.get(feature);

				// loop through the found scenarios for each found feature
				for (Iterator<Map<String,String>> scenarioIt = featureScenarios.iterator(); scenarioIt.hasNext(); ) {
					pw.println();
					pw.println();
					// get the next scenario
					Map<String,String> scenario = scenarioIt.next();

					// only add the automated scenarios
//					if(scenario.get("Automated").equals("Yes")) {
						// tags
						pw.write("@Scenario:"+scenario.get("ScenarioKey"));
						tags = scenario.get("Labels").split(",");   
						tagsCounter=0;
						while (tagsCounter<tags.length) {
							tags[tagsCounter]=tags[tagsCounter].replace("[", "");
							tags[tagsCounter]=tags[tagsCounter].replace("]", "");
							tags[tagsCounter]=tags[tagsCounter].replace(" ", "");
							if(tags[tagsCounter].length()>2) {
								pw.write(" @"+tags[tagsCounter]);
							}
							tagsCounter++;
						}

						pw.println();
						if(scenario.get("Description").contains("Examples:")) {
							pw.print("Scenario Outline: " + scenario.get("Summary"));
						}
						else {
							pw.print("Scenario: " + scenario.get("Summary"));
						}
						pw.println();
						pw.print(scenario.get("Description"));
						pw.println();
//					}
				}

				pw.flush();
				pw.close();
				fw.close();
//			}
		}  
	}

	public Map<Map<String,String>,List<Map<String,String>>> getAllIssuesAndSubTasks(String projectKey, String issueType) {

		// holds the jira items mapping features to their scenarios
		Map<Map<String,String>,List<Map<String,String>>> jiraItems = new HashMap<Map<String,String>,List<Map<String,String>>>();
		// execute jql query in jira
		Promise<SearchResult> searchJqlPromise = restClient.getSearchClient().searchJql("project = " + projectKey + " AND issuetype in (Story) ORDER BY Rank ASC");
		// counts the features
		int featureCounter = 1;
		// gets each issue in turn
		for (BasicIssue issue : searchJqlPromise.claim().getIssues()) {    
			Map<String,String> feature = new HashMap<String,String>();            
			// get the issue unique key for the feature
			feature.put("FeatureKey", issue.getKey());
			System.out.println("Getting feature issue: "+issue.getKey());
			// get the full jira issue for the feature
			Issue featureIssue = restClient.getIssueClient().getIssue(issue.getKey()).claim();
			Issue storyIssue = null;
			Iterator<IssueLink> links = (featureIssue.getIssueLinks().iterator());
			while ((links.hasNext()) ) {
				IssueLink link = links.next();
				String transitiveLinkName = link.getIssueLinkType().getName();
				String transitiveLinkDescription = link.getIssueLinkType().getDescription();
				if (transitiveLinkName.equals("Test Coverage") && transitiveLinkDescription.equals("is a test for")) {
					storyIssue = restClient.getIssueClient().getIssue(link.getTargetIssueKey()).claim();
				}
			}

			feature.put("StoryKey", storyIssue.getKey());
			// get the issue summary
			feature.put("Summary", featureIssue.getSummary());
			// get the issue description
			feature.put("StoryDescription", storyIssue.getDescription());
			feature.put("FeatureDescription", featureIssue.getDescription());
			// get the issue labels
			feature.put("Labels", featureIssue.getLabels().toString());
			// add a feature counter to determine the order of execution
			feature.put("Counter", Integer.toString(featureCounter));
			// find out if the scenairo is automated            
			try {
//				IssueField field = featureIssue.getField(props.getString("automatedField"));
//				if(field.getValue().toString().contains("Yes")) {
					feature.put("Automated", "Yes");
//				}
//				else {
//					feature.put("Automated", "No");
//				}
			}
			catch (Exception e) {
				feature.put("Automated", "No");
			}
			// get all the issue subtasks
			Iterable<Subtask> subtasks = featureIssue.getSubtasks();
			int i=0;
			// iterate over subtasks
			Iterator<Subtask> subtasksIterator = subtasks.iterator();
			List<Map<String,String>> scenarios = new ArrayList<Map<String,String>>();

			while (subtasksIterator.hasNext()) {
				Map<String,String> scenario = new HashMap<String,String>();                
				Subtask subtask = Iterables.get(subtasks, i);
				// get the scenario summary
				scenario.put("Summary", subtask.getSummary());
				// get the feature issue key
				scenario.put("ScenarioKey", subtask.getIssueKey());
				// get the full issue for the scenario
				System.out.println("Getting sub issue: "+ subtask.getIssueKey());
				Issue fullSubtask = restClient.getIssueClient().getIssue(subtask.getIssueKey()).claim();
				// add subtask description
				scenario.put("Description", fullSubtask.getDescription());                
				// try to get the issue priority
				String priority = "";
				try {
					priority = fullSubtask.getPriority().getName();
				}
				catch (Exception e) {
					priority = "Blocker";
				}
				scenario.put("Priority", priority);
				// add the task labels
				scenario.put("Labels", fullSubtask.getLabels().toString());
				// add the status                
				String status = fullSubtask.getStatus().getName().replace(" ", "").replace(".", "");
				scenario.put("Status", status);
				// add the fix version
				String fixVersion = "";                
				try {

					Iterator<Version> versionIterator = fullSubtask.getFixVersions().iterator();
					while ((versionIterator.hasNext()) ) {
						Version version = versionIterator.next();
						fixVersion = fixVersion + version.getName()+",";
					}
					fixVersion = fixVersion.substring(0,fixVersion.length()-1);
				}                
				catch (Exception e) {
					fixVersion = "";
				}
				scenario.put("FixVersion", fixVersion);
				// add priority to labels if present
				if (priority!="") {
					scenario.put("Labels", scenario.get("Labels") + "," + priority);
				}
				// add fix version to labels if present
				if (fixVersion!="") {
					scenario.put("Labels", scenario.get("Labels") + "," + fixVersion);
				}
				// add status to labels if present
				if (status!="") {
					scenario.put("Labels", scenario.get("Labels") + "," + status);
				}                
				// find out if the scenairo is automated
//				try {
//					IssueField fieldSub = fullSubtask.getField(props.getString("automatedField"));
//					if(fieldSub.getValue().toString().contains("Yes")) {
						scenario.put("Automated", "Yes");
//					}
//					else {
//						scenario.put("Automated", "No");
//					}
//				}
//				catch (Exception e) {
//					scenario.put("Automated", "No");
//				}
				subtasksIterator.next();
				scenarios.add(scenario);

				i++;
			}

			jiraItems.put(feature,scenarios);
			featureCounter++;
		}

		return jiraItems;
	}

}