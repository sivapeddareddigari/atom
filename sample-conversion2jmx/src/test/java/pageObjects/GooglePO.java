package pageObjects;

import automation.library.selenium.exec.BasePO;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static java.lang.Thread.sleep;

public class GooglePO extends BasePO {

    By googleSearchBar = By.xpath("//*[@id='tsf']/div[2]/div[1]/div[1]/div/div[2]/input");
    By googleImagesTab = By.xpath("//*[@id='hdtb-msb-vis']/div[2]/a");

    public void googleSearch(String searchCriteria) throws InterruptedException {
        driver.findElement(googleSearchBar).sendKeys(searchCriteria);
        sleep(5000);
        driver.findElement(googleSearchBar).sendKeys(Keys.ENTER);
        sleep(5000);
    }

    public void viewPictures(){
        driver.findElement(googleImagesTab).click();
    }

    public void openPicture(int resultNumber) throws InterruptedException {
        driver.findElement(By.cssSelector("div[data-ri='" + (resultNumber-1) + "']")).click();
        sleep(5000);
    }
}
