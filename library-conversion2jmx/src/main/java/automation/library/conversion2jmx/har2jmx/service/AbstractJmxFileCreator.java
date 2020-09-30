package automation.library.conversion2jmx.har2jmx.service;

import java.io.InputStream;
import java.io.OutputStream;

import automation.library.conversion2jmx.common.config.Conversion2JmxConfig;
import automation.library.conversion2jmx.common.model.jmx.*;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.testelement.TestPlan;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import org.apache.jmeter.save.SaveService;
import java.io.FileOutputStream;
import java.io.File;
import org.apache.jorphan.collections.HashTree;
import java.util.List;
import de.sstoehr.harreader.model.HarEntry;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy;
import java.util.ArrayList;
import automation.library.conversion2jmx.har2jmx.exception.NoHarEntryException;
import automation.library.conversion2jmx.har2jmx.exception.NullHarModelException;
import de.sstoehr.harreader.model.Har;

public abstract class AbstractJmxFileCreator implements IJmxFileCreator
{
    protected JmxFile createJmxFile(Har har, String jmxOutputFilePath) throws Exception {
        if (har == null) {
            throw new NullHarModelException();
        }
        if (har.getLog() == null || har.getLog().getEntries().isEmpty()) {
            throw new NoHarEntryException();
        }
        Conversion2JmxConfig config = new Conversion2JmxConfig();
        config.setJMeterHome();
        TestPlan testPlan = JmxTestPlan.newInstance("HAR2JMX");
        LoopController loopController = JmxLoopController.newInstance();
        ThreadGroup threadGroup = JmxThreadGroup.newInstance(loopController);
        List<HTTPSamplerProxy> httpSamplerProxies = new ArrayList<HTTPSamplerProxy>();
        List<HeaderManager> headerManagers = new ArrayList<HeaderManager>();
        for (HarEntry harEntry : har.getLog().getEntries()) {
            HTTPSamplerProxy httpSamplerProxy = JmxHTTPSamplerProxy.newInstance(harEntry);
            httpSamplerProxies.add(httpSamplerProxy);
            headerManagers.add(JmxHeaderManager.newInstanceHar("HAR2JMX", harEntry.getRequest().getHeaders()));
        }
        HashTree testPlanHashTree = new HashTree();
        testPlanHashTree.add(testPlan);
        HashTree threadGroupHashTree = new HashTree();
        threadGroupHashTree = testPlanHashTree.add(testPlan, threadGroup);
        HashTree httpSamplerHashTree = new HashTree();
        HashTree headerHashTree = null;
        for (int i = 0; i < httpSamplerProxies.size(); ++i) {
            HTTPSamplerProxy httpSamplerProxy2 = httpSamplerProxies.get(i);
            HeaderManager headerManager = headerManagers.get(i);
            httpSamplerHashTree = threadGroupHashTree.add(httpSamplerProxy2);
            headerHashTree = new HashTree();
            headerHashTree = httpSamplerHashTree.add(headerManager);
        }
        File file = new File(jmxOutputFilePath);
        OutputStream os = new FileOutputStream(file);
        SaveService.saveTree(testPlanHashTree, os);
        InputStream is = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
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
