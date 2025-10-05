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
public class VerifyElementIsEnabledCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static VerifyElementIsEnabledCmd verifyElement = new VerifyElementIsEnabledCmd();

    public VerifyElementIsEnabledCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static VerifyElementIsEnabledCmd getInstance() {
        //TODO Fix Me for concurrent access
        return verifyElement;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 1)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey). ");


            //WebElement element =  wait(param);
            WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));

           /* assertThat(element.isEnabled()).as(MessageFormat.format(
                    "Assertion Failed... Given Element should be enabled but it is not enabled: {0}",
                    new Object[] {param.getArgs()[0]})
            ).isEqualTo(true);
            *//*if(!element.isEnabled()) {
                //verifyElement.failOver(param);
                throw new VerificationFailedException("Element is not enabled for command argument: " + param.getArgs()[0]);
            }*//*
        } catch (TimeoutException tex) {
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ tex.getMessage(), tex);
        } catch (ElementNotInteractableException eix) {
            throw new CommandException("For command " + param.getCommand() + ", element is not reachable while clicking to property key "
                    + param.getArgs()[0] + "'. "+ eix.getMessage(), eix);
        } catch (JavascriptException | PropertyKeyNotFoundException | LocatorNotFoundException | NoSuchElementException | NoSuchWindowException ex){
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);
        } catch (Exception ex) {
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);
        }*/
            assertElement((element.isEnabled()) ? "Element should be enabled" : "Element is disabled",
                    "Element should be enabled");

        } catch (NoSuchElementException | LocatorNotFoundException ex) {
            LogUtil.logTestCaseErrorMsg("NoSuchElementException for the command VerifyElementIsDisplayedCmd, this is not expected result.", ex);
            try {
                assertElement("Element is not present at all", "Element should be present and enabled");
            } catch (VerificationException e) {
                throw new AssertionError("<br>Verification failed for command'"
                        + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);
            }
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowAssertion(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


