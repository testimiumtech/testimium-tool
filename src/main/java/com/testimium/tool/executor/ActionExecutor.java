package com.testimium.tool.executor;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.testimium.tool.action.NonReportableCommands;
import com.testimium.tool.action.NonScreenshotCommands;
import com.testimium.tool.base.DriverManager;
import com.testimium.tool.command.Command;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;

import com.testimium.tool.exception.*;
import com.testimium.tool.factory.CommandFactory;
import com.testimium.tool.html.generator.TestStepErrorLogHtmlGenerator;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.report.generator.ReportGenerator;
import com.testimium.tool.utility.PropertyUtility;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.io.IOException;
import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class ActionExecutor {

    /**
     * This method is used to execute the command.
     * TODO Fix Me for parallel action execution by multiple threads
     * @param commandParam input param
     * @return response CommandParam
     * @throws ShutdownTestExecution If any unwanted exception
     * @throws HandleFailOverTestExecution If not able to handle fail over
     * @throws TestException If not able to execute test
     * @throws RecoverBrokenTestExecutionException If not able to recover test
     */
    public CommandResponse execute(CommandParam commandParam) throws ShutdownTestExecution, HandleFailOverTestExecution, TestException, RecoverBrokenTestExecutionException {
        Action<CommandParam, CommandResponse> action = param -> {
            StringBuilder appendErrorLog = new StringBuilder();
            CommandResponse commandResponse = null;
            String printParams = null;
            try {
                TestContext.getTestContext("").setTestStepErrorLog(appendErrorLog);
                if(StringUtils.isNotEmpty(param.getCommand())) {
                    printParams = ActionHelper.printParams(param.getCommand().trim(), param.getArgs());
                    LogUtil.logTestCaseMsg("Start Command Action: " + printParams);
                    //TODO Fix Me make chain of operations
                    if(!commandParam.isNestedNodeEnabled()) {
                        ReportGenerator.getReportInstance().createChildNode(printParams);
                    }
                    Command command = CommandFactory.getCommandInstance(param.getCommand());
                    //Command command = CommandFactory.getCommandInstance(param.getCommand());
                    //command.wait(param);
                    new ActionHelper().processOptions(command, param);
                    //commandResponse = (CommandResponse) command.execute(param);
                    try {
                        commandResponse = (CommandResponse) command.executeCmd(param, param.getCommand());

                    }catch (NoSuchSessionException ex) {
                        LogUtil.logTestCaseErrorMsg("NoSuchSessionException under ActionExecutor: ",ex);
                        new ActionHelper().handleBrokenTestExecution(printParams);
                    } catch (NoSuchWindowException ex){
                        LogUtil.logTestCaseErrorMsg("NoSuchWindowException under ActionExecutor: With command statement: " + printParams,ex);
                        try {
                            DriverManager.getInstance().focusCurrentWindow();
                        } catch (WebDriverException e) {
                            LogUtil.logTestCaseErrorMsg("ActionExecutor Browser closed: With command statement: " + printParams, e);
                            //throw new ShutdownTestExecution("ActionExecutor Shutdown called - 1 ");
                            new ActionHelper().handleBrokenTestExecution(printParams);
                        }
                    } catch (UnreachableBrowserException ubex){
                        LogUtil.logTestCaseErrorMsg("UnreachableBrowserException under ActionExecutor: With command statement: " + printParams,ubex);
                       /* ReportGenerator.getReportInstance().getChildNodeLevel1().fail("<B>" + printParams
                                + "</B><br><font color='red'>Lost Browser connection due to UnreachableBrowserException</font>");*/
                        //throw new ShutdownTestExecution("ActionExecutor Shutdown called - 2 ");
                        new ActionHelper().handleBrokenTestExecution(printParams);
                    } catch (WebDriverException ex){
                        LogUtil.logTestCaseErrorMsg("WebDriverException under ActionExecutor: With command statement: " + printParams,ex);
                        try {
                            DriverManager.getInstance().focusCurrentWindow();
                        } catch (WebDriverException e) {
                            LogUtil.logTestCaseErrorMsg("WebDriverException inside catch: With command statement: " + printParams,ex);
                            new ActionHelper().handleBrokenTestExecution(printParams);
                            //throw new ShutdownTestExecution("WebDriverException - ActionExecutor Shutdown called - 3");
                           /* LogUtil.logTestCaseMsg("Re-load the web driver.........");
                            DriverManager.getInstance().shutdownWebDriver();
                            DriverManager.getInstance().loadDriver();
                            LogUtil.logTestCaseMsg("Re-load finish.........");*/
                        }
                    } catch (VerificationException e) {
                        LogUtil.logTestCaseErrorMsg("VerificationException under ActionExecutor: With command statement: " + printParams,e);
                        throw new AssertionError(e);
                    } catch (HttpTimeoutException ex) {
                        LogUtil.logTestCaseErrorMsg("HttpTimeoutException under ActionExecutor: ",ex);
                        new ActionHelper().handleBrokenTestExecution(printParams);
                    }

                    if(null == commandResponse)
                        throw new CommandException("Command Response is NULL");
                    // End of Chain Operation
                    if(commandResponse.isSuccess()
                            && NonReportableCommands.identifyOperation(commandResponse.getExecutedCommand()) == NonReportableCommands.REPORTABLE_COMMAND) {
                        //LogUtil.logTestCaseMsg("..............Command Action Passed................ ");
                        ReportGenerator.getReportInstance().getChildNodeLevel1().pass(printParams
                                + "<br>" + ((null == commandResponse) ? "" : commandResponse.getMessage()));
                        /*TestContext.getTestContext("").getTest()
                                .log(Status.PASS, printParams
                                        + "<br>" + ((null == commandResponse) ? "" : commandResponse.getMessage()));*/
                    } else if(NonReportableCommands.identifyOperation(commandResponse.getExecutedCommand()) == NonReportableCommands.REPORTABLE_COMMAND){
                        LogUtil.logTestCaseMsg("..............Command Action Failed................ ");
                        LogUtil.logTestCaseMsg("..............Please check error log............... ");
                        ReportGenerator.getReportInstance().getChildNodeLevel1().fail("<B>" + printParams
                                + "</B><br><font color='red'>" + commandResponse.getMessage() + "</font>");
                        /*TestContext.getTestContext("").getTest()
                                .log(Status.FAIL, *//*commandParam.getCommand().trim(),*//*
                                        "<B>" + printParams
                                                + "</B><br><font color='red'>" + commandResponse.getMessage() + "</font>");*/
                        //new HandleFailOverExecutor().perform(commandParam);
                    }
                    LogUtil.logTestCaseMsg("End Command Action: " + param.getCommand().trim());
                    return commandResponse;
                }
            /*} catch (NoSuchWindowException ex){
                LogUtil.logTestCaseErrorMsg("NoSuchWindowException under ActionExecutor: ",ex);
                try {
                    DriverManager.getInstance().focusCurrentWindow();
                } catch (WebDriverException e) {
                    LogUtil.logTestCaseMsg("Re-load the web driver.........");
                    DriverManager.getInstance().shutdownWebDriver();
                    DriverManager.getInstance().loadDriver();
                    new OpenUrlCmd().execute(null);
                    LogUtil.logTestCaseMsg("Re-load finish.........");
                }
            } catch (UnreachableBrowserException ubex){
                LogUtil.logTestCaseErrorMsg("UnreachableBrowserException under ActionExecutor: ",ubex);
                LogUtil.logTestCaseMsg("Re-establish the web driver.........");
                DriverManager.getInstance().shutdownWebDriver();
                DriverManager.getInstance().loadDriver();
                try {
                    new OpenUrlCmd().execute(null);
                } catch (CommandException e) {
                    throw new RuntimeException(e);
                }
                LogUtil.logTestCaseMsg("Re-establish finish.........");
                ubex.printStackTrace();*/

            } catch (CommandNotFoundException | CommandException | PropertyKeyNotFoundException |
                     LocatorNotFoundException | NoSuchElementException | HandleFailOverTestExecution ex) {
                LogUtil.logTestCaseMsg("..............Command Action Failed................ ");
                LogUtil.logTestCaseMsg("..............Please check error log............... ");
                LogUtil.logTestCaseErrorMsg("Command Action Exception: ", ex);

                if(commandParam.isNestedNodeEnabled()) {
                    ReportGenerator.getReportInstance().getChildNodeLevel1().fail(printParams);
                }

                appendErrorLog = TestContext.getTestContext("").getTestStepErrorLog();
                //String printParams = printParams;
                appendErrorLog.append(printParams);
                appendErrorLog.append("\n" + ex);
                TestContext.getTestContext("").setTestStepErrorLog(appendErrorLog);

                //ReportGenerator.getReportInstance().getChildNodeLevel1().fail(appendErrorLog.toString());
                //TestContext.getTestContext("").getTest().log(Status.FAIL, "<B>" + printParams + "</B>");
                //System.out.println("Command Exception:- " + param.toString());
                //ex.printStackTrace();
                /*TestContext.getTestContext("").getTest()
                        .log(Status.FAIL, "<B>" + printParams + "</B><br><font color='red'>" + ex.getMessage() + "</font>");*/
                try {
                    if(NonScreenshotCommands.identifyOperation(param.getCommand().toUpperCase()) == NonScreenshotCommands.INVALID) {
                        LogUtil.logTestCaseMsg("..............Screenshot captured............... ");
                        ReportGenerator.getReportInstance().getChildNodeLevel1().fail("Captured Screenshot - ",
                                MediaEntityBuilder.createScreenCaptureFromBase64String(DriverManager.getInstance().takeScreenShotBase64("screenshot_" + RandomUtils.nextInt())).build());
                        /*TestContext.getTestContext("").getTest().log(Status.FAIL,
                                DriverManager.getInstance().takeScreenShot("screenshot_" + RandomUtils.nextInt()));*/
                    }
                } catch (IOException e) {
                    TestContext.getTestContext("").getTestStepErrorLog().append("Command Action Screenshot Exception: " + e);
                    LogUtil.logTestCaseErrorMsg("Command Action Screenshot Exception: ", e);
                }

                new TestStepErrorLogHtmlGenerator().generateHtml();

                if(PropertyUtility.isFailOverExecEnabled()) {
                    TestContext.getTestContext("").setHandleFailOver(true);
                    throw new HandleFailOverTestExecution("Starts", commandParam);
                } else {
                    TestContext.getTestContext("").setHandleFailOver(false);
                    throw new ShutdownTestExecution(ex.getMessage());
                }
                //Assert.fail();
            } catch(AssertionError ex) {
                LogUtil.logTestCaseMsg("..............AssertionError - Command Action Failed................ ");
                LogUtil.logTestCaseMsg("..............Please check error log............... ");
                LogUtil.logTestCaseErrorMsg("ActionExecutor Class AssertionError: ", ex);
                TestContext.getTestContext("").setTestStepFailed(true);
                //System.out.println(param.toString());
                //ex.printStackTrace();
                TestContext.getTestContext("").getTestStepErrorLog().append(ex);
                //String printParams = printParams;
                TestContext.getTestContext("").getTestStepErrorLog().append(printParams);
                ReportGenerator.getReportInstance().getChildNodeLevel1().fail(
                        "<B>" + printParams + "</B><br><font color='red'>" + replaceAssertMessage(ex.getMessage()) + "</font>");
                /*TestContext.getTestContext("").getTest()
                        .log(Status.FAIL, *//*commandParam.getCommand().trim(),*//*
                                "<B>" + printParams + "</B><br><font color='red'>" + replaceAssertMessage(ex.getMessage()) + "</font>");*/
                try {
                    if(NonScreenshotCommands.identifyOperation(param.getCommand().toUpperCase()) == NonScreenshotCommands.INVALID) {
                        LogUtil.logTestCaseMsg("..............Screenshot captured............... ");
                        ReportGenerator.getReportInstance().getChildNodeLevel1().fail("Captured Screenshot - ",
                                MediaEntityBuilder.createScreenCaptureFromBase64String(DriverManager.getInstance().takeScreenShotBase64("screenshot_" + RandomUtils.nextInt())).build());
                        /*TestContext.getTestContext("").getTest().log(Status.FAIL,
                                DriverManager.getInstance().takeScreenShot("screenshot_" + RandomUtils.nextInt()));*/
                    }
                } catch (IOException e) {
                    TestContext.getTestContext("").getTestStepErrorLog().append("AssertionError Screenshot Exception: " + e);
                    LogUtil.logTestCaseErrorMsg("AssertionError Screenshot Exception: ", e);
                }
                new TestStepErrorLogHtmlGenerator().generateHtml();
            } /*catch (Exception ex){
                LogUtil.logTestCaseErrorMsg("ActionExecutor Class Exception: ", ex);
                TestContext.getTestContext("").getTestStepErrorLog().append(ex);
                //String printParams = printParams;
                TestContext.getTestContext("").getTestStepErrorLog().append(printParams);
                ReportGenerator.getReportInstance().getChildNodeLevel1().fail("<B>" + printParams+ "</B>");
                *//*TestContext.getTestContext("").getTest()
                        .log(Status.FAIL,
                                "<B>" + printParams
                                        + "</B><br><font color='red'>" + ex.getMessage() + "</font>");*//*
                new TestStepErrorLogHtmlGenerator().generateHtml();
                Assert.fail();
            }*/
            return null;
        };
        return action.perform(commandParam);
    }


    /**
     * Used to replace assert param
     * @param exceptionMsg input param
     * @return response text
     */
    private String replaceAssertMessage(String exceptionMsg) {
        String replaceMsg = null;
        if(null != exceptionMsg) {
            replaceMsg = exceptionMsg.replace("Expecting ArrayList:", "<br/><br/> <B>Actaual Record:</B>");
        }
        return replaceMsg;
    }

    /*public static void main(String[] args) throws CommandNotFoundException {
        List<CommandParam> commandParam =  new ArrayList<>();
       *//* commandParam.setCommand("SetElement");
       new ActionExecutor().execute(commandParam);*//*
    }*/

}
