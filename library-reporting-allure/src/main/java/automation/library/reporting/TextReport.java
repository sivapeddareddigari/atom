package automation.library.reporting;

import automation.library.common.Property;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportParser;
import net.masterthought.cucumber.ReportResult;
import net.masterthought.cucumber.Reportable;
import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.sorting.SortingMethod;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * class to create the plain test execution report (suitable for email)
 */
public class TextReport {

    public TextReport() {
        String cucumberJsonDir = System.getProperty("user.dir") + File.separator + "RunReports/cucumberJson";
        try (Stream<Path> walk = Files.walk(Paths.get(cucumberJsonDir))) {

            jsonFiles = walk.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        Configuration configuration = new Configuration(new File(""), "");
        configuration.setSortingMethod(SortingMethod.NATURAL);

        //parse the json files
        ReportParser reportParser = new ReportParser(configuration);
        features = reportParser.parseJsonFiles(jsonFiles);

        ReportResult reportResult = new ReportResult(features, configuration);
        reportable = reportResult.getFeatureReport();
    }

    private List<String> jsonFiles;
    private List<Feature> features;
    private Reportable reportable;

    private String newlineChar = "\n";
    private StringBuilder sb = new StringBuilder();
    private StringBuilder sbl = new StringBuilder();

    private int countFeature = 0;
    private int countFeatureFail = 0;
    private int countFeaturePass = 0;
    private int countFeatureOther = 0;

    private int countScenario = 0;
    private int countScenarioFail = 0;
    private int countScenarioPass = 0;
    private int countScenarioOther = 0;

    private int countStep = 0;
    private int countStepFail = 0;
    private int countStepPass = 0;
    private int countStepSkip = 0;
    private int countStepPending = 0;
    private int countStepUndefined = 0;

    private int longestFeatureName = 10;
    private int longestScenarioName = 19;


    public void tempM() {
        createReport(true);
    }

    private void generateReport() {

        checkMaxName(features);
        getCounter(reportable);

        //summary results - High level
        int summaryPad = 9;
        String summaryPadding = StringUtils.leftPad("", (4 * (summaryPad + 3) + 14), '-');
        String pat = "| %-" + summaryPad + "s ";

        sb.append("Run Result Summary");
        sb.append(newlineChar);
        sb.append(summaryPadding);
        sb.append(newlineChar);
        sb.append(String.format("| %-" + 10 + "s " + pat + pat + pat + pat + "|", "Status", "Passed", "Failed", "Other", "Total"));
        sb.append(newlineChar);
        sb.append(summaryPadding);
        sb.append(newlineChar);
        sb.append(String.format("| %-" + 10 + "s " + pat + pat + pat + pat + "|", "Features", countFeaturePass, countFeatureFail, countFeatureOther, countFeature));
        sb.append(newlineChar);
        sb.append(String.format("| %-" + 10 + "s " + pat + pat + pat + pat + "|", "Scenarios", countScenarioPass, countScenarioFail, countScenarioOther, countScenario));
        sb.append(newlineChar);
        sb.append(summaryPadding);
        sb.append(newlineChar);
        sb.append(newlineChar);

        //summary results - Feature Detailed
        String patx = "  %-" + summaryPad + "s ";
        String patxs = " %-" + summaryPad + "s ";

        String pat1 = "| %-" + longestFeatureName + "s ";
        String fSummaryPadding = StringUtils.leftPad("", (11 * (summaryPad + 3) + longestFeatureName + 7), '-');


        sb.append("Run Result Summary: Feature Detailed");
        sb.append(newlineChar);
        sb.append(fSummaryPadding);
        sb.append(newlineChar);
        sb.append(String.format(pat1 + "||" + patxs + patx + patx + patx + patx + patx + "||" + patxs + patx + patx + "||" + patxs + patx + "|", "", "Steps", "", "", "", "", "", "Scenarios", "", "", "Feature", ""));
        sb.append(newlineChar);
        sb.append(String.format(pat1 + "|" + pat + pat + pat + pat + pat + pat + "|" + pat + pat + pat + "|" + pat + pat + "|", "Feature", "Passed", "Failed", "Skipped", "Pending", "Undefined", "Total", "Passed", "Failed", "Total", "Duration", "Status"));
        sb.append(newlineChar);
        sb.append(fSummaryPadding);
        sb.append(newlineChar);

        //Run Results detailed at scenario level
        sbl.append("Run Result Listing");
        sbl.append(newlineChar);
        String listingPadding = StringUtils.leftPad("", (longestScenarioName + 35), '-');
        sbl.append(listingPadding);
        sbl.append(newlineChar);
        sbl.append(String.format("|%-" + longestScenarioName + "s       | %-10s | %-10s |", "Feature -> Scenario", "Status", "Duration"));
        sbl.append(newlineChar);
        sbl.append(listingPadding);

        sbl.append(newlineChar);
        for (Feature f : features) {
            String featureName = f.getName();

            int passedSteps = f.getPassedSteps();
            int failedSteps = f.getFailedSteps();
            int skippedSteps = f.getSkippedSteps();
            int pendingSteps = f.getPendingSteps();
            int undefinedSteps = f.getUndefinedSteps();
            int totalSteps = f.getSteps();

            int failedScenario = f.getFailedScenarios();
            int passedScenario = f.getPassedScenarios();
            int totalScenario = f.getScenarios();

            String duration = f.getFormattedDuration();
            String featureStatus = f.getStatus().getLabel();

            //add feature
            sb.append(String.format(pat1 + "|" + pat + pat + pat + pat + pat + pat + "|" + pat + pat + pat + "|" + pat + pat + "|",
                    featureName, passedSteps, failedSteps, skippedSteps, pendingSteps, undefinedSteps,
                    totalSteps, passedScenario, failedScenario, totalScenario, duration, featureStatus));

            sb.append(newlineChar);

            sbl.append(String.format("|%-" + longestScenarioName + "s       | %-10s | %-10s |", featureName, featureStatus, duration));
            sbl.append(newlineChar);
            Element[] es = f.getElements();

            //add all the scenarios
            for (Element e : es) {
                sbl.append(String.format("|  -> %-" + longestScenarioName + "s  | %-10s | %-10s |", e.getName(), e.getStatus().getLabel(), e.getFormattedDuration()));
                sbl.append(newlineChar);
            }
            sbl.append(newlineChar);
        }

        sb.append(fSummaryPadding);
        sb.append(newlineChar);
        sb.append(String.format(pat1 + "|" + pat + pat + pat + pat + pat + pat + "|" + pat + pat + pat + "|" + pat + pat + "|", "Total", countStepPass, countStepFail, countStepSkip, countStepPending, countStepUndefined, countStep, countScenarioPass, countScenarioFail, countScenario, reportable.getFormattedDuration(), countFeature));
        sb.append(newlineChar);
        sb.append(String.format(pat1 + "|" + pat + pat + pat + pat + pat + pat + "|" + pat + pat + pat + "|" + pat + pat + "|", "%", ((countStepPass * 100) / countStep) + "%", ((countStepFail * 100) / 100) + "%",
                ((countStepSkip * 100) / countStep) + "%", ((countStepPending * 100) / countStep) + "%", ((countStepUndefined * 100) / countStep) + "%", "", ((countScenarioPass * 100) / countScenario) + "%",
                ((countScenarioFail * 100) / countScenario) + "%", "", "", ((countFeaturePass * 100) / countFeature) + "%"));
        sb.append(newlineChar);
        sb.append(fSummaryPadding);
        sb.append(newlineChar);
        sb.append(newlineChar);

        sbl.append(listingPadding);
        int x = longestScenarioName + 33;
        sbl.append(newlineChar);
        String finished = "Finished at: " + new Date();
        sbl.append(String.format("|%-" + x + "s|", finished));
        sbl.append(newlineChar);
        sbl.append(listingPadding);
        sb.append(sbl);
    }

    private void getCounter(Reportable reportable) {
        countFeaturePass = reportable.getPassedFeatures();
        countFeatureFail = reportable.getFailedFeatures();
        countFeature = reportable.getFeatures();
        countFeatureOther = countFeature - countFeaturePass - countFeatureFail;

        countScenarioFail = reportable.getFailedScenarios();
        countScenarioPass = reportable.getPassedScenarios();
        countScenario = reportable.getScenarios();
        countScenarioOther = countScenario - countScenarioFail - countScenarioPass;

        countStep = reportable.getSteps();
        countStepPass = reportable.getPassedSteps();
        countStepFail = reportable.getFailedSteps();
        countStepSkip = reportable.getSkippedSteps();
        countStepPending = reportable.getPendingSteps();
        countStepUndefined = reportable.getUndefinedSteps();
    }

    private void checkMaxName(List<Feature> features) {
        for (Feature f : features) {
            longestFeatureName = Math.max(f.getName().length(), longestFeatureName);
            Element[] es = f.getElements();
            for (Element e : es) {
                longestScenarioName = Math.max(e.getName() == null ? 0 : e.getName().length(), longestScenarioName);
            }
        }

    }

    public void createReport(String path) {
        generateReport();
        printReport();
        saveReport(path);
    }

    public void createReport(boolean saveReport) {
        generateReport();
        printReport();
        if (saveReport) saveReport();
    }

    private void saveReport() {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String filename = "RunSummary_" + format.format(new Date());

        String defaultReportPath = System.getProperty("user.dir") + File.separator + "RunReports" + File.separator;
        String reportPath = Property.getVariable("textReportPath");
        saveReport((reportPath == null ? defaultReportPath : reportPath) + filename);
    }

    //Saves the report to file
    private void saveReport(String path) {

        File file = new File(path);
        if (file.getParentFile() != null && !file.getParentFile().isDirectory()) {
            boolean ok = file.getParentFile().mkdirs() || file.getParentFile().isDirectory();
            if (!ok) {
                try {
                    throw new IOException("Failed to create directory " + file.getParentFile().getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try (PrintWriter printer = new PrintWriter(path)) {
            printer.println(sb.toString());
            System.out.println("report saved at :" + path);
        } catch (FileNotFoundException e) {
            System.err.println("Unable to save text report to file!: " + path);
            throw new RuntimeException(e);
        }
    }

    //prints the report to console
    private void printReport() {
        System.out.println(sb.toString());
    }

    //converts long into readable format
    private String convertTime(long diff) {
        long secs = diff / 1000;
        long millis = diff % 1000;
        long mins = secs / 60;
        secs = secs % 60;
        long hours = mins / 60;
        mins = mins % 60;

        return hours + "h " + mins + "m " + secs + "s " + millis + "ms";
    }

    //used if on a different OS or file system
    public void setNewLineChar(String chars) {
        newlineChar = chars;
    }


}
