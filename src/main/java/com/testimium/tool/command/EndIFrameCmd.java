package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import org.openqa.selenium.WebDriver;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class EndIFrameCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static EndIFrameCmd endIFrameCmd = new EndIFrameCmd();

    public EndIFrameCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }


    public static EndIFrameCmd getInstance() {
        //TODO Fix Me for concurrent access and change it to lazy
        return endIFrameCmd;
    }
    /**
     * EndIFrame
     */

    //TODO Have to decide either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null != param.getArgs() && param.getArgs().length > 0)
                throw new CommandException("Command argument not required");

                driver.switchTo().defaultContent();

        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}
