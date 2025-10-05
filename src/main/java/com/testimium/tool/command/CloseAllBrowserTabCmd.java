package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import org.openqa.selenium.WebDriver;

import java.net.http.HttpTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sandeep Agrawal
 */
public class CloseAllBrowserTabCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static CloseAllBrowserTabCmd closeBrowserTabCmd = new CloseAllBrowserTabCmd();

    public CloseAllBrowserTabCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static CloseAllBrowserTabCmd getInstance() {
        //TODO Fix Me for concurrent access
        return closeBrowserTabCmd;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {

        //closeWindowsPrintPopup();
        try {
            List<String> tabs = new ArrayList<String>(driver.getWindowHandles());
            for (int itr = tabs.size(); itr >= 2; itr--) {
                DriverManager.getInstance().switchTab(itr - 1);
                //DriverManager.getInstance().focusCurrentWindow();
                DriverManager.getInstance().closeCurrentBrowser();
            }
            DriverManager.getInstance().focusCurrentWindow();
            /*tabs = new ArrayList<String>(driver.getWindowHandles());
            LogUtil.logTestCaseMsg("CloseAllBrowserTabCmd - Do not close browser tab index: " + ((null != tabs) ? tabs.size() : 0));
            if (null != tabs && tabs.size() > 0) {
                LogUtil.logTestCaseMsg("CloseAllBrowserTabCmd - Do not close browser tab index: " + 1);
                new SwitchTabCmd().execute(new CommandParam(null, null, null, null, null));
            }*/
        } catch (Exception ex){
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }

        return new CommandResponse("(Success)", true);
    }
}


