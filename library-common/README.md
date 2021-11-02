## Common Library

#### 1. Purpose and Contents

The 'common' library is used by all test projects (and a number of the other libraries).  It contains a small set of java helper classes:

| Class       | Purpose                                                      |
| ----------- | ------------------------------------------------------------ |
| TestContext | Singleton holding thread specific SoftAssert and TestData objects. The SoftAssert object enables test assertions to be chained together and reported on collectively. The TestData (map) object enables test data to be easily shared across any level of code within a running test. |
| Property    | Set of helper methods to read property files and system/environment variables |
| JsonHelper  | Set of helper methods to simplify reading JSON and Excel files<br/>Also includes methods to perform deep merge of 2 json objects (assists test data prep) |
| ExcelHelper | Set of helper methods to simplify reading excel sheet data|
| ZipHelper   | Set of helper methods to simplify file/directory zipping and unzipping |
| PDFHelper   | Set of helper methods to simplify reading and validating PDF files |

#### 2. Usage
#### 2.1 TestContext 
##### TestContext - Soft Assert

It is recommended that all test assertions are performed within the step definition methods rather than within page objects.  This helps to create a clear definition of responsibilities across the steps and page object layers of the test code:

- Steps coordinate the test flow, invoke page object methods, and perform "tests" (aka assertions)
- Page objects interface with the application and perform the "heavy lifting" selenium actions

Any assertion library such as Hamcrest or AssertJ can be used within the test code by adding the dependency to the test project Build.gradle file, however, for most situations it is recommended that the in-built TestNG assertions are used.

TestNG provides *hard assertions* and *soft assertions*.  Both include the same set of assertion checks (e.g. equals, notEquals, null et al). With hard assertions as soon as a single check fails then an exception will be thrown and the test will fail and stop.  With soft assertions it is possible to chain together multiple checks and report the aggregate result across the checks.

Hard assertions can be applied as:

```
import org.testng.Assert;
....
Assert.assertEquals(<actObject1>, <expObject1>);
Assert.assertEquals(<actObject2>, <expObject2>);
```

Soft assertions can be applied as:

```
import org.testng.asserts.SoftAssert;
....
SoftAssert sa = new SoftAssert();
sa.assertEquals(<actObject1>, <expObject1>);
sa.assertEquals(<actObject2>, <expObject2>);
sa.assertAll();
```

To further support soft assertions the framework TestContext class already includes a TestNG SoftAssert object for each running thread.  This removes the need to import and instantiate the SoftAssert and also allows soft asserts to be aggregated across step methods.  In addition, the framework includes a fail safe assertAll() statement within the after hooks of the test against this ThreadContext assert object.

To use this built-in soft assert within any test code:

```
TestContext.getInstance().sa().assertEquals(<actObject1>, <expObject1>);
TestContext.getInstance().sa().assertEquals(<actObject2>, <expObject2>);
TestContext.getInstance().sa().assertAll();
```

###### TestContext object - to share data across methods in different step classes

The framework includes a thread specific Context object can be used to share any data across multiple step classes or be accessed from any test code including page objects.  This object includes a map variable that can be used to store and share values or objects throughout the executing thread.

```
 public class TestContext {
	private Map<String,Object>          testdata = null;
 	.....
 	public Map<String, Object> testdata(){
		if (testdata ==null )
			testdata = new HashMap<String,Object>();
		return testdata;
	}
 }
```

A test project uses the `testdata()` method of the TestContext object to access the map and subsequently get or set entries in the map.  For example:

```
TestContext.getInstance().testdataPut(<key>, <object>);
e.g.
TestContext.getInstance().testdata().put("foo", "bar");
.....

- To pull back test data from the test context for the current thread (casting to required object type):

(<objectType>) TestContext.getInstance().testdataGet(<key>);
e.g. 
TestContext.getInstance().testdata().get("foo");
```

Any variable or object type can be added to the testdata map.

For easy access to the testdata, you can use `testdataPut(String key, Object data)` for saving your data, `testdataGet(String key)` for getting your testdata as an object or `testdataToClass(String key, Class<T> type)` which would return your testdata as your data type.
e.g. With a class that contains Person information and anthoer containing a list
```java
class Person {
	String name;
	Date dob;
	String gender;
	String email;
}

class Persons {
	List<Person> persons;
}
```
Using the example scenario
```
Scenario: Submitting multiple people to survay requests
	Given the application "foo"
	When submitting peoples information
		| Name          | Dob        | Gender  | Email                           |
		| Sean Goodmen  | 13/12/1993 | Male    | sean.goodmen@example.com        |
		| Emma White    | 23/09/1991 | Female  | emma.white@example.com          |
		| Harry Hampton | 18/05/1986 | Male    | harry.hampton@example.com       |
	Then the table contains the submitted people
```
We can save the data set and read it in another step
```java
	@When("^submitting peoples information$")
	public void submitting_peoples_information(List<Person> persons){
		Persons p = new Persons();
		p.persons = persons;
		TestContext.getInstance().testdataPut("personsInformation", p);
	}

	@Then("^the table contains the submitted people$")
	public void the_table_contains_the_submitted_people(){
		Persons p = TestContext.getInstance().testdataToClass("personsInformation", Persons.class);
	}
```

