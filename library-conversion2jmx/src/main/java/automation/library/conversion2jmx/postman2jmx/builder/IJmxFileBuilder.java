package automation.library.conversion2jmx.postman2jmx.builder;

import automation.library.conversion2jmx.postman2jmx.model.postman.PostmanCollection;
import automation.library.conversion2jmx.common.model.jmx.JmxFile;

public interface IJmxFileBuilder {

    JmxFile build(PostmanCollection postmanCollection, String jmxOutputFilePath) throws Exception;
}
