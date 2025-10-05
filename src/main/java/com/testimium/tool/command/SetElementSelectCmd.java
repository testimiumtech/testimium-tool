package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class SetElementSelectCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static SetElementSelectCmd setElementSelectCommand = new SetElementSelectCmd();

    public SetElementSelectCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static SetElementSelectCmd getInstance() {
        //TODO Fix Me for concurrent access
        return setElementSelectCommand;
    }

    //SetElementSelect <locator-property>  <HTML Element Value>
    //SetElementSelect <locator-property>  <-paste or -copiedVariable> <Variable Name>
    //SetElementSelect <locator-property>  -globalVariable <Variable Name>
    //TODO Have to decide either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 2)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey, Value). ");

            WebElement element = wait(param);
            //WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
            //DriverManager.getInstance().scrollToElement("arguments[0].scrollIntoView(true);", element);
            Select dropdown = new Select(element);
            String value = param.getArgs()[1].replace("''","").replaceAll("(\\$\\$|##|@@)", " ").trim();

            if("-paste".equalsIgnoreCase(param.getArgs()[1]) || "-copiedVariable".equalsIgnoreCase(param.getArgs()[1])){
                if(param.getArgs().length < 3)
                    throw new CommandException("Command argument is missing: provide Command argument (propertyKey, -paste, name (copied key/name)). ");

                value = String.valueOf(TestContext.getTestContext("").getCopiedVariable().get(param.getArgs()[2])).replace("''","").replaceAll("(\\$\\$|##|@@)", " ").trim();

            } else if("-globalVariable".equals(param.getArgs()[1])) {
                if(param.getArgs().length < 3)
                    throw new CommandException("Command argument is missing: provide Command argument (propertyKey, -globalVariable, name (global Variable key/name)). ");

                value = String.valueOf(TestContext.getTestContext("").getGlobalVariable().get(param.getArgs()[2])).replace("''","").replaceAll("(\\$\\$|##|@@)", " ").trim();
            }

            dropdown.selectByVisibleText(value);

        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}
