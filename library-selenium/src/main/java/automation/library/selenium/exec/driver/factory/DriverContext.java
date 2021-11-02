package automation.library.selenium.exec.driver.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Used to hold the execution context including scenario, properties and platform/browser combo
 * for each execution thread
 */
public class DriverContext {

	private static List<DriverContext> 	threads = new ArrayList<DriverContext>();
	private Map<String, String> 		techStack = null;
	private long 						threadToEnvID;
	private boolean						keepBrowserOpen=false;
	private Logger logger = LogManager.getLogger(DriverContext.class);

	private DriverContext(){}

	private DriverContext(long threadID){
		this.threadToEnvID = threadID;
	}

	public static synchronized DriverContext getInstance(){
		long currentRunningThreadID = Thread.currentThread().getId();
		for(DriverContext thread : threads){
			if (thread.threadToEnvID == currentRunningThreadID){
				return thread;
			}
		}
		DriverContext temp = new DriverContext(currentRunningThreadID);
		threads.add(temp);
		return temp;
	}

	public void setDriverContext(Map<String, String> techStack) {
		setTechStack(techStack);
	}

	public Map<String, String> getTechStack(){
		return this.techStack;
	}

	public String getBrowserName(){
		if (techStack == null) {
			return null;
		}else {
			return this.techStack.get("browserName")==null?this.techStack.get("browser"):this.techStack.get("browserName");
		}
	}

	public String getBrowserVersion(){
		if (techStack == null) {
			return null;
		}else {
			return this.techStack.get("version")==null?this.techStack.get("browser_version"):this.techStack.get("version");
		}
	}

	public String getPlatform(){
		if (techStack == null) {
			return null;
		}else {
			return this.techStack.get("platform")==null?this.techStack.get("os")+"_"+this.techStack.get("os_version"):this.techStack.get("platform");
		}
	}

	public void setTechStack(Map<String, String> techStack){
		this.techStack = techStack;
	}

	public Boolean getKeepBrowserOpen(){
		return this.keepBrowserOpen;
	}

	public void setKeepBrowserOpen(Boolean val){
		this.keepBrowserOpen=val;
	}
}
