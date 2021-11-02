package core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class HerokuTests {

    protected WebDriver driver;

    @AfterMethod
    public void close() {
        getDriver().quit();
    }

    @Test()
    public void runTest(){
        getDriver().get("https://the-internet.herokuapp.com/");
        Assert.assertEquals(getDriver().getTitle(),"The Internet");
        HerokuHomePO po = new HerokuHomePO(getDriver());
        po.pickMenu("Sortable Data Tables");
        Assert.assertEquals(po.getHeading(),"Data Tables");
    }

    protected WebDriver getDriver() {
        if (driver == null) {
            WebDriverManager.chromedriver().version("80.0.3987.106").setup();
            driver = new ChromeDriver();
        }
        return driver;
    }

}
