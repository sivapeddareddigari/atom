package automation.library.conversion2jmx.postman2jmx.parser;

import automation.library.conversion2jmx.postman2jmx.model.postman.PostmanCollection;

import java.io.IOException;
import java.io.InputStream;

public class PostmanParserV2 extends AbstractParser {

    @Override
    public PostmanCollection parse(String postmanJsonFile) throws IOException {
        PostmanCollection postmanCollection = readValue(postmanJsonFile);
        return postmanCollection;
    }

    @Override
    public PostmanCollection parse(InputStream is) throws IOException {
        PostmanCollection postmanCollection = readValue(is);
        return postmanCollection;
    }

}
