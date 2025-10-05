package com.testimium.tool.report.generator;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.exception.FileWriterException;
import com.testimium.tool.html.IHtmlGenerator;
import com.testimium.tool.html.generator.DashboardReportHtmlGenerator;
import com.testimium.tool.html.generator.TestReportHtmlGenerator;
import com.testimium.tool.report.domain.BatchData;
import com.testimium.tool.report.domain.TestSuiteData;
import com.testimium.tool.utility.FileUtility;
import com.testimium.tool.writer.FileWriterFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportGeneratorBackup {

    private static ReportGeneratorBackup reportGenerator = null;
    private String reportDir = "./Reports";
    private String screenshotDir = "./Reports/Screenshots";
    private String batchDir = "";

    private String dashboardReport = "Test-Report-Dashboad.html";

    private ExtentReports extentReport;

    private ExtentHtmlReporter testsuiteHtmlReport;

    private ExtentTest extentTest;
    private ExtentTest childNodeLevel1;
    private String testsuiteName;
    private static int batchCount;
    private static int testsuiteCount;
    private static String batchName;
    private List<BatchData> batchDataList = new ArrayList<>();
    private List<TestSuiteData> testSuiteDataList;
    private BatchData batchData = null;
    private TestSuiteData testSuiteData = null;

    public static ReportGeneratorBackup startNewReport(){
        reportGenerator = new ReportGeneratorBackup();
        return reportGenerator;
    }
    public static ReportGeneratorBackup startNewReport(String batchName){
        if(!batchName.equals(ReportGeneratorBackup.batchName)) {
            if(null != ReportGeneratorBackup.batchName) {
                endBatch();
            }
            testsuiteCount = 0;
            reportGenerator = new ReportGeneratorBackup();
            reportGenerator.startNewBatch(batchName);
        }
        return reportGenerator;
    }

    public static ReportGeneratorBackup getReportInstance(){
        return reportGenerator;
    }

    public void startNewBatch(String batchName){
        batchCount = getPreviousReportCount();
        //batchCount++;
        this.batchName = batchCount + "-" +  batchName;
        batchData = new BatchData();
        batchData.setName(batchName);
        //ReportGenerator.batchName = this.batchName;
        testSuiteDataList = new ArrayList<>();
        this.batchDir = reportDir + "/" + this.batchName;
        batchData.setLocation(batchDir);
        boolean isDirrCeated = FileUtility.createDirectory(this.batchDir);
    }

    public static void endBatch() {
        if(null != reportGenerator && null != reportGenerator.batchData) {
            reportGenerator.endTestsuite();
            reportGenerator.prepareBatchTestCount();
            reportGenerator.batchData.setReportDataList(reportGenerator.testSuiteDataList);
            System.out.println("Batch Date: --- " + reportGenerator.batchData.toString());
            reportGenerator.batchDataList.add(reportGenerator.batchData);
            regenerateReport(true);
        }
    }

    public static void regenerateReport(boolean isBatchComplete) {
        try {
            if(null != reportGenerator && null != reportGenerator.batchData) {

                refreshTestsuiteToReport();
                reportGenerator.batchData.setReportDataList(reportGenerator.testSuiteDataList);

                IHtmlGenerator htmlGenerator = new TestReportHtmlGenerator();
                htmlGenerator.setData(reportGenerator.batchData);
                htmlGenerator.generateHtml();

                if (isBatchComplete) {
                    reportGenerator.batchData.setReportDataList(null);
                }

                FileWriterFactory.getInstance().writeFile("JSON", "DEFAULT", reportGenerator.batchData.getLocation() + "/batch" + ".json", reportGenerator.batchData);

                new DashboardReportHtmlGenerator().generateHtml();
            }
        } catch (FileWriterException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }

    private static void refreshTestsuiteToReport() {
        if(null != reportGenerator.testSuiteDataList) {
            if(reportGenerator.testSuiteDataList.size() > 0) {
                TestSuiteData existingTestSuiteData = reportGenerator.testSuiteDataList.get(reportGenerator.testSuiteDataList.size() - 1);
                if (null != reportGenerator.testSuiteData && reportGenerator.testSuiteData.getName().equals(existingTestSuiteData.getName())) {
                    existingTestSuiteData = reportGenerator.testSuiteData;
                } else if (null != reportGenerator.testSuiteData) {
                    reportGenerator.testSuiteDataList.add(reportGenerator.testSuiteData);
                }
            } else if (null != reportGenerator.testSuiteData) {
                reportGenerator.testSuiteDataList.add(reportGenerator.testSuiteData);
            }
        }
    }

    public void startNewTestsuite(String testsuiteName){
        flush();
        endTestsuite();
        testSuiteData = new TestSuiteData();
        testsuiteCount++;
        this.testsuiteName = testsuiteCount + "-" +  testsuiteName;
        testSuiteData.setName( this.testsuiteName);
        TestContext.getTestContext("").setTestSuiteName(batchDir + "/" + this.testsuiteName);
        testSuiteData.setLocation(this.batchDir);
        testSuiteData.setFilePath(batchDir + "/" + this.testsuiteName + ".html");
        this.testsuiteHtmlReport = new ExtentHtmlReporter(TestContext.getTestContext("").getTestSuiteName() + ".html");
        this.extentReport = new ExtentReports();
        this.extentReport.attachReporter(testsuiteHtmlReport);
    }

    public void endTestsuite()  {
        if(null != testSuiteData) {
            flush();
            prepareTestsuiteTestCount();
            System.out.println("Testsuite Data: --- " + testSuiteData.toString());
            refreshTestsuiteToReport();
            //testSuiteDataList.add(testSuiteData);

            /*try {
                FileWriterFactory.getInstance().writeFile("JSON", "DEFAULT", reportGenerator.batchData.getLocation() + "/batch" + ".json", reportGenerator.batchData);
            } catch (FileWriterException e) {
                e.printStackTrace();
                //throw new RuntimeException(e);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                //throw new RuntimeException(e);
            }*/
            //regenerateReport(false);
            /*try {
                new DashboardReportHtmlGenerator().generateHtml();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
        }
    }


    /**
     * Add the test in the report with description
     * @param testName
     * @param description
     */
    /*PASS,
    FAIL,
    FATAL,
    ERROR,
    WARNING,
    INFO,
    DEBUG,
    SKIP;*/
    public void createTest(String testName, String description){
        flush();
        if(null != extentTest) {
            prepareBatchTestCount();
            prepareTestsuiteTestCount();
        }
        this.extentTest = extentReport.createTest(testName, description);
        this.extentTest.assignCategory(this.testsuiteName);
        this.extentReport.flush();
    }

    private void prepareBatchTestCount() {
        if(null != extentTest) {
            switch (extentTest.getStatus().toString().toUpperCase()) {
                case "PASS":
                    batchData.setTotalPassed(batchData.getTotalPassed() + 1);
                    //batchData.setTotalTest(batchData.getTotalTest() + 1);
                    break;
                case "FAIL":
                    batchData.setTotalFailed(batchData.getTotalFailed() + 1);
                    //batchData.setTotalTest(batchData.getTotalTest() + 1);
                    break;
                case "SKIP":
                    batchData.setTotalSkipped(batchData.getTotalSkipped() + 1);
                    //batchData.setTotalTest(batchData.getTotalTest() + 1);
                    break;
            }
            batchData.setTotalTest(batchData.getTotalPassed() + batchData.getTotalFailed() + batchData.getTotalSkipped());
            batchData.setTotalPassedRate();
            batchData.setTestEndTime();
            batchData.setTotalDuration();
        }
    }

    private void prepareTestsuiteTestCount() {
        switch (extentTest.getStatus().toString().toUpperCase()) {
            case "PASS":
                testSuiteData.setTotalPassed(testSuiteData.getTotalPassed() + 1);
                //testSuiteData.setTotalTest(testSuiteData.getTotalTest() + 1);
                break;
            case "FAIL":
                testSuiteData.setTotalFailed(testSuiteData.getTotalFailed() + 1);
                //testSuiteData.setTotalTest(testSuiteData.getTotalTest() + 1);
                break;
            case "SKIP":
                testSuiteData.setTotalSkipped(testSuiteData.getTotalSkipped() + 1);
                //testSuiteData.setTotalTest(testSuiteData.getTotalTest() + 1);
                break;
        }
        testSuiteData.setTotalTest(testSuiteData.getTotalPassed() + testSuiteData.getTotalFailed() + testSuiteData.getTotalSkipped());
        testSuiteData.setTotalPassedRate();
        testSuiteData.setTestEndTime();
        testSuiteData.setTotalDuration();
    }

    public ExtentTest getCurrentTest(){
        flush();
        return this.extentTest;
    }

    public ExtentTest getChildNodeLevel1() {
        flush();
        return childNodeLevel1;
    }

    public void setChildNodeLevel1(ExtentTest childNodeLevel1) {
        this.childNodeLevel1 = childNodeLevel1;
    }

    public void createChildNode(String nodeName) {
        flush();
        this.childNodeLevel1 = extentTest.createNode(nodeName);
    }

    public void flush(){
        if(null != extentReport) {
            this.extentReport.flush();
        }
    }

    public static void endReport(){
        endBatch();
        try {
            new DashboardReportHtmlGenerator().generateHtml();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int getPreviousReportCount() {
        File inputSrc = new File("./Reports");
        String[] dirs = FileUtility.getAllDirectory(inputSrc);
        /*for(int itr = 0; itr < dirs.length; itr++) {

        }*/
        int reportFolderCnt = 0;
        if(null != dirs && dirs.length > 0){
            String[] folderName = dirs[dirs.length-1].split("-");
            try {
                reportFolderCnt = Integer.parseInt(folderName[0]);
                reportFolderCnt += 1;
            }catch (NumberFormatException ex) {
                reportFolderCnt += 1;
                ex.printStackTrace();
            }

        } else {
            reportFolderCnt += 1;
        }
        return reportFolderCnt;
    }
}
