package steps;

import automation.library.cucumber.selenium.BaseSteps;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pageobjects.*;

import java.util.HashMap;

public class DigitalBankSteps extends BaseSteps {

    @Given("I login to the DigitalBanking application")
    public void loginToDigitalBanking() throws Throwable {
        LoginBankingPage loginPage = new LoginBankingPage();
        loginPage.login();
    }


    @Then("^I must be able to see \"(.*)\" of \"(.*)\" in the account$")
    public void checkBalance(String action, String amount){
        HomePage homePage = new HomePage();
        ViewCheckingPage viewCheckingPage = new ViewCheckingPage();
        homePage.navigateToViewJointChecking();
        HashMap<String, String> map = viewCheckingPage.getLastTransaction();

        if(action.equalsIgnoreCase("withdrawal")) {
            Assert.assertTrue(map.get("description").contains("Withdrawl"), "Transaction must be of type Withdrawal");
        }else if(action.equalsIgnoreCase("deposit")){
            Assert.assertTrue(map.get("description").contains("Deposit"), "Transaction must be of type Deposit");
        }

        Assert.assertTrue(map.get("amount").contains(amount), "Amount is "+amount);
    }

    @When("I {string} {string} into the checking account")
    public void depositWithdraw(String action, String amount) {
        HomePage home = new HomePage();
        DepositPage depositPage = new DepositPage();
        WithDrawPage withDrawPage = new WithDrawPage();
        if(action.trim().equalsIgnoreCase("deposit")){
            home.navigateToDeposit();

            depositPage.deposit(amount);
        }else if(action.trim().equalsIgnoreCase("withdraw")){
            home.navigateToWithdraw();
            withDrawPage.withdraw(amount);
        }
    }
}
