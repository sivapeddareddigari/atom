package automation.library.selenium.core;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

public class Locator {

    public static enum Loc{
        CSS, XPATH, ID, NAME, CLASSNAME, TAGNAME, LINKTEXT, PARTIALLINKTEXT, ACCESSIBILITYID;
    }

    /** Returns Selenium Locator based on Locator Type and specified string
     *  Optionally the locator string can contain one or substitution variables
     */
    public static By getLocator(Loc type, String locator, Object...params){
        By by = null;
        switch (type) {
            case CSS: by = By.cssSelector(String.format(locator, params));	break;
            case XPATH: by = By.xpath(String.format(locator, params));	break;
            case ID: by = By.id(String.format(locator, params));	break;
            case NAME: by = By.name(String.format(locator, params));	break;
            case CLASSNAME: by = By.className(String.format(locator, params));	break;
            case TAGNAME: by = By.tagName(String.format(locator, params));	break;
            case LINKTEXT: by = By.linkText(String.format(locator, params));	break;
            case PARTIALLINKTEXT: by = By.partialLinkText(String.format(locator, params));	break;
            case ACCESSIBILITYID: by = MobileBy.AccessibilityId(String.format(locator, params));	break;
        }
        return by;
    }
}
