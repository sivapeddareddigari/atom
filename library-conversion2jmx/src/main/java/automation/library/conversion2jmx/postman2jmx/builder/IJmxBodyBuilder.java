package automation.library.conversion2jmx.postman2jmx.builder;

import automation.library.conversion2jmx.postman2jmx.model.postman.PostmanItem;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;

public interface IJmxBodyBuilder {

    HTTPSamplerProxy buildJmxBody(PostmanItem postmanItem) throws Exception;
}
