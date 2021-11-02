package steps;

import automation.library.reporting.Reporter;
import io.cucumber.java.en.Given;

public class DoNothingSteps {

    @Given("do nothing")
    public void doNothing(){
        //
    }

    @Given("do something")
    public void doSomething(){
        System.out.println("print message to console");
        Reporter.addStepLog("print message to HTML Report");
    }
}
