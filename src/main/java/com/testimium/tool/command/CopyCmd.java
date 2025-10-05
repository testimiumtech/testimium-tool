package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.locator.LocatorFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class CopyCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static CopyCmd copyCmd = new CopyCmd();

    public CopyCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static CopyCmd getInstance() {
        //TODO Fix Me for concurrent access
        return copyCmd;
    }

    //Copy <locator-property> <Variable Name>
    //Copy <locator-property> <Variable Name> <Html Attribute>
    //Copy <locator-property> -setAsGlobalVariable <Variable Name>
    //Copy <locator-property> -setAsGlobalVariable <Variable Name> <Html Attribute>
    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 2 || param.getArgs().length > 4)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey/locator, (optional) -setAsGlobalVariable <Variable Name>, userDefinedName, Html attribute is optional).  ");

            //Command Syntex
            // Copy <locator-property> <Variable Name>
            // Copy <locator-property> <Variable Name> <Html Attribute>
            // Copy <locator-property> -setAsGlobalVariable <option is Optional> <Variable Name>
            // Copy <locator-property> -setAsGlobalVariable <option is Optional> <Variable Name> <Html Attribute>

            //TODO Command should able to copy the value from dropdown and for any attribute
            wait(param);
            WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
            //If argument length is equal to 2 then below is the default
            String variableName = param.getArgs()[1];
            String value = element.getText();

            switch(param.getArgs().length){
                case 2:
                    variableName = param.getArgs()[1];
                    value = element.getText();
                    break;
                case 3:
                    if("-setAsGlobalVariable".equals(param.getArgs()[1])) {
                        variableName = param.getArgs()[2];
                        value = element.getText();
                    } else {
                        variableName = param.getArgs()[1];
                        value = element.getAttribute(param.getArgs()[2]);
                    }
                    break;
                case 4:
                    //If option to setAsGlobalVariable
                    variableName = param.getArgs()[2];
                    value = element.getAttribute(param.getArgs()[3]);
                    TestContext.getTestContext("").getGlobalVariable().put(variableName, value);
                    break;
                default:

            }

            if(param.getArgs().length < 4)
                TestContext.getTestContext("").getCopiedVariable().put(variableName, value);

        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


