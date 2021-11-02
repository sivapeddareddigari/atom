package automation.library.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to hold the execution context including scenario, properties and platform/browser combo
 * for each execution thread
 */
public class TestContext {

	private static List<TestContext> 	threads = new ArrayList<TestContext>();
	private SoftAssert sa = null;
	private Map<String,Object> 			testdata = null;
	private Map<String,Object>			fwSpecificData = null;
	private long 						threadToEnvID;
	private Logger logger = LogManager.getLogger(TestContext.class);

	private TestContext(){}

	private TestContext(long threadID){
		this.threadToEnvID = threadID;
	}

	/**
	 * Singleton class implementation, holding thread specific SoftAssert and TestData objects.
	 * @return the instance of TestContext class.
	 */
	public static synchronized TestContext getInstance(){
		long currentRunningThreadID = Thread.currentThread().getId();
		for(TestContext thread : threads){
			if (thread.threadToEnvID == currentRunningThreadID){
				return thread;
			}
		}
		TestContext temp = new TestContext(currentRunningThreadID);
		threads.add(temp);
		return temp;
	}

	/**
	 * @return SoftAssert object within the test context for the current thread
	 */
	public SoftAssert sa(){
		if (sa == null)
			sa = new SoftAssert();
		return sa;
	}

	public void resetSoftAssert(){
		sa = new SoftAssert();
	}

	/**
	 * @return Map(String, Object) testdata object for the current thread
	 */
	public Map<String, Object> testdata(){
		if (testdata ==null )
			testdata = new HashMap<String,Object>();
		return testdata;
	}

	/**
	 * @return value of the key stored in testdata object for the current thread (casting to required object type)
	 */
	public Object testdataGet(String key) {
		return testdata.get(key);
	}

	/**
	 *
	 * @return To pull back test data from the test context for the current thread cast to provided class
	 */
	public <T> T testdataToClass(String key, Class<T> type){
		return type.cast(testdata.get(key));
	}

	/**
	 * store the data in the testdata Map object for the current thread. The key-value can be asses by testdataGet(String key) or
	 * complete object by testdata() method
	 */
	public void testdataPut(String key, Object data) {
		testdata().put(key, data);
	}


	/**
	 * @return Map(String, Object) fwSpecificData object for the current thread
	 */
	public Map<String, Object> fwSpecificData(){
		if (fwSpecificData ==null )
			fwSpecificData = new HashMap<String,Object>();
		return fwSpecificData;
	}

	/**
	 * @return value of the key stored in fwSpecificData object for the current thread (casting to required object type)
	 * - used by framework for test execution
	 */
	public Object getFwSpecificData(String key) {
		return fwSpecificData.get(key);
	}

	/**
	 * store the data in the getFwSpecificData Map object for the current thread. The key-value can be asses by getFwSpecificData(String key)
	 */
	public void putFwSpecificData(String key, Object data) {
		fwSpecificData().put(key, data);
	}
}