This process can also be applied to the Dependency Injection to only share information between steps.



#### 2.2 Property

- To read a property file from a given file path:

```
PropertiesConfiguration props = Property.getProperties(<filepath>);			
```

- To get a property value from a given property file:

```
String val = Property.getProperty(<filepath>, <key>);
```

- To get set of values from a given property file where multiple entries exist for same key:

```
String[] vals = Property.getPropertyArray(<filepath>, <key>);
```

- To get a java system property or environment variable value:

```
String val = Property.getVariable(<variable>);
```



#### 2.3 JsonHelper

- To read a whole JSON file:

```
JSONObject json = FileHelper.getJSONData(<filepath>);
```

- To read a nested json object within a JSON file:

```
JSONObject json = FileHelper.getJSONData(<filepath>, <key>);
```

- To read a nested json array within a JSON file:

```
JSONArray json = FileHelper.getJSONArray(<filepath>, <key>);
```

- To read a json file and cast into a given POJO class structure:	

```
<MyClass> obj = (MyClass) FileHelper.getDataPOJO(<filepath>, <MyClass.class>);
```

- To read a json file and convert into a map object:	

```
Map<String, String> map = FileHelper.getJSONToMap(<jsonObject>);
```

- To merge 2 json objects together substituting values in the target object with those from source object:	

```
JSONObject json = FileHelper.jsonMerge(<sourceJSONObject>, <targetJSONObject>);
```

*Note: the source object can include all or a subsidiary of the keys in the target object.  This is useful where a default object can be defined with all data values and then test cases can merge into this 1 or more data values specific to that test*.

- To read all rows and columns from an Excel worksheet:	

```
ArrayList<ArrayList<Object>> data = FileHelper.getDataAsArrayList(<filepath>, <sheetname>);
OR
Map<String, Map<String, Object>> data = FileHelper.getDataAsArrayList(<filepath>, <sheetname>);
```

- To read only rows matching a given key value (column A) from an Excel worksheet:	

```
ArrayList<ArrayList<Object>> = FileHelper.getDataAsArrayList(<filepath>, <sheetname>, <key>);
```

#### 2.4 ExcelHelper

- To read all rows and columns from an Excel worksheet:

```
ArrayList<ArrayList<Object>> data = FileHelper.getDataAsArrayList(<filepath>, <sheetname>);
OR
Map<String, Map<String, Object>> data = FileHelper.getDataAsArrayList(<filepath>, <sheetname>);
```

- To read only rows matching a given key value (column A) from an Excel worksheet:

```
ArrayList<ArrayList<Object>> = FileHelper.getDataAsArrayList(<filepath>, <sheetname>, <key>);
```


#### 2.5 ZipHelper

- To zip a set of files or directories to an archive:

```
ZipHelper.zipit(<sourcefilepath>, <targetfilepath>);
```

- To unzip an archive to a target directory

```
ZipHelper.unzipit(<sourcefilepath>, <targetfilepath>);
```

#### 2.6 PDFHelper

- return the PDF document for the input PFG file path

```
PDDocument pdfDoc = PDFHelper.getDoc(String path) {
or
PDDocument pdfDoc = PDFHelper.getDoc(byte[] contents)
```

- return PDFpage (index start from 0) for the given PDF document input
```
PDPage pdfPage = PDFHelper.getPDFPage(PDDocument doc, int num)
```

- return PageTree og input PDF document object
```
PDPageTree pdfPageTree = PDFHelper.getPDFPages(PDDocument doc) {
```

- returns the text of entire PDF document
```
PDFHelper.public static String getPDFText(PDDocument doc) {
```

- return text of give pdf page range, startpage start from 1
```
String text =  PDFHelper.getPDFText(PDDocument doc, int startPage, int...endPage)
```

- return array of string , separated by token provided
```
String[] text = PDFHelper.getPDFText(PDDocument doc, String token, int startPage, int... endPage)
```

- read the text form PDF document for given page number and area.
```
Rectangle rectangle = new Rectangle(10, 10, 400, 400 );
String text = PDFHelper.getPDFText(PDDocument doc, int pageNum, Rectangle rectangle) {
```