package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.exception.LocatorNotFoundException;
import com.testimium.tool.exception.VerificationException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.locator.LocatorFactory;
import com.testimium.tool.logging.LogUtil;
import org.openqa.selenium.*;

import java.net.http.HttpTimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sandeep Agrawal
 */
public class VerifyElementIsDisplayedCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static VerifyElementIsDisplayedCmd verifyElement = new VerifyElementIsDisplayedCmd();

    public VerifyElementIsDisplayedCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static VerifyElementIsDisplayedCmd getInstance() {
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

           /* assertThat(element.isDisplayed()).as(MessageFormat.format(
                    "Assertion Failed... Given Element should be displayed but it is not displayed: {0}",
                    new Object[] {param.getArgs()[0]})
            ).isEqualTo(true);*/
            /*if(!element.isDisplayed()) {
                throw new VerificationFailedException("Element is not displayed for command argument: " + param.getArgs()[0]);
            }*/
            assertElement((element.isDisplayed()) ? "Element should be displayed" : "Element is not displayed",
                    "Element should be displayed");
        } catch (NoSuchElementException | LocatorNotFoundException ex) {
            LogUtil.logTestCaseErrorMsg("NoSuchElementException for the command VerifyElementIsDisplayed, this is not expected result.", ex);
            try {
                assertElement("Element is not displayed", "Element should be displayed");
            } catch (VerificationException e) {
                throw new AssertionError("<br>Verification failed for command'"
                        + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ e.getMessage(), e);
            }
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowAssertion(null, param, ex);
        }

        return new CommandResponse("(Success)", true);
    }
}


