package exec;

import automation.library.selenium.core.Element;
import automation.library.selenium.exec.BasePO;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import java.io.IOException;
import java.util.List;

public class Navigation extends BasePO {

    public By navDrawer = MobileBy.AccessibilityId("Open navigation drawer");
	public By splashBtn = By.id("android:id/button1");
	public By shopDepartment = By.id("com.walmart.android:id/design_menu_item_text");
	public By department = By.id("com.walmart.android:id/taxonomy_list_entry_title");

	public void launchApp() throws IOException, InterruptedException {
		$(splashBtn).clickable().click();
		Thread.sleep(1000);
	}

	public void shopByDepartment(String...departments) throws IOException, InterruptedException {
		$(navDrawer).clickable().click();

		Thread.sleep(2000);
		List<Element> list = $$(shopDepartment);

		for (Element me : list){
			System.out.println(me.getText());
			if (me.getText().contains("Shop by Department")) {
				me.click();
				break;
			}
		}

		for (String department : departments){
			Thread.sleep(2000);
			list = $$(this.department);

			for (Element me : list){
				System.out.println(me.getText());
				if (me.getText().equalsIgnoreCase(department)) {
					me.click();
					break;
				}
			}
		}

	}
}
