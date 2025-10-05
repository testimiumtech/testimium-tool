package com.testimium.tool.command;

import com.aventstack.extentreports.Status;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.executor.ActionHelper;

/**
 * @author Sandeep Agrawal
 */
public class LogMessageCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private static LogMessageCmd logMessageCmd = new LogMessageCmd();

    public LogMessageCmd() {
            }

    public static LogMessageCmd getInstance() {
        //TODO Fix Me for concurrent access
        return logMessageCmd;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException {
        if(null == param.getArgs() || param.getArgs().length < 1)
            throw new CommandException("Command argument is missing: provide Command argument (messageType(Fail/Pass/Info) message). ");

        String message = ((param.getArgs().length == 2) ?
                param.getArgs()[1].replace("''","").replaceAll("(\\$\\$|##|@@)", " ")
                : ActionHelper.printParams("", param.getArgs()));

        switch (param.getArgs()[0].toUpperCase()){
            case "FAIL" :
                TestContext.getTestContext("").getTest().log(Status.FAIL, message);
                break;
            case "PASS" :
                TestContext.getTestContext("").getTest().log(Status.PASS, message);
                break;
            case "INFO" :
                TestContext.getTestContext("").getTest().log(Status.INFO, message);
                break;
            default:
                TestContext.getTestContext("").getTest().log(Status.INFO, message);

        }
        return new CommandResponse("(Success)", true, param.getCommand());
    }
}


