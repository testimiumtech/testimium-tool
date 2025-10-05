package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.http.HttpTimeoutException;

/**
 * Set IFRAME Element
 * @author Sandeep Agrawal
 */
public class SetIFrameElementCmd implements ExternalCommand<CommandParam, CommandResponse> {
    /**
     * Web driver object
     */
    private WebDriver driver;
    /**
     * Static object of SetIFrameElementCmd
     */
    private static SetIFrameElementCmd setIFrameElementCmd = new SetIFrameElementCmd();

    public SetIFrameElementCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    /**
     * Get instance of this class
     * @return static object SetIFrameElementCmd
     */
    public static SetIFrameElementCmd getInstance() {
        //TODO Fix Me for concurrent access and change it to lazy
        return setIFrameElementCmd;
    }

    /**
     * SetIFrameElement -body Value
     * @param param input param
     * @return command response
     * @throws CommandException if command exception
     * @throws HttpTimeoutException if http timeout exception
     */
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 2)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey, Value). ");

            if("-body".equalsIgnoreCase(param.getArgs()[0])){
                WebElement element = driver.findElement(By.cssSelector("body"));
                element.sendKeys(param.getArgs()[1].trim().replace("''", "").replaceAll("(\\$\\$|##|@@)", " "));
            }

        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}
