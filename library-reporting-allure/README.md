## Reporting Library - Allure

#### 1. Purpose and Contents

The 'reporting-Allure' library can be used in conjunction with the 'cucumber' library and automatically generates output reports when running BDD based cucumber testing.  

The main output is a rich html report (Allure) with test run statistics, summary and detailed test results with drilldowns, and embedded screenshots.  A supplementary text report (suitable for email) with tabular results can also be generated.  

From a test project perspective the report generators are effectively codeless.  The listener / plugin will automatically capture and output the results for the test run and require no additional results capture or results logging within the the test project itself.  

The library contains a small set of java helper classes:

| Class                  | Purpose                                                      |
| -----------------------| ------------------------------------------------------------ |
| AllureScreenShotPub    | set of helper methods to set context for the ExtentReport such as report file path and name |
| AllureScreenShot       | set of helper methods to read information from the ExtentReport |
| TextReport             | helper class that is invoked after test run completion and which reads the cucumber test results from the cucumber-report json and summarises the results in a plain text file that is suitable for outputting to the console/log or attaching to email. |

#### 2. Usage

To generate the html output report within a project using the 'cucumber' library
 
Create ```META-INF.Services``` folder and ```io.qameta.allure.listener.StepLifecycleListener``` file with following line:```automation.library.reporting.AllureScreenShotPub```
```
project/  
|---src/test/java/
|---src/test/resources/  
    |---META-INF/  
        |---Services/  		
            |---io.qameta.allure.listener.StepLifecycleListener
```

 
simply add the following code snippets for @CucumberOptions plugin for Allure report generation:
```
@CucumberOptions(
        plugin = {"io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm"},
        .......
        )
```

To generate supplementary text report (suitable for email) and Allure report add the following code snippets 
for @CucumberOptions plugin and @AfterTest hook to the test projects runner classes:

```
@CucumberOptions(
        plugin = {"io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm","json:RunReports/cucumberJson/cucumber-report.json"},
        .......
        )

        @AfterTest
        public void teardown() {
                TextReport tr = new TextReport();
                tr.createReport(true);

        }
```
*tip: create a base runner class that includes the above code snippets and then simply extend this class from all subsequent runner classes which means the above code is required to be added to the test project only once.*

#### 3. Examples

The *sample-cucumber-selenium-Allure* project for the Allure report generation.