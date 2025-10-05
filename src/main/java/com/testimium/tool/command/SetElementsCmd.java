package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;

import com.testimium.tool.exception.*;
import org.openqa.selenium.WebDriver;
/**
 * @author Sandeep Agrawal
 */
public class SetElementsCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static SetElementsCmd setElementsCommand = new SetElementsCmd();

    public SetElementsCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static SetElementsCmd getInstance() {
        //TODO Fix Me for concurrent access
        return setElementsCommand;
    }

    //TODO Have to decide either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, ShutdownTestExecution, TestException, HandleFailOverTestExecution, RecoverBrokenTestExecutionException {

        if(null == param.getArgs() || param.getArgs().length != 1)
            throw new CommandException("Command argument is missing: provide Command argument (JsonKey). ");

        processMultiTestSteps(param);
        return new CommandResponse("(Success)", true);
    }
}
