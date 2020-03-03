package steps;

import automation.library.cucumber.selenium.BaseSteps;
//import cucumber.api.java.en.When;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.testng.Assert;

public class CalculatorSteps extends BaseSteps {

	@When("^the user does a sum correct$")
	public void sum1() throws Throwable {
        getDriver().findElement(By.id("com.android2.calculator3:id/digit_5")).click();
        getDriver().findElement(By.id("com.android2.calculator3:id/op_add")).click();
        getDriver().findElement(By.id("com.android2.calculator3:id/digit_6")).click();
        String actVal = getDriver().findElement(By.id("com.android2.calculator3:id/result")).getText();
        Assert.assertEquals(actVal, "11");
        Thread.sleep(2000);
	}

    @When("^the user does a sum incorrect$")
    public void sum2() throws Throwable {
        getDriver().findElement(By.id("com.android2.calculator3:id/digit_5")).click();
        getDriver().findElement(By.id("com.android2.calculator3:id/op_add")).click();
        getDriver().findElement(By.id("com.android2.calculator3:id/digit_6")).click();
        String actVal = getDriver().findElement(By.id("com.android2.calculator3:id/result")).getText();
        Assert.assertEquals(actVal, "12");
        Thread.sleep(2000);
    }


}	
