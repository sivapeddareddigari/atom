## API Library

#### 1. Purpose and Contents

The 'api' library contains a set of java helper classes that support api testing by leveraging Rest-Assured.

| Class             | Purpose                                                      |
| ----------------- | ------------------------------------------------------------ |
| Constants         | List of directory references relative to the test project    |
| ResponseValidator | Simple pojo defining the structure of the test data to be supplied when validating an individual field in an api response |
| RestAssuredHelper | Set of helper methods that use rest assured to setup and make api calls and validate the returned responses, including:- setting authentication<br/>- setting base uri, path and port<br/>- setting request headers<br/>- setting request path parameters based on supplied list<br/>- setting request query parameters based on supplied list<br/>- setting request body based on supplied json string<br/>- calling the API (get, post, put, patch, delete)<br/>- validating the API response code<br/>- validating the API response time<br/>- validating the API response conforms to a given schema<br/>- validating the API response headers<br/>- validating the API response body content |

When validating a response body there are a number of options available:

- entire object validation (whole expected json object against actual response object)
- field by field validation

When performing entire object validation this can be done either strictly or leniently.  Strictly enforces that all fields must be present and value match.  Leniently allows fields in the response body to be ignored (e.g. timestamps) by omitting them from the expected result object.

When validating individual fields within the response body the checks shown in the table below are available. A simple list is defined of the fields to be checked based on the ResponseValidator object with the json path for the element to be checked, the check to be performed (from table below), the data type, and the expected value.

| Check              | Description                                                  |
| ------------------ | ------------------------------------------------------------ |
| equals             | simple equality check between actual and expected values     |
| hasItem            | checks that array object at least contains the expected value |
| hasItems           | checks that array object at least contains the set of expected values |
| contains           | checks that array object contains all and only the set of expected values (ordered) |
| containsInAnyOrder | checks that array object contains all and only the set of expected values (in any order) |
| hasSize            | checks that an array has the expected number of entries      |
| isNull             | checks that either an individual field or array is null      |
| isEmpty            | checks that either an individual field or array is present but empty |
| startsWith         | checks that the string value of a field starts with the expected value |
| endsWith           | checks that the string value of a field ends with the expected value |
| containsString     | checks that the string value of a field includes the expected value |
| regex              | checks that a field conforms to the expected regular expression pattern |
| !                  | all the above checks exist in negative form (e.g. !equals)   |

#### 2. Usage

### 2.1 Writing API Tests (features and steps)

A very simple example feature that calls the Google Books public API and checks the response is shown below:

```
Feature: Google Maps Rest API

@googlemaps
Scenario: calculations for london to edinburgh
Given a rest api "GoogleMaps"
Given query parameters
| origins		| London, UK	|
| destinations		| Edinburgh, UK	|
When the system requests GET "/maps/api/distancematrix/json"
Then the response code is 200
And the response body contains
| distance | 666 km			|
| duration | 7 hours 21 mins		|
```

This leverages the frameworks built-in Cucumber DSL (pre-written Gherkin statements and Java Step Definitions) that wraps around the RestAssured library.  This is available to any test project that includes the *cf_api_utils* dependency.

A test project can trigger GET / POST / PUT / DELETE / PATCH calls with detailed validation of the responses simply by re-using the built-in Gherkin statements within the test project features.  No further coding is needed beyond the simple runner class described in the previous section.

Re-using the frameworks common steps can significantly reduce the amount of custom code (and repetitive code) that a project needs to create when testing rest Web-Services.

##### 2.1.1 API Common Steps

The following list gives the common Gherkin statements that are available within the framework and their associated step definition methods.

*To specify the base uri/path/port for the API using an end point defined in the test project configuration*

```
Given a rest api "foo"

@Given("^a rest api \"(.*)\"$")
public void public void setAPI (String api)
```

