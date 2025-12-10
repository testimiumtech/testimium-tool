package com.testimium.tool.executor;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.command.Command;
import com.testimium.tool.action.Options;
import com.testimium.tool.command.OpenNewBrowserTabCmd;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.domain.testcase.ExcelTestCase;

import com.testimium.tool.exception.*;
import com.testimium.tool.helper.TestCaseHelper;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.reader.PropertyReader;
import com.testimium.tool.report.generator.ReportGenerator;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.event.KeyEvent;

public class ActionHelper {

    //private List<String>
    /**
     * This method is used to process the options for the particular element
     * TODO Fix Me for concurrent execution
     * @param command Command
     * @param param CommandParam
     * @return response command
     * @throws PropertyKeyNotFoundException if property key not found
     * @throws LocatorNotFoundException if locator not found
     */
    public Command<CommandParam, CommandResponse> processOptions(Command command, CommandParam param) throws PropertyKeyNotFoundException, LocatorNotFoundException {
        LogUtil.logTestCaseMsg("Command " + ActionHelper.printParams(param.getCommand().trim(), param.getArgs()));
        try {
            //TODO put everything in MAP and then check if it contains the command
            if(null != param && param.getArgs().length > 0 && !"Wait".equalsIgnoreCase(param.getCommand())
                    //&& !"SwitchTab".equalsIgnoreCase(param.getCommand())
                    && !"OpenUrl".equalsIgnoreCase(param.getCommand())
                    && !"SetElements".equalsIgnoreCase(param.getCommand())
                    && !"HandleFailOver".equalsIgnoreCase(param.getCommand())
                    && !"SwitchTab".equalsIgnoreCase(param.getCommand())
                    && !"CloseBrowserTab".equalsIgnoreCase(param.getCommand())
                    && !"CloseAllBrowserTab".equalsIgnoreCase(param.getCommand())
                    && !"SetGlobalVariable".equalsIgnoreCase(param.getCommand())
                    /*&& !"UploadFile".equalsIgnoreCase(param.getCommand())*/
                    && !"ClickOSElement".equalsIgnoreCase(param.getCommand())
                    && !param.getCommand().contains("VerifySQLResponse")
                    && !param.getCommand().contains("VerifyElements")
                    && !param.getCommand().contains("VerifyElementByJS")
                    && !param.getCommand().contains("ExecuteSQL")
                    && !param.getCommand().contains("ExecuteJS")
                    && !param.getCommand().contains("ExecuteBatchScript")
                    && !param.getCommand().contains("CompareFile")
                    && !param.getCommand().contains("PressKey")
                    && !param.getCommand().contains("ClearBrowserCache")
                    && !param.getCommand().contains("Terminate")
                    && !param.getCommand().contains("OpenBrowserTab")
                    && !param.getCommand().contains("IF")
                    && !param.getCommand().contains("LogMessage")
                    && !param.getCommand().contains("ExecuteTest")
                    && !param.getCommand().contains("SaveAsPDF")
                    && !param.getCommand().contains("ClickIcon")
                    && !param.getCommand().contains("VerifyBrowserTab")
                    && !param.getCommand().contains("SetBrowserTimezone")
                    && !param.getCommand().contains("ResetBrowserTimezone")
                    && !param.getCommand().contains("CopyFile")
                    && !param.getCommand().contains("LOOP")
                    && !param.getCommand().contains("EndIFrame")
                    && !param.getCommand().contains("SetIFrameElement")
                    && !param.getCommand().contains("CompareDate")
                    && !param.getCommand().contains("ExecuteTest")
            ){
                System.out.println("===============================" + param.getCommand());
                if(null == param.getArgs() || param.getArgs().length <= 0)
                    return null;

                if(null == PropertyReader.getProperty(param.getArgs()[0])) {
                    throw new PropertyKeyNotFoundException(param.getArgs()[0]);
                }
                String[] propertyObject = PropertyReader.getProperty(param.getArgs()[0]).split(",");
                if (propertyObject.length >= 3 && StringUtils.isNotEmpty(propertyObject[2].trim())) {
                    switch (Options.valueOf(propertyObject[2].trim().toUpperCase()).getOption()) {
                        case "ScrollToElement":
                            command.scrollToElement(param.getArgs()[0]);
                            break;
                        case "MoveToElement":
                            command.moveToElement(param.getArgs()[0]);
                        default:
                    }
                }
            }
        } catch (IllegalArgumentException iex){
            System.out.println("ActionHelper - This will fix in future release - Option not found " + param.toString());
            iex.printStackTrace();
        }
        return null;
    }

    /**
     * Print command params
     * @param params input params
     * @return command params as text
     */
    public static String printParams(String opration, String[] params) {
        //String paramStr = "";
        if(params.length == 0) {
            //return paramStr = opration;
            return opration;
        }
        /*String[] paramArray = ArrayUtils.addAll(new String[]{opration},
                (null == PropertyReader.getProperty(params[0])) ?
                        new String[]{params[0]} : PropertyReader.getProperty(params[0]).split(","));*/
        StringBuilder sb = new StringBuilder();
        sb.append(opration);
        sb.append(" ");
        //paramStr += opration + " ";
        for (String param : params) {
            //paramStr += param + " ";
            sb.append(param);
            sb.append(" ");
        }
        //}
        return sb.toString().replaceAll("#@##@#", "<br>").replaceAll("#@#"," ");
    }

    /**
     * Handle broken test
     * @param printParams input param
     * @throws RecoverBrokenTestExecutionException if recover broken test fails
     * @throws TestException if test issue
     * @throws ShutdownTestExecution if shutdown issue
     * @throws HandleFailOverTestExecution if anything happen while handling the fail-over steps
     * @throws CommandException if command fails
     */
    public void handleBrokenTestExecution(String printParams) throws RecoverBrokenTestExecutionException, TestException, ShutdownTestExecution, HandleFailOverTestExecution, CommandException {
        LogUtil.logTestCaseMsg("Start Handling the execution for broken test....");
        ReportGenerator.getReportInstance().getChildNodeLevel1().fail("<B>" + printParams
                + "</B><br><font color='red'>Lost communication with browser due to some error</font>"
                + "</B><br><font color='red'>Re-Establishing connection/communication with browser</font>");
        String currentUrl = TestContext.getTestContext("").getCurrentUrl();

        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
        } catch (AWTException e) {
            LogUtil.logTestCaseErrorMsg("Try to verify and Escape any OS popup windows - ", e);
        }

        //throw new ShutdownTestExecution("HttpTimeoutException - ActionExecutor Shutdown called - 4 ");
        DriverManager.getInstance().shutdownWebDriver();
        DriverManager.getInstance().loadDriver();
        DriverManager.getInstance().getWebDriver().get(String.valueOf(TestContext.getTestContext("").getGlobalVariable().get("lastUsedOpenAppUrl")));
        ExcelTestCase excelTestCase = (ExcelTestCase) TestContext.getTestContext("").getGlobalVariable().get("lastLoginTestSteps");
        boolean nestedNodeEnabled = Boolean.valueOf(String.valueOf(TestContext.getTestContext("").getGlobalVariable().get("lastLoginNestedNodeEnabled")));
        new TestCaseHelper().executeTestStep(excelTestCase, nestedNodeEnabled, false);
        new OpenNewBrowserTabCmd().execute(null);
        DriverManager.getInstance().getWebDriver().get(currentUrl);
        throw new RecoverBrokenTestExecutionException("", excelTestCase,true, currentUrl);

        //DriverManager.getInstance().refereshPage();
    }
}
