package pageobjects;

import automation.library.selenium.exec.BasePO;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class JourneyPF extends BasePO {
	
	public JourneyPF() {
		initialise(this);
	}
	
	@FindBy(how=How.ID, using = "attendee_location") private WebElement location;
	@FindBy(how=How.ID, using = "distanceByBike") private WebElement byBike;
	@FindBy(how=How.ID, using = "distanceByCar") private WebElement byCar;
	@FindBy(how=How.ID, using = "distanceByTransit") private WebElement byPublicTrans;
	@FindBy(how=How.ID, using = "travelTimeDistance") private WebElement distance;
	@FindBy(how=How.ID, using = "travelTimeDuration") private WebElement duration;
	@FindBy(how=How.ID, using = "distanceResultsHeading") private WebElement resultHeading;

	public void setJourney(String location, String type){
		this.location.clear();
		this.location.sendKeys(location);
		WebElement el = null;
		switch(type){
		case "car": el=byCar;break;
		case "bike": el=byBike;break;
		case "public transport": el=byPublicTrans;break;
		}
		getWait().until(ExpectedConditions.elementToBeClickable(el)).click();
		getWait().until(ExpectedConditions.textToBePresentInElement(resultHeading, location));
	}
	
	public String getDistance(){
		getWait().until(ExpectedConditions.visibilityOf(distance));
		return this.distance.getText();
	}
	
	public String getDuration(){
		getWait().until(ExpectedConditions.visibilityOf(this.duration));
		return this.duration.getText();
	}
}
