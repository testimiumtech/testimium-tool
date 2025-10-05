package com.testimium.tool.executor;

import com.aventstack.extentreports.ExtentTest;
import com.testimium.tool.command.Command;
import com.testimium.tool.command.HandleFailOverCmd;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.domain.TestJsonInputData;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.exception.ShutdownTestExecution;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.report.generator.ReportGenerator;
import org.apache.commons.lang3.StringUtils;

import java.net.http.HttpTimeoutException;

public class HandleFailOverExecutor implements Action<CommandParam, CommandResponse>{


    @Override
    public CommandResponse perform(CommandParam commandParam) throws ShutdownTestExecution {
        CommandResponse commandResponse = null;
        ExtentTest initialExtentTestChild = ReportGenerator.getReportInstance().getChildNodeLevel1();
        try {
            if(StringUtils.isNotEmpty(commandParam.getInputParam())) {
                Command command = new HandleFailOverCmd();
                TestJsonInputData inputData = command.getTestJsonInputData(commandParam);
                //Test Case level fail over test steps will executes only and only if key called 'failOverSteps' present in input param under formElements.
                if(null != inputData && null != inputData.getFormElements()
                        && inputData.getFormElements().size() > 0
                        && inputData.getFormElements().containsKey("failOverSteps")) {
                    LogUtil.logTestCaseErrorMsg("Start - Testcase Level - Handle FailOver Steps", null);
                    //TestContext.getTestContext("").getTest().log(Status.FAIL, "Start - Testcase Level - Handle FailOver Steps");

                    ExtentTest nestedChild =  initialExtentTestChild.createNode("Start - Testcase Level - Handle FailOver Steps");
                    ReportGenerator.getReportInstance().setChildNodeLevel1(nestedChild);

                    String inputParam = commandParam.getInputParam();
                    inputData.getFormElements().remove("failOverSteps");

                    commandResponse = new HandleFailOverCmd().execute(new CommandParam(
                            commandParam.getTestCaseName(),
                            "HandleFailOver",
                            commandParam.getArgs(), //new String[]{"failOverSteps"},
                            inputParam,
                            commandParam.getAssertParam()));

                    commandResponse.setSkipNumberOfNextTestCases(inputData.getSkipNumberOfNextTestCases());
                    commandResponse.setFailNumberOfNextTestCases(inputData.getFailNumberOfNextTestCases());
                    LogUtil.logTestCaseErrorMsg("Finish - Testcase Level - Handle FailOver Steps", null);
                    //TestContext.getTestContext("").getTest().log(Status.FAIL, "Finish - Testcase Level - Handle FailOver Steps");
                    ReportGenerator.getReportInstance().getChildNodeLevel1().fail("Finish - Testcase Level - Handle FailOver Steps");
                    ReportGenerator.getReportInstance().setChildNodeLevel1(initialExtentTestChild);
                } else {
                    commandResponse = new CommandResponse("(Sucess)", true);
                }
            } else {
                commandResponse = new CommandResponse("(Sucess)", true);
            }
        } catch (CommandException | HttpTimeoutException ex) {
            ReportGenerator.getReportInstance().setChildNodeLevel1(initialExtentTestChild);
            //ex.printStackTrace();
            LogUtil.logTestCaseErrorMsg("HandleFailOverExecutor class Exception handling: " + ex.getMessage(), ex);
            commandResponse = new CommandResponse("(Failed)", false);
            /*if(ex instanceof HttpTimeoutException){
                DriverManager.getInstance().shutdownWebDriver();
                DriverManager.getInstance().loadDriver();
                DriverManager.getInstance().refereshPage();
            }*/
        }

        return commandResponse;
    }
}
