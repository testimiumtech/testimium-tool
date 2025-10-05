package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.logging.LogUtil;
import org.openqa.selenium.WebDriver;

/**
 * @author Sandeep Agrawal
 */
public class WaitCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static WaitCmd waitCommand = new WaitCmd();

    public WaitCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static WaitCmd getInstance() {
        //TODO Fix Me for concurrent excess
        return waitCommand;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException {
        try {


            if(null != param.getArgs() && param.getArgs().length == 2) {
                long second = Long.parseLong(param.getArgs()[1])/1000;
                switch(param.getArgs()[0].trim().toUpperCase()){
                    case "-ALERT":
                        waitForAlert(driver, second);
                        break;
                    case "-PAGELOAD":
                        waitForPageToLoad(driver, second);
                        break;
                    default:
                        Thread.sleep(Integer.valueOf(param.getArgs()[1]));
                }


            } else {
                Thread.sleep(Integer.valueOf(param.getArgs()[0]));
            }
        } catch (InterruptedException ex) {
            LogUtil.logTestCaseErrorMsg("WaitCmd.execute() ", ex);
            throw new CommandException(param.getCommand() + " " + ex.getMessage(), ex);
        }
        return new CommandResponse("(Success)", true);
    }
}
