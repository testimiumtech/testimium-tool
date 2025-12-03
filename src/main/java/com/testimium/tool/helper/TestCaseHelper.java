package com.testimium.tool.helper;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.testimium.tool.action.NonScreenshotCommands;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.domain.InputParameter;
import com.testimium.tool.domain.TestJsonInputData;
import com.testimium.tool.domain.testcase.ExcelTestCase;
import com.testimium.tool.domain.testcase.TestStep;
import com.testimium.tool.exception.*;
import com.testimium.tool.executor.ActionExecutor;
import com.testimium.tool.executor.HandleFailOverExecutor;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.report.generator.ReportGenerator;
import com.testimium.tool.utility.JsonParserUtility;
import com.testimium.tool.utility.PropertyUtility;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Test Case Helper Class
 * @author Sandeep Agrawal
 */
public class TestCaseHelper {
    /**
     * ActionExecutor
     */
    private ActionExecutor actionExecutor = new ActionExecutor();
    /**
     * Execute the test steps
     * @param excelTestCase input param
     * @throws ShutdownTestExecution if any issue which will not be able to handle
     * @throws HandleFailOverTestExecution If anything fails
     * @throws TestException If any test exception
     */
    public void executeTestStep(ExcelTestCase excelTestCase, boolean nestedNodeEnabled, boolean isFailOverStep) throws ShutdownTestExecution, HandleFailOverTestExecution, TestException {


        List<TestStep> testSteps = excelTestCase.getTestSteps();
        if(excelTestCase.getTestCaseName().contains("Do Login") || excelTestCase.getTestCaseName().contains("Login into")
                || excelTestCase.getTestCaseName().contains("Login Successful") || excelTestCase.getTestCaseName().contains("Successful Login")){
            TestContext.getTestContext(excelTestCase.getTestCaseName()).setGlobalVariable("lastLoginTestSteps", excelTestCase);
            TestContext.getTestContext(excelTestCase.getTestCaseName()).setGlobalVariable("lastLoginNestedNodeEnabled", nestedNodeEnabled);
        }
        for (int itr = 0; itr < testSteps.size(); itr++) {
            //ReportGenerator.getReportInstance().getCurrentTest().assignCategory(TestContext.getTestContext("").getTestCaseName());
            TestStep testStep = testSteps.get(itr);
            TestContext.getTestContext("").setTestStepFailed(false);
            TestContext.getTestContext("").setHandleFailOver(false);
            CommandParam commandParam = null;
            try {
                if((!PropertyUtility.isAllTestExecEnabled() && excelTestCase.isDisabled()) || !excelTestCase.isAutomated()) {
                    //TestContext.getTestContext("").getTest().log(Status.SKIP, testStep.logCommand());
                    skipTest(testStep);
                    continue;
                }
                commandParam = new CommandParam(
                        excelTestCase.getTestCaseName(),
                        testStep.getOperation().trim(),
                        testStep.getParams(),
                        excelTestCase.getInputParam(),
                        excelTestCase.getAssertParam());
                commandParam.setFailOverStep(isFailOverStep);
                commandParam.setNestedNodeEnabled(nestedNodeEnabled);
                try {
                    actionExecutor.execute(commandParam);
                } catch (RecoverBrokenTestExecutionException ex) {
                    LogUtil.logTestCaseMsg("End Broken test have been recovered successfully....");
                }
            } catch (AssertionError error) {
                //TODO handles logs correctly
                //Call from actionExecutor.execute()
                LogUtil.logTestCaseErrorMsg("Assertion Error TestCaseHelper:- ", error);
                TestContext.getTestContext("").getTestStepErrorLog().append("\nAssertion Error TestCaseHelper:-" + error);
                //TODO Why this handle failOver is here under Assertion Error. Analyse and remove this
                if(TestContext.getTestContext("").isHandleFailOver()) {
                    throw  new HandleFailOverTestExecution("Starts", commandParam);
                }
            }
        }
        TestContext.getTestContext(excelTestCase.getTestCaseName()).clearInputMap();
        //If Test step fails then test must fail
            /*if(TestContext.getTestContext("").isTestStepFailed() && TestContext.getTestContext("").isHandleFailOver())
                Assert.fail();*/
    }

