package pageobjects;

import automation.library.selenium.exec.BasePO;
import org.openqa.selenium.By;

public class HomePage extends BasePO {

    private static final By lnkDepositMenu = By.id("deposit-menu-item");
    private static final By lnkWithdrawMenu = By.id("withdraw-menu-item");
    private static final By lnkCheckingMenu = By.id("checking-menu");
    private static final By lnkViewCheckingMenu = By.id("view-checking-menu-item");


    public void navigateToDeposit(){
        performElementOperation("click", "link", "Deposit", "Deposit", lnkDepositMenu);
    }

    public void navigateToWithdraw(){
        performElementOperation("click", "link", "Withdraw", "Withdraw", lnkWithdrawMenu);
    }

    public void navigateToViewJointChecking(){
        performElementOperation("click", "link", "Checking", "Checking", lnkCheckingMenu);
        performElementOperation("click", "link", "ViewChecking", "ViewChecking", lnkViewCheckingMenu);
    }

}
