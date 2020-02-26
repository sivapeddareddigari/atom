## Selenium Library

#### 1. Purpose and Contents

The 'selenium' library contains a set of java helper classes that support browser and mobile app testing by leveraging Selenium and Appium.

The helper classes are split into 2 packages 'core' and 'exec'.

The 'core' package includes a set of helper classes and methods that wrap around some of the Selenium objects (WebElement, WebDriverWait, Select, Actions) providing the most commonly used selenium functions whilst resolving some of the issues typically encountered in test projects (e.g. timeouts and stale element exceptions).

The 'exec' package includes a driver factory which manages the driver lifecycle, parallel test execution (multi-threaded) and the webdriver *desired capabilities* and *driver options*.  The factory supports:

- multi-browser testing (chrome, firefox, internet explorer, edge, safari, htmlunit, phantomJS)
- mobile app testing (android,  iOS, windows)
- local execution, selenium grid execution, cloud execution (saucelabs / browserstack)

The factory enables parallel execution in 2 dimensions of 'tests' x 'tech stack'.  For example running 3 tests against a tech stack of 'chrome+linux' and 'internet explorer+windows' would generate 6 parallel threads of execution for accelerated test execution times.

Parallel execution within a test project requires simple configuration files only and multiple configurations can be pre-defined and called upon at runtime.

Local, grid, cloud based execution within a test project requires simple configuration files only and multiple configurations can be pre-defined and called upon at runtime.

Test projects can choose to make use of the 'core' package only and manage their own driver lifecycles, however, even if a project is not planning to run parallel testing, it is strongly recommended that projects re-use the driver factory provided by the library as it greatly simplifies the test project code. 

It is also highly recommended that a test project adopts the page object pattern.  As detailed in the table below a BasePO class is included in the 'exec' package that will be the super class for each page object in the test project.

##### 1.1 Package: Core
 

| Class                     | Purpose                                                      |
| ------------------------- | ------------------------------------------------------------ |
| Constants                 | list of directory references used by the library classes relative to the test project |
| Element                   | provides a set of methods commonly used for finding and interacting with individual elements on a browser or mobile app.  When finding elements automatic waits are performed for element availability, and scroll to element is performed before performing any selenium/appium action.  In addition automatic retries for stale element exceptions. |
| Locator                   | provides enum + method to derive dynamic element locators    |
| Page Object               | provides a set of page level methods that generally return the Element class when performing find operations.  Also includes methods that perform page level waits for DOM loading, JQuery and Angular readiness. |
| Screenshot                | provides helper methods for taking screenshots and writing to file |

##### 1.2 Package: Exec

|SubPackage| Class                    | Purpose                                                      |
|----------| -------------------------| ------------------------------------------------------------ |
| factory  | DriverFactory            | Factory class that manages the webdriver lifecycle based on test project configuration files |
| factory  | Capabilities             | Sets up the base desired capabilities object with common values for re-use across all the driver managers |
| factory  | DriverContext            | Threadlocal object holding tech stack information for each running thread such as the target browser name, platform, operating system, version etc |
| factory  | DriverManager            | Abstract class extended by each driver manager.  Each driver manager instantiates the driver and sets specific options and desired capabilities. |
| manager  | ChromeDriverManager      | Instantiates chrome driver for local execution and sets any chrome specific options defined in the test project configuration |
| manager  | FirefoxDriverManager     | Instantiates firefox driver for local execution and sets any firefox specific options defined in the test project configuration |
| manager  | IEDriverManager          | Instantiates IE driver for local execution and sets any IE specific options defined in the test project configuration |
| manager  | EdgeDriverManager        | Instantiates Edge driver for local execution and sets any Edge specific options defined in the test project configuration |
| manager  | SafariDriverManager      | Instantiates Safari driver for local execution and sets any Safari specific options defined in the test project configuration |
| manager  | HTMLUnitDriverManager    | Instantiates HTMLUnit driver for local execution             |
| manager  | PhantomJSDriverManager   | Instantiates PhantomJS driver for local execution            |
| manager  | GridDriverManager        | Instantiates driver for remote grid execution (either AndroidDriver, iOSDriver or  RemoteWebDriver instance depending on whether the test is targeting a mobile app or browser).  Reads the grid connection settings from either environment variables or system properties. |
| manager  | SaucelabsDriverManager   | Instantiates driver for remote execution (either AndroidDriver, iOSDriver or  RemoteWebDriver instance depending on whether the test is targeting a mobile app or browser).  Reads the saucelabs connection settings from either environment variables or system properties.  Also updates saucelabs with test results. |
| manager  | BrowserStackDriverManager| Instantiates driver for remote execution (either AndroidDriver, iOSDriver or  RemoteWebDriver instance depending on whether the test is targeting a mobile app or browser).  Reads the browserstack connection settings from either environment variables or system properties. |
| manager  | AppiumDriverManager      | Instantiates driver for execution against a local appium server (either AndroidDriver, iOSDriver or WindowsDriver as required for the test) |
|          | BasePO                   | Extends the PageObject class from 'core' package adding additional methods that access the thread specific driver from the driver factory.<br/><br/>This is the base class that should be extended by each page object class in the test project. |
|          | BaseTest                 | Includes startup and teardown hooks to set test context information, and shut down web drivers on text completion. <br/><br/>Includes TestNG data providers that will read the 'tech stack' for a given test run and enable parallel execution.  Tech stack can be defined in either .json or .xls format.<br/><br/>This is the base class that should be extended by each test class (containing @Test annotated methods) in the test project. |

