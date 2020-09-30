package automation.library.conversion2jmx.postman2jmx.parser;

import automation.library.conversion2jmx.postman2jmx.model.postman.PostmanCollection;

import java.io.IOException;
import java.io.InputStream;

public interface IParser {

    PostmanCollection parse(String postmanJsonFile) throws IOException;

    PostmanCollection parse(InputStream is) throws IOException;

}
