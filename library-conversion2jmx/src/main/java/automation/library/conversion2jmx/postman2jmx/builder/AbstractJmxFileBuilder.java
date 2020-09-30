package automation.library.conversion2jmx.postman2jmx.builder;

import automation.library.conversion2jmx.common.config.Conversion2JmxConfig;
import automation.library.conversion2jmx.postman2jmx.exception.NoPostmanCollectionItemException;
import automation.library.conversion2jmx.postman2jmx.exception.NullPostmanCollectionException;
import automation.library.conversion2jmx.common.model.jmx.*;
import automation.library.conversion2jmx.postman2jmx.model.postman.PostmanCollection;
import automation.library.conversion2jmx.postman2jmx.model.postman.PostmanItem;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.extractor.json.jsonpath.JSONPostProcessor;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jorphan.collections.HashTree;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJmxFileBuilder implements IJmxFileBuilder {

    protected JmxFile buildJmxFile(PostmanCollection postmanCollection, String jmxOutputFilePath) throws Exception {
        if (postmanCollection == null) {
            throw new NullPostmanCollectionException();
        }

        if (postmanCollection.getItems() == null || postmanCollection.getItems().isEmpty()) {
            throw new NoPostmanCollectionItemException();
        }

        Conversion2JmxConfig config = new Conversion2JmxConfig();
        config.setJMeterHome();

        // TestPlan
        TestPlan testPlan = JmxTestPlan.newInstance(postmanCollection.getInfo() != null ? postmanCollection.getInfo().getName() : "");

        // ThreadGroup controller
        LoopController loopController = JmxLoopController.newInstance();

        // ThreadGroup
        ThreadGroup threadGroup = JmxThreadGroup.newInstance(loopController);

        // HTTPSamplerProxy
        List<HTTPSamplerProxy> httpSamplerProxies = new ArrayList<>();
        List<HeaderManager> headerManagers = new ArrayList<>();

        for (PostmanItem item : postmanCollection.getItems()) {
            IJmxBodyBuilder bodyBuilder = JmxBodyBuilderFactory.getJmxBodyBuilder(item);
            HTTPSamplerProxy httpSamplerProxy = bodyBuilder.buildJmxBody(item);
            httpSamplerProxies.add(httpSamplerProxy);

            headerManagers.add(JmxHeaderManager.newInstancePostman(item.getName(), item.getRequest().getHeaders()));
        }

        // Create TestPlan hash tree
        HashTree testPlanHashTree = new HashTree();
        testPlanHashTree.add(testPlan);

        // Add ThreadGroup to TestPlan hash tree
        HashTree threadGroupHashTree = new HashTree();
        threadGroupHashTree = testPlanHashTree.add(testPlan, threadGroup);

        // Add Http Sampler to ThreadGroup hash tree
        HashTree httpSamplerHashTree = new HashTree();

        // Add header manager hash tree
        HashTree headerHashTree = null;

        // Add Java Sampler to ThreadGroup hash tree
        for (int i = 0; i < httpSamplerProxies.size(); i++) {
            HTTPSamplerProxy httpSamplerProxy = httpSamplerProxies.get(i);
            HeaderManager headerManager = headerManagers.get(i);

            httpSamplerHashTree = threadGroupHashTree.add(httpSamplerProxy);

            headerHashTree = new HashTree();
            headerHashTree = httpSamplerHashTree.add(headerManager);

            PostmanItem postmanItem = postmanCollection.getItems().get(i);
            if (!postmanItem.getEvents().isEmpty()) {
                List<JSONPostProcessor> jsonPostProcessors = JmxJsonPostProcessor.getJsonPostProcessors(postmanItem);
                httpSamplerHashTree.add(jsonPostProcessors);
            }
        }

        File file = new File(jmxOutputFilePath);
        OutputStream os = new FileOutputStream(file);
        SaveService.saveTree(testPlanHashTree, os);

        InputStream is = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        for (int len = 0; (len = is.read(buffer)) != -1; ) {
            bos.write(buffer, 0, len);
        }
        bos.flush();

        byte[] data = bos.toByteArray();
        JmxFile jmxFile = new JmxFile(data, testPlanHashTree);

        os.close();
        is.close();
        bos.close();

        return jmxFile;
    }

}
