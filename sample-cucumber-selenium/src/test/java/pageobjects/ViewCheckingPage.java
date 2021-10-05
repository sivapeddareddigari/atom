package pageobjects;

import automation.library.selenium.exec.BasePO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewCheckingPage extends BasePO {
    private static final By transactionContents = By.xpath("//table[@id = 'transactionTable']//tbody/tr[1]/td");

    public HashMap<String, String> getLastTransaction() {
        HashMap<String, String> mapTransactionDetails = new HashMap<>();
        List<WebElement> tdTransactionContents =  getDriver().findElements(transactionContents);
        mapTransactionDetails.put("date", tdTransactionContents.get(0).getText());
        mapTransactionDetails.put("category", tdTransactionContents.get(1).getText());
        mapTransactionDetails.put("description", tdTransactionContents.get(2).getText());
        mapTransactionDetails.put("amount", tdTransactionContents.get(3).getText());
        mapTransactionDetails.put("balance", tdTransactionContents.get(4).getText());

        return mapTransactionDetails;

    }
}
