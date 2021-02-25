package runners;

import io.cucumber.testng.CucumberOptions;

//@CucumberOptions(tags = {"@donothing", "not @ignore"})
@CucumberOptions(tags = {"@TimofeyPirogovWixTest", "not @ignore"})

public class BrowserTest1 extends BaseRunner {}