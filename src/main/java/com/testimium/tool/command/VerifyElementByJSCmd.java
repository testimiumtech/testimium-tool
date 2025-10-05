package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.domain.TestJsonInputData;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.locator.LocatorFactory;
import org.openqa.selenium.*;

import java.net.http.HttpTimeoutException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sandeep Agrawal
 */
public class VerifyElementByJSCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private WebElement element = null;
    private static VerifyElementByJSCmd verifyElementByJSCmd = new VerifyElementByJSCmd();

    public VerifyElementByJSCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static VerifyElementByJSCmd getInstance() {
        //TODO Fix Me for concurrent access
        return verifyElementByJSCmd;
    }
    //VerifyElementByJs <locator-property> <Script Key> <Actual Html Attribute>
    //Below is for internal use call from If Command
    //VerifyElementByJs <locator-property> <Script Key> <Actual Html Attribute> <-fromIf>

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        AtomicBoolean isSuccess = new AtomicBoolean(true);
        element = null;
        try {
            if(null == param.getArgs() || param.getArgs().length < 1)
                throw new CommandException("Command argument is missing: provide Command argument (Json Key).  ");

            TestJsonInputData inputData = getTestJsonInputData(param);
            if(null == inputData || null == inputData.getScripts() || inputData.getScripts().size() <= 0)
                throw new CommandException("System not able to find the Javascript as input ");

            if(param.getArgs().length > 1) {
                element = wait(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
                //element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
            }

            inputData.getScripts().entrySet().stream()
                    .filter(key -> key.getKey().equalsIgnoreCase((param.getArgs().length == 1) ? param.getArgs()[0] : param.getArgs()[1]))
                    .forEach(map -> {
                        System.out.println("value: " + map.getValue());
                        map.getValue().stream().forEach(innerM -> {

                            List<Object> eles = new ArrayList<>();
                            if(null != innerM.get("args")) {
                                Arrays.asList(innerM.get("args")).forEach(prop -> {
                                    eles.add(prop.toString());
                                });
                            }
                            //TODO change the implementation for scripts from Input Param to Assert param as this command is used to verify the assert params
                            if(param.getArgs().length == 1) {
                                assertThat(verifyElementByJSCmd.executeJavaScript(innerM.get("script").toString())).isEqualTo(true);
                            } else if(param.getArgs().length == 4 && "-fromIf".equals(param.getArgs()[3])) {
                                isSuccess.set(element.getAttribute(param.getArgs()[2]).equals(verifyElementByJSCmd.executeJavaScript(innerM.get("script").toString(), eles)));
                            } else {
                                assertThat(element.getAttribute(param.getArgs()[2])).as(MessageFormat.format(
                                        "Assertion Failed... Given Actual value {0} should be equal to expected value {1}",
                                        new Object[]{verifyElementByJSCmd.executeJavaScript(innerM.get("script").toString()), element.getAttribute(param.getArgs()[2])})
                                ).isEqualTo(verifyElementByJSCmd.executeJavaScript(innerM.get("script").toString(), eles));
                            }
                           /* innerM.entrySet().stream().forEach(innerMap -> {
                                if (innerMap.getKey().equalsIgnoreCase("script")) {
                                    System.out.println("Script: " + innerMap.getValue());
                                    assertThat(element.getAttribute(param.getArgs()[2])).as(MessageFormat.format(
                                            "Assertion Failed... Given Actual value {0} should be equal to expected value {1}",
                                            new Object[] {verifyElementByJSCmd.executeJavaScript(innerMap.getValue(), element).toString()}));
                                }
                            });*/
                        });
                    });
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowAssertion(null, param, ex);
        }
        return new CommandResponse("(Success)", isSuccess.get());
    }
}


