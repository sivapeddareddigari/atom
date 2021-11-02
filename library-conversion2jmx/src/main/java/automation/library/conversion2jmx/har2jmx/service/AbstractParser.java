// 
// Decompiled by Procyon v0.5.36
// 

package automation.library.conversion2jmx.har2jmx.service;

import java.io.File;

import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.HarReader;

public abstract class AbstractParser implements IParser
{
    private HarReader harReader;
    
    public AbstractParser() {
        this.harReader = new HarReader();
    }
    
    protected Har readValue(String fileName) throws HarReaderException {
        return this.harReader.readFromString(fileName);
    }
    
    protected Har readValue(File file) throws HarReaderException {
        return this.harReader.readFromFile(file);
    }
}