#### 2. Usage

##### Test project page object classes should extend the BasePO class from the library:

```
public class MyPageObjec extends BasePO {
...
}
```



##### For any given page object the following operations will then be available:

- obtain the driver instance from the factory for the current thread:

```
getDriver()
```

- obtain a default selenium wait object for the driver for the current thread:

```
getWait()
```

- navigate to a given url:

```
gotoURL(<url>);
```

- switch driver to another window:

```
switchWindow(<currentwindowhandle>);
```

- switch driver to another frame:

```
switchFrame(<frameLocator>);
or
switchFrame(<By>);
or
switchFrame(<Element>);
or
switchToDefaultContent();
```

- grab a screenshot:

```
grabScreenshot();
```

*will either grab standard screenshot or scrolling based on value of scrollingScreenshot property within the test project configuration - see further down for details*

- wait for page loading (DOM, JQuery, Angular):

```
waitPageToLoad();
```

- find web elements and return library Element object or list of Element objects based on By locators:

```
Element el = $(<locator>);						//find first matching element
or
List<Element> els = $$(<locator>);				//find all matching elements
or
Element el = $(<parentlocator>, <sublocator>);	 //find a nested web element 	
```

- find web elements and return library Element object or list of Element objects based on a custom selenium wait condition:

```
Element el = $(<ExpectedCondition>);			//find first element for custom condition
or
List<Element> els = $(<ExpectedCondition>);		//find all elements for custom condition
```

- find web elements using a locator type (from enum), string locator value and optional tokens to substitute into the locator (this is useful for dynamic locators): 

```
Element el = findElement(<loctype>, <stringlocator>, <tokens>)
or
List<Element> els = $$(<loctype>, <stringlocator>, <tokens>)	

where loctype is a enum with values (CSS, XPATH, ID, NAME, CLASSNAME, TAGNAME, LINKTEXT, PARTIALLINKTEXT, ACCESSIBILITYID;)
```



*Note: in the above examples the \$ and \$$ methods of the BasePO are used.  These are also available in longer hand as findElement() and findElements() respectively.



##### For any given Element object the following operations will be available:

- wait for the element to become clickable:

```
el.clickable();
```

- get the currently displayed text (innertext):

```
el.getText();
```

- get the currently displayed value of any input type field:

```
el.getValue();
```

- get the value of any element attribute:

```
el.getAttribute(<attributename>);
```

- clear the current value of an element:

```
el.clear();
```

- populate an element value using standard selenium event:

```
el.sendKeys(<value>);
```

- populate an element value using javascript:

```
el.sendKeysJS(<value>);
```

- click an element using standard selenium event:

```
el.click();
```

- click an element using javascript :

```
el.clickJS();
```

- send a keys combination to an element:

```
el.click();
```

- send a keys combination to an element:

```
el.click();
```

- send a keys combination to an element:

