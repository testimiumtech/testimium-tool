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
public class VerifyElementIsNotDisplayedCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static VerifyElementIsNotDisplayedCmd verifyElement = new VerifyElementIsNotDisplayedCmd();

    public VerifyElementIsNotDisplayedCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static VerifyElementIsNotDisplayedCmd getInstance() {
        //TODO Fix Me for concurrent access
        return verifyElement;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length != 1)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey). ");

            //WebElement element =  wait(param);
            WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));

            assertElement((!element.isDisplayed()) ? "Element should not be displayed" : "Element is displayed",
                    "Element should not be displayed");

            /*assertThat(element.isDisplayed()).as(MessageFormat.format(
                    "Assertion Failed... Given Element should not be displayed but it is displayed: {0}",
                    new Object[] {param.getArgs()[0]})
            ).isEqualTo(false);
            *//*if(element.isDisplayed()) {
               // verifyElement.failOver(param);
                throw new VerificationFailedException("Element is displayed for command argument: " + param.getArgs()[0]);
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
        } catch (NoSuchElementException | LocatorNotFoundException ex) {
            LogUtil.logTestCaseMsg("NoSuchElementException for the command VerifyElementIsNotDisplayedCmd, this is expected result.");
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowAssertion(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


