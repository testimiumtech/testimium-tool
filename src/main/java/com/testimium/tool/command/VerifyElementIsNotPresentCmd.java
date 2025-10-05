package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.exception.LocatorNotFoundException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.locator.LocatorFactory;
import com.testimium.tool.logging.LogUtil;
import org.openqa.selenium.*;

import java.net.http.HttpTimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sandeep Agrawal
 */
public class VerifyElementIsNotPresentCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static VerifyElementIsNotPresentCmd verifyElement = new VerifyElementIsNotPresentCmd();

    public VerifyElementIsNotPresentCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static VerifyElementIsNotPresentCmd getInstance() {
        //TODO Fix Me for concurrent access
        return verifyElement;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length != 1)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey).  ");

            //WebElement element =  wait(param);
            WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
            /*assertThat(element).as(MessageFormat.format(
                    "Assertion Failed... Given Element should not be displayed but it is  present: {0}",
                    new Object[] {param.getArgs()[0]})
            ).isNull();
       */
            assertElement((null != element) ? "Element is present" : "Element should not be present",
                    "Element should not be present");

        } catch (NoSuchElementException | LocatorNotFoundException ex) {
            LogUtil.logTestCaseMsg("NoSuchElementException for the command VerifyElementIsNotPresentCmd, this is expected result.");
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowAssertion(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


