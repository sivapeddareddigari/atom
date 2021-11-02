package steps;

import io.cucumber.java.en.Given;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import org.testng.Assert;

public class DoNothingSteps {

    @Given("do nothing")
    public void doNothing(){
        //
    }

    @Given("do something")
    public void doSomething(){
        System.out.println("print message to console");
        Allure.step("print message to HTML report");
        Allure.step("this message will be displayed as failed", Status.FAILED);
    }

    @Given("a failing step")
    public void aFailStep(){
        Allure.step("print message to HTML report");
        Assert.assertEquals("2","3");


    }
}
