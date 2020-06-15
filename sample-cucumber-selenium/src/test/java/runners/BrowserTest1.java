package runners;

        import io.cucumber.testng.CucumberOptions;

@CucumberOptions(tags = {"@registerinterest", "not @ignore"})

public class BrowserTest1 extends BaseRunner {}