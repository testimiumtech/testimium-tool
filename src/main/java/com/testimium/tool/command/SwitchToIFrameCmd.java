package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.http.HttpTimeoutException;

/**
 * Command is used to switch to Frame element
 * @author Sandeep Agrawal
 */
public class SwitchToIFrameCmd implements ExternalCommand<CommandParam, CommandResponse> {
    /**
     * Web driver object
     */
    private WebDriver driver;
    /**
     * Static object of SwitchToIFrameCmd
     */
    private static SwitchToIFrameCmd switchToIFrameCmd = new SwitchToIFrameCmd();

    public SwitchToIFrameCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    /**
     * Get the static instance
     * @return response SwitchToIFrameCmd
     */
    public static SwitchToIFrameCmd getInstance() {
        //TODO Fix Me for concurrent access and change it to lazy
        return switchToIFrameCmd;
    }

    /**
     * SwitchToIFrame locator-property
     *     SetIFrameElement -body Value
     * EndIFrame
     * @param param input
     * @return command response
     * @throws CommandException if command exception
     * @throws HttpTimeoutException if http timeout
     */
    //SwitchToIFrame <locator-property>
    //TODO Have to decide either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 1)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey for locator). ");

            WebElement element = wait(param);
            //WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
            driver.switchTo().frame(element);

            /*if("-iframe".equalsIgnoreCase(param.getArgs()[1])){
                driver.switchTo().frame(element);
                element = driver.findElement(By.cssSelector("body"));
                element.sendKeys(param.getArgs()[2].trim().replace("''", "").replaceAll("(\\$\\$|##|@@)", " "));
                driver.switchTo().defaultContent();
            } */

        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}
