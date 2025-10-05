package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import org.openqa.selenium.WebDriver;

public class TerminateCmd implements ExternalCommand<CommandParam, CommandResponse>  {
    private WebDriver driver;
    private static TerminateCmd terminateCmd = new TerminateCmd();

    public TerminateCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static TerminateCmd getInstance() {
        //TODO Fix Me for concurrent access
        return terminateCmd;
    }
    @Override
    public CommandResponse execute(CommandParam commandParam) throws CommandException {
        throw new CommandException(true, "Terminate command applied to stop execution of test");
    }
}