    /**
     * Skip test based on configuration
     * enable.all.testcase.execution=false and IsDisabled=Yes
     * @param testStep input param
     */
    private static void skipTest(TestStep testStep) {
        ExtentTest initialExtentTestChild = ReportGenerator.getReportInstance().getChildNodeLevel1();
        if(null == initialExtentTestChild){
            ReportGenerator.getReportInstance().getCurrentTest().skip(testStep.logCommand());
        } else {
            initialExtentTestChild.skip(testStep.logCommand());
        }
    }

    /**
     * Skip all the test steps
     * @param excelTestCase input param
     */
    public void skipTestStep(ExcelTestCase excelTestCase)  {

        List<TestStep> testSteps = excelTestCase.getTestSteps();
        for (int itr = 0; itr < testSteps.size(); itr++) {
            TestStep testStep = testSteps.get(itr);
            TestContext.getTestContext("").getTest().log(Status.SKIP, testStep.logCommand());
        }
    }

    /**
     * Make fail all the test steps
     * @param excelTestCase input param
     */
    public void failTestStep(ExcelTestCase excelTestCase)  {

        List<TestStep> testSteps = excelTestCase.getTestSteps();
        for (int itr = 0; itr < testSteps.size(); itr++) {
            TestStep testStep = testSteps.get(itr);
            TestContext.getTestContext("").getTest().log(Status.FAIL, testStep.logCommand());
        }
    }

