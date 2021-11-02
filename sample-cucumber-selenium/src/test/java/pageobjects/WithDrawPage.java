package pageobjects;

import automation.library.selenium.exec.BasePO;
import org.openqa.selenium.By;

public class WithDrawPage extends BasePO {

    private static final By drpDownAccount = By.id("selectedAccount");
    private static final By txtAmount =  By.id("amount");
    private static final By btnSubmit = By.xpath("//div[@class='card-footer']//button[@type='submit']");


    public void withdraw(String amount) {
        performElementOperation("dropdown", "selectByText", "Joint Checking (Standard Checking)", "selectedAccount", drpDownAccount);
        performElementOperation("sendKeys", "text", amount, "amount", txtAmount);
        performElementOperation("click", "button", "Submit", "Submit", btnSubmit);
    }
}
