package automation.library.conversion2jmx.common.model.jmx;

import automation.library.conversion2jmx.postman2jmx.model.postman.PostmanHeader;
import de.sstoehr.harreader.model.HarHeader;
import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.gui.HeaderPanel;
import org.apache.jmeter.testelement.TestElement;

import java.util.List;

public class JmxHeaderManager {
    public static HeaderManager newInstancePostman(String name, List<PostmanHeader> headers) {
        HeaderManager headerManager = new HeaderManager();
        headerManager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
        headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
        headerManager.setEnabled(true);
        headerManager.setName(name);

        for (PostmanHeader header : headers) {

            headerManager.add(new Header(header.getKey(), header.getValue()));
        }

        return headerManager;
    }

    public static HeaderManager newInstanceHar(String name, List<HarHeader> headers) {
        HeaderManager headerManager = new HeaderManager();
        headerManager.setProperty(TestElement.GUI_CLASS, HeaderPanel.class.getName());
        headerManager.setProperty(TestElement.TEST_CLASS, HeaderManager.class.getName());
        headerManager.setEnabled(true);
        headerManager.setName(name);

        for (HarHeader header : headers) {
            headerManager.add(new Header(header.getName(), header.getValue()));
        }

        return headerManager;
    }
}
