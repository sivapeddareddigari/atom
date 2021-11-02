package exec;

import org.openqa.selenium.By;
import automation.library.selenium.core.Element;
import automation.library.selenium.core.Locator;
import automation.library.selenium.exec.BasePO;

import java.util.List;

public class HerokuSortableTablesPO extends BasePO {

    By table = By.id("table1");
    String col = "//*[@id='table1']//tr/td[contains(text(),'%s')]/following-sibling::*[3]";

    public String getCash(String val) {
        String cash = $(Locator.Loc.XPATH, col, val).getText();
        return cash;
    }

    public int getNumberRows() {
        List<Element> rows = $(table).$(By.tagName("tbody")).$$(By.tagName("tr"));
        int count = rows.size();
        return count;
    }
}
