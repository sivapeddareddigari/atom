package automation.library.cucumber.core;

/**
 * POJO used to define constants
 */
public class Constants  extends automation.library.api.core.Constants{
    public static final String BASEPATH = System.getProperty("user.dir") + "/src/test/resources/";
    public static final String ENVIRONMENTPATH = BASEPATH + "config/environments/";
    public static final String APISTRUCTUREPATH = BASEPATH + "apistructures/";
}
