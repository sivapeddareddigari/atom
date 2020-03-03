package automation.library.selenium.exec;

import automation.library.common.FileHelper;
import automation.library.common.TestContext;
import automation.library.selenium.core.Element;
import automation.library.selenium.core.Locator;
import automation.library.selenium.exec.driver.factory.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

import static automation.library.selenium.core.Locator.getLocator;

public class BasePO extends automation.library.selenium.core.PageObject {

    public BasePO() {
        super(DriverFactory.getInstance().getDriver());
    }

    public WebDriver getDriver() {
        log.debug("obtaining the driver for current thread");
        return DriverFactory.getInstance().getDriver();
    }

    public WebDriverWait getWait() {
        log.debug("obtaining the wait for current thread");
        return DriverFactory.getInstance().getWait();
    }

    public void quitDriver() {
        log.debug("quitting the driver and removing from current thread of driver factory");
        DriverFactory.getInstance().quit();
    }

    public SoftAssert sa() {
        return TestContext.getInstance().sa();
    }

    public void performDriverOperation(String action, String value) {
        log.debug("performing selenium driver operation:" + action + ";" + value);
        switch (action) {
            case "launch":
                getDriver();
                break;
            case "goto":
                gotoURL(value);
                break;
            case "back":
                getDriver().navigate().back();
                break;
            case "forward":
                getDriver().navigate().forward();
                break;
            case "quit":
                quitDriver();
                break;
        }
    }

    public void performElementOperation(String action, String type, String value, String fieldName, Locator.Loc loc, String loctype, Object... variables) {
        By by = getLocator(loc, loctype, variables);
        performElementOperation(action, type, value, fieldName, by);
    }

    public void performElementOperation(String action, String type, String value, String fieldName, By by) {
        log.debug("performing selenium operation:" + fieldName + ";" + type + ";" + action + ";" + value);

        Element el = findElement(by);
        switch (action) {
            case "sendKeys":
                el.clickable().sendKeys(value);
                break;
            case "click":
                el.clickable().click();
                break;
            case "dropdown":
                switch (type) {
                    case "selectByText":
                        el.clickable().dropdown().selectByVisibleText(value);
                        break;
                    case "selectByIndex":
                        el.clickable().dropdown().selectByIndex(Integer.parseInt(value));
                        break;
                    case "selectByValue":
                        el.clickable().dropdown().selectByValue(value);
                        break;
                }
                break;
            case "assert":
                switch (type) {
                    case "text":
                        sa().assertEquals(el.getText(), value);
                        break;
                    case "selectedOption":
                        sa().assertEquals(el.dropdown().getFirstSelectedOption(), value);
                        break;
                    case "visible":
                        sa().assertEquals(el.element().isDisplayed(), Boolean.parseBoolean(value), "[field: " + fieldName + "; check: visible]");
                        break;
                    case "enabled":
                        sa().assertEquals(el.element().isEnabled(), Boolean.parseBoolean(value), "[field: " + fieldName + "; check: enabled]");
                        break;
                    case "selected":
                        sa().assertEquals(el.element().isSelected(), Boolean.parseBoolean(value), "[field: " + fieldName + "; check: enabled]");
                        break;
                    case "classes":
                        String actClasses = el.getAttribute("class");
                        for (String expectedClass : value.split(" ")) {
                            sa().assertTrue(includesClass(actClasses, expectedClass.trim()), "Element class not found: " + expectedClass.trim());
                        }
                        break;
                    default:
                        sa().assertEquals(el.getAttribute(type), value);
                        break;
                }
                break;
            case "wait":
                switch (type) {
                    case "page":
                        waitPageToLoad();
                        break;
                    case "visible":
                        getWait().until(ExpectedConditions.visibilityOf(el.element()));
                        break;
                    case "clickable":
                        getWait().until(ExpectedConditions.elementToBeClickable(el.element()));
                        break;
                    case "text":
                        getWait().until(ExpectedConditions.textToBePresentInElement(el.element(), value));
                        break;
                }
                break;
        }
    }

    public void performListElementOperation(String action, String type, String value, String fieldName, Locator.Loc loc, String loctype, Object... variables) {
        log.debug("performing selenium operation:" + fieldName + ";" + type + ";" + action + ";" + value);
        By by = getLocator(loc, loctype, variables);
        performListElementOperation(action, type, value, fieldName, by);
    }

