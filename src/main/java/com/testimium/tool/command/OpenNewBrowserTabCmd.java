package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;

/**
 * @author Sandeep Agrawal
 */
public class OpenNewBrowserTabCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static OpenNewBrowserTabCmd browserTabCmd = new OpenNewBrowserTabCmd();

    public OpenNewBrowserTabCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static OpenNewBrowserTabCmd getInstance() {
        //TODO Fix Me for concurrent access
        return browserTabCmd;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException {
        //browserTabCmd.executeJavaScript("window.open()");
        this.driver.switchTo().newWindow(WindowType.TAB);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //new SwitchTabCmd().execute(null);
        return new CommandResponse("(Success)", true);
    }
}


