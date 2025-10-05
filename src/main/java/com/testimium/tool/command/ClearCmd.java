package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import org.openqa.selenium.*;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class ClearCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static ClearCmd clearCmd = new ClearCmd();

    public ClearCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static ClearCmd getInstance() {
        //TODO Fix Me for concurrent access
        return clearCmd;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length != 1)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey).  ");

            WebElement element = wait(param);
            //WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
            element.clear();
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }

    /* TODO
        1. You have need to use deselectAll() method to unchecked / clear all selected single / all Drop Down / Multi select options if by default selected any option within code.
            To clear all drop down selections example:
             -first, initialize the WebElement (using the appropriate selector type, this is just an example)
                WebElement element = driver.findElement(By.id("elementId"));
             -next, pass it to the Select object
                Select selectElement = new Select(element);
             -then, select an option from the element
                selectElement.selectByVisibleText("Option Text");
             -finally, to clear your selection, try:
                selectElement.deselectAll();


     */
}


