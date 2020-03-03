package runners;

import io.cucumber.testng.CucumberOptions;

@CucumberOptions(tags = {"@registerinterest,@journeyplanner,@donothing", "not @ignore"})

public class BrowserTest1 extends BaseRunner {}