```
el.sendKeysChord(<stringVal>);
or 
el.sendKeysChord(Keys.<key>);
```

- hit the return/enter key on keyboard

```
el.sendEnter();
```

- set a radio button or checkbox as checked:

```
el.select();
```

- deselect a radio button or checkbox:

```
el.deselect();
```

- set a radio button or checkbox as selected or deselected based on specified true/false value:

```
el.select(<booleanValue>);
```

- get a dropdown object for an element:

```
Select sel = el.dropdown();
```

- get all available options within a dropdown:

```
List<string> = el.getDropdownOptionsText();				//get the visible text of the options
or
List<string> = el.getDropdownOptionsValues();			//get the values of the options
```

- get all available option groups within a dropdown:

```
List<String> groups = el.getDropdownOptGroupsText();
or
List<WebElement> groups = getDropdownOptGroupsElements();
```

- get all the available options within an option group of a dropdown:

```
List<String> options = getDropdownOptionsTextWithin(<groupname>);
or
List<WebElement> options = getDropdownOptionsElementsWithinGroup(<groupname>);
```

- get the inner text for list of elements:

```
List<String> list = getAllText(<ElementList>);
```

- perform mouse action to move to a given element:

```
el.move();
```

- perform mouse action to move to a given element and click();

```
el.moveAndClick();
```

- perform mouse action to move to a given element, locate its sub element and click();

```
el.moveAndClick(<subElement>);
```

- perform mouse action to move to a given element and click and hold;

```
el.clickAndHold();
```

- perform mouse action to release a held click:

```
el.release();
```

- highlight an element with a blue border (useful for screenshot capture):

```
element.highlight();
```

- perform a scroll to a given element:

```
el.scroll();
```

- a number of methods can be used on an element that represents a table:

```
int count = el.getHeadRowCount();						//count of header rows for a table
int count = el.getDataRowCount();						//count of data rows for a table
int count = el.getHeadColumnCount(<rowIndex>);			 //count of columns in a header row
int count = el.getDataColumnCount(<rowIndex>);			 //count of columns in a data row
List<Element> cols = getHeadRowElements(<rowIndex>);	 //get columns in a header row
List<Element> cols = getDataRowElements(<rowIndex>);	 //get columns in a data row
List<Element> rows = getAllRows();					    //get all rows in a table
Element row = getRow(<rowIndex>);						//get a specific row in a table
ArrayList<ArrayList<String>> data = el.getTableAsArray(); //get all cell values in a table
```

##### Command Chaining

The majority of Element methods return back the element that was being operated on and therefore the commands can be chained.  For example:

```
By myField = By.id("foo");
$(myField).clickable().clear().sendKeys("bar");
```

##### StaleElement Exceptions

The majority of Element methods catch stale element exceptions that can occasionally occur with normal selenium operations (e.g. page continues rendering after element initially located causing element to go stale).  Where a stale element exception is caught then the Element methods will retry the operation and therefore help mitigate the 'flaky tests' and avoid testers putting hard waits into test code.  The max number of retries is defined in the test project configuration files

##### Getting the underlying Selenium WebElement

In the majority of cases the methods available in the Element class will be sufficient for browser and mobile app testing, however, it is possible to obtain the underlying a Selenium WebElement object or an Appium MobileElement object as follows:

```
el.element();					//obtain a selenium WebElement for the Element object 
el.mobElement();				//obtain an appium MobileElement for the Element object 
```

##### Project Structure

Any package structure can be used for the test projects java code, however, the library does look for some configuration files in a standard path within the test project:

