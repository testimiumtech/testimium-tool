package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.reader.PropertyReader;
import org.openqa.selenium.WebDriver;

/**
 * @author Sandeep Agrawal
 */
public class SetGlobalVariableCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static SetGlobalVariableCmd globalVariableCmd = new SetGlobalVariableCmd();

    public SetGlobalVariableCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static SetGlobalVariableCmd getInstance() {
        //TODO Fix Me for concurrent access
        return globalVariableCmd;
    }

    //SetGlobalVariable <property (property key consider as Variable Name which contains value)>
    //SetGlobalVariable <Variable Name> value1
    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 1)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey/locator, userDefinedName, Html attribute is optional).  ");

            String key = param.getArgs()[0];
            String value = "";

            if(param.getArgs().length == 1) {
               value = PropertyReader.getProperty(key);
            } else {
                key = param.getArgs()[0];
                value = param.getArgs()[1].trim().replace("''","").replaceAll("(\\$\\$|##|@@)", " ");
            }

            TestContext.getTestContext("").getGlobalVariable().put(key, value);

        } catch (Exception ex) {
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


