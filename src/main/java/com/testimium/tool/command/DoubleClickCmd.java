package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class DoubleClickCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static DoubleClickCmd doubleClickCmd = new DoubleClickCmd();

    public DoubleClickCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static DoubleClickCmd getInstance() {
        //TODO Fix Me for concurrent access
        return doubleClickCmd;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length != 1)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey).  ");

            WebElement element = wait(param);
            //WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
            Actions actions = new Actions(driver);
            actions.moveToElement(element).doubleClick().perform();

        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


