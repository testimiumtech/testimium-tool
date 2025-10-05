package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.domain.TestJsonInputData;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.exception.LocatorNotFoundException;
import com.testimium.tool.exception.PropertyKeyNotFoundException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.locator.LocatorFactory;
import org.openqa.selenium.*;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class ExecuteJSCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static ExecuteJSCmd javaScriptCmd = new ExecuteJSCmd();

    public ExecuteJSCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static ExecuteJSCmd getInstance() {
        //TODO Fix Me for concurrent access
        return javaScriptCmd;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 1)
                throw new CommandException("Command argument is missing: provide Command argument (Property key or/and Json Key).  ");

            TestJsonInputData inputData = getTestJsonInputData(param);
            if(null == inputData || null == inputData.getScripts() || inputData.getScripts().size() <= 0)
                throw new CommandException("System not able to find the Javascript as input ");

            //TODO Fix me validate the argument correctly, it should take 2 orr either args propertyKey and Script key
            WebElement element = null;

            if(param.getArgs().length == 2) {
                element = wait(param);
                //element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
                //element = findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
            } else {
                new WaitCmd().execute(new CommandParam(null,null, new String[]{"-second","2000"}, null, null));
            }

            WebElement finalElement = element;

            inputData.getScripts().entrySet().stream()
                    .filter(key -> key.getKey().equalsIgnoreCase((param.getArgs().length == 1) ? param.getArgs()[0] : param.getArgs()[1]))
                    .forEach(map -> {
                        System.out.println("Script value: " + map.getValue());
                        map.getValue().stream().forEach(innerM -> {
                            System.out.println("Script: " + innerM.get("script"));

                            if(param.getArgs().length == 1) {
                                javaScriptCmd.executeJavaScript(innerM.get("script").toString());
                            } else if(param.getArgs().length == 2) {
                                javaScriptCmd.executeJavaScript(innerM.get("script").toString(), finalElement);
                            } else {
                                try {
                                    javaScriptCmd.executeJavaScript(innerM.get("script").toString(),
                                            driver.findElement(LocatorFactory.getByLocatorProperty(
                                                    (null == innerM.get("propertyKey"))? param.getArgs()[0] : innerM.get("propertyKey").toString())));
                                } catch (PropertyKeyNotFoundException | LocatorNotFoundException ex) {
                                    throw new RuntimeException("Failed to execute command'"
                                            + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. " + ex.getMessage(), ex);
                                }
                            }
                        });
                    });
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


