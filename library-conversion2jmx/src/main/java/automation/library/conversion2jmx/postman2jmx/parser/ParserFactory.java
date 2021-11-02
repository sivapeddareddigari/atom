package automation.library.conversion2jmx.postman2jmx.parser;

import automation.library.conversion2jmx.postman2jmx.utils.CollectionVersion;

public class ParserFactory {

    public static IParser getParser(CollectionVersion version) {
        if(CollectionVersion.V2 == version) {
            return new PostmanParserV2();
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
