package scripts;

import automation.library.selenium.exec.BaseTest;
import org.testng.annotations.BeforeMethod;

public class ConversionBaseTest extends BaseTest {

    @BeforeMethod
    public void startUp() {
        System.out.println("Starting");
    }
}
