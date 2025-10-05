package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import org.openqa.selenium.WebDriver;
/**
 * @author Sandeep Agrawal
 */
public class SwitchTabCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static SwitchTabCmd waitCommand = new SwitchTabCmd();

    public SwitchTabCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static SwitchTabCmd getInstance() {
        //TODO Fix Me for concurrent excess
        return waitCommand;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException {
        new WaitCmd().execute(new CommandParam(null,null, new String[]{"2000"}, null, null));
        if (null == param.getArgs() || param.getArgs().length == 0)
            DriverManager.getInstance().focusCurrentWindow();
        else
            DriverManager.getInstance().switchTab(Integer.valueOf(param.getArgs()[0]) - 1);

        new WaitCmd().execute(new CommandParam(null,null, new String[]{"2000"}, null, null));
        return new CommandResponse("(Success)", true);
    }
}
