package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.logging.LogUtil;
import org.openqa.selenium.WebDriver;

import java.net.http.HttpTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sandeep Agrawal
 */
public class CloseBrowserTabCmd implements ExternalCommand<CommandParam, CommandResponse> {

    private WebDriver driver;
    private static CloseBrowserTabCmd closeBrowserTabCmd = new CloseBrowserTabCmd();

    public CloseBrowserTabCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static CloseBrowserTabCmd getInstance() {
        //TODO Fix Me for concurrent access
        return closeBrowserTabCmd;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {

        try {
            if(null != param && null != param.getArgs() && param.getArgs().length == 1) {
                new SwitchTabCmd().execute(new CommandParam(null,null, param.getArgs(), null, null));
            }

            //closeWindowsPrintPopup();

            List<String> tabs = null;

            tabs = new ArrayList<String>(driver.getWindowHandles());

            LogUtil.logTestCaseMsg("Tab size: " + ((null != tabs) ? tabs.size() : 0));
            if(null != tabs && tabs.size() > 1) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DriverManager.getInstance().closeCurrentBrowser();
                DriverManager.getInstance().focusCurrentWindow();
            }
        } catch (Exception ex){
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }

        return new CommandResponse("(Success)", true);
    }
}


