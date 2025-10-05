package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.logging.LogUtil;
import org.openqa.selenium.*;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class HandleFailOverCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static HandleFailOverCmd handleFailOverCmd = new HandleFailOverCmd();

    public HandleFailOverCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static HandleFailOverCmd getInstance() {
        //TODO Fix Me for concurrent access
        return handleFailOverCmd;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        LogUtil.logTestCaseErrorMsg("HandleFailOverCmd Starts", null);
        try {
            if(null == param.getArgs() || param.getArgs().length != 1)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey).  ");
            param.setNestedNodeEnabled(true);
            handleFailOverCmd.handleFailOver(param);

        /*} catch (TimeoutException tex) {
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ tex.getMessage(), tex);
        } catch (ElementNotInteractableException eix) {
            throw new CommandException("For command " + param.getCommand() + ", element is not reachable while clicking to property key "
                    + param.getArgs()[0] + "'. "+ eix.getMessage(), eix);
        } catch (JavascriptException | NoSuchElementException | NoSuchWindowException ex){
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);*/
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        LogUtil.logTestCaseErrorMsg("HandleFailOverCmd Ends", null);
        return new CommandResponse("(HandleFailOverSteps Success)", true);
    }
}


