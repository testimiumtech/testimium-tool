package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.net.http.HttpTimeoutException;
import java.util.List;

/**
 * @author Sandeep Agrawal
 */
public class SelectHtmlTableRowsCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static SelectHtmlTableRowsCmd selectHtmlTableRowsCmd = new SelectHtmlTableRowsCmd();

    public SelectHtmlTableRowsCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static SelectHtmlTableRowsCmd getInstance() {
        //TODO Fix Me for concurrent access
        return selectHtmlTableRowsCmd;
    }

    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if (null == param.getArgs() || param.getArgs().length < 2)
                throw new CommandException("Argument is missing for command " + param.getCommand());
            //List<WebElement> tableRows = this.getWebElements(param.getArgs()[0]);
            Actions builder = new Actions(this.driver);
            builder.keyDown(Keys.CONTROL).build().perform();

            if(param.getArgs()[1].contains("-"))
                processSequenceRows(param.getArgs()[1], this.getWebElements(param.getArgs()[0]));
            else
                processRandomRows(param.getArgs()[1], this.getWebElements(param.getArgs()[0]));

            builder.build().perform();
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }

    private void processRandomRows(String rows, List<WebElement> tableRows) {
        String[] rowNums = rows.split("\\|");
        for(int ikr = 0; ikr < rowNums.length; ikr++){
            tableRows.get(Integer.parseInt(rowNums[ikr]) - 1).click();
        }
    }

    private void processSequenceRows(String rows, List<WebElement> tableRows) {
        String[] rowNums = rows.split("-");
        for(int ikr = Integer.parseInt(rowNums[0]); ikr <= Integer.parseInt(rowNums[1]); ikr++){
            tableRows.get(Integer.parseInt(rowNums[ikr]) - 1).click();
        }
    }
}
