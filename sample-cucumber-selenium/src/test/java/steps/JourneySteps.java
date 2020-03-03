package steps;

import automation.library.cucumber.selenium.BaseSteps;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pageobjects.Journey;
import pageobjects.JourneyPF;

import java.util.List;
import java.util.Map;

public class JourneySteps extends BaseSteps {
	
@When("^a conference delegate checks their travel details from \"(.*)\" by \"(.*)\"$")
public void enterTravel1(String location, String type) throws Throwable {
	Journey journey = new Journey();		//this PO uses standard BY locators
	journey.setJourney(location, type);
}
	
@Then("^the correct journey information will be calculated as \"(.*)\" and \"(.*)\"$")
public void checkJourney1(String distance, String duration) throws Throwable {
	JourneyPF journey = new JourneyPF();	//this PO uses Selenium PageFactory
	sa().assertEquals(journey.getDistance(), "DISTANCE: "+distance);
	sa().assertEquals(journey.getDuration(), "JOURNEY TIME: "+duration);
	sa().assertAll();
}

@Then("^the journey information will be calculated correctly$")
public void checkJourney2(List<Map<String, String>> list) throws Throwable {
	Journey journey = new Journey();
	for (int i=0; i< list.size(); i++){
		journey.setJourney(list.get(i).get("location"),list.get(i).get("travel type") );
		sa().assertEquals(journey.getDistance(), "DISTANCE: "+list.get(i).get("distance"));
		sa().assertEquals(journey.getDuration(), "JOURNEY TIME: "+list.get(i).get("duration"));
	}
	sa().assertAll();
}

@When("^a trainee checks their travel details$")
public void dummyStep() throws Throwable {
	//dummy step
}

}	
