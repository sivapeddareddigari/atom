package pageobjects;

import automation.library.selenium.exec.BasePO;
import org.openqa.selenium.By;

import java.io.IOException;
import java.util.Map;

public class RegisterInterest extends BasePO {

	public By name = By.id("attendee_name");
	public By email = By.id("attendee_email");
	public By mobile = By.id("attendee_mobile");
	public By houseNumber = By.id("attendee_housenum");
	public By street = By.cssSelector("input[placeholder='Street']");
	public By city = By.cssSelector("input[placeholder='City']");
	public By county = By.cssSelector("input[placeholder='County']");
	public By postcode = By.cssSelector("input[placeholder='Postcode']");
	public By jobtitle = By.id("attendee_job_title");
	public By contactEmail = By.cssSelector("#registrationForm>div:nth-of-type(1)>label>input");
	public By contactSMS = By.cssSelector("#registrationForm>div:nth-of-type(2)>label>input");
	public By contactPost = By.cssSelector("#registrationForm>div:nth-of-type(3)>label>input");
	public By submit = By.cssSelector("#registrationForm>button");
	public By confirmationMsg = By.id("results_response");

	public void registerInterest(Map<String, String> map) throws IOException{		
		setEmail(map.get("email"));
		setMobile(map.get("telephone"));
		setAddressStreet(map.get("street"));
		setAddressCity(map.get("city"));
		setAddressCounty(map.get("county"));
		setAddressPostcode(map.get("postcode"));
		setTitle(map.get("title"));
		setContactEmail(map.get("contactEmail"));
		setContactSMS(map.get("contactSMS"));
		setContactPost(map.get("contactPost"));	
		setUsername(map.get("user"));
		setAddressHouseNum(map.get("housenumber"));	
		submit();	
	}

	public String getResponse() {
		return $(confirmationMsg).getText();
	}
	
	private void setEmail(String val) {
		$(email).sendKeys(val);
	}
	
	private void setMobile(String val) {
		$(mobile).sendKeys(val);
	}
	
	private void setAddressStreet(String val) {
		$(street).sendKeys(val);
	}
	
	private void setAddressCity(String val) {
		$(city).sendKeys(val);
	}
	
	private void setAddressCounty(String val) {
		$(county).sendKeys(val);
	}
	
	private void setAddressPostcode(String val) {
		$(postcode).sendKeys(val);
	}
	
	private void setTitle(String val) {
		$(jobtitle).sendKeys(val);
	}
	
	private void setContactEmail(String val) {
		if (val.equalsIgnoreCase("yes")){
			$(contactEmail).select();
		}
	}
	
	private void setContactSMS(String val) {
		if (val.equalsIgnoreCase("yes")){
			$(contactSMS).select();
		}
	}
	
	private void setContactPost(String val) {
		if (val.equalsIgnoreCase("yes")){
			findElement(contactPost).select();
		}
	}
	
	private void setUsername(String val) {
		$(name).sendKeys(val);
	}

	private void setAddressHouseNum(String val) {
		$(houseNumber).sendKeys(val);
	}
	
	private void submit() {
		$(submit).clickable().click();
	}
}
