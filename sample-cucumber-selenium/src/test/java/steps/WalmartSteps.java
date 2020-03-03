package steps;

import automation.library.cucumber.selenium.BaseSteps;
import io.cucumber.java.en.When;
import pageobjects.Navigation;
import pageobjects.Product;
import pageobjects.Trolley;

import java.util.List;

public class WalmartSteps extends BaseSteps {
	




//	@When("^the walmart application is launched$")
//	public void launchApplication() throws Throwable {
//		Navigation nav = new Navigation();
//		nav.launchApp();
//		Thread.sleep(1000);
//	}

	@When("^the customer shops by department$")
	public void startShopping(List<String> value) throws Throwable {
		Navigation nav = new Navigation();
		nav.shopByDepartment(value.get(0).split(" -> "));
	}

	@When("^chooses to purchase$")
	public void buyProduct(List<String> value) throws Throwable {
		Product prod = new Product();
		prod.buyProduct(value.get(0));
	}

	@When("^the shopping cart will show the correct total price$")
	public void checkCart() throws Throwable {
		Trolley trolley = new Trolley();
		trolley.checkCart();
	}
}	
