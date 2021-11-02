package pageobjects;

import automation.library.selenium.exec.BasePO;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;


public class Journey extends BasePO {

	public By location = By.id("attendee_location");
	public By byBike = By.id("distanceByBike");
	public By byCar = By.id("distanceByCar");
	public By byPublicTrans = By.id("distanceByTransit");
	public By distance = By.id("travelTimeDistance");
	public By duration = By.id("travelTimeDuration");
	public By resultHeading = By.id("distanceResultsHeading");

	public void setJourney(String location, String type){
		$(this.location).clear().sendKeys(location);
		By by = null;
		switch(type){
		case "car": by=byCar;break;
		case "bike": by=byBike;break;
		case "public transport": by=byPublicTrans;break;
		}
		$(by).clickable().click();
		getWait().until(ExpectedConditions.textToBePresentInElementLocated(resultHeading, location));
		
	}
	
	public String getDistance(){
		getWait().until(ExpectedConditions.visibilityOfElementLocated(distance));
		return $(this.distance).getText();
	}
	
	public String getDuration(){
		getWait().until(ExpectedConditions.visibilityOfElementLocated(this.duration));
		return $(this.duration).getText();
	}
	
}
