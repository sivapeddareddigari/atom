package hooks;

import automation.library.common.TestContext;
import io.cucumber.java.After;

public class Hooks {

//    @Before
//    public void startUp(Scenario scenario) throws IOException {
//    }

//    @After(order=40)
//    public void closeDown(Scenario scenario) throws InterruptedException {
//    }

//    @After(order=40)
//    public void closeDown2(Scenario scenario) throws InterruptedException {
//    }

    @After
    public void runSA(){
        TestContext.getInstance().sa().assertAll();
    }
}
