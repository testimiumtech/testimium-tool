package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.locator.LocatorFactory;
import com.testimium.tool.utility.FileUtility;
import com.testimium.tool.utility.ImagePath;
import com.testimium.tool.utility.ImageUtil;
import com.testimium.tool.utility.PropertyUtility;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import java.io.IOException;

/**
 * Upload File command
 * @author Sandeep Agrawal
 */
public class UploadFileCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static UploadFileCmd uploadFileCmd = new UploadFileCmd();

    public UploadFileCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static UploadFileCmd getInstance() {
        //TODO Fix Me for concurrent access
        return uploadFileCmd;
    }

    /**
     * UploadFile file based on location passed in command param
     * Provide json input parameter -  "{\"scripts\": {\"uploadFile\":[{ \"script\":\"document.getElementById('importFileHidden').style.display='inline-block';\"} ]}}"
     * @param param input param
     * @return CommandResponse as response
     * @throws CommandException if command issue
     */
    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException {
        String filePath = null;
        try {
            if(null == param.getArgs() || param.getArgs().length < 2)
                throw new CommandException("Command argument is missing: provide both locator and file name to upload e.g. test.csv");


           /* if(PropertyUtility.isSikuliExecutionEnabled()) {
                filePath = FileUtility.getAbsolutePath(PropertyUtility.getExternalDirPath().trim() + "/" + param.getArgs()[0]);
                executeWithSikuli(param, filePath);
                return  new CommandResponse("(Success)", true);
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_ESCAPE);
                robot.keyRelease(KeyEvent.VK_ESCAPE);
            } catch (AWTException e) {
                LogUtil.logTestCaseErrorMsg("UploadFileCmd - Try to verify and Escape any OS popup windows - ", e);
            }*/

            //wait(param);
            //WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
            //TODO remove hardcorded property and Change to above commented line
            //WebElement element = wait(LocatorFactory.getByLocatorProperty("Common.ImportFileHidden.Display.txt"));
            //WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty("Common.ImportFileHidden.Display.txt"));
            //WebElement element = findElement(LocatorFactory.getByLocatorProperty("Common.ImportFileHidden.Display.txt"));
            WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
            filePath = FileUtility.getAbsolutePath(PropertyUtility.getExternalDirPath().trim() + "/" + param.getArgs()[1]);

            String isReadonly = element.getAttribute("readonly");
            System.out.println("Readonly is now set=============" + isReadonly);
            if(null != isReadonly) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].removeAttribute('readonly')", element);
                new WaitCmd().execute(new CommandParam(null,null, new String[]{"-second","2000"}, null, null));
                element.sendKeys(filePath);
                js.executeScript("arguments[0].setAttribute('readonly', '')", element);
            } else if(!element.isDisplayed()){
                String inputParam = param.getInputParam();
                new ExecuteJSCmd().execute(
                        new CommandParam(param.getTestCaseName(),
                                "ExecuteJS",
                                new String[]{"uploadFile"},
                                inputParam,
                                null));
                new WaitCmd().execute(new CommandParam(null,null, new String[]{"-second","2000"}, null, null));
                //element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
                element.sendKeys(filePath);
            }
            System.out.println("Readonly is now de-set=============" + isReadonly);
            // Provide the absolute path of the file


        } catch (Exception ex) {
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);
        }
        return new CommandResponse("(Success)", true);
    }

    /**
     * Work with only Sikuli for windows components
     * @param param input param
     * @param filePath input param
     * @throws IOException if io exception
     * @throws FindFailed if image not found
     */
    private static void executeWithSikuli(CommandParam param, String filePath) throws IOException, FindFailed {

        Screen screen = new Screen();

        //Pattern openFileTextBoxImg = new Pattern(FileUtility.getAbsolutePath(ImagePath.getToolImagePath() + "TextBox.png"));
        Pattern openFileTextBoxImg = ImageUtil.matchImagePattern(screen, ImagePath.getToolImagePath() + "opendailogbox/TextBox.png");
        screen.type(openFileTextBoxImg, filePath.replaceAll("/", "\\\\"));
        Pattern openButtonImg = ImageUtil.matchImagePattern(screen, ImagePath.getToolImagePath() + "opendailogbox/OpenButton.png");
        screen.click(openButtonImg);
            /*Pattern openFileTextBoxImg = new Pattern(FileUtility.getAbsolutePath("images/OpenFileTextBox.png"));

            Pattern openButtonImg = new Pattern(FileUtility.getAbsolutePath("images/OpenButton.png"));
            filePath = FileUtility.getAbsolutePath(PropertyUtility.getExternalDirPath().trim() + "/" + param.getArgs()[0]);
            screen.wait(openFileTextBoxImg, 5000);
            screen.type(openFileTextBoxImg, filePath.replaceAll("/","\\\\"));
            screen.click(openButtonImg);*/
    }
}


