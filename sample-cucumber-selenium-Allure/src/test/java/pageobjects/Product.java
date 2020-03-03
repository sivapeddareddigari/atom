package pageobjects;

import automation.library.selenium.core.Element;
import automation.library.selenium.exec.BasePO;
import org.openqa.selenium.By;

import java.io.IOException;
import java.util.List;

public class Product extends BasePO {

	public By product = By.id("com.walmart.android:id/shelf_item_view_title");
	public By addToCart = By.id("com.walmart.android:id/add_to_cart_button_button");

	public void buyProduct(String product) throws IOException, InterruptedException {
		Thread.sleep(2000);
		List<Element> list = $$(this.product);
		for (Element me : list){
			System.out.println(me.getText());
			if (me.getText().contains(product)) {
				me.click();
				break;
			}
		}
		$(addToCart).clickable().click();

		Thread.sleep(2000);
	}

}
