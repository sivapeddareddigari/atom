package automation.library.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class FileHelper {
    private static Logger log = LogManager.getLogger(FileHelper.class);

    public static void mergeFiles(File[] files, File mergedFile) {

        FileWriter fstream = null;
        BufferedWriter out = null;
        try {
            fstream = new FileWriter(mergedFile, true);
            out = new BufferedWriter(fstream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        for (File f : files) {
            log.debug("merging: " + f.getName());
            FileInputStream fis;
            try {
                fis = new FileInputStream(f);
                BufferedReader in = new BufferedReader(new InputStreamReader(fis));

                String aLine;
                while ((aLine = in.readLine()) != null) {
                    out.write(aLine);
                    out.newLine();
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
