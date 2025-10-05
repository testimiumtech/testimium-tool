package com.testimium.tool.base;

import com.aventstack.extentreports.Status;
import com.testimium.tool.context.TestContext;
/*import com.bopsen.testimium.custom.annotation.TestCaseData;*/
import com.testimium.tool.domain.testcase.ExcelTestCase;
import com.testimium.tool.exception.FileReaderException;
import com.testimium.tool.exception.ShutdownTestExecution;
import com.testimium.tool.exception.TaskNotFoundException;
import com.testimium.tool.exception.TestException;
import com.testimium.tool.factory.TaskFactory;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.parser.ExcelParser;
import com.testimium.tool.reader.FileReaderFactory;
import com.testimium.tool.report.generator.ReportGenerator;
import com.testimium.tool.schedular.ITaskScheduler;
import com.testimium.tool.utility.FileUtility;
import com.testimium.tool.utility.PropertyUtility;
import org.apache.commons.lang3.StringUtils;
import org.testng.ITestContext;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Sandeep Agrawal
 */
public class BaseTestCase extends MasterTestCase {
    //private FEOperation operation = new FEOperation();
    //private TestContext testContext;
    private boolean isDBConnectionFailed = false;
    //private ThreadLocal<String> testName = new ThreadLocal<>();
    //private ExtentHtmlReporter htmlReports;
    //private ITestContext ctx;
    //public ExtentReports extent;
    //public ExtentTest test;
    //public ExtentTest parentTest;
    //public ExtentTest childTest;


    //@BeforeClass
    public void beforeClass() {

    }

