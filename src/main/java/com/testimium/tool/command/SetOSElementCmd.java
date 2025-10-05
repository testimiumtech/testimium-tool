package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.utility.FileUtility;
import com.testimium.tool.utility.PropertyUtility;
import org.openqa.selenium.WebDriver;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

/**
 * @author Sandeep Agrawal
 * This command only for windows
 */
public class SetOSElementCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private static WebDriver driver;
    private static SetOSElementCmd setOSElementCmd = new SetOSElementCmd();
    /*private String[] winDropDownImages = {"images/saveaspdf/dropdown/MSDropDown.png","images/LinuxSaveDropDown.png","images/SaveDropDown.png",
            "images/OneNoteWindows.png", "Fax.png","images/MSXpsDocument.png"};*/
    //private String[] winAddressBarImages = {"images/EmptyAddressBar.png","images/EmptyAddressBar_1.png"};

    public SetOSElementCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static SetOSElementCmd getInstance() {
        //TODO Fix Me for concurrent access
        return setOSElementCmd;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException {
        String filePath = null;
        try {
            if(null == param.getArgs() || param.getArgs().length < 2) {
                throw new CommandException("Command argument is missing: image path and text value");
            }

            Screen screen = new Screen();
            Pattern componentImage = new Pattern(FileUtility.getAbsolutePath(PropertyUtility.getExternalDirPath().trim() + "/" + param.getArgs()[0]));
            filePath = FileUtility.getAbsolutePath(PropertyUtility.getExternalDirPath().trim() + "/" + param.getArgs()[0]);
            screen.wait(componentImage, 5);
            screen.type(componentImage, param.getArgs()[1].trim().replace("''","").replaceAll("(\\$\\$|##|@@)", " "));
            new LogMessageCmd().execute(
                    new CommandParam(param.getTestCaseName(), param.getCommand(), new String[]{"INFO", param.getArgs()[1]}, null, null));

        } catch (FindFailed ff) {
            throw new CommandException("Failed to find a image file - "
                    + param.getCommand() + " " + filePath + ". "+ ff.getMessage(), ff);
        } catch (Exception ex) {
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);
        }

        return new CommandResponse("(Success)", true);
    }
}


