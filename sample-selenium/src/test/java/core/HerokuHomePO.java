package core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import automation.library.selenium.core.Locator.Loc;
import automation.library.selenium.core.PageObject;

public class HerokuHomePO extends PageObject {

    By heading = By.tagName("h3");
    String menu = "//li/a[contains(text(),'%s')]";

    public HerokuHomePO(WebDriver driver) {
        super(driver);
    }

    public void pickMenu(String val) {
        $(Loc.XPATH, menu, val).click();
    }

    public String getHeading(){
        String val = $(heading).getText();
        return val;
    }
}
