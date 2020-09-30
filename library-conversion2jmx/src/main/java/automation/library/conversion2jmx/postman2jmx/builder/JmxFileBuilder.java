package automation.library.conversion2jmx.postman2jmx.builder;

import automation.library.conversion2jmx.common.model.jmx.JmxFile;
import automation.library.conversion2jmx.postman2jmx.model.postman.PostmanCollection;

public class JmxFileBuilder extends AbstractJmxFileBuilder {

    @Override
    public JmxFile build(PostmanCollection postmanCollection, String jmxOutputFilePath) throws Exception {
        return super.buildJmxFile(postmanCollection, jmxOutputFilePath);
    }
}
