package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.utility.FileUtility;
import com.testimium.tool.utility.PropertyUtility;
import org.openqa.selenium.WebDriver;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 * This command may useful for windows as well as other OS
 */
public class ClickOSElementCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private static WebDriver driver;
    private static ClickOSElementCmd clickOSElementCmd = new ClickOSElementCmd();
    /*private String[] winDropDownImages = {"images/saveaspdf/dropdown/MSDropDown.png","images/LinuxSaveDropDown.png","images/SaveDropDown.png",
            "images/OneNoteWindows.png", "Fax.png","images/MSXpsDocument.png"};*/
    //private String[] winAddressBarImages = {"images/EmptyAddressBar.png","images/EmptyAddressBar_1.png"};

    public ClickOSElementCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static ClickOSElementCmd getInstance() {
        //TODO Fix Me for concurrent access
        return clickOSElementCmd;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        String filePath = null;
        try {
            if(null == param.getArgs() || param.getArgs().length < 2) {
                throw new CommandException("Command argument is missing: image path and description");
            }

            Screen screen = new Screen();
            filePath = PropertyUtility.getExternalDirPath().trim();
            if (param.getArgs()[1].contains("${")) {
                filePath = FileUtility.getAbsolutePath(filePath + "/" + FileUtility.getPathByReplaceWithProperty(param.getArgs()[0]));
            } else {
                filePath = FileUtility.getAbsolutePath(PropertyUtility.getExternalDirPath().trim() + "/" + param.getArgs()[0]);
            }
            Pattern componentImage = new Pattern(filePath);
            screen.wait(componentImage, 2000);
            screen.click(componentImage);
            new LogMessageCmd().execute(
                    new CommandParam(param.getTestCaseName(), param.getCommand(), new String[]{"INFO", param.getArgs()[1]}, null, null));

        } catch (FindFailed ff) {
            throw new CommandException("Failed to find a image file - "
                    + param.getCommand() + " " + filePath + ". "+ ff.getMessage(), ff);
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }

        return new CommandResponse("(Success)", true);
    }
}


