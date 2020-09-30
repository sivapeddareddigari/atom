package automation.library.conversion2jmx.har2jmx.service;

import automation.library.conversion2jmx.common.model.jmx.JmxFile;
import de.sstoehr.harreader.model.Har;

public class JmxFileCreator extends AbstractJmxFileCreator
{
    @Override
    public JmxFile create(Har har, String jmxOutputFilePath) throws Exception {
        return super.createJmxFile(har, jmxOutputFilePath);
    }
}