    public void performListElementOperation(String action, String type, String value, String fieldName, By by) {
        log.debug("performing selenium operation:" + fieldName + ";" + type + ";" + action + ";" + value);

        List<Element> els = findElements(by);
        switch (action) {
            case "assert":
                switch (type) {
                    case "size":
                        sa().assertEquals(els.size(), Integer.parseInt(value));
                        break;
                }
                break;
        }
    }

    public Boolean includesClass(String actClasses, String expClass) {
        Optional<String> classFindResult = Arrays.stream(actClasses.split(" ")).filter(cl -> cl.equals(expClass)).findFirst();
        if (classFindResult.isPresent()) {
            return true;
        } else {
            return false;
        }
    }


    @Deprecated
    public Map<String, By> getUI(String className) {
        Map<String, By> map = new HashMap<String, By>();
        Class<?> cl;
        try {
            cl = Class.forName(className);
            Constructor<?> con;
            con = cl.getConstructor();
            Object obj;
            obj = con.newInstance();
            log.debug("found uimap:" + className);
            Field[] fields = cl.getFields();

            for (Field f : fields) {
                String name = f.getName().toString();
                if (f.get(obj) instanceof By) {
                    By by = (By) f.get(obj);
                    map.put(name, by);
                    log.debug("found element:" + name);
                }
            }
        } catch (Exception e) {
            log.error("unable to process uimap", e);
            return null;
        }
        return map;
    }

    @Deprecated
    public void checkElement(String fieldname, By by, String propertyList) {
        String[] properties = propertyList.split(",");
        for (String property : properties) {
            String[] state = property.split("=");
            performElementOperation("assert", state[0].trim(), state[1].trim(), fieldname, by);
        }
    }

    public void runKeyword() {
        Map<String, ArrayList<String>> locators = new HashMap<String, ArrayList<String>>();

        int numberActions = 0;

        ArrayList<ArrayList<Object>> scripts = FileHelper.getDataAsArrayList(Constants.TESTCASEPATH, "scripts");

        ArrayList<ArrayList<Object>> tempLocators = FileHelper.getDataAsArrayList(Constants.TESTCASEPATH, "locators");

        //build map of locators
        for (int i = 1; i < tempLocators.size(); i++) {
            ArrayList<String> locator = new ArrayList<String>();

            String locatorGroup = tempLocators.get(i).get(0).toString();
            String locatorField = tempLocators.get(i).get(1).toString();
            String locatorType = tempLocators.get(i).get(2).toString();
            String locatorValue = tempLocators.get(i).get(3).toString();

            locator.add(locatorType);
            locator.add(locatorValue);
            locators.put(locatorGroup + "." + locatorField, locator);
        }

        //loop through all scripts and run those marked execute=y
        for (int i = 1; i < scripts.size(); i++) {
            String scriptName = scripts.get(i).get(0).toString();
            Boolean execute = scripts.get(i).get(1).toString().equalsIgnoreCase("y") ? true : false;

            if (execute) {
                ArrayList<ArrayList<Object>> steps = FileHelper.getDataAsArrayList(Constants.TESTCASEPATH, "steps", scriptName);
                numberActions = steps.size();

                for (int j = 0; j < numberActions; j++) {

                    ArrayList<Object> method = steps.get(j);
                    String object = method.get(1) == null ? null : method.get(1).toString();
                    String field = method.get(2) == null ? null : method.get(2).toString();
                    String action = method.get(3) == null ? null : method.get(3).toString();
                    String type = method.get(4) == null ? null : method.get(4).toString();
                    String dataValue = method.get(5) == null ? null : method.get(5).toString();
                    Object[] locatorValue = method.get(6) == null ? null : method.get(6).toString().split(",");

                    List<String> locator = locators.get(object + "." + field);

                    if (object.equalsIgnoreCase("test")) {
                        if (action.equalsIgnoreCase("verify")) {
                            sa().assertAll();
                        }
                    } else if (object.equalsIgnoreCase("driver")) {
                        if (action.equalsIgnoreCase("launch")) {
                            //DriverFactory.getInstance().setDM();
                            this.driver = DriverFactory.getInstance().getDriver();
                        }
                        performDriverOperation(action, dataValue);
                    } else if (type != null && type.equalsIgnoreCase("size")) {
                        performListElementOperation(action, type, dataValue, field, Locator.Loc.valueOf(locator.get(0)), locator.get(1), locatorValue);
                    } else {
                        performElementOperation(action, type, dataValue, field, Locator.Loc.valueOf(locator.get(0)), locator.get(1), locatorValue);
                    }
                }
            }
            sa().assertAll();
        }
    }
}