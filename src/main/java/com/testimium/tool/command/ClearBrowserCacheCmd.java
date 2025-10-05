package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.utility.PropertyUtility;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class ClearBrowserCacheCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static ClearBrowserCacheCmd clearBrowserCacheCmd = new ClearBrowserCacheCmd();

    public ClearBrowserCacheCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static ClearBrowserCacheCmd getInstance() {
        //TODO Fix Me for concurrent access
        return clearBrowserCacheCmd;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            JavascriptExecutor js = (JavascriptExecutor) this.driver;
            switch(PropertyUtility.getWebBrowser().toUpperCase()){
                case "CHROME":
                    this.driver.get("chrome://settings/clearBrowserData");
                    Thread.sleep(2000);
                    try {
                        //this.driver.findElement(By.cssSelector(".settings-ui .settings-main .settings-basic-page .settings-section > settings-privacy-page .settings-clear-browsing-data-dialog.clearBrowsingDataDialog .settings-dropdown-menu.select")).click();
                        WebElement element = (WebElement) js.executeScript("return document.querySelector('settings-ui').shadowRoot.querySelector('settings-main').shadowRoot.querySelector('settings-basic-page').shadowRoot.querySelector('settings-section > settings-privacy-page').shadowRoot.querySelector('settings-clear-browsing-data-dialog').shadowRoot.querySelector('#clearBrowsingDataDialog').querySelector('settings-dropdown-menu').shadowRoot.querySelector('select')");
                        element.click();
                        Select dropdown = new Select(element);
                        dropdown.selectByVisibleText("All time");
                        //Thread.sleep(1000);
                    } catch (Exception ex){}
                    WebElement clearData = (WebElement)js.executeScript("return document.querySelector('settings-ui').shadowRoot.querySelector('settings-main')" +
                            ".shadowRoot.querySelector('settings-basic-page').shadowRoot.querySelector('settings-section > settings-privacy-page')" +
                            ".shadowRoot.querySelector('settings-clear-browsing-data-dialog').shadowRoot.querySelector('#clearBrowsingDataDialog')" +
                            ".querySelector('#clearButton')");
                    clearData.click();
                    break;
                case "EDGE":
                    this.driver.get("edge://settings/clearBrowserData/clear");
                    WebElement dropDownEle = wait(param.getArgs()[1]);
                    WebElement clearBtnEle = wait(param.getArgs()[3]);
                    break;
                default:
                    this.driver.get("chrome://settings/clearBrowserData");
            }
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException("Not able to clear the browser cache", param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


