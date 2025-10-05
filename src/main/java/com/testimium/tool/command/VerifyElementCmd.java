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
 * @author Sandeep Agrawal
 */
public class VerifyElementCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static VerifyElementCmd verifyCommand = new VerifyElementCmd();

    public VerifyElementCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static VerifyElementCmd getInstance() {
        //TODO Fix Me for concurrent access
        return verifyCommand;
    }

    //Supported Syntax
    // VerifyElement <locator-property> <Expected Value>
    // VerifyElement <locator-property> -globalVariable <Expected Variable Name>
    // VerifyElement <locator-property> -matchAny
    // VerifyElement <locator-property> -exactMatch
    // VerifyElement <locator-property> -matchAny <-assertKey> <Assert key Name>
    // VerifyElement <locator-property> -exactMatch <-assertKey> <Assert key Name>
    // VerifyElement <locator-property> <Expected Value> <Actual Html Attribute>
    // VerifyElement <locator-property> -globalVariable <Expected Variable Name> <Actual Html Attribute>
    // VerifyElement <locator-property> -matchAny <Actual Html Attribute>
    // VerifyElement <locator-property> -exactMatch <Actual Html Attribute>
    // VerifyElement <locator-property> -matchAny <-assertKey> <Assert key Name> <Actual Html Attribute>
    // VerifyElement <locator-property> -exactMatch <-assertKey> <Assert key Name> <Actual Html Attribute>


    //Assert Param Json Configuration
   /* {
        "assertParams": {
        "Verify-Dropdown-values": {
            "exactMatch" : ["A",5,"C", 10.00],
            "matchAny": ["A",5,"C", 10.00]
        }
    }
    }*/
    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || (param.getArgs().length < 2))
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey, Value)  ");

            WebElement element = wait(param);
            //WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));

            /*assertThat((param.getArgs().length == 2) ? element.getText() : element.getAttribute(param.getArgs()[2]).trim()).as(MessageFormat.format(
                    "Assertion Failed... Given Element should be "
                            + ((param.getArgs()[1].trim().equals("''")) ? "empty" : element.getText())
                            +"displayed but it is not matched: {0}",
                    new Object[] {param.getArgs()[0]})
            ).isEqualTo(param.getArgs()[1].trim().replace("''","").replaceAll("(\\$\\$|##|@@)", " "));*/

            String actual = element.getText();
            String expected = param.getArgs()[1];

            switch(param.getArgs().length){
                case 2:
                    actual = element.getText();
                    expected = param.getArgs()[1].trim().replace("''","").replaceAll("(\\$\\$|##|@@)", " ");
                    break;
                case 3:
                    //Both situation handled
                    // VerifyElement <locator-property> <Expected Value> <Actual Html Attribute>
                    // VerifyElement <locator-property> -globalVariable <Expected Variable Name>
                    if("-globalVariable".equals(param.getArgs()[1])) {
                        actual = element.getText();
                    } else {
                        actual = element.getAttribute(param.getArgs()[2]).trim();
                    }
                    expected = param.getArgs()[1].trim().replace("''","").replaceAll("(\\$\\$|##|@@)", " ");
                    break;
                case 4:
                    actual = element.getAttribute(param.getArgs()[3]);
                    expected = param.getArgs()[1].trim().replace("''","").replaceAll("(\\$\\$|##|@@)", " ");
                    break;
                case 5:
                    actual = element.getAttribute(param.getArgs()[4]);
                    expected = param.getArgs()[1].trim().replace("''","").replaceAll("(\\$\\$|##|@@)", " ");
                    break;
                default:

            }

            assertElement(actual, expected, param.getArgs());
          /*assertElement((param.getArgs().length == 2) ? element.getText() : element.getAttribute(param.getArgs()[2]).trim(),
                  param.getArgs()[1].trim().replace("''","").replaceAll("(\\$\\$|##|@@)", " "));*/


           /* if(!param.getArgs()[1].trim().replace("''","").equals(element.getText())) {
                System.out.println("Given Element (param.getArgs()[0]) value (" + element.getText() + ", " + param.getArgs()[1].trim() + ") does not match.");
                throw new VerificationFailedException("Given Element (param.getArgs()[0]) value (" + element.getText() + ", " + param.getArgs()[1].trim() + ") does not match.");
            }*/

        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowAssertion(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