- One or more test suite xml files can optionally be included in folder *src/test/resources/config/testsuites/*
- One or more stacks json files should be located in folder  *src/test/resources/config/selenium/stacks/*

- A runtime properties file should be located in folder *src/test/resources/config/*

Each of the configuration files is described below.

##### Test Suites

Test projects can define one or more suites using the standard functionality of TestNG which provides the execution engine used by the library code.

These suites allow tests to be grouped, the order of test execution to be defined and sequential / parallel execution of tests to be performed.  

In the following example suite file, all tests (aka methods annotated with @Test) in the classes 'myFirstClass', 'mySecondClass', 'myThirdClass' will be executed.  

The tests in 'myFirstClass' will run first and following their completion the tests in 'mySecondClass' and 'myThirdClass' will then be executed in parallel (as determined by the parallel="classes" attribute)

```
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="MySuite" parallel="classes" thread-count="10" data-provider-thread-count="10">
    <test name="Test1">
        <classes>
            <class name="myFirstClass"/>
        </classes>
    </test>
    <test name="Test2">
        <classes>
           <class name="mySecondClass"/>
           <class name="myThirdClass"/>
        </classes>
    </test>
</suite>
```

The standard TestNG documentation provides full details of the various options available when defining test suites and defining parallel execution at the test/class/method levels.

##### Runtime.properties

The *runtime.properties* file allows some basic project level parameters to be set:

- wait time to be used when locating web elements
- screenshots to use scrolling screenshot or standard screenshot
- delay before taking a screenshot

The runtime.properties also allows project level desired capabilities or options to be set using the format:

desiredcapabilities.\<browsertype>=\<value to be applied>

options.\<browsertype>=\<value to be applied>

Any number of desired capabilities or options can be included.

An example properties file is shown below:

```
######Waits######
defaultWait=10
scrollingScreenshot=false
screenshotDelay=2

######JavaScript Actions######
clickUsesJavaScript.internetexplorer=false
sendKeysUsesJavaScript.internetexplorer=false

######Desired Capabilities######
desiredCapabilities.internetexplorer=ignoreProtectedModeSettings==true
desiredCapabilities.internetexplorer=ignoreZoomSetting==true
desiredCapabilities.internetexplorer=requireWindowFocus==false

######Driver Options######
options.chrome=--start-maximized
```



##### Stacks.json

The library *BaseTest* class includes TestNG data provider methods to support running tests against different tech stacks, aka the combination of browser/app, platform, os version.  

The tech stacks are pre-defined in one or more json files held in *src/test/resources/config/selenium/stacks/*. 

For any given execution run the choice of tech stack to be used is specified as a system runtime parameter (-Dtechstack=<filename excluding .json extension>).  This tells the library code which json file to read and the required stack(s) to apply.

Each stack file can define one or more stack combinations (in its json array).  For a given test run if multiple stacks are applied then the library code will execute the tests against the various stacks in parallel, significantly reducing end-to-end execution time.

The stack files also define:

- whether to run the tests locally, against a grid or via a cloud based service such as saucelabs
- the desired capabilities to be applied when setting up each selenium or appium webdriver instance

The stack files have the following format:

```
[{
	"description": "<some description>",
	"seleniumServer":"<target server type>",
	"<desired capability key>":"<desired capability value>"
	...
	"<desired capability key>":"<desired capability value>"
},{
	"description": "<some description>",
	"seleniumServer":"<target server type>",
	"<desired capability key>":"<desired capability value>"
	...
	"<desired capability key>":"<desired capability value>"
}]

```



- the "description" field is arbitrary and is used by the library code for reporting purposes only

- the "seleniumServer" field is used by the library code to set the target selenium/appium server type and must be one of:   local | appium | grid | saucelabs | browserstack

- the remaining fields are then used by the library code to set up the DesiredCapabilities of the selenium or appium webdriver instance with each key / value pair from the stacks json being applied literally.  

  For example:

  - an entry in the json file:   "deviceName": "Samsung Galaxy S9"
  - is equivalent to:	

  ​	DesiredCapabilities cap = new DesiredCapabilities();

  ​	cap.setCapability("deviceName", "Samsung Galaxy S9");



To illustrate this some sample stack files are shown below:  

- browser tests being executed locally against chrome:

```
[{
	"description": "local-CH",
	"seleniumServer":"local",
	"browserName":"chrome"
}]


```

- browser tests being executed locally against chrome + firefox in parallel:

```
[{
	"description": "local-CH",
	"seleniumServer":"local",
	"browserName":"chrome"
},{
	"description": "local-FF",
	"seleniumServer":"local",
	"browserName":"firefox"
}]


```

- browser tests being executed against a selenium grid in parallel using 3 different tech stacks:

```
[{
    "description": "grid-CH68-Windows",
    "seleniumServer": "grid",
    "browserName": "chrome",
    "version": "68",
    "platform": "WINDOWS"
  },{
    "description": "grid-CH48-Linux",
    "seleniumServer": "grid",
    "browserName": "chrome",
    "version": "48",
    "platform": "LINUX"
  },{
    "description": "grid-IE11-Windows",
    "seleniumServer": "grid",
    "browserName": "internet explorer",
    "version": "11",
    "platform": "WINDOWS"
}]


```

- browser tests being executed against saucelabs in parallel using 2 different tech stacks:

```
[{
	"description": "saucelabs-FF54-Win10",
	"seleniumServer":"saucelabs",
	"browserName":"firefox",
	"version":"54",
	"platform":"Windows 10"
},{
	"description": "saucelabs-CH48-Linux",
	"seleniumServer":"saucelabs",
	"browserName":"chrome",
	"version":"48",
	"platform":"Linux"
}]


```

- mobile app tests being executed in parallel against a locally running appium server:

```
[{
    "description": "Samsung S9 on Android Pie",
    "seleniumServer": "appium",
    "deviceName": "Samsung Galaxy S9",
    "udid": "192.168.106.101:5555",
    "platformName": "Android",
    "platformVersion": "9.0",
    "deviceOrientation": "portrait",
    "skipUnlock": "true",
    "noReset": "true",
    "systemPort": "8202"
  },{
    "description": "Pixel 3 on Android Pie",
    "seleniumServer": "appium",
    "deviceName": "Google Pixel 3",
    "udid": "192.168.106.102:5555",
    "platformName": "Android",
    "platformVersion": "9.0",
    "deviceOrientation": "portrait",
    "skipUnlock": "true",
    "noReset": "true",
    "systemPort": "8201"
  }]


```

- mobile app tests being executed in parallel against a selenium grid with appium nodes:

```
[{
    "description": "Samsung S9 on Android Pie",
    "seleniumServer": "grid",
    "deviceName": "Samsung Galaxy S9",
    "udid": "192.168.106.101:5555",
    "platformName": "Android",
    "platformVersion": "9.0",
    "automationName": "uiautomator2",
    "systemPort": "8201"
  },{
    "description": "Pixel 3 on Android Pie",
    "seleniumServer": "grid",
    "deviceName": "Google Pixel 3",
    "udid": "192.168.106.102:5555",
    "platformName": "Android",
    "platformVersion": "9.0",
    "automationName": "uiautomator2",
    "systemPort": "8202"
}]


```

- mobile app tests being executed in parallel against saucelabs:

```
[{
    "description": "Samsung S9 on Android Pie",
    "seleniumServer": "saucelabs",
    "browserName": "",
    "appiumVersion": "1.9.1",
    "deviceName": "Samsung Galaxy S9 HD GoogleAPI Emulator",
    "deviceOrientation": "portrait",
    "platformVersion": "8.1",
    "platformName": "Android"
  },{
    "description": "Pixel 3 on Android Pie",
    "seleniumServer": "saucelabs",
    "browserName": "",
    "appiumVersion": "1.9.1",
    "deviceName": "Google Pixel GoogleAPI Emulator",
    "deviceOrientation": "portrait",
    "platformVersion": "8.1",
    "platformName": "Android"
  }]
```



##### Logging

The library classes use Log4J to capture log messages. Both the BaseTest and the BasePO classes declare a log4J logger and therefore any classes that extend these within the test project can also optionally write to the same log files.  For example:

```
log.debug("doing some tests");
```

To enable logging the test project just needs to include a log4j2.xml file within the */src/test/resources* folder of the test project.  An example file is shown below:

