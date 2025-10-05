package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
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
public class VerifyElementIsSelectedCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static VerifyElementIsSelectedCmd verifyElement = new VerifyElementIsSelectedCmd();

    public VerifyElementIsSelectedCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static VerifyElementIsSelectedCmd getInstance() {
        //TODO Fix Me for concurrent access
        return verifyElement;
    }

    //VerifyElementIsSelected <<locator-property>
    //VerifyElementIsSelected <<locator-property> <HTML Attribute>

    //Internal use from IfCmd
    //VerifyElementIsSelected <<locator-property> <-fromIf>
    //VerifyElementIsSelected <<locator-property> <HTML Attribute> <-fromIf>
    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        boolean isSelected = false;
        try {
            if(null == param.getArgs() || param.getArgs().length < 1)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey). ");

            wait(param);
            WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
            //Call from IfCmd Command class will be the length == 3
            if((param.getArgs().length == 2 && !"-fromIf".equals(param.getArgs()[1]))
                    || (param.getArgs().length == 3 && "-fromIf".equals(param.getArgs()[2]))) {
                String status = element.getAttribute(param.getArgs()[1].trim());
                switch((null==status)? "": status) {
                    case "true":
                        isSelected = true;
                        break;
                    default:
                        new LogMessageCmd().execute(new CommandParam(param.getTestCaseName(), param.getCommand(), new String[]{"INFO", "HTML Attribute value not found"}, null, null));
                }
            } else {
                isSelected = element.isSelected();
            }

            /*assertThat(element.isSelected()).as(MessageFormat.format(
                    "Assertion Failed... Given Element should be selected but it is not selected: {0}",
                    new Object[] {param.getArgs()[0]})Ok
            ).isEqualTo(true);
            *//*if(!element.isSelected()) {
                //verifyElement.failOver(param);
                throw new VerificationFailedException("Element is not selected for command argument: " + param.getArgs()[0]);
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
            if(param.getArgs().length == 1 || (param.getArgs().length == 2 && !"-fromIf".equals(param.getArgs()[1]))) {
                assertElement((isSelected) ? "Element should be selected" : "Element is not selected",
                        "Element should be selected");
            }
        } catch (NoSuchElementException ex) {
            LogUtil.logTestCaseErrorMsg("NoSuchElementException for the command VerifyElementIsSelectedCmd, this is not expected result.", ex);
            try {
                assertElement("Element is not present/displayed", "Element should be present/displayed");
            } catch (VerificationException e) {
                throw new AssertionError("<br>Verification failed for command'"
                        + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);
            }
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowAssertion(null, param, ex);
        }
        return new CommandResponse("(Success)", isSelected);
    }
}


