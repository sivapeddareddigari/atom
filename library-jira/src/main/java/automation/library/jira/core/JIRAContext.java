package automation.library.jira.core;

import java.util.Map;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

public class JIRAContext {
	
	public volatile static MultiMap<String, Object> jiraIssues = new MultiValueMap<String, Object>();
	
	public static void addIssue(String key, Map<String,Object> map) {
		jiraIssues.put(key, map);
	}
	
	
	public static MultiMap<String, Object> getIssues() {
		return jiraIssues;
	}
	

}