```
<?xml version="1.0" encoding="UTF-8"?>
<configuration status="WARN">
    <appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>

        <File name="MyFile" fileName="logs/testrun.log">
            <PatternLayout pattern="%d{yyyy-mm-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </File>
    </appenders>

    <loggers>
        <root level="info">
            <appender-ref ref="Console" level="info"/>
            <appender-ref ref="MyFile" level="info"/>
        </root>
    </loggers>
</configuration>
```



##### WebDriver Drivers for Local Selenium Execution

Test projects can manually download the drivers required by Selenium (e.g. IEDriverServer, GeckoDriver, FirefoxDriver) and add these into the test project to make them available to anyone running the tests locally**.

By default the library code will look within the */lib/drivers/windows/* or */lib/drivers/linux/* folders at the root of the test project to find the driver executable.  At runtime the library code determines which folder to look in based on the operating system that it is currently running on so test projects can pre-setup the drivers needed for either environment. 

Alternatively the drivers can be simply downloaded at runtime by the library's driver factory (which uses the WebDriverManager java library).  This is achieved by setting a runtime parameter:

*-Dwebdrivermanager=true*

This will result in the latest available drivers being used.  To download and use a specific driver version then additional parameters can be set:

*-DchromeDriver=<version number>*					

*-DieDriver=*<version number>


***when running on a grid then the drivers should be installed on the grid nodes as part of the grid setup; when running against a cloud service such as saucelabs or browserstack then the required drivers will be made available on those services automatically*



#### 3. Sample Test Code - Browser Test (page objects)

The following example shows a very simple browser test example.

Page Objects extending BasePO of the library:

```
public class HerokuHomePO extends BasePO {
    By heading = By.tagName("h3");
    String menu = "//li/a[contains(text(),'%s')]";

    public void pickMenu(String val) {
        $(Locator.Loc.XPATH, menu, val).click();
    }

    public String getHeading(){
        String val = $(heading).getText();
        return val;
    }
}
```

```
public class HerokuSortableTablesPO extends BasePO {
    By table = By.id("table1");
    String col = "//*[@id='table1']//tr/td[contains(text(),'%s')]/following-sibling::*[3]";

    public String getCash(String val) {
        String cash = $(Locator.Loc.XPATH, col, val).getText();
        return cash;
    }

    public int getNumberRows() {
        List<Element> rows = $(table).$(By.tagName("tbody")).$$(By.tagName("tr"));
        int count = rows.size();
        return count;
    }
}
```

Test class extending BaseTest of the library:

```
public class HerokuTests extends BaseTest {
    String app = "https://the-internet.herokuapp.com/";

    @Test(dataProvider = "techStackJSON")
    public void runTest1(Map<String, String> map) throws IOException {
        getDriver().get(app);
        Assert.assertEquals(getDriver().getTitle(),"The Internet");
        HerokuHomePO po = new HerokuHomePO();
        po.pickMenu("Sortable Data Tables");
        Assert.assertEquals(po.getHeading(),"Data Tables");
    }

    @Test(dataProvider = "techStackJSON")
    public void runTest2(Map<String, String> map) throws IOException {
        getDriver().get(app);
        HerokuHomePO menu = new HerokuHomePO();
        String pick = "Sortable Data Tables";
        menu.pickMenu(pick);

        Map<String, String> values = new HashMap<String, String>();
        values.put("Conway", "$50.00");
        values.put("Smith", "$50.00");
        values.put("Doe", "$100.00");
        values.put("Bach", "$51.00");

        HerokuSortableTablesPO tab = new HerokuSortableTablesPO();
        sa().assertEquals(tab.getNumberRows(),values.size());

        for (Map.Entry<String, String> entry : values.entrySet()) {
            sa().assertEquals(tab.getCash(entry.getKey()), entry.getValue(), "Checking row:"+entry.getKey());
        }
        sa().assertAll();
    }
}
```



#### 4. Sample Test Code - Mobile App (page objects)

The following example shows a very simple mobile app test example based on the Walmart shopping app.

##### Page Objects extending BasePO of the library:

```
public class Navigation extends BasePO {
    public By navDrawer = MobileBy.AccessibilityId("Open navigation drawer");
	public By splashBtn = By.id("android:id/button1");
	public By shopDepartment = By.id("com.walmart.android:id/design_menu_item_text");
	public By department = By.id("com.walmart.android:id/taxonomy_list_entry_title");

	public void launchApp() throws IOException {
		$(splashBtn).clickable().click();
	}

	public void shopByDepartment(String...departments) throws IOException {
		$(navDrawer).clickable().click();
		List<Element> list = $$(shopDepartment);

		for (Element me : list){
			System.out.println(me.getText());
			if (me.getText().contains("Shop by Department")) {
				me.click();
				break;
			}
		}

		for (String department : departments){
			list = $$(this.department);
			for (Element me : list){
				if (me.getText().equalsIgnoreCase(department)) {
					me.click();
					break;
				}
			}
		}
	}
}
```

```
public class Product extends BasePO {

	public By product = By.id("com.walmart.android:id/shelf_item_view_title");
	public By addToCart = By.id("com.walmart.android:id/add_to_cart_button_button");

	public void buyProduct(String product) throws IOException {
		List<Element> list = $$(this.product);
		for (Element me : list){
			System.out.println(me.getText());
			if (me.getText().contains(product)) {
				me.click();
				break;
			}
		}
		$(addToCart).clickable().click();
	}
}
```

```
public class Trolley extends BasePO {
	public By viewCart = By.id("com.walmart.android:id/cart_view");

	public void checkCart() throws IOException {
		$(viewCart).clickable().click();
	}
}
```

##### Test class extending BaseTest of the library:

```
public class WalmartTests extends BaseTest {
String walmartAndroid = "Walmart.apk::com.walmart.android::com.walmart.android.app.main.MainActivity";

@BeforeMethod()
public void runTest1()
{
    //set up context variables that will be used by driver factory
    String[] target = walmartAndroid.split("::");
    TestContext.getInstance().putFwSpecificData("fw.appName", target[0]);
    TestContext.getInstance().putFwSpecificData("fw.appPackage", target[1]);
    TestContext.getInstance().putFwSpecificData("fw.appActivity", target[2]);
}

@Test(dataProvider = "techStackJSON", description = "buy some headphones")
public void runTest1(Map<String, String> map) throws IOException {
    Navigation nav = new Navigation();
    nav.launchApp();
    nav.shopByDepartment("Electronics -> Audio -> Headphones -> All Headphones".split(" -> "));
    Product prod = new Product();
    prod.buyProduct("Apple AirPods");
    Trolley trolley = new Trolley();
    trolley.checkCart();
}
}
```



#### 5. Sample Test Code - Browser Test (keyword)

For simple browser tests and as an alternative to page object based coding this library also supports keyword based execution.  

Keyword test scripts can be setup in excel to leverage the in-built selenium methods of the library.

The excel scripts should be placed in the *src/test/resources/scripts/* folder of the test project.

The excel scripts are made up of 4 tabs:

| tab      | description                                                  |
| -------- | ------------------------------------------------------------ |
| scripts  | holds the list of scripts and allows execution of each script to be toggled on/off |
| locators | holds the reference list of browser page elements and the selenium By locators |
| steps    | holds the scripts and their individual steps with the selenium actions to be performed, locator references and data values |
| browsers | holds the tech stacks to be used for the execution           |

The diagram below shows an example of the steps tab and the locators tab.  The steps tab details 3 scripts each with multiple steps.   Each step either performs a driver level operation or field/element level operation.  Where a field/element level operation is performed then the step cross references the locators defined on the locators tab using the object and field values.

![](diagrams\Keyword1.png)

​		





The following driver level actions are available:

| action  | description                                         |
| ------- | --------------------------------------------------- |
| launch  | launches the browser                                |
| goto    | navigates to the url specified in data value column |
| back    | navigates back                                      |
| forward | navigates forward                                   |
| quit    | closes the browser                                  |

The following element level actions are available (type distinguishes sub-action):

| action   | type           | description                                                  |
| -------- | -------------- | ------------------------------------------------------------ |
| sendKeys |                |                                          |
| click    |                | clicks the given element                                         |
| dropdown | selectByText   | select the dropdown by given text                                               |
| dropdown | selectByIndex  | select the dropdown by given index                                            |
| dropdown | selectByValue  | select the dropdown by given value                            |
| assert   | text           | validates the innertext of an element                        |
| assert   | selectedOption | validates the currently selected value of a dropdown         |
| assert   | visible        | validates if the field is currently displayed                |
| assert   | enabled        | validates if the field is currently enabled                  |
| assert   | selected       | validates if the field (e.g. checkbox) is currently selected |
| assert   | classes        | validates if the element is at least of the expected class type |
| assert   |                | validates the value attribute of an element                  |
| assert   | size           | checks the size of an element list                           |
| wait     | page           |                                                              |
| wait     | visible        |                                                              |
| wait     | clickable      |                                                              |
| wait     | text           |                                                              |