package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.exception.VerificationException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.locator.LocatorFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.net.http.HttpTimeoutException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sandeep Agrawal
 */
public class VerifyElementSelectCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static VerifyElementSelectCmd verifyCommand = new VerifyElementSelectCmd();

    public VerifyElementSelectCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static VerifyElementSelectCmd getInstance() {
        //TODO Fix Me for concurrent access
        return verifyCommand;
    }

    //Supported Syntax
    // VerifyElementSelect <locator-property> <Expected Value>
    // VerifyElementSelect <locator-property> -globalVariable <Expected Variable Name>
    // VerifyElementSelect <locator-property> -list -exactMatch
    // VerifyElementSelect <locator-property> -matchAny
    // VerifyElementSelect <locator-property> -exactMatch
    // VerifyElementSelect <locator-property> -matchAny <-assertKey> <Assert key Name>
    // VerifyElementSelect <locator-property> -exactMatch <-assertKey> <Assert key Name>

    //TODO - Need to implement below syntax to support HTML attribute
    // VerifyElementSelect <locator-property> <Expected Value> <Actual Html Attribute>
    // VerifyElementSelect <locator-property> -globalVariable <Expected Variable Name> <Actual Html Attribute>
    // VerifyElementSelect <locator-property> -matchAny <Actual Html Attribute>
    // VerifyElementSelect <locator-property> -exactMatch <Actual Html Attribute>
    // VerifyElementSelect <locator-property> -matchAny <-assertKey> <Assert key Name> <Actual Html Attribute>
    // VerifyElementSelect <locator-property> -exactMatch <-assertKey> <Assert key Name> <Actual Html Attribute>

    //Assert Param Json Configuration
   /* {
        "assertParams": {
        "Verify-Dropdown-values": {
            "exactMatch" : ["A",5,"C", 10.00],
            "matchAny": ["A",5,"C", 10.00]
        }
    }
    }*/

    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 2)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey, Value)  ");

            wait(param);
            WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
            Select dropdown = new Select(element);

            String actual = dropdown.getFirstSelectedOption().getText();
            String expected = param.getArgs()[1].trim().replace("''","").replaceAll("(\\$\\$|##|@@)", " ");;

            switch(param.getArgs().length){
                case 3:
                    if("-list".equals(param.getArgs()[1])) {
                        List<WebElement> Options = dropdown.getOptions();
                        List<Object> actuals = new ArrayList<>();
                        for (WebElement option : Options) {
                            actuals.add(option.getText());
                        }
                        assertElement(actuals, new ArrayList<Object>(), param.getArgs());
                        //actual = element.getAttribute(param.getArgs()[3]);
                        //expected = param.getArgs()[1].trim().replace("''","").replaceAll("(\\$\\$|##|@@)", " ");
                        break;
                    }
                default:
                    assertElement(actual, expected, param.getArgs());
            }



            /*assertThat(dropdown.getFirstSelectedOption().getText()).as(MessageFormat.format(
                    "Assertion Failed... Given Actual value {0} should be equal to expected value {1}",
                    new Object[] {dropdown.getFirstSelectedOption().getText(),
                            "''".equalsIgnoreCase(param.getArgs()[1].trim()) ? "No options are selected": param.getArgs()[1].trim()})
            ).isEqualTo(param.getArgs()[1].replace("''","").replace("$$", " ").trim());

        } catch (TimeoutException tex) {
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ tex.getMessage(), tex);
        } catch (ElementNotInteractableException eix) {
            throw new CommandException("For command " + param.getCommand() + ", element is not reachable while clicking to property key "
                    + param.getArgs()[0] + "'. "+ eix.getMessage(), eix);
        } catch (JavascriptException | PropertyKeyNotFoundException | LocatorNotFoundException | NoSuchElementException | NoSuchWindowException ex){
            if(ex instanceof NoSuchElementException && "''".equalsIgnoreCase(param.getArgs()[1].trim())) {
                //No options are selected
                assertThat(((NoSuchElementException) ex).getRawMessage()).as(MessageFormat.format(
                        "Assertion Failed... Given Actual value {0} should be equal to expected value {1}",
                        new Object[] { ((NoSuchElementException) ex).getMessage(), "No options are selected"})
                ).isEqualTo("No options are selected");
            } else {
                throw new CommandException("Failed to execute command'"
                        + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);
            }

        } catch (Exception ex) {
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);
        }*/

           /* assertElement(dropdown.getFirstSelectedOption().getText(),
                    param.getArgs()[1].trim().replace("''", "").replaceAll("(\\$\\$|##|@@)", " "), param.getArgs());*/

        } catch(NoSuchElementException ex){
            if("''".equalsIgnoreCase(param.getArgs()[1].trim())) {
                try {
                    assertElement(ex.getRawMessage(), "No options are selected");
                } catch (VerificationException e) {
                        throw new AssertionError("<br>Verification failed for command'"
                                + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);
                }
            } else {
                throw new AssertionError("Verification failed for command'"
                        + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);
            }
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowAssertion(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


