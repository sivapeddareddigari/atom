package steps;

import automation.library.cucumber.selenium.BaseSteps;
import automation.library.reporting.AllureScreenShotPub;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.StepResult;
import org.apache.xmlbeans.impl.xb.xsdschema.NamedGroup;
import pageobjects.Journey;
import pageobjects.JourneyPF;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public class JourneySteps extends BaseSteps {

//    @Description("123456789")
    @When("^a conference delegate checks their travel details from \"(.*)\" by \"(.*)\"$")
    public void enterTravel1(String location, String type) throws Throwable {
        Journey journey = new Journey();        //this PO uses standard BY locators
        journey.setJourney(location, type);

//
//		ServiceLoader<StepLifecycleListener> loader;
//		loader = ServiceLoader.load(StepLifecycleListener.class);
//		StepLifecycleListener a = loader.iterator().next();
//		StepResult stepResult = null;
//		a.afterStepUpdate(stepResult);
//        Allure.step("line1 \n line2");
//	Allure.step("line3");
//
//	Allure.parameter("firstname","ANshul");
//	Allure.link("gmail","www.gmail.com");
        Allure.addAttachment("temAtt", "<b>just a content</b>");
//	Allure.description("**Step2**");
//	Allure.step("**Step2**");
    }

    @Then("^the correct journey information will be calculated as \"(.*)\" and \"(.*)\"$")
    public void checkJourney1(String distance, String duration) throws Throwable {
        JourneyPF journey = new JourneyPF();    //this PO uses Selenium PageFactory
        sa().assertEquals(journey.getDistance(), "DISTANCE: " + distance);
        sa().assertEquals(journey.getDuration(), "JOURNEY TIME: " + duration);
        sa().assertAll();
    }

    @Then("^the journey information will be calculated correctly$")
    public void checkJourney2(List<Map<String, String>> list) throws Throwable {
        Journey journey = new Journey();
        for (int i = 0; i < list.size(); i++) {
            journey.setJourney(list.get(i).get("location"), list.get(i).get("travel type"));
            sa().assertEquals(journey.getDistance(), "DISTANCE: " + list.get(i).get("distance"));
            sa().assertEquals(journey.getDuration(), "JOURNEY TIME: " + list.get(i).get("duration"));
        }
        sa().assertAll();
    }

    @When("^a trainee checks their travel details$")
    public void dummyStep() throws Throwable {
        //dummy step
    }

}	
