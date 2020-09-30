// 
// Decompiled by Procyon v0.5.36
// 

package automation.library.conversion2jmx.har2jmx.service;

import org.slf4j.LoggerFactory;
import java.io.File;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import org.slf4j.Logger;

public class HarParser extends AbstractParser
{
    private static Logger logger;
    
    @Override
    public Har parse(String harFile) throws Exception {
        try {
            return this.readValue(harFile);
        }
        catch (HarReaderException e) {
            HarParser.logger.error("Error occurred while reading har file!", e.getCause());
            throw new Exception(e.getMessage());
        }
    }
    
    @Override
    public Har parse(File harFile) throws Exception {
        try {
            return this.readValue(harFile);
        }
        catch (HarReaderException e) {
            HarParser.logger.error("Error occurred while reading har file!", e.getCause());
            throw new Exception(e.getMessage());
        }
    }
    
    static {
        HarParser.logger = LoggerFactory.getLogger(HarParser.class.getName());
    }
}