where "foo" references an end point defined in the test project environment properties file (e.g. foo=//localhost:8081/googleapis.com/books/)"

<br>*To define a base uri and port value for the API directly in the feature:*

```
Given a base uri "foo"
Given a port bar

@Given("^a base uri \"(.*)\"$")
public void setBaseURI(String uri)

@Given("^a port (\\d+)$")
public void setPort(int port)
```

where foo = the end point url, and bar is a port number (e.g. 8080)

<br>*To define a list of header values for the API call (accepts list of one or more header values as data table)*

```
Given a header
|key	|value	|

@Given("^a header$")
public void setHeader(Map<String, String> map)
```

<br>

*To define a list of parameters for the API call (accepts list of one or more parameter values as data table)*

```
Given form parameters
|key	|value	|

Given query parameters
|key	|value	|

Given path parameters
|key	|value	|

@Given("^(form parameters|query parameters|path parameters|parameters)$")
public void withParams(String type, Map<String, String> map)
```

<br>*To specify the json data for the request body of the API from a test data file*

```
Given a request body "<<foo.bar>>"

@Given("^a request body \"(.*)\"$")
public void requestBody(String data)
```

Where foo=the name of a json file, bar=a json object within the file, for example:

```
{
	"bar":{
		json body for test case 1 goes here
	},
	"anotherCase":{
		json body for test case 2 goes here
	}
}
```

<br>*To specify the json data for the request body of the API as a json string*

```
Given a request body "foo"

@Given("^a request body \"(.*)\"$")
public void requestBody(String data)
```

Where "foo" is a json string such as "{"field1" : "some value", "field2" : "some value"}"

<br>*To define a base data set for an API body which is common to multiple tests*

```
Given base input data "foo.bar"

@Given("^base input data \"([^\"]*)\"$")
public void setBaseInputData(String arg1)
```

Where foo=the name of a json file, bar=a json object within the file

<br>*To trigger the API call (to end point defined by the base path/port + the path defined in this step*

```
When the system requests GET "bar"
When the system requests PUT "bar"
When the system requests POST "bar"
When the system requests PATCH "bar"
When the system requests DELETE "bar"

@When("^the system requests (GET|PUT|POST|PATCH|DELETE) \"(.*)\"$")
public void apiGetRequest(String apiMethod, String path)
```

<br>*To validate the response code from the API call*

```
Then the response code is 200

@Then("^the response code is (\\d+)$")
public void verify_status_code(int code)
```

<br>*To validate the response time from the API call*

```
Then the response time is less than 1000 milliseconds

@Then("^the response time is less than (\\d+) milliseconds$")
public void verifyResponseTime(long duration)
```

<br>*To validate the response body matches an expected schema format*

```
Then the response matches the json schema "foo"
Then the response matches the xml schema "foo"

@And("^the response matches the (json|xml) schema \"(.*)\"$")
public void matchJSONSchema(String type, String path)
```

<br>*To validate the response header contains the expected values (accepts list of one or more header values as data table)*

```
Then the response header contains
|key	|value	|

@Then("^the response header contains$")
public void verifyHeader(Map<String, String> map)
```

<br>*To validate that the response body is empty*

```
Then the response body is empty

@And("^the response body is empty$")
public void responseBodyEmpty()
```

<br>*To validate the response body contents against a pre-defined expected results json* (see below for further details)

```
Then the response body contains
|<<foo.bar>>	|

@And("^the response body contains$")
public void responseBodyValid(DataTable table)
```

Where foo=the name of a json file, bar=a json object within the file

<br>To validate the response body for a specific set of fields (see below for further details)

```
Then the response body contains
|field		|value	|

Then the response body contains
|element	|matcher	|value		|type	|

@And("^the response body contains$")
public void responseBodyValid(DataTable table)
```

<br>*To trace out the request and response json strings*

```
And trace out request response

@And("^trace out request response$")
public void traceOut()
```

##### 2.1.2 Setting Base Data for JSON Body

The step *Given base input data* enables a root json object to be applied as the base data for all subsequent tests.

When used alongside the step *Given a request body* it enables multiple tests to be defined that apply delta's or variations to the base data without the need to repeat/duplicate the full data set.  The data from the *Given a request body* step is overlaid onto the base data replacing any common fields whilst retaining the rest of the base data.

For example with a base data file called BaseData.json:

```
{
	"sunnyDay":{
		"field1":"abc",
		"field2":"def",
		"field3":"ghi"
		"field4":123
	}
}
```

And the following steps in a cucumber scenario:

```
Given base input data "BaseData.sunnyDay"
And a request body "{"field1" : "zzz", "field4": 777}"
```

Would result in the following json body for the test:

```
{
	"sunnyDay":{
		"field1":"zzz",
		"field2":"def",
		"field3":"ghi"
		"field4":777
	}
}
```

This approach can be used in conjunction with Cucumber Example tables to create multiple tests where only some part of the json body varies in value.

note: the json data overlay uses recursive merging and therefore handles nested objects and arrays.

##### 2.1.3 Validating the Response Body

The step *Then the response body contains* enables validation of the API response body.  The step accepts a datatable and based on the contents of this table will either compare the entire response body against a predefined expected results JSON object, or will perform a field by field comparison based on a supplied field + expected results list.

To validate against a JSON object a single row, single column table would be defined in the feature:

```
Then the response body contains
|<<foo.bar>>	|
```

Where foo=the name of a json file and bar=a json object within the file, for example:

```
{
	"bar":{
		json body for test case 1 goes here
	},
	"anotherCase":{
		json body for test case 2 goes here
	}
}
```

note: the << >> prefix/suffix around the filename.object is simply to make it clear in the feature that the expected results are being taken from an external json file.

<br>To validate the response body for a specific set of fields then the datatable is supplied with either 2 or 4 columns.  Each row in the table details a field to be validated and the expected results.

When using a 4 column table all the values needed to locate each field and perform the validation are defined directly in the feature including the [json path query](#json-path-query) needed to find the element, the [hamcrest matcher](#hamcrest-matcher) to be applied, the expected value and the [data type](#data-types) (such as string).

For example validating fields returned by the GoogleBooks public API:

```
Then the response body contains
|element				|matcher	|value		|type	|
|items.volumeInfo.title			|equalTo	|Steve Jobs	|str	|
|items.volumeInfo.pageCount		|hasItem	|630		|int	|
|items.volumeInfo.averageRating		|hasItem	|4.0		|num	|
```

When using a 2 column table the feature defines the list of functional fields and expected values and is supported by a json file that defines for each field the [json path query](#json-path-query) needed to find the element, the [hamcrest matcher](#hamcrest-matcher) to be applied and the [data type](#data-types).

Using the same example as above:

```
Then the response body contains
|title			|Steve Jobs	|
|pageCount		|630		|
|rating			|4.0		|
```

The supporting json would be structured as:

```
{
	"title":{
		"element":"items.volumeInfo.title",
		"matcher":"equalTo",
		"type":"str"
	},
	"pageCount":{
		"element":"items.volumeInfo.pageCount",
		"matcher":"hasItem",
		"type":"int"
	},
	"rating":{
		"element":"items.volumeInfo.averageRating",
		"matcher":"hasItem",
		"type":"num"
	}
}
```

The 2 column approach is recommended since this simplifies the cucumber feature.

<br>

##### 2.2 Json Path for Locating Elements in the Response Body

RestAssured and therefore this framework use the JsonPath ([GPath](http://groovy-lang.org/processing-xml.html#_gpath)) syntax for locating objects and elements within the response body.

Nested elements within the response are identified by the `.` symbol.

A json path of `a.b.c` will locate element c nested within object b nested within object a.

A json path of `a.b[1].c` will locate an element c nested within the second occurrence of array b within object a

RestAssured also accepts groovy queries and filters as part of the json path including:

- `find` – finds the first item matching a closure predicate
- `findAll` – finds all the items matching a closure predicate
- `collect` – collect the return value of calling a closure on each item in a collection
- `sum` – sum all the items in the collection
- `max`/`min` – returns the max/min values of the collection
- `size` - returns the number of elements of the collection

For example the json path:

 `store.book.findAll { it.price < 10 }.title`

would return an array/collection with the 1st and 3rd item titles from the below json i.e. [Sayings of the Century, Moby Dick]

```
{
   "store":{
      "book":[
         {
            "author":"Nigel Rees",
            "category":"reference",
            "price":8.95,
            "title":"Sayings of the Century"
         },
         {
            "author":"Evelyn Waugh",
            "category":"fiction",
            "price":12.99,
            "title":"Sword of Honour"
         },
         {
            "author":"Herman Melville",
            "category":"fiction",
            "isbn":"0-553-21311-3",
            "price":8.99,
            "title":"Moby Dick"
         }
      ]
   }
}
```

<br>

##### 2.3 Hamcrest Matchers

RestAssured (and therefore this framework) use Hamcrest matchers when validating the field values held within the json response body from an API call.

The available matchers in this framework for single object validation are:

| Matcher        | Description                                                  |
| -------------- | ------------------------------------------------------------ |
| equalTo        | checks that the value of the object in the response matches the expected value |
| isNull         | checks that an object in the response is null                |
| isEmpty        | checks that a string field in the response is empty          |
| startsWith     | checks that a string field in the response starts with the expected text |
| endsWith       | checks that a string field in the response ends with the expected text |
| containsString | checks that a string field in the response contains the expected text |

The available matchers in this framework for array validation are:

| Matcher             | Description                                                  |
| ------------------- | ------------------------------------------------------------ |
| hasItem             | checks that an array object includes at least the expected value |
| hasItems            | checks that an array object includes at least the list of expected values ** |
| contains            | checks that an array object contains only the list of expected values ** and in the same order |
| containsAnyOrder    | checks that an array object contains only the list of expected values ** but in any order |
| hasSize             | checks that an array object has at least one entry           |
| containsStringArray | checks that a string array contains the expected text        |

** A list of expected values are specified as a comma separated list in square brackets i.e. in the format `[a,b,c]`.

Some examples:

```
|matcher			|expected value	|
|equalTo			|a		        |
|hasItem			|a		        |
|hasItems			|[a,b]		    |
|contains			|[a,b,c]	    |
|containsAnyOrder	|[a,b,c]		|
```

##### 2.4 Data Types

When defining elements in the JSON response for validation it is necessary to specify the data type as one of either `str` for String values, `int` for integer values or `num` for floats.

##### 2.5 Combining JSON Path and Hamcrest Matchers for Validation
Given the API response body below then the following entry in a cucumber feature file shows some example validations.
```
Then the response body contains
|element											|matcher	|value			|type		|
|store.book[0].author								|equalTo	|Nigel Rees		|str 		|
|store.book.findAll {it.category=='fiction'}.sum()	|equalTo	|21.97			|num 		|
|store.book.title									|hasItems	|[Life of Pi,Moby Dick]	|str|

```

```
{
   "store":{
      "book":[
         {
            "author":"Nigel Rees",
            "category":"reference",
            "price":8.95,
            "title":"Sayings of the Century"
         },
         {
            "author":"Yann Martel",
            "category":"fiction",
            "price":12.99,
            "title":"Life of Pi"
         },
         {
            "author":"Herman Melville",
            "category":"fiction",
            "isbn":"0-553-21311-3",
            "price":8.99,
            "title":"Moby Dick"
         }
      ]
   }
}
```

##### 2.6 Example Cucumber Features

###### Example 1 - GET Request

This example shows 2 working scenarios with GET requests to the public GoogleMaps API and validates the response.

```
Feature: Google Maps Rest API

@google @maps
Scenario: calculations for london to edinburgh
Given a rest api "GoogleMaps"
Given query parameters
| origins		| London, UK		|
| destinations		| Edinburgh, UK		|
When the system requests GET "/maps/api/distancematrix/json"
Then the response code is 200
And the response body contains
| distance | 666 km		|
| duration | 7 hours 21 mins	|


@google @maps
Scenario Outline: calculations for <start point> - <end point> - <travel type>
Given a rest api "GoogleMaps"
Given query parameters
| origins		| <start point> |
| destinations		| <end point>	|
| mode			| <travel type> |
When the system requests GET "/maps/api/distancematrix/json"
Then the response code is 200
And the response body contains
| distance 		| <distance>	|
| duration 		| <duration> 	|

Examples:
| start point | end point	| travel type	| distance	| duration			|
| London, UK  | Edinburgh, UK	| driving		| 666 km	| 7 hours 21 mins	|
| London, UK  | Edinburgh, UK	| walking		| 607 km	| 5 days 4 hours	|
| London, UK  | Edinburgh, UK	| bicycling		| 713 km	| 1 day 14 hours	|
| Bath, UK    | York, UK	| driving		| 377 km	| 4 hours 10 mins	|
| Bath, UK    | Paris, FR	| driving		| 641 km	| 7 hours 21 mins	|
```

This is supported by the following entry in the environment property file:

```
GoogleMaps=https://maps.googleapis.com
```

And by the following json file which defines how to access the fields distance and duration in the response:

```
{
	"status":{
		"element":"status",
		"matcher":"equalTo",
		"type":"str"
	},
	"distance":{
		"element":"rows.elements.distance.flatten().text",
		"matcher":"contains",
		"type":"str"
	},
	"duration":{
		"element":"rows.elements.duration.flatten().text",
		"matcher":"contains",
		"type":"str"
	}
```

###### Example 2 - POST Request to Swagger demo pet store site

This example shows multiple working POST and GET requests to the public demo API Pet Store.  The POST request includes setting a default json body for re-use across 3 tests with replacement of the id and name elements.

```
Feature: Pet Store Rest API

@petstore
Scenario Outline: Create Pet Information
Given a rest api "PetStore"
Given a header
|Content-Type	| application/json		|
And base input data "PetStoreTestData.default"
And a request body "{"id":<petID>,"name":"<petName>"}"
When the system requests POST "/v2/pet/"
Then the response code is 200

Examples:
|petID	|petName	|
|1	|Spot		|
|2	|Whiskers	|
|3	|Rover		|

@petstore
Scenario Outline: Get Pet Information
Given a rest api "PetStore"
Given a header
|Content-Type	| application/json		|
And path parameters
|petID	|<id>	|
When the system requests GET "/v2/pet/{petID}"
Then the response code is 200
And the response body contains
|element	|matcher	|value		|type	|
|name		|equalTo	|<petName>	|str	|

Examples:
|id		|petName	|
|1		|Spot		|
|2		|Whiskers	|
|3		|Rover		|
```

In support of this example the PetStoreTestData.json file had the following content:

```
{
	"default": {
		"id": 0,
		"category": {
			"id": 0,
			"name": "string"
		},
		"name": "my new pet",
		"status": "available"
	}
}
```


##### 2.7 Custom Steps

Additional custom steps can also be created by a test project to work alongside or extend these framework steps.

To support this the Framework includes a context object which gets populated with all the api request / response data from the framework steps as the test execution progresses.

The simplest way for any custom step classes to access this context object and its data is to use the built-in Pico dependency injection via a constructor method in the custom steps class.  This is shown below:

```
public class YourProjectSteps {
	RestContext restContext;

	public YourProjectSteps(RestContext restContext){
		this.restContext = restContext;
	}
```

All of the request / response data (and the associated methods to access this) will then be available in the *restContext* instance variable of the custom steps class.

##### 2.8 API Test Configuration

As described above, when using the *"Then the response body contains"* built-in step from the framework DSL there is the option to simplify the feature by defining the way the step reads the API response outside of the feature and in a json configuration file.

For each element of the API response to be validated the configuration file defines the field name, the JsonPath query used to locate the element, the hamcrest matcher to be applied, and the data type of the element.

The configuration file name should be *"\<API\>.json"* where *\<API\>* is the same value as used to define the API end point in the environment properties file.

The file should be held in the test project under *src/test/resources/config/apistructures/*.

##### 2.9 Test Assertions

The framework uses *soft assertions* in each of the built-in steps when performing the validation of the api response and reports an aggregated result.  For example if 5 fields in the response body were being checked and 2 of these failed then both failures will be included in the output report for that step.
