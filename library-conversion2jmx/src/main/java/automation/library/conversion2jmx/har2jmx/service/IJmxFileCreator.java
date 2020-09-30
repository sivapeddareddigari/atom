// 
// Decompiled by Procyon v0.5.36
// 

package automation.library.conversion2jmx.har2jmx.service;

import automation.library.conversion2jmx.common.model.jmx.JmxFile;
import de.sstoehr.harreader.model.Har;

public interface IJmxFileCreator
{
    JmxFile create(Har p0, String p1) throws Exception;
}
