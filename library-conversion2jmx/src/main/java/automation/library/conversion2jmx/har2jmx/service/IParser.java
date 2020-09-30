// 
// Decompiled by Procyon v0.5.36
// 

package automation.library.conversion2jmx.har2jmx.service;

import java.io.File;
import de.sstoehr.harreader.model.Har;

public interface IParser
{
    Har parse(String p0) throws Exception;
    
    Har parse(File p0) throws Exception;
}
