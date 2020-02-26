## Cucumber Library

#### 1. Purpose and Contents

The 'cucumber' library builds on the API Library and Selenium Library to enable browser/mobile app/api testing using Cucumber BDD.

The helper classes are split into 3 packages 'core', 'api' and 'selenium'.

| Package  | Class                  | Purpose                                                      |
| -------- | -----------------------| ------------------------------------------------------------ |
| Core     | Constants              | List of directory references used by the library classes relative to the test project |
| Core     | Hooks                  | Startup cucumber hook that sets some basic test context information |

| Package  | Class                  | Purpose                                                      |
| -------- | -----------------------| ------------------------------------------------------------ |
| API      | RestAssuredSteps       | Set of cucumber step definitions that invoke various classes/methods from the API Library. These step definitions provide a comprehensive set of gherkin statements for the commonly performed actions when invoking and validating api calls.  This enables largely "codeless" testing of api's with test projects only needing to define API tests in plain language format via cucumber feature files. |
| API      | RunnerClass            | Simple base runner class that sets the initial @CucumberOptions and invoke test via TestNG. This class will be extended by each 'runner' class in the test project used for api testing. |

| Package  | Class                  | Purpose                                                      |
| -------- | -----------------------| ------------------------------------------------------------ |
| Selenium | BaseSteps              | Base class providing access to the driver, wait and BasePO objects from the Selenium Library. This class will be extended by each 'step definition' class in the test project. |
| Selenium | CommonSteps            | Small set of common step definitions used for browser/app testing |
| Selenium | Accessibility          | Helper class to run the accessibility test using AXE library |
| Selenium | Hooks                  | Teardown cucumber hooks that ensure screenshot capture for failed tests.  Screenshots are written to the report directory within the test project. The screenshot file path is also written to the test context (Common Library object) and if the Reporting Library is used by the test project then the screenshot will be automatically embedded in the run report. |
| Selenium | RunnerClass            | Simple base runner class that sets the initial @CucumberOptions and the @Test method that invokes cucumber via TestNG (with data provider). This class will be extended by each 'runner' class in the test project used for browser / app testing. |

#### 2. Usage
#### 2.1 Core 
##### _Constants_

Holds the reference to the list of directories used by library classes relative to test project

- BASEPATH : src/test/resources folder path
- ENVIRONMENTPATH : path for the environment specific config properties file; BASEPATH + config/environments/
- APISTRUCTURE : used by api library for the API structure; BASEPATH + apistructures/

##### _Hooks_

Startup cucumber hook that sets some basic test context information. Sets the feature name and scenario name as test description and sets the cucumber test as true.

#### 2.2 API 
##### _RestAssuredSteps_

Set of cucumber step definitions that invoke various classes/methods from the API Library. These step definitions provide a comprehensive set of gherkin statements for the commonly performed actions when invoking and validating api calls. 
<br/>This enables largely "codeless" testing of api's with test projects only needing to define API tests in plain language format via cucumber feature files.

<br/> Refer API Library documentation for more on the Feature file. 

##### _RunnerClass_
Simple base runner class that sets the initial @CucumberOptions and extends to io.cucumber.testng.AbstractTestNGCucumberTests to invoke the test TestNG.
<br/><br/>This class will be extended by each 'runner' class in the test project used for api testing.
- RunnerClass: extends to AbstractTestNGCucumberTests and runs the test sequentially
- RunnerClassParallel: extends to AbstractTestNGCucumberTests and runs the test in parallel, 10 by default. This value can be updated using below config in TestNG XML file: ```
<suite name="suite1" data-provider-thread-count="2">```

#### 2.3 Selenium 
##### _BaseSteps_
Class which should be extended to the custom step definition class, allow to access the driver and wait objects.
```
public class MyCustomSteps extends BaseSteps {
    public void aMethod(){
        WebDriver driver = getDriver(); 
        WebDriverWait wait = getWait();
    }
}
```

##### _CommonSteps_
Common Step definition class. This has some basic steps defs to launch the open browser, launch rul navigate forward & back and launch mobile application

To open a browser
```
When the browser is opened

step def - 
When("^the browser is opened$",() -> getDriver().manage().window().maximize());
```

To launch a web application
```
When the application "foo"

step def - 
When("^the application \"(.*)\"$",(String app) -> {...});
```
where "foo" references an end point defined in the test project environment properties file (e.g. foo=https://example.com)"

To launch a mobile application - 
```
When the mobile application "foo"

step def - 
When("^the mobile application \"(.*)\"$",(String app) -> {...});
```
where "foo" references an end point ```appName::applicationPackage::applicationMainActivity``` defined in the test project environment properties file 
(e.g. foo=application.apk::com.application.android::com.application.android.app.main.MainActivity)"

To launch a specific url provided in the Feature file
```
When the url "http://example.com"

step def - 
When("^the url \"(.*)\"$", (String url) -> {...});
```

To navigate the browser back & forward
```
When browser is navigates back
When browser is navigated forward
```

##### _AccessibilitySteps_
Wrapper class provides the helper method to run the web accessibility test using AXE library.  To use the methods extend 
the class to custom step definition class. ``` axe.min.js``` should be present in the ``` /srctest/resources``` folder.

##### _Hooks_

Teardown cucumber hooks that ensure screenshot capture for failed tests.  Screenshots are written to the report directory 
within the test project. The screenshot file path is also written to the test context (Common Library object) and if the 
Reporting Library is used by the test project then the screenshot will be automatically embedded in the run report.

Note: There are two choice(libraries) for html report generation - Extent Reports or Allure Reports

Screenshot taken in this class is attached in Extent Report. The screen shot is attached to very last executing step method
for the scenario (either step of the Hook/LambdaGlue). 

For Allure reporting another method is used which is defined in the Allure porting library.

##### _RunnerClass_
Simple base runner class that sets the initial @CucumberOptions and the @Test method that invokes cucumber via TestNG (with data provider for the browser stack details).
<br/><br/>This class will be extended by each 'runner' class in the test project used for browser / app testing.

- RunnerClass/RunnerCLassSequential: Extending these classes will run the test sequentially.
- RunnerClassParallel: Extending this class will run the test in parallel, 10 by default. This value can be updated using
 below config in TestNG XML file: ```<suite name="suite1" data-provider-thread-count="2">```