    /**
     * Handle Fail Over Test Execution
     * @param ex input param
     * @param testCase input param
     * @return command response
     * @throws ShutdownTestExecution
     * @throws  TestException
     */
    public CommandResponse handleFailOverTestExecution(HandleFailOverTestExecution ex, ExcelTestCase testCase) throws ShutdownTestExecution, TestException {
        if(null !=  ex.getCommandParam()
                && NonScreenshotCommands.identifyOperation(
                ex.getCommandParam().getCommand().toUpperCase()) == NonScreenshotCommands.INVALID) {
            LogUtil.logTestCaseErrorMsg("HandleFailOverTestExecution for test case: " + testCase.getTestCaseName() + "\n Test case data detail: " + testCase, ex);
            try {
                //TestCaseLevel
                CommandResponse commandResponse = new HandleFailOverExecutor().perform(new CommandParam(
                        testCase.getTestCaseName(),
                        null,
                        new String[]{"failOverSteps"},
                        testCase.getInputParam(),
                        testCase.getAssertParam()));

                //TestSuiteLevel
                if (!"(HandleFailOverSteps Success)".equals(commandResponse.getMessage()) && TestContext.getTestContext("").isHandleFailOver()) {
                    LogUtil.logTestCaseErrorMsg("Start - TestSuite Level - Handle FailOver Steps", null);
                    //TestContext.getTestContext("").getTest().log(Status.FAIL, "Start - TestSuite Level - Handle FailOver Steps");
                    ExtentTest initialExtentTestChild = ReportGenerator.getReportInstance().getChildNodeLevel1();
                    //ReportGenerator.getReportInstance().createChildNode("Start - TestSuite Level - Handle FailOver Steps");
                    ExtentTest nestedChild =  initialExtentTestChild.createNode("Start - TestSuite Level - Handle FailOver Steps");
                    ReportGenerator.getReportInstance().setChildNodeLevel1(nestedChild);

                    InputParameter inputParam = null;
                    List<String> failOverTestCaseKeys = null;
                   if(StringUtils.isNotEmpty(TestContext.getTestContext("").getTestInputJson())) {
                        inputParam = JsonParserUtility.getInputParam();
                    } else if(!testCase.isFailOverTest()){
                       failOverTestCaseKeys = new ArrayList<>();
                       failOverTestCaseKeys.add("TestSuiteLevel");
                   }


                    if(null != inputParam && null != inputParam.getFailOverTestCaseKeys() && inputParam.getFailOverTestCaseKeys().size() > 0) {
                        failOverTestCaseKeys = inputParam.getFailOverTestCaseKeys();
                    } /*else {
                        failOverTestCaseKeys = new ArrayList<>();
                        failOverTestCaseKeys.add("TestSuiteLevel");
                    }*/

                    if(null != failOverTestCaseKeys) {
                        for (int ktr = 0; ktr < failOverTestCaseKeys.size(); ktr++) {
                            //Object testCaseObj = TestContext.getTestContext("").getFailOver().get("TestSuiteLevel");
                            Object testCaseObj = TestContext.getTestContext("").getFailOver().get(failOverTestCaseKeys.get(ktr));
                            LogUtil.logTestCaseErrorMsg("TestSuiteLevel" + failOverTestCaseKeys.get(ktr) + " testCaseSteps -> " + testCaseObj, ex);
                            if (null != testCaseObj) {
                                if (null != ((ExcelTestCase) testCaseObj).getTestSteps()) {
                                    executeTestStep((ExcelTestCase) testCaseObj, true, true);
                                    LogUtil.logTestCaseMsg("Handling the fail over test execution: Finished");
                                    ReportGenerator.getReportInstance().getChildNodeLevel1().info("Handling the fail over test execution: Finished");
                            /*TestContext.getTestContext("").getTest()
                                    .log(Status.INFO, "Handling the fail over test execution: Finished");*/
                                } else {
                                    LogUtil.logTestCaseMsg("Handling the fail over test scripts Not Found");
                                    ReportGenerator.getReportInstance().getChildNodeLevel1().fail("Handling the fail over test scripts Not Found");
                            /*TestContext.getTestContext("").getTest()
                                    .log(Status.FAIL, "Handling the fail over test scripts Not Found");*/
                                }
                            } else {
                                LogUtil.logTestCaseMsg("Handling the fail over test scripts Not Found");
                                ReportGenerator.getReportInstance().getChildNodeLevel1().fail("Handling the fail over test scripts Not Found");
                        /*TestContext.getTestContext("").getTest()
                                .log(Status.FAIL, "failOverSteps test not defined");*/
                            }
                        }
                    }
                    LogUtil.logTestCaseErrorMsg("Finish - TestSuite Level - Handle FailOver Steps", null);
                    ReportGenerator.getReportInstance().getChildNodeLevel1().fail("Finish - TestSuite Level - Handle FailOver Steps");
                    ReportGenerator.getReportInstance().setChildNodeLevel1(initialExtentTestChild);
                    /*TestContext.getTestContext("").getTest().log(Status.FAIL, "Finish - TestSuite Level - Handle FailOver Steps");*/
                } else if (!TestContext.getTestContext("").isHandleFailOver()) {
                    throw new ShutdownTestExecution("FailOverSteps execution is disabled. Enable the property enable.fail.over.execution=true");
                }
                return commandResponse;
            } catch (HandleFailOverTestExecution exd) {
                //TODO FIX ME
                LogUtil.logTestCaseErrorMsg("HandleFailOverTestExecution Execution in TestCaseHelper.handleFailOverTestExecution()", exd);
            } catch (InputParamNotFoundException e) {
                LogUtil.logTestCaseErrorMsg("InputParamNotFoundException Execution in TestCaseHelper.handleFailOverTestExecution()", e);
            } catch (InputParamParsingException e) {
                LogUtil.logTestCaseErrorMsg("InputParamParsingException Execution in TestCaseHelper.handleFailOverTestExecution()", e);
            }
        }
        return new CommandResponse("", true);
    }
}
