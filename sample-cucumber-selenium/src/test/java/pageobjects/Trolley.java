package pageobjects;

import automation.library.selenium.exec.BasePO;
import org.openqa.selenium.By;

import java.io.IOException;

public class Trolley extends BasePO {

	public By viewCart = By.id("com.walmart.android:id/cart_view");

	public void checkCart() throws IOException, InterruptedException {
		$(viewCart).clickable().click();
		Thread.sleep(2000);
	}

}
