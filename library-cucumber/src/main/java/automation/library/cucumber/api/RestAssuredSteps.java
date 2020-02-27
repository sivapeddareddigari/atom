package automation.library.cucumber.api;

import automation.library.api.core.ResponseValidator;
import automation.library.api.core.RestAssuredHelper;
import automation.library.api.core.RestContext;
import automation.library.api.core.SwaggerSoftValidationFilter;
import automation.library.common.Property;
import automation.library.common.TestContext;
import automation.library.cucumber.core.Constants;
import com.fasterxml.jackson.core.JsonParseException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.filter.Filter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * Common Steps class which invokes the RestAssuredHelper class and enables
 * rest api calls to be configured and invoked from cucumber features along
 * with detailed response validation.
 */
public class RestAssuredSteps {
    RestContext restContext;
    RequestSpecification request = given();

    public RestAssuredSteps(RestContext restContext) {
        this.restContext = restContext;
    }

    private String baseTestData = null;
    private String baseDataSet = null;
    private String api = null;
    private String path = null;
    private String method = null;

    public void resetRest() {
        request = given();
        restContext.getRestData().setRequest(request);
    }

    @Given("^a rest api \"(.*)\"$")
    public void setAPI(String api) {
        restContext.getRestData().setRequest(request);
        this.api = api;
        String envURI = Property.getProperty(Constants.ENVIRONMENTPATH + Property.getVariable("cukes.env") + ".properties", api);
        RestAssuredHelper.setBaseURI(restContext.getRestData().getRequest(), envURI);
    }

    @Given("^(challenged basic|basic) authorisation$")
    public void setBasicAuth(String type) {
        String[] auth = Property.getProperty(Constants.ENVIRONMENTPATH + Property.getVariable("cukes.env") + ".properties", api + ".basicauth").split(":");
        if (type.equalsIgnoreCase("basic")) {
            RestAssuredHelper.setBasicAuth(restContext.getRestData().getRequest(), auth[0], auth[1]);
        } else {
            RestAssuredHelper.setChallengedBasicAuth(restContext.getRestData().getRequest(), auth[0], auth[1]);
        }
    }

    @Given("^a base uri \"(.*)\"$")
    public void setBaseURI(String uri) {
        RestAssuredHelper.setBaseURI(restContext.getRestData().getRequest(), uri);
    }

    @Given("^a base path \"(.*)\"$")
    public void setBasePath(String basepath) {
        RestAssuredHelper.setBasePath(restContext.getRestData().getRequest(), basepath);
    }

    @Given("^a port (\\d+)$")
    public void setPort(int port) {
        RestAssuredHelper.setPort(restContext.getRestData().getRequest(), port);
    }

//    @Given("^a header$")
//    public void setHeader(Map<String, String> map) {
//        map.forEach((key, val) -> {
//            RestAssuredHelper.setHeader(restContext.getRestData().getRequest(), key, val);
//        });
//    }

    @Given("^a header$")
    public void setHeader(Map<String, String> map) {
        map.forEach((key, val) -> {

            val = val.trim();

            Pattern pattern = Pattern.compile("<(.*?)>");
            Matcher matcher = pattern.matcher(val);

            if (matcher.find()) {

                String keyForThreadContextContext = matcher.group(1);
                try {
                    String valFromContext = TestContext.getInstance().testdataGet(keyForThreadContextContext.trim()).toString();
                    val = val.replace("<" + keyForThreadContextContext + ">", valFromContext);
                    RestAssuredHelper.setHeader(restContext.getRestData
                            ().getRequest(), key, val);
                } catch (Exception e) {
                    assertTrue(false, "'" + keyForThreadContextContext + "' : key not found in ThreadContext");
                }
            } else {
                RestAssuredHelper.setHeader(restContext.getRestData
                        ().getRequest(), key, val);
            }
        });
    }

    @Given("^Extract and Save \"(.*)\" from response$")
    public void extractAndSave(String jsonPath) {
        JSONObject responseJSON = new JSONObject(restContext.getRestData
                ().getRespString());

        String value = responseJSON.get(jsonPath).toString();
        TestContext.getInstance().testdataPut(jsonPath, value);
    }




    @Given("^(form parameters|query parameters|path parameters|parameters)$")
    public void withParams(String type, Map<String, String> map) {
        map.forEach((key, val) -> {
            Boolean list = false;
            List<String> vals = new ArrayList<String>();
            if (val.contains("::")) {
                list = true;
                vals = Arrays.asList(val.split("::"));
            }

            if (!list)
                RestAssuredHelper.setParam(restContext.getRestData().getRequest(), type, key, val);
            else
                RestAssuredHelper.setParamList(restContext.getRestData().getRequest(), key, vals);
        });
    }

