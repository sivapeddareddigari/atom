package automation.library.selenium.core;

/**
 * POJO used to define constants of selenium
 */
public class Constants {

    public static final String BASEPATH = System.getProperty("user.dir") + "/src/test/resources/";
    public static final String SELENIUMRUNTIMEPATH = BASEPATH + "config/selenium/" + "runtime.properties";
    public static final String SELENIUMDRIVERMANAGER = BASEPATH + "config/selenium/" + "driverManager.properties";
}
