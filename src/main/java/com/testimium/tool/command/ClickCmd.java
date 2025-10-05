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
public class ClickCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static ClickCmd clickCommand = new ClickCmd();

    public ClickCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static ClickCmd getInstance() {
        //TODO Fix Me for concurrent access
        return clickCommand;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 1)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey).  ");

           /* if(param.getArgs().length > 1 && "-scrollRowIntoView".equals(param.getArgs()[1])) {
                //Added for slick grid data table
                executeJavaScript("grid.scrollRowIntoView(" + param.getArgs()[2] + ",true)", "");
            }*/
            WebElement element = wait(param);
            //WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().build().perform();
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


