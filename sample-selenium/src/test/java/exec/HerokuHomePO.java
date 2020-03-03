package exec;

import org.openqa.selenium.By;
import automation.library.selenium.core.Locator;
import automation.library.selenium.exec.BasePO;

public class HerokuHomePO extends BasePO {

    By heading = By.tagName("h3");
    String menu = "//li/a[contains(text(),'%s')]";

    public void pickMenu(String val) {
        $(Locator.Loc.XPATH, menu, val).click();
    }

    public String getHeading(){
        String val = $(heading).getText();
        return val;
    }
}
