package automation.library.conversion2jmx.postman2jmx.builder;

import automation.library.conversion2jmx.common.model.jmx.JmxHTTPSamplerProxy;
import automation.library.conversion2jmx.postman2jmx.model.postman.PostmanFileBody;
import automation.library.conversion2jmx.postman2jmx.model.postman.PostmanItem;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPFileArg;

public class JmxBinaryBodyBuilder extends AbstractJmxBodyBuilder {

    @Override
    public HTTPSamplerProxy buildJmxBody(PostmanItem postmanItem)  {
        HTTPSamplerProxy httpSamplerProxy = JmxHTTPSamplerProxy.newInstance(postmanItem);
        PostmanFileBody fileBody = postmanItem.getRequest().getBody().getFile();

        HTTPFileArg argument = new HTTPFileArg();
        argument.setPath(fileBody.getSrc() == null ? "" : fileBody.getSrc());
        argument.setParamName(" ");
        argument.setMimeType(" ");

        HTTPFileArg[] fileArgs = new HTTPFileArg[1];
        fileArgs[0] = argument;

        httpSamplerProxy.setHTTPFiles(fileArgs);

        return httpSamplerProxy;
    }
}
