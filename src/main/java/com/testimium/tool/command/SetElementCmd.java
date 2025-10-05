package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.reader.PropertyReader;
import com.testimium.tool.utility.FileUtility;
import com.testimium.tool.utility.PropertyUtility;
import org.openqa.selenium.*;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class SetElementCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static SetElementCmd setElementCommand = new SetElementCmd();

    public SetElementCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }


    public static SetElementCmd getInstance() {
        //TODO Fix Me for concurrent access and change it to lazy
        return setElementCommand;
    }
    //SetElement <locator-property>  <HTML Element Value>
    //SetElement <locator-property>  <${<Property key which contains value>}>
    //SetElement <locator-property>  <-paste or -copiedVariable> <Variable Name>
    //SetElement <locator-property>  -globalVariable <Variable Name>
    //TODO Have to decide either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 2)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey, Value). ");

            WebElement element = wait(param);
            //WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));

            if("-paste".equalsIgnoreCase(param.getArgs()[1]) || "-copiedVariable".equalsIgnoreCase(param.getArgs()[1])){
                if(param.getArgs().length < 3)
                    throw new CommandException("Command argument is missing: provide Command argument (propertyKey, -paste, name (copied key/name)). ");

                element.sendKeys(String.valueOf(TestContext.getTestContext("").getCopiedVariable().get(param.getArgs()[2])));
            } else if("-globalVariable".equals(param.getArgs()[1])) {
                if(param.getArgs().length < 3)
                    throw new CommandException("Command argument is missing: provide Command argument (propertyKey, -globalVariable, name (global Variable key/name)). ");

                element.sendKeys(String.valueOf(TestContext.getTestContext("").getGlobalVariable().get(param.getArgs()[2])));
            } else if("-getAbsolutePath".equalsIgnoreCase(param.getArgs()[1])){
                if(param.getArgs().length < 3)
                    throw new CommandException("Command argument is missing: provide Command argument (propertyKey -getAbsolutePath relativeFilePath). ");

                element.sendKeys(FileUtility.getAbsolutePath(PropertyUtility.getExternalDirPath().trim() + "/" +  param.getArgs()[2].trim()));
            } else {
                if (param.getArgs()[1].contains("${")) {
                    element.sendKeys(PropertyReader.getProperty(param.getArgs()[1].substring(param.getArgs()[1].indexOf("${") + 2, param.getArgs()[1].indexOf("}"))));
                } else {
                    element.sendKeys(param.getArgs()[1].trim().replace("''", "").replaceAll("(\\$\\$|##|@@)", " "));
                }
            }
                /*TestContext.getTestContext("")
                        .getTestInputMap().put(PropertyReader.getProperty(param.getArgs()[0]).split(",")[1].trim(),
                        param.getArgs()[1].replaceAll("(\\$\\$|##|@@)", " ").trim());*/
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}
