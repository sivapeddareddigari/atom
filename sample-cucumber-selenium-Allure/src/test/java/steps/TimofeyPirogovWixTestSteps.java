package steps;

import automation.library.cucumber.selenium.BaseSteps;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import pageobjects.TimofeyPirogovWixTestPageObject;

import java.util.Map;

public class TimofeyPirogovWixTestSteps extends BaseSteps {
	
	TimofeyPirogovWixTestPageObject tpws;
	
@When("^User Filled Forms$")
public void launchApplication(DataTable table) throws Throwable {
	Map<String, String> map = table.asMap(String.class, String.class);
	tpws = new TimofeyPirogovWixTestPageObject();
	tpws.registerInterest(map);
	try {
		Thread.sleep(5000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
}



}	
