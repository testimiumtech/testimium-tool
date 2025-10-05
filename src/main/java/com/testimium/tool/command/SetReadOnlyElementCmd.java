package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.reader.PropertyReader;
import org.openqa.selenium.*;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class SetReadOnlyElementCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static SetReadOnlyElementCmd setElementCommand = new SetReadOnlyElementCmd();

    public SetReadOnlyElementCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }


    public static SetReadOnlyElementCmd getInstance() {
        //TODO Fix Me for concurrent access and change it to lazy
        return setElementCommand;
    }

    //TODO Have to decide either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 2)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey, Value). ");

            WebElement element = wait(param);
            //WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));

                if (param.getArgs()[1].contains("${")) {
                    element.sendKeys(PropertyReader.getProperty(param.getArgs()[1].substring(param.getArgs()[1].indexOf("${") + 2, param.getArgs()[1].indexOf("}"))));
                } else {
                    element.sendKeys(param.getArgs()[1].trim().replace("''", "").replaceAll("(\\$\\$|##|@@)", " "));
                }
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}
