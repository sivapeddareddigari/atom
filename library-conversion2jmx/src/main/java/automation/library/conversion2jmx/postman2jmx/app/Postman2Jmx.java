package automation.library.conversion2jmx.postman2jmx.app;

import automation.library.common.Property;
import automation.library.conversion2jmx.postman2jmx.builder.JmxFileBuilder;
import automation.library.conversion2jmx.postman2jmx.model.postman.PostmanCollection;
import automation.library.conversion2jmx.postman2jmx.parser.IParser;
import automation.library.conversion2jmx.postman2jmx.parser.ParserFactory;
import automation.library.conversion2jmx.postman2jmx.utils.CollectionVersion;
import automation.library.selenium.exec.Constants;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;


public class Postman2Jmx {


    /**
     * Reads all files which are stored in the /jmeter/postmanFiles/ directory. This directory will be read and all
     * files within it will be parsed and used to create JMX files.
     */
    public void convertAllPostmanScripts() throws Exception {
        // Get all JSON files in postmanFiles directory
        File folder = new File(Property.getProperties(Constants.SELENIUMRUNTIMEPATH).getString("postmanPath"));
        File[] listOfFiles = folder.listFiles();

        // Loop through all files and create a JMX
        for (File file : listOfFiles) {
            if (file.isFile()) {
                createJmx(file);
            }
        }
    }

    /**
     * Reads all files which are stored in the /jmeter/postmanFiles/ directory. This directory will be read and all
     * filenames given as part of @postmanJsonFiles will be located. These files will then be parsed and used to create
     * JMX files.
     *
     * @param postmanJsonFiles List of JSON filenames which exist in the /jmeter/postmanFiles/ directory.
     */
    public void convertSpecificPostmanScripts(List<String> postmanJsonFiles) throws Exception {
        // Get all JSON files in postmanFiles directory
        File folder = new File(Property.getProperties(Constants.SELENIUMRUNTIMEPATH).getString("postmanPath"));
        File[] listOfFiles = folder.listFiles();

        // Loop through all files and create a JMX
        for (File file : listOfFiles) {
            if (file.isFile() && postmanJsonFiles.contains(file.getName())) {
                createJmx(file);
            }
        }
    }

    /**
     * Takes a file as a parameter and uses this file to build a JMX file from. The JMX is then stored in the
     * /jmeter/jmxFiles/ directory with the same filename as the file that was passed as a parameter.
     *
     * @param postmanCollectionJson The json file which is to be parsed and converted
     */
    private void createJmx(File postmanCollectionJson) throws Exception {
        IParser parser = ParserFactory.getParser(CollectionVersion.V2);
        PostmanCollection postmanCollection = parser.parse(postmanCollectionJson.getPath());

        String jmxOutputFile = Property.getProperties(Constants.SELENIUMRUNTIMEPATH).getString("jmxPath") + FilenameUtils.removeExtension(postmanCollectionJson.getName()) + ".jmx";

        JmxFileBuilder jmxFileBuilder = new JmxFileBuilder();
        jmxFileBuilder.build(postmanCollection, jmxOutputFile);
    }


}
