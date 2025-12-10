package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class ClearCheckboxCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static ClearCheckboxCmd clearCheckboxCmd = new ClearCheckboxCmd();

    public ClearCheckboxCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static ClearCheckboxCmd getInstance() {
        //TODO Fix Me for concurrent access
        return clearCheckboxCmd;
    }

    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length != 1)
                throw new CommandException("Command argument is missing: provide Command argument (locator/propertyKey).  ");

            WebElement element = wait(param);
            if (element.isSelected()) {
                element.click();
            }
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


