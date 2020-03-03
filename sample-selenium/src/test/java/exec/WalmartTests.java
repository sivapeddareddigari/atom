package exec;

import automation.library.selenium.exec.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import automation.library.common.TestContext;

import java.io.IOException;
import java.util.Map;

public class WalmartTests extends BaseTest {

    String walmartAndroid = "Walmart.apk::com.walmart.android::com.walmart.android.app.main.MainActivity";
    String calculatorAndroid = "Calculator.apk::com.android2.calculator3::com.xlythe.calculator.material.Calculator";
    String postofficeAndroid = "PostOffice.apk::uk.co.postofficeqa.PostOfficeTravel::uk.co.postofficeqa.PostOfficeTravel.MainActivity";

    @BeforeMethod()


    @Test(dataProvider = "techStackJSON", description = "buy some headphones")
    public void runTest1(Map<String, String> map) throws IOException, InterruptedException {
        String[] target = walmartAndroid.split("::");
        TestContext.getInstance().testdataPut("fw.appName", target[0]);
        TestContext.getInstance().testdataPut("fw.appPackage", target[1]);
        TestContext.getInstance().testdataPut("fw.appActivity", target[2]);
        getDriver();
        Navigation nav = new Navigation();
        nav.launchApp();
        nav.shopByDepartment("Electronics -> Audio -> Headphones -> All Headphones".split(" -> "));
        Product prod = new Product();
        prod.buyProduct("Apple AirPods");
        Trolley trolley = new Trolley();
        trolley.checkCart();
    }

    //@Test(dataProvider = "techStackJSON", description = "buy some headphones")
    public void runTest5(Map<String, String> map) throws IOException, InterruptedException {
        String[] target = postofficeAndroid.split("::");
        TestContext.getInstance().testdataPut("fw.appName", target[0]);
        TestContext.getInstance().testdataPut("fw.appPackage", target[1]);
        TestContext.getInstance().testdataPut("fw.appActivity", target[2]);
        getDriver();
        Navigation nav = new Navigation();
        nav.launchApp();
    }


    //@Test(dataProvider = "techStackJSON", description = "my passing calc test")
    public void runTest2(Map<String, String> map) throws IOException, InterruptedException {
        String[] target = calculatorAndroid.split("::");
        TestContext.getInstance().testdataPut("fw.appName", target[0]);
        TestContext.getInstance().testdataPut("fw.appPackage", target[1]);
        TestContext.getInstance().testdataPut("fw.appActivity", target[2]);
        getDriver().findElement(By.id("com.android2.calculator3:id/digit_5")).click();
        getDriver().findElement(By.id("com.android2.calculator3:id/op_add")).click();
        getDriver().findElement(By.id("com.android2.calculator3:id/digit_6")).click();
        String actVal = getDriver().findElement(By.id("com.android2.calculator3:id/result")).getText();
        Assert.assertEquals(actVal, "11");
        Thread.sleep(2000);
    }

   //@Test(dataProvider = "techStackJSON", description = "my failing calc test")
    public void runTest3(Map<String, String> map) throws IOException, InterruptedException {
        String[] target = calculatorAndroid.split("/");
        TestContext.getInstance().testdataPut("fw.appName", target[0]);
        TestContext.getInstance().testdataPut("fw.appPackage", target[1]);
        TestContext.getInstance().testdataPut("fw.appActivity", target[2]);
        getDriver().findElement(By.id("com.android2.calculator3:id/digit_5")).click();
        getDriver().findElement(By.id("com.android2.calculator3:id/op_add")).click();
        getDriver().findElement(By.id("com.android2.calculator3:id/digit_6")).click();
        String actVal = getDriver().findElement(By.id("com.android2.calculator3:id/result")).getText();
        Assert.assertEquals(actVal, "12");
        Thread.sleep(2000);
    }

//    @Test(dataProvider = "techStackJSON")
    public void runTest4(Map<String, String> map) throws IOException, InterruptedException {
        //PERFORMANCE DATA
        //        List<List<Object>> performanceData = ((AndroidDriver) getDriver()).getPerformanceData(target[0], "batteryinfo", 5);
        //       System.out.println("BATTERY:" + performanceData.get(1).get(0));

        //SCROLL
//        int height = getDriver().manage().window().getSize().getHeight();
//        int width = getDriver().manage().window().getSize().getWidth();
//
//        PointOption po = new PointOption();
//
//        TouchAction ta = new TouchAction<>((AppiumDriver) getDriver());
//        ta.press(PointOption.point(width/2, (int) (height*0.9))).moveTo(PointOption.point(width/2, (int) (height*0.4))).release().perform();
//        Thread.sleep(2000);
        //        ta.press(PointOption.point(width/2, (int) (height*0.4))).moveTo(PointOption.point(width/2, (int) (height*0.9))).release().perform();
        //
        //        //SWIPE
        //
        //        ta.press(PointOption.point(width/2, (int) (height*0.9))).moveTo(PointOption.point(width/2, (int) (height*0.4))).release().perform();
        //        Thread.sleep(2000);
        //        ta.press(PointOption.point(width/2, (int) (height*0.4))).moveTo(PointOption.point(width/2, (int) (height*0.9))).release().perform();


        //HOME BUTTON
//        ((AndroidDriver) getDriver()).pressKey(new KeyEvent(AndroidKey.HOME));

        //
        //            nav.findElement(MobileBy.AccessibilityId("Open navigation drawer")).click();
    }
}