    @Given("^base input data \"([^\"]*)\"$")
    public void setBaseInputData(String arg1) throws Throwable {
        String[] str = arg1.split("\\.");
        baseTestData = "src/test/resources/testdata/inputs/" + str[0] + ".json";
        baseDataSet = str[1];
    }

    @When("^a request body$")
    @Given("^a request body \"(.*)\"$")
    public void requestBody(String data) throws JsonParseException, IOException, JSONException {
        String jsonData = RestAssuredHelper.createJson(data);
        RestAssuredHelper.setBody(restContext.getRestData().getRequest(), jsonData);
    }

    @When("^the system requests (GET|PUT|POST|PATCH|DELETE) \"(.*)\"$")
    public void apiGetRequest(String apiMethod, String path) {
        this.path = path;
        this.method = apiMethod;
        Response response = RestAssuredHelper.callAPI(restContext.getRestData().getRequest(), apiMethod, path);
        restContext.getRestData().setResponse(response);
        resetRest();        //enables multiple api calls within single scenario
    }

    @Then("^the response code is (\\d+)$")
    public void verify_status_code(int code) throws NumberFormatException, IOException {
        RestAssuredHelper.checkStatus(restContext.getRestData(), code);
        TestContext.getInstance().sa().assertAll();
    }

    @Then("^the response status is \"(.*)\"$")
    public void verify_status_message(String msg) throws NumberFormatException, IOException {
        RestAssuredHelper.checkStatus(restContext.getRestData(), msg);
        TestContext.getInstance().sa().assertAll();
    }

    @Then("^the response time is less than (\\d+) milliseconds$")
    public void verifyResponseTime(long duration) {
        RestAssuredHelper.checkResponseTime(restContext.getRestData(), duration);
    }

    @Then("^the response header contains$")
    public void verifyHeader(List<List<String>> list) {
        Object val;
        String key;
        String matcher;
        for (List<String> row : list) {
            if (row.size() == 2) {
                matcher = "equals";
                key = row.get(0);
                val = row.get(1);
            } else {
                matcher = row.get(1);
                key = row.get(0);
                val = row.get(2);
            }
            RestAssuredHelper.checkHeader(restContext.getRestData(), key, matcher, val);
        }
    }

    @And("^the response body is empty$")
    public void responseBodyEmpty() throws IOException {
        if (!(restContext.getRestData().getRespString().equalsIgnoreCase("{}")
                || restContext.getRestData().getRespString().equalsIgnoreCase("[]"))) {
            fail("response body not empty....contains: " + restContext.getRestData().getRespString());
        }
    }

    @And("^the (json|response) body( strictly|) contains$")
    public void responseBodyValid(String type, String mode, DataTable table) throws IOException, JSONException {
        List<List<String>> temp = table.cells();

        String responseString = null;
        if (type.equalsIgnoreCase("response")) {
            responseString = restContext.getRestData().getRespString();
        }else{
            responseString = TestContext.getInstance().testdataGet("jsonBody").toString();
        }

        if (temp.get(0).size() == 1) {
            String[] filename = temp.get(1).get(0).replace("<<", "").replace(">>", "").split("\\.");
            RestAssuredHelper.responseBodyValid(mode, filename[0], filename[1], responseString);
        } else if (temp.get(0).size() == 2) {
            RestAssuredHelper.responseBodyValid(this.api, this.method, this.path, table.asMap(String.class, String.class), responseString);
        } else {
            RestAssuredHelper.responseContains(table.asList(ResponseValidator.class), responseString);
        }
    }


    @And("^the response matches the (json|xml) schema \"(.*)\"$")
    public void matchJSONSchema(String type, String path) {
        RestAssuredHelper.checkSchema(restContext.getRestData(), type, "testdata/schemas/" + path);
    }


    @And("^trace out request response$")
    public void traceOut() {
        System.out.println("request: " + restContext.getRestData().getRequestString());
        System.out.println("response: " + restContext.getRestData().getRespString());
    }

    @Given("^the (request|response|request and response) (?:satisfy|satisfies) the contract \"([^\"]*)\"$")
    public void requestResponseSatisfiesContract(String mode, String contract) {
        Filter validationFilter = new SwaggerSoftValidationFilter(mode, contract);
        request.filter(validationFilter);
    }

    @Given("^the (request|response|request and response) (?:satisfy|satisfies) the contract$")
    public void requestResponseSatisfiesContract(String mode) {
        String contract = Property.getProperty(Constants.ENVIRONMENTPATH + Property.getVariable("cukes.env") + ".properties", String.join(".", api, "Contract"));
        requestResponseSatisfiesContract(mode, contract);
    }

}



