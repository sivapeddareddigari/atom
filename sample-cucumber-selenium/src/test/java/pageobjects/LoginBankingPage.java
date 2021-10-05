package pageobjects;

import automation.library.cucumber.selenium.BaseSteps;
import automation.library.selenium.exec.BasePO;
import org.openqa.selenium.By;

public class LoginBankingPage extends BasePO {

    private static final By txtUserName = By.id("username");
    private static final By txtPassword = By.id("password");
    private static final By btnSignIn = By.id("submit");
    private static final By btnAdvanced = By.id("details-button");
    private static final By lnkProceedLink = By.id("proceed-link");

    public void login(){
        performDriverOperation("goto", "https://localhost:8443/bank/login");
        performElementOperation("click", "button", "", "Advanced", btnAdvanced);
        performElementOperation("click", "button", "", "Proceed Link", lnkProceedLink);
        performElementOperation("sendKeys", "text", "jsmith@demo.io", "userName", txtUserName );
        performElementOperation("sendKeys", "text", "Demo123!", "userName", txtPassword );
        performElementOperation("click", "button", "", "SignIn", btnSignIn);
    }
}
