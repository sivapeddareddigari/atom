package automation.library.cucumber.core;

import automation.library.common.TestContext;
import io.cucumber.core.api.Scenario;
import io.cucumber.java8.En;

public class Hooks implements En{

	public Hooks(){
		Before(10, (Scenario scenario) -> {
			String featureName = (scenario.getId().split(";")[0].replace("-", " "));
			String scenarioName = scenario.getName();
			TestContext.getInstance().putFwSpecificData("fw.testDescription", featureName + "-" + scenarioName);
			TestContext.getInstance().putFwSpecificData("fw.cucumberTest","true");
		});

	}
}
