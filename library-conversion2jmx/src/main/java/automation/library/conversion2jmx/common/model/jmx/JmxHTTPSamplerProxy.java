package automation.library.conversion2jmx.common.model.jmx;

import automation.library.conversion2jmx.common.utils.UrlUtils;
import automation.library.conversion2jmx.postman2jmx.model.postman.PostmanItem;
import automation.library.conversion2jmx.postman2jmx.model.postman.PostmanQuery;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarQueryParam;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.http.control.gui.HttpTestSampleGui;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.protocol.http.util.HTTPArgument;
import org.apache.jmeter.testelement.TestElement;

import java.net.MalformedURLException;
import java.net.URL;

public class JmxHTTPSamplerProxy {
    public static HTTPSamplerProxy newInstance(PostmanItem postmanItem) {
        HTTPSamplerProxy httpSamplerProxy = new HTTPSamplerProxy();
        httpSamplerProxy.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
        httpSamplerProxy.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
        httpSamplerProxy.setName(postmanItem.getName());
        httpSamplerProxy.setEnabled(true);
        httpSamplerProxy.setAutoRedirects(false);
        httpSamplerProxy.setFollowRedirects(true);
        httpSamplerProxy.setUseKeepAlive(true);
        httpSamplerProxy.setMonitor(false);
        httpSamplerProxy.setDoMultipartPost(false);
        httpSamplerProxy.setComment("");
        httpSamplerProxy.setConnectTimeout("");
        httpSamplerProxy.setResponseTimeout("");
        httpSamplerProxy.setEmbeddedUrlRE("");
        httpSamplerProxy.setContentEncoding("");
        httpSamplerProxy.setMethod(postmanItem.getRequest().getMethod());


        String rawUrl = postmanItem.getRequest().getUrl().getRaw();

        if (UrlUtils.isVariableUrl(rawUrl)) {
            String url = UrlUtils.getVariableUrl(rawUrl);
            String path = UrlUtils.getVariablePath(rawUrl);

            httpSamplerProxy.setDomain(url);
            httpSamplerProxy.setPath(path);
        } else {
            URL url = null;

            try {
                url = UrlUtils.getUrl(rawUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            String domain = UrlUtils.getDomain(url);
            int port = UrlUtils.getPort(url);
            String path = UrlUtils.getPath(url);
            String protocol = UrlUtils.getProtocol(url);

            httpSamplerProxy.setDomain(domain);
            httpSamplerProxy.setPath(path);

            if (port != -1) {
                httpSamplerProxy.setPort(port);
            }
            httpSamplerProxy.setProtocol(protocol);
        }


        Arguments arguments = new Arguments();
        HTTPArgument argument;


        for (PostmanQuery query : postmanItem.getRequest().getUrl().getQueries()) {
            argument = new HTTPArgument();
            argument.setName(query.getKey());
            argument.setValue(query.getValue());
            argument.setDescription(query.getKey());
            argument.setEnabled(true);
            argument.setMetaData("=");
            argument.setAlwaysEncoded(false);
            argument.setUseEquals(true);
            arguments.addArgument(argument);
        }
        httpSamplerProxy.setArguments(arguments);

        return httpSamplerProxy;
    }

    public static HTTPSamplerProxy newInstance(HarEntry harEntry) {
        HTTPSamplerProxy httpSamplerProxy = new HTTPSamplerProxy();
        httpSamplerProxy.setProperty(TestElement.TEST_CLASS, HTTPSamplerProxy.class.getName());
        httpSamplerProxy.setProperty(TestElement.GUI_CLASS, HttpTestSampleGui.class.getName());
        httpSamplerProxy.setName(harEntry.getRequest().getUrl());
        httpSamplerProxy.setEnabled(true);
        httpSamplerProxy.setAutoRedirects(false);
        httpSamplerProxy.setFollowRedirects(true);
        httpSamplerProxy.setUseKeepAlive(true);
        httpSamplerProxy.setMonitor(false);
        httpSamplerProxy.setDoMultipartPost(false);
        httpSamplerProxy.setComment("");
        httpSamplerProxy.setConnectTimeout("");
        httpSamplerProxy.setResponseTimeout("");
        httpSamplerProxy.setEmbeddedUrlRE("");
        httpSamplerProxy.setContentEncoding("");
        httpSamplerProxy.setMethod(harEntry.getRequest().getMethod().name());
        URL url = null;
        try {
            url = UrlUtils.getUrl(harEntry.getRequest().getUrl());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String domain = UrlUtils.getDomain(url);
        int port = UrlUtils.getPort(url);
        String path = UrlUtils.getPath(url);
        String protocol = UrlUtils.getProtocol(url);
        httpSamplerProxy.setDomain(domain);
        httpSamplerProxy.setPath(path);
        if (port != -1) {
            httpSamplerProxy.setPort(port);
        }
        httpSamplerProxy.setProtocol(protocol);
        Arguments arguments = new Arguments();
        for (HarQueryParam query : harEntry.getRequest().getQueryString()) {
            HTTPArgument argument = new HTTPArgument();
            argument.setName(query.getName());
            argument.setValue(query.getValue());
            argument.setDescription(query.getComment());
            argument.setEnabled(true);
            argument.setMetaData("=");
            argument.setAlwaysEncoded(false);
            argument.setUseEquals(true);
            arguments.addArgument(argument);
        }
        httpSamplerProxy.setArguments(arguments);
        return httpSamplerProxy;
    }
}
