package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.exception.PropertyKeyNotFoundException;
import com.testimium.tool.factory.VerificationTypeFactory;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.reader.PropertyReader;
import org.openqa.selenium.*;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class VerifyHtmlTableCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static VerifyHtmlTableCmd verifyHtmlTableCmd = new VerifyHtmlTableCmd();

    public VerifyHtmlTableCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static VerifyHtmlTableCmd getInstance() {
        //TODO Fix Me for concurrent access
        return verifyHtmlTableCmd;
    }

    //TODO Have to decide either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 2)
                throw new CommandException("Argument is missing for command " + param.getCommand());

            if(null == PropertyReader.getProperty(param.getArgs()[0])) {
                throw new PropertyKeyNotFoundException(param.getArgs()[0]);
            }

            if(null == PropertyReader.getProperty(param.getArgs()[1])) {
                throw new PropertyKeyNotFoundException(param.getArgs()[1]);
            }
            //wait(param);
            String[] args = PropertyReader.getProperty(param.getArgs()[0]).split(",");
            VerificationTypeFactory.getVerificationInstance(args[3]).verify(param);

        /*} catch (VerificationException | PropertyKeyNotFoundException ex) {
            throw new CommandException("Verification Failed for '"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);
        } catch (TimeoutException tex) {
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ tex.getMessage(), tex);
        } catch (ElementNotInteractableException eix) {
            throw new CommandException("For command " + param.getCommand() + ", element is not reachable while clicking to property key "
                    + param.getArgs()[0] + "'. "+ eix.getMessage(), eix);
        } catch (JavascriptException | NoSuchElementException | NoSuchWindowException ex){
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);*/
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowAssertion(ex.getMessage(), param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}
