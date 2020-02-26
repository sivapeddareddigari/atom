package automation.library.selenium.exec;


import automation.library.common.Property;

/**
 * POJO used to define constants for parallel execution in selenium test
 */
public class Constants extends automation.library.selenium.core.Constants{
    public static final String SELENIUMSTACKSPATH = BASEPATH + "config/selenium/stacks/" + Property.getVariable("cukes.techstack") + ".json";
    public static final String TESTCASEPATH = BASEPATH + "scripts/testcases.xlsx";
}
