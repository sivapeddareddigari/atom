package exec;

import automation.library.selenium.exec.BasePO;
import automation.library.selenium.exec.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HerokuTests extends BaseTest {

    String app = "https://the-internet.herokuapp.com/";

    @Test(dataProvider = "techStackJSON")
    public void runTest1(Map<String, String> map) throws IOException {
        getDriver().get(app);
        Assert.assertEquals(getDriver().getTitle(), "The Internet");
        HerokuHomePO po = new HerokuHomePO();
        po.pickMenu("Sortable Data Tables");
        Assert.assertEquals(po.getHeading(), "Data Tables");
    }

    @Test(dataProvider = "techStackJSON")
    public void runTest2(Map<String, String> map) throws IOException {
        getDriver().get(app);
        HerokuHomePO menu = new HerokuHomePO();
        String pick = "Sortable Data Tables";
        menu.pickMenu(pick);

        Map<String, String> values = new HashMap<String, String>();
        values.put("Conway", "$50.00");
        values.put("Smith", "$50.00");
        values.put("Doe", "$100.00");
        values.put("Bach", "$51.00");

        HerokuSortableTablesPO tab = new HerokuSortableTablesPO();
        sa().assertEquals(tab.getNumberRows(), values.size());

        for (Map.Entry<String, String> entry : values.entrySet()) {
            sa().assertEquals(tab.getCash(entry.getKey()), entry.getValue(), "Checking row:" + entry.getKey());
        }
        sa().assertAll();
    }

    @Test(dataProvider = "techStackExcel")
    public void runTest3(Map<String, String> map) throws IOException {
        BasePO po = new BasePO();
        po.runKeyword();
    }

}
