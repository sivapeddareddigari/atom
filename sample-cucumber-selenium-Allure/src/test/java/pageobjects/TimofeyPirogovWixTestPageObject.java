package pageobjects;

import automation.library.selenium.exec.BasePO;
import automation.library.selenium.exec.driver.factory.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.Map;

public class TimofeyPirogovWixTestPageObject extends BasePO {

	public By name = By.xpath("//input[@id='input_comp-klba33ez']");
	public By email = By.xpath("//input[@id='input_comp-klba33fq']");
	public By iframe = By.xpath("//iframe[@name='htmlComp-iframe']");
	public By inputNameiniFrame = By.xpath("//input[@id='NameInput2']");
	public By inputNameInShadowRoot = By.cssSelector("input#NameInputInShowRoot3");
	public By shadowRoot = By.cssSelector("show-hello");
	public By inputNameinsideShadowRoot = By.xpath("//input[@id='BrakeNameInputInShowRoot']");

	public static WebElement getShadowElement(WebDriver driver, WebElement shadowHost, String cssOfShadowElement) {
		WebElement shardowRoot = getShadowRoot(driver, shadowHost);
		return shardowRoot.findElement(By.cssSelector(cssOfShadowElement));
	}

	private static WebElement getShadowRoot(WebDriver driver,WebElement shadowHost) {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		return (WebElement) js.executeScript("return arguments[0].shadowRoot", shadowHost);
	}

	public void registerInterest(Map<String, String> map) throws IOException{		

		setUsername(map.get("user"));
		setEmail(map.get("email"));
		inputNameToIframe(map.get("user"));

	}

	private void setEmail(String val) {
		$(email).sendKeys(val);

	}

	
	private void setUsername(String val) {
		$(name).sendKeys(val);
	}

	private void inputNameToIframe(String val){
		switchFrame($(iframe));
		$(inputNameiniFrame).sendKeys(val);
		//$(shadowRoot).$(inputNameinsideShadowRoot).sendKeys(val);


		WebElement shadowHost = driver.findElement(shadowRoot);
		WebElement shadowRoot = getShadowRoot(driver,shadowHost);
		WebElement shadowElementL1 = shadowRoot.findElement(inputNameInShadowRoot);

		shadowElementL1.sendKeys(val);

		switchToDefaultContent();
	}

}