    //@BeforeMethod
    public void beforeMethod(Method method, Object[] object, ITestContext ctx) throws TaskNotFoundException {
        //System.out.println("Class name =====" + ctx.getClass().getName());
        //ifAnnotationPresent(method, ctx);
       /* try {
            LogUtil.removeAllAppenders();
            new LogsComprassor().compress();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //this.ctx = ctx;
        try {
            ITaskScheduler taskScheduler = TaskFactory.getTaskInstance("Report_Generator");
            taskScheduler.execute("Report_Generator");
            if(!isDBConnectionFailed)
                executeTest();

            taskScheduler.shutdown();
        } catch (ShutdownTestExecution | TestException ex) {
            LogUtil.logToolErrorMsg("Test Execution finished with Exception-: " + ex.getMessage(), ex);
            LogUtil.logToolMsg("Test Execution finished with Exception. Please the error log: " + ex.getMessage());
            ITaskScheduler taskScheduler = TaskFactory.getTaskInstance("Report_Generator");
            taskScheduler.shutdown();
            System.exit(1);
        } catch (TaskNotFoundException ex) {
            LogUtil.logToolErrorMsg("Test Execution interrupted:- " + ex.getMessage(), ex);
            LogUtil.logToolMsg("Test Execution interrupted. Please the error log: " + ex.getMessage());
            System.exit(1);
        }
    }

    /*private void ifAnnotationPresent(Method method) throws ShutdownTestExecution, TestException {
        if(method.isAnnotationPresent(TestCaseData.class)) {
            System.out.println(method.getName() + " : " + method.isAnnotationPresent(TestCaseData.class));
            TestCaseData testData = method.getAnnotation(TestCaseData.class);
            executeTest();
        }
    }*/

    private void executeTest() throws ShutdownTestExecution, TestException {
        //System.out.println("File name passed in annotation " + PropertyReader.getProperty("testsuite.file.name")/*testData.fileName()*/);
        // String sheetName = PropertyReader.getProperty("testsuite.sheet.name");
        if(StringUtils.isEmpty(PropertyUtility.getTestType())) {
            LogUtil.logToolErrorMsg("perform.test.type configuration is missing: ", new TestException("Define the test type to perform the test."));
            TestContext.getTestContext("").getTest()
                    .log(Status.FAIL, "perform.test.type configuration is missing:");
            throw new TestException("Define the test type to perform the test.");
        }

        TestContext.getTestContext("").setTestTypes(PropertyUtility.getTestType());
        String fileNameStr = null;
        try {
            //TODO verify file extension 
            if(StringUtils.isNotEmpty(PropertyUtility.getStartTestsuiteName())) {
                ExcelParser excelParser = (ExcelParser)FileReaderFactory.getInstance().readFile("EXCEL", "TESTCASE", FileUtility.getAbsolutePath(PropertyUtility.getStartTestsuiteName()));
                String batchName = excelParser.getFileName().substring(0, excelParser.getFileName().lastIndexOf("."));
                ReportGenerator.getReportInstance().startNewReport(batchName);
                process(excelParser);
            }

            if(!PropertyUtility.isMultiTestsuiteExecEnabled()) {
                //TODO Fix NullPointerException if file name is not provided in testing.properties
                fileNameStr = PropertyUtility.getTestsuiteFileName();
                if(null != fileNameStr && !fileNameStr.isEmpty()) {
                    String[] fileNames = fileNameStr.split(",");
                    for(int k=0; k< fileNames.length; k++){
                        ExcelParser excelParser = (ExcelParser)FileReaderFactory.getInstance()
                                .readFile("EXCEL", "TESTCASE", FileUtility.getFile(FileUtility.getFile(PropertyUtility.getTestCasesPath()),
                                        fileNames[k]));

                        //Creating Batch
                        String batchName = excelParser.getFileName().substring(0, excelParser.getFileName().lastIndexOf("."));

                        if (!batchName.contains("AllInOne")){
                            ReportGenerator.getReportInstance().startNewReport(batchName);
                        }


                        if ("ALL".equalsIgnoreCase(PropertyUtility.getTestsuiteSheetName())) {
                            //try {
                            //excelParser.getAllSheets().forEach(name -> {
                            List<String> sheetList = excelParser.getAllSheets();
                            for(int itr = 0; itr < sheetList.size(); itr++) {
                                try {
                                    List<ExcelTestCase> testCases = excelParser.getTestCasesBySheetName(sheetList.get(itr).trim());
                                    if(null != testCases && testCases.size() > 0) {
                                        if (!batchName.contains("AllInOne")) {
                                            ReportGenerator reportGenerator = ReportGenerator.getReportInstance();
                                            reportGenerator.startNewTestsuite(sheetList.get(itr));
                                        }
                                        //testContext.setAssigneCatogary(excelParser.getFileName().substring(0, excelParser.getFileName().lastIndexOf(".")) + "-->" + name);
                                        //processTest(excelParser, sheetList.get(itr), false);
                                        processTest(testCases, excelParser.getFileName(), sheetList.get(itr), false);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    LogUtil.logToolErrorMsg("========================1 TODO Fix Exception and Log===================================", e);
                                } /*catch (ShutdownTestExecution | TestException ex) {
                                        LogUtil.logToolErrorMsg("=======================ShutdownTestExecution OR TestException==================================", ex);
                                        throw new RuntimeException(ex.getMessage());
                                    }*/
                                //ReportGenerator.getReportInstance().endTestsuite();
                            }
                            //);
                           /* } catch (RuntimeException ex) {
                                throw new ShutdownTestExecution(ex.getMessage());
                            }*/
                        } else {
                            String[] sheetNames = PropertyUtility.getTestsuiteSheetName().split(",");
                            ReportGenerator reportGenerator = ReportGenerator.getReportInstance();
                            for (int ikr = 0; ikr < sheetNames.length; ikr++) {
                                try{
                                    if(StringUtils.isNotEmpty(sheetNames[ikr])) {
                                        List<ExcelTestCase> testCases = excelParser.getTestCasesBySheetName(sheetNames[ikr].trim());
                                        if(null != testCases && testCases.size() > 0) {
                                            if (!batchName.contains("AllInOne")) {
                                                testContext.setAssigneCatogary(excelParser.getFileName().substring(0, excelParser.getFileName().lastIndexOf(".")) + "-->" + sheetNames[ikr]);
                                                reportGenerator.startNewTestsuite(sheetNames[ikr]);
                                            }
                                            //processTest(excelParser, sheetNames[ikr], false);
                                            processTest(testCases, excelParser.getFileName(), sheetNames[ikr], false);
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    LogUtil.logToolErrorMsg("========================2 TODO Fix Exception and Log===================================", e);
                                }
                                //ReportGenerator.getReportInstance().endTestsuite();
                            }

                        }
                    }
                } else {
                    LogUtil.logToolErrorMsg("testsuite.file.name is missing: ", new TestException("if enable.multi.testsuite.execution=false than provide the testsuite.file.name=sample.xlsx"));
                    TestContext.getTestContext("").getTest()
                            .log(Status.FAIL, "testsuite.file.name is missing: if enable.multi.testsuite.execution=false than provide the testsuite.file.name=sample.xlsx");
                    throw new TestException("Define the test type to perform the test.");
                }
                /*ExcelParser excelParser = (ExcelParser)FileReaderFactory.getInstance()
                        .readFile("EXCEL", FileUtility.getFile(FileUtility.getFile(PropertyUtility.getTestCasesPath()),
                                PropertyUtility.getTestsuiteFileName()));

                if ("ALL".equalsIgnoreCase(PropertyUtility.getTestsuiteSheetName())) {
                    try {
                        excelParser.getAllSheets().forEach(name -> {
                            try {
                                processTest(excelParser, name);
                            } catch (IOException e) {
                                System.out.println("========================1 TODO Fix Exception and Log===================================");
                                e.printStackTrace();
                            } catch (ShutdownTestExecution | TestException ex) {
                                throw new RuntimeException(ex.getMessage());
                            }
                        });
                    } catch (RuntimeException ex) {
                        throw new ShutdownTestExecution(ex.getMessage());
                    }
                } else {
                    String[] sheetNames = PropertyUtility.getTestsuiteSheetName().split(",");
                    for (int ikr = 0; ikr < sheetNames.length; ikr++) {
                        if(StringUtils.isNotEmpty(sheetNames[ikr]))
                            processTest(excelParser, sheetNames[ikr]);
                    }
                }*/
            } else  if(PropertyUtility.isMultiTestsuiteExecEnabled()) {
                for (String file : FileUtility.getAllFiles(PropertyUtility.getTestCasesPath())) {
                    //testServiceContext.setCurrentModule(file.substring(file.lastIndexOf("/"), file.length()).replace(".xlsx",""));
                    if(StringUtils.isEmpty(PropertyUtility.getStartTestsuiteName()) || !file.contains(PropertyUtility.getStartTestsuiteName())) {
                        ExcelParser excelParser = (ExcelParser)FileReaderFactory.getInstance().readFile("EXCEL", "TESTCASE", file);
                        String batchName = excelParser.getFileName().substring(0, excelParser.getFileName().lastIndexOf("."));
                        ReportGenerator.getReportInstance().startNewReport(batchName);
                        process(excelParser);
                    }
                }
            }

            if(StringUtils.isNotEmpty(PropertyUtility.getEndTestsuiteName())) {
                //testServiceContext.setCurrentModule(PropertyReader.getProperty("end.with.testsuite").replace(".xlsx",""));
                process((ExcelParser) FileReaderFactory.getInstance().readFile("EXCEL", "TESTCASE",
                        FileUtility.getAbsolutePath(PropertyUtility.getEndTestsuiteName())));
            }
            //ReportGenerator.endReport();
        } catch (IOException e) {
            LogUtil.logToolErrorMsg("IOException BaseTestCase.executeTest(): " + e.getMessage() + " file  - " + fileNameStr, e);
            throw new ShutdownTestExecution(e.getMessage());
        } catch (FileReaderException e) {
            LogUtil.logToolErrorMsg("FileReaderException BaseTestCase.executeTest(): " + e.getMessage(), e);
            throw new ShutdownTestExecution(e.getMessage());
        }
    }

    //@AfterMethod
    public void afterMethod() {}


}
