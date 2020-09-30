package scripts;

import automation.library.conversion2jmx.har2jmx.HarHelper;
import automation.library.conversion2jmx.postman2jmx.app.Postman2Jmx;
import org.testng.annotations.Test;
import pageObjects.GooglePO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class ConversionTests extends ConversionBaseTest {

    @Test(dataProvider = "techStackJSON")
    public void runSelenium2Jmx(Map<String, String> map) throws Exception {
        GooglePO google = new GooglePO();

        getDriver().get("https://www.google.co.uk");

        google.googleSearch("Cute Cats");

        google.viewPictures();

        google.openPicture(8);
        google.openPicture(17);
    }

//    @Test(dataProvider = "techStackJSON")
    public void runHar2Jmx(Map<String, String> map) throws Exception {
//        HarHelper.getInstance().createJmx(new ArrayList<>(Arrays.asList("foo.har", "bar.har")));
        HarHelper.getInstance().createJmx(new ArrayList<>(Arrays.asList("images.har")));
    }

    //@Test(dataProvider = "techStackJSON")
    public void runPostman2Jmx(Map<String, String> map) throws Exception {
        Postman2Jmx postman2Jmx = new Postman2Jmx();
        //postman2Jmx.convertAllPostmanScripts();
        postman2Jmx.convertSpecificPostmanScripts(new ArrayList<>(Arrays.asList("foo.json", "bar.json")));
    }
}
