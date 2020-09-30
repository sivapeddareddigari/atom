package automation.library.conversion2jmx.postman2jmx.builder;

import automation.library.conversion2jmx.common.model.jmx.JmxHTTPSamplerProxy;
import automation.library.conversion2jmx.postman2jmx.model.postman.PostmanItem;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;

public abstract class AbstractJmxBodyBuilder implements IJmxBodyBuilder {

    @Override
    public HTTPSamplerProxy buildJmxBody(PostmanItem postmanItem) throws Exception {
        return JmxHTTPSamplerProxy.newInstance(postmanItem);
    }
}
