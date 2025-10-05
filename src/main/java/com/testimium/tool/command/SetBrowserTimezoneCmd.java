package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.reader.PropertyReader;
import org.openqa.selenium.WebDriver;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class SetBrowserTimezoneCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static SetBrowserTimezoneCmd setBrowserTimezoneCmd = new SetBrowserTimezoneCmd();

    public SetBrowserTimezoneCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }


    public static SetBrowserTimezoneCmd getInstance() {
        //TODO Fix Me for concurrent access and change it to lazy
        return setBrowserTimezoneCmd;
    }
    //SetBrowserTimezone <timezoneId>
    //SetBrowserTimezone -property <Property key which contains timezoneId value>
    //TODO Have to decide either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 1)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey or timezoneId). ");

            switch (param.getArgs()[0].toLowerCase()){
                case "-property":
                    DriverManager.getInstance().setBrowserTimezone(PropertyReader.getProperty(param.getArgs()[1]));
                default:
                    DriverManager.getInstance().setBrowserTimezone(param.getArgs()[0]);
            }


        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}
