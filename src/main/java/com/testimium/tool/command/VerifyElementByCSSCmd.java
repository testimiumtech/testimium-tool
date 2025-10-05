package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.http.HttpTimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sandeep Agrawal
 */
public class VerifyElementByCSSCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static VerifyElementByCSSCmd verifyElementByCssCmd = new VerifyElementByCSSCmd();

    public VerifyElementByCSSCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static VerifyElementByCSSCmd getInstance() {
        //TODO Fix Me for concurrent access
        return verifyElementByCssCmd;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 3)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey/locator, CSS value, CSS Attribute).  ");

            //Command Syntex
            // Copy locator-property, name, Html Attribute
            WebElement element = wait(param);
            //WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
            /*assertThat(element.getCssValue(param.getArgs()[2]))
                    .isEqualTo(param.getArgs()[1].trim().replace("''", "").replaceAll("(\\$\\$|##|@@)", " "))
                    .as("Assertion Failed...Expected is "
                            + param.getArgs()[1].trim().replace("''", "").replaceAll("(\\$\\$|##|@@)", " ")
                            + "but actual is " + element.getCssValue(param.getArgs()[2])
                            + "for given CSS " + param.getArgs()[2]);*/
            assertElement(element.getCssValue(param.getArgs()[2]),
                    param.getArgs()[1].trim().replace("''", "").replaceAll("(\\$\\$|##|@@)", " "));

        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowAssertion(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


