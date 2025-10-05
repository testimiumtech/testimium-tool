package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class PressKeyCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static PressKeyCmd presskeyCmd = new PressKeyCmd();

    public PressKeyCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }


    public static PressKeyCmd getInstance() {
        //TODO Fix Me for concurrent access and change it to lazy
        return presskeyCmd;
    }

    //TODO Have to decide either ConmmandResponse/WebElement/Dynamic as response object

    /**
     * It supports Below Keys as second arguments:
     * Keys.ARROW_DOWN
     * Keys.ARROW_UP
     * Keys.ARROW_LEFT
     * Keys.ARROW_RIGHT
     * Keys.BACK_SPACE
     * Keys.CONTROL
     * Keys.ALT
     * Keys.DELETE
     * Keys.ENTER
     * Keys.SHIFT
     * Keys.SPACE
     * Keys.TAB
     * Keys.EQUALS
     * Keys.ESCAPE
     * Keys.HOME
     * Keys.INSERT
     * Keys.PAGE_UP
     * Keys.PAGE_DOWN
     * Keys.F1
     * Keys.F2
     * Keys.F3
     * Keys.F4
     * Keys.F5
     * Keys.F6
     * Keys.F7
     * Keys.F8
     * Keys.F9
     * Keys.F10
     * Keys.F11
     * Keys.F12
     * @param param
     * @return
     * @throws CommandException
     */
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 1)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey, Value). ");

            WebElement element = null;
            if(param.getArgs().length == 1) {
                element = driver.findElement(By.cssSelector("body"));
                //body.sendKeys(Keys.Control + 't');
            } else {
                element = wait(param);
                //element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
            }
            //Value must be in uppercase
            String keys[] = param.getArgs()[(param.getArgs().length == 1) ? 0 : 1].trim().toUpperCase().split("\\+");
            //CharSequence[] keyObj = new CharSequence[keys.length];
                /*CharSequence[] finalObj = null;
                for(int itr = 0; itr < keys.length; itr++) {
                    String[] plusArr = keys[itr].split("\\+");
                    keyObj[itr] = Keys.valueOf(plusArr[0]);
                    if (itr == keys.length - 1 && plusArr.length > 1) {
                       finalObj = Arrays.copyOf(keyObj, keyObj.length + plusArr.length-1);
                        for(int itk = 1; itk < plusArr.length; itk++)
                            finalObj[itr + itk] = plusArr[itk];
                    } else if (itr == keys.length - 1) {
                        finalObj = keyObj;
                    }
                }*/
            /*for(int itr = 0; itr < keys.length; itr++) {
                try {
                    keyObj[itr] = Keys.valueOf(keys[itr]);
                } catch (IllegalArgumentException iex) {
                    keyObj[itr] = keys[itr];
                }
            }*/
            System.out.println("Press Key = " + keys[0]);
            sendkeys(element, keys[0], (keys.length > 1)? keys[1] : "");
            //element.sendKeys(Keys.chord(keyObj));
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }


    private void sendkeys(WebElement element, String key, String arg) throws CommandException {
        //element.sendKeys(Keys.chord(keyObj));
        Keys keys = null;
        switch (key.toUpperCase()){
            case "ENTER":
                element.sendKeys(Keys.ENTER);
                break;
            case "RETURN":
                element.sendKeys(Keys.RETURN);
                break;
            case "PAGE_UP":
                element.sendKeys(Keys.PAGE_UP);
                break;
            case "PAGE_DOWN":
                element.sendKeys(Keys.PAGE_DOWN);
                break;
            case "BACK_SPACE":
                element.sendKeys(Keys.BACK_SPACE);
                break;
            case "TAB":
                element.sendKeys(Keys.TAB);
                break;
            case "ARROW_DOWN":
                element.sendKeys(Keys.ARROW_DOWN);
                break;
            case "CONTROL":
                Actions actions = new Actions(driver);
               // element.sendKeys(Keys.chord(Keys.CONTROL, arg));
                actions.keyDown(element, Keys.CONTROL).sendKeys(arg.toLowerCase()).keyUp(element, Keys.CONTROL).build().perform();
                break;
            default:
                throw new CommandException(" key " + key + " Not supported");

        }
    }
}
