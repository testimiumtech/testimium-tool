package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;

import com.testimium.tool.exception.*;
import org.openqa.selenium.WebDriver;

import java.net.http.HttpTimeoutException;

public class RefreshBrowserCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;

    private static RefreshBrowserCmd refreshBrowser = new RefreshBrowserCmd();

    public RefreshBrowserCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static RefreshBrowserCmd getInstance() {
        //TODO Fix Me for concurrent access
        return refreshBrowser;
    }

    /**
     * This command will refresh the current browser page. There are other 2 ways to refresh the browser page:
     * 1. Pass the below javascript to ExecuteJS command
     *      "location.reload()"
     *      example in java - ((JavascriptExecutor) driver).executeScript("location.reload()");
     * 2. Press function key F5 by using command PressKey
     *
     * @param commandParam
     * @return
     * @throws CommandException
     * @throws VerificationException
     * @throws TestException
     * @throws ShutdownTestExecution
     * @throws HandleFailOverTestExecution
     * @throws HttpTimeoutException
     * @throws RecoverBrokenTestExecutionException
     */
    @Override
    public CommandResponse execute(CommandParam commandParam) throws CommandException, VerificationException, TestException, ShutdownTestExecution, HandleFailOverTestExecution, HttpTimeoutException, RecoverBrokenTestExecutionException {
        this.driver.navigate().refresh();
        return new CommandResponse("(Success)", true);
    }
}
