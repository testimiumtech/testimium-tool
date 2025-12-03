package com.testimium.tool.base;

import com.aventstack.extentreports.Status;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.domain.testcase.ExcelTestCase;
import com.testimium.tool.exception.HandleFailOverTestExecution;
import com.testimium.tool.exception.ShutdownTestExecution;
import com.testimium.tool.exception.TestException;
import com.testimium.tool.helper.TestCaseHelper;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.parser.ExcelParser;
import com.testimium.tool.reader.PropertyReader;
import com.testimium.tool.report.generator.ReportGenerator;
import com.testimium.tool.utility.PropertyUtility;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

public class MasterTestCase /*implements ITest*/ {
    public static TestContext testContext;
    public static ThreadLocal<String> testName = new ThreadLocal<>();
    //public static ExtentHtmlReporter htmlReports;
    //protected ITestContext ctx;
    //public static ExtentReports extent;
    //public static ExtentTest test;
    //public static ExtentTest parentTest;

    //public static String reportPath = new File("").getAbsolutePath().trim()+"/Reports/";

    //@BeforeTest
    public void beforeTest() {
        new PropertyReader();
        LogUtil.logToolMsg("Initializing Tool...........");
        this.testContext = TestContext.getTestContext("");
        //Date date = new Date();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
       /* htmlReports = new ExtentHtmlReporter(reportPath+"Report-"+ DateUtility.getDateForFileName()+".html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReports);
        this.testContext = TestContext.getTestContext("");
        LogUtil.logToolMsg("Report Initialized.... :" + htmlReports.getFilePath());*/

        //try {
            /*if(null != PropertyReader.getProperty("enable.datasource.connection")
                    && "true".equalsIgnoreCase(PropertyReader.getProperty("enable.datasource.connection"))) {*/
        if(PropertyUtility.isDBConnectionEnabled()) {
            //DataSourceRegistry.buildConnectorMap();
            this.testContext.setMainConnectorKey();
        }
       /* } catch (DBConnectorException ex) {
            //TODO Fix Me
            isDBConnectionFailed = true;
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
    }

    public static void process(ExcelParser excelParser) throws ShutdownTestExecution, TestException {
        process(excelParser, false);
    }
    /**
     *
     * @param excelParser
     * @throws TestException
     * @throws ShutdownTestExecution
     */
    public static void process(ExcelParser excelParser, boolean isNestedNodeEnabled) throws ShutdownTestExecution, TestException {
        //ExcelReader reader = new ExcelReader(fileName);
        //reader.read();
        //try {
            //excelParser.getAllSheets().forEach(name -> {
            List<String> sheetList = excelParser.getAllSheets();
            for(int itr = 0; itr < sheetList.size(); itr++) {
                try {
                    List<ExcelTestCase> testCases = excelParser.getTestCasesBySheetName(sheetList.get(itr).trim());
                    if(null != testCases && testCases.size() > 0) {
                        ReportGenerator reportGenerator = ReportGenerator.getReportInstance();
                        reportGenerator.startNewTestsuite(sheetList.get(itr));
                        //testContext.setAssigneCatogary(excelParser.getFileName().substring(0, excelParser.getFileName().lastIndexOf(".")) + "-->" + sheetList.get(itr));
                        //processTest(excelParser, sheetList.get(itr), false);
                        processTest(testCases, excelParser.getFileName(), sheetList.get(itr), isNestedNodeEnabled, false);
                    }
                } catch (IOException ex) {
                    System.out.println("========================1 TODO Fix Exception and Log===================================");
                    ex.printStackTrace();
                    TestContext.getTestContext("").getTestStepErrorLog().append(ex);
                } /*catch (ShutdownTestExecution ex) {
                    System.out.println("========================2 TODO Fix Exception and Log===================================");
                    ReportGenerator.getReportInstance().createTest("ShutdownTestExecution Test Error", "Exception or Error occurred");
                    ReportGenerator.getReportInstance().getCurrentTest().fail(ex.getMessage());
                    throw new RuntimeException(ex.getMessage());
                } catch (TestException ex) {
                    System.out.println("========================2.1 TODO Fix Exception and Log===================================");
                    ReportGenerator.getReportInstance().createTest("Test Error", "Exception or Error occurred");
                    ReportGenerator.getReportInstance().getCurrentTest().fail(ex.getMessage());
                    throw new RuntimeException(ex.getMessage());
                }*/
                //ReportGenerator.getReportInstance().endTestsuite();
            }
            //);
       /* } catch (RuntimeException ex) {
            throw new ShutdownTestExecution(ex.getMessage());
        }*/
    }

    public static void processTest(List<ExcelTestCase> testCases, String fileName, String sheetName, boolean isAppendTestcase, boolean isFailOverStep) throws IOException, ShutdownTestExecution, TestException {
        ReportGenerator reportGenerator = ReportGenerator.getReportInstance();
        //TestContext.getTestContext("").getFailOver().put("TestSuiteLevel", null);
        int skipNumberOfNextTestCases = 0;
        int failNumberOfNextTestCases = 0;
        if(StringUtils.isNotEmpty(sheetName)) {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            //List<ExcelTestCase> testCases = excelReader.getTestCasesBySheetName(sheetName.trim());

            for (int itr = 0; itr < testCases.size(); itr++) {

                ExcelTestCase testCase = testCases.get(itr);
                testCase.setFailOverTest(isFailOverStep);
                //List<String> executeTestCmdHierarchy = TestContext.getTestContext("").getExecuteTestCmdHierarchy();
                /*if((isAppendTestcase && executeTestCmdHierarchy.size() > 0) || !isAppendTestcase && executeTestCmdHierarchy.size() == 0) {
                    testContext.setTestCaseName(testCase.getTestCaseName());
                } else {
                    testContext.setTestCaseName(testContext.getTestCaseName());
                }*/

                if (fileName.contains("AllInOne") && !isAppendTestcase){
                    //FileUtility.createDirectory(testCase.getTestCaseName());
                    ReportGenerator.getReportInstance().startNewReport(testCase.getTestCaseName());
                    reportGenerator = ReportGenerator.getReportInstance();
                    reportGenerator.startNewTestsuite(sheetName);
                }

                if(!isAppendTestcase) {
                    testContext.setTestCaseName(testCase.getTestCaseName());
                    LogUtil.logTestCaseMsg("TestCase started: " + testCase.getTestCaseName());
                    LogUtil.logTestCaseMsg("Testcase Data: " + testCase.toString());
                    //testName.set(testContext.getTestCaseName());
                    //ctx.setAttribute("testName", testName.get());
                    reportGenerator.createTest(testContext.getTestCaseName(),
                            "<H5>TestSuite Name : </H5> \n <p>" + sheetName + "</p>"
                                    + "<H5>Test Case ID : </H5> \n <p>" + testCase.getTestCaseId() + "</p>"
                                    + "<H5>Module : </H5> \n <p>" + testCase.getModule() + "</p>"
                                    + "<H5>Sub-Module : </H5> \n <p>" + testCase.getSubModule() + "</p>"
                                    + "<H5>Test Description</H5> \n <p>" + testCase.getDescription() + "</p>"
                                    + "<H5>Pre-Condition</H5> \n <p>" + testCase.getPreCondition() + "</p>"
                                    + "<H5>Manual Test Step</H5> \n" + testCase.getManualTestStep()
                                    + "<H5>Expected Result</H5> \n <p>" + testCase.getExpectedResult() + "</p>");
                }


                /*parentTest = extent.createTest(testCase.getTestCaseName(),
                        "<H5>TestSuite Name : </H5> \n <p>" + sheetName + "</p>"
                                + "<H5>Test Case ID : </H5> \n <p>" + testCase.getTestCaseId() + "</p>"
                                + "<H5>Module : </H5> \n <p>" + testCase.getModule() + "</p>"
                                + "<H5>Sub-Module : </H5> \n <p>" + testCase.getSubModule() + "</p>"
                                + "<H5>Test Description</H5> \n <p>" + testCase.getDescription() + "</p>"
                                + "<H5>Pre-Condition</H5> \n <p>" + testCase.getPreCondition() + "</p>"
                                + "<H5>Manual Test Step</H5> \n" + testCase.getManualTestStep()
                                + "<H5>Expected Result</H5> \n <p>" + testCase.getExpectedResult() + "</p>"
                );*/
                //parentTest.assignCategory(excelReader.getFileName().substring(0, excelReader.getFileName().lastIndexOf(".")) + " --> " + testCase.getModule() + " --> " + testCase.getSubModule());
                //parentTest.assignCategory(excelReader.getFileName().substring(0, excelReader.getFileName().lastIndexOf(".")) + " --> " + sheetName);
                //parentTest.assignCategory(testContext.getAssigneCatogary());
            /*if((!PropertyUtility.isAllTestExecEnabled() && testCase.isDisabled()) || !testCase.isAutomated()) {
                //parentTest.skip(testCase.getTestCaseName());
                parentTest.log(Status.SKIP, testCase.getTestSteps().toString());
                continue;
            }*/
                testContext.setTest(reportGenerator.getCurrentTest());

                if(skipNumberOfNextTestCases > 0) {
                    --skipNumberOfNextTestCases;
                    failNumberOfNextTestCases = 0;
                    reportGenerator.getCurrentTest().skip(testCase.getTestCaseName());
                    reportGenerator.getCurrentTest().log(Status.SKIP, testCase.getTestSteps().toString());
                    new TestCaseHelper().skipTestStep(testCase);
                    continue;
                }

                if(failNumberOfNextTestCases > 0) {
                    --failNumberOfNextTestCases;
                    skipNumberOfNextTestCases = 0;
                    reportGenerator.getCurrentTest().fail(testCase.getTestCaseName());
                    reportGenerator.getCurrentTest().log(Status.FAIL, testCase.getTestSteps().toString());
                    new TestCaseHelper().failTestStep(testCase);
                    continue;
                }

                try {
                    new TestCaseHelper().executeTestStep(testCase, isAppendTestcase, testCase.isFailOverTest());
                } catch (HandleFailOverTestExecution ex) {
                    CommandResponse commandResponse = new TestCaseHelper().handleFailOverTestExecution(ex, testCase);
                    skipNumberOfNextTestCases = commandResponse.getSkipNumberOfNextTestCases();
                    failNumberOfNextTestCases = commandResponse.getFailNumberOfNextTestCases();

                    /*if(null !=  ex.getCommandParam()
                            && NonScreenshotCommands.identifyOperation(
                                    ex.getCommandParam().getCommand().toUpperCase()) == NonScreenshotCommands.INVALID) {
                        LogUtil.logTestCaseErrorMsg("HandleFailOverTestExecution for test case: " + testCase.getTestCaseName() + "\n Test case data detail: " + testCase.toString(), ex);
                        try {
                            //TestCaseLevel
                            CommandResponse commandResponse = new HandleFailOverExecutor().perform(new CommandParam(
                                    testCase.getTestCaseName(),
                                    null,
                                    new String[]{"failOverSteps"},
                                    testCase.getInputParam(),
                                    testCase.getAssertParam()));

                            skipNumberOfNextTestCases = commandResponse.getSkipNumberOfNextTestCases();
                            failNumberOfNextTestCases = commandResponse.getFailNumberOfNextTestCases();
                            //TestSuiteLevel
                            if (!"(HandleFailOverSteps Success)".equals(commandResponse.getMessage()) && TestContext.getTestContext("").isHandleFailOver()) {
                                TestContext.getTestContext("").getTest().log(Status.FAIL, "Start - Handle FailOver Steps");
                                Object testCaseObj = TestContext.getTestContext("").getFailOver().get("TestSuiteLevel");
                                if (null != testCaseObj) {
                                    if (null != ((ExcelTestCase) testCaseObj).getTestSteps()) {
                                        new TestCaseHelper().executeTestStep((ExcelTestCase) testCaseObj);
                                        LogUtil.logTestCaseMsg("Handling the fail over test execution: Finished");
                                        TestContext.getTestContext("").getTest()
                                                .log(Status.INFO, "Handling the fail over test execution: Finished");
                                    } else {
                                        LogUtil.logTestCaseMsg("Handling the fail over test scripts Not Found");
                                        TestContext.getTestContext("").getTest()
                                                .log(Status.FAIL, "Handling the fail over test scripts Not Found");
                                    }
                                } else {
                                    LogUtil.logTestCaseMsg("Handling the fail over test scripts Not Found");
                                    TestContext.getTestContext("").getTest()
                                            .log(Status.FAIL, "failOverSteps test not defined");
                                }
                                TestContext.getTestContext("").getTest().log(Status.FAIL, "Finish - Handle FailOver Steps");
                            } else if (!TestContext.getTestContext("").isHandleFailOver()) {
                                throw new ShutdownTestExecution("FailOverSteps execution is disabled. Enable the property enable.fail.over.execution=true");
                            }
                        } catch (HandleFailOverTestExecution exd) {
                            //TODO FIX ME
                            LogUtil.logTestCaseErrorMsg("HandleFailOverTestExecution Execetion in MasterTestCase.processTest()", exd);
                        }
                    }*/
                } catch (AssertionError ex) {
                    LogUtil.logTestCaseErrorMsg("AssertionError in MasterTestCase.processTest()", ex);
                }

                if (testCase.getDescription().isEmpty()) {
                    reportGenerator.getCurrentTest().log(Status.FAIL, "Testcase Description is missing for test case id - " + testCase.getTestCaseId());
                }
                if (testCase.getManualTestStep().isEmpty()) {
                    reportGenerator.getCurrentTest().log(Status.FAIL, "Testcase Manual Steps are missing for test case id - " + testCase.getTestCaseId());
                }

                if (testCase.isAutomated() && testCase.getTestSteps().isEmpty()) {
                    reportGenerator.getCurrentTest().log(Status.FAIL, "Automation Test steps are missing for test case id - " + testCase.getTestCaseId());
                }
                //Closing the report
                //parentTest.endTest(test);
                /*if (null != extent) {
                    //extent.removeTest(parentTest);
                    extent.flush();
                    //extent.close();
                }*/
                reportGenerator.flush();
                LogUtil.logTestCaseMsg("TestCase End: " + testCase.getTestCaseName());
                LogUtil.removeLogTestCase();
                //testContext.setTestCaseName(null);
                //testName.set(null);
            }
            //TODO Implement write output to excel with status
        }
    }

    //@AfterTest
    public void afterTest() {
        /*if(null != extent) {
            extent.flush();
        }*/
        ReportGenerator.getReportInstance().flush();
        DriverManager.getInstance().getWebDriver().quit();
    }

    //@Override
    /*public String getTestName() {
        return testName.get();
    }*/
}
