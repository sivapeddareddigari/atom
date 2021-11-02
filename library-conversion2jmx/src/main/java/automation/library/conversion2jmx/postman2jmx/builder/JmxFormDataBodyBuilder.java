package automation.library.conversion2jmx.postman2jmx.builder;

import automation.library.conversion2jmx.common.model.jmx.JmxHTTPSamplerProxy;
import automation.library.conversion2jmx.postman2jmx.model.postman.PostmanFormDataBody;
import automation.library.conversion2jmx.postman2jmx.model.postman.PostmanItem;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPArgument;

import java.util.List;

public class JmxFormDataBodyBuilder extends AbstractJmxBodyBuilder {

    @Override
    public HTTPSamplerProxy buildJmxBody(PostmanItem postmanItem) {
        HTTPSamplerProxy httpSamplerProxy = JmxHTTPSamplerProxy.newInstance(postmanItem);

        Arguments arguments = new Arguments();
        List<PostmanFormDataBody> formDataList = postmanItem.getRequest().getBody().getFormDataList();

        HTTPArgument argument;
        for(PostmanFormDataBody formData : formDataList) {
            argument = new HTTPArgument();
            argument.setEnabled(true);
            argument.setName(formData.getKey());
            argument.setValue(formData.getValue());
            argument.setMetaData("=");
            argument.setAlwaysEncoded(false);
            argument.setUseEquals(true);
            arguments.addArgument(argument);
        }

        httpSamplerProxy.setArguments(arguments);
        return httpSamplerProxy;
    }
}
