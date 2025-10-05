package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import org.openqa.selenium.*;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
@Deprecated
public class ClickIconCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    //private static ClickIconCmd clickIconCmd = new ClickIconCmd();

    public ClickIconCmd() {
        //this.driver = DriverManager.getInstance().getWebDriver();
    }

    /*public static ClickIconCmd getInstance() {
        //TODO Fix Me for concurrent access
        return clickIconCmd;
    }*/

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            this.driver= DriverManager.getInstance().getWebDriver();
            //this.driver.get("chrome://settings/clearBrowserData");
            Thread.sleep(2000);
            JavascriptExecutor js = (JavascriptExecutor) this.driver;
            ///WebElement clearData = (WebElement)
                    js.executeScript("document.querySelector('pdf-viewer').shadowRoot.querySelector('viewer-toolbar')" +
                    ".shadowRoot.querySelector('div#end').getElementsByTagName('cr-icon-button')[0].click();");
            ///clearData.click();

            /*WebElement element = driver.findElement(By.cssSelector(".#icon > iron-icon"));
            Actions actions = new Actions(driver);
            actions.moveToElement(element).click().build().perform();
            Thread.sleep(3000);*/
            Thread.sleep(5000);

        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


