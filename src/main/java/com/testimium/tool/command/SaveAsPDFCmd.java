package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.utility.FileUtility;
import com.testimium.tool.utility.ImageUtil;
import com.testimium.tool.utility.OSValidator;
import com.testimium.tool.utility.PropertyUtility;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.print.PageMargin;
import org.openqa.selenium.print.PrintOptions;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.http.HttpTimeoutException;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sandeep Agrawal
 * This command only for windows
 */
public class SaveAsPDFCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private static WebDriver driver;
    private static SaveAsPDFCmd saveAsPDFCmd = new SaveAsPDFCmd();
    /*private String[] winDropDownImages = {"images/saveaspdf/dropdown/MSDropDown.png","images/LinuxSaveDropDown.png","images/SaveDropDown.png",
            "images/OneNoteWindows.png", "Fax.png","images/MSXpsDocument.png"};*/
    //private String[] winAddressBarImages = {"images/EmptyAddressBar.png","images/EmptyAddressBar_1.png"};

    public SaveAsPDFCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static SaveAsPDFCmd getInstance() {
        //TODO Fix Me for concurrent access
        return saveAsPDFCmd;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {

        String filename = null;
        Robot robotExe = null;
        try {
            robotExe = new Robot();
            if(null == param.getArgs() || param.getArgs().length < 1) {
                throw new CommandException("Command argument is missing: provide file name to save as pdf");
            }

            filename = (param.getArgs().length == 2) ? param.getArgs()[1] : param.getArgs()[0];
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(PropertyUtility.isSikuliExecutionEnabled()) {
                if(OSValidator.isWindows()) {
                    winSaveAsPDF(param);
                } else {
                    //linuxSaveAsPDF(param);
                    assertThat(false).as("Command is not yet supported for OS Linux/Unix/Mac/Solaries").isEqualTo(true);
                }
            } else {
                printWithSelenium4(driver, PropertyUtility.getDefaultDownloadPath()
                        + "/" + filename + ((filename.contains(".pdf"))? "" : ".pdf").replaceAll("/","\\\\"));
            }
        } catch (FindFailed ff) {
            robotExe.keyPress(KeyEvent.VK_ESCAPE);
            robotExe.keyRelease(KeyEvent.VK_ESCAPE);
            throw new CommandException("Failed to find a file - "
                    + param.getCommand() + " " + param.getArgs()[0] + "'. "+ ff.getMessage(), ff);

        } catch (IOException ex) {
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);

        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException("Save as file name: " + filename, param, ex);
            /*throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + filename + "'. "+ ex.getMessage(), ex);*/
        }

        return new CommandResponse("(Success)", true);
    }

    private void linuxSaveAsPDF(CommandParam param) throws InterruptedException, IOException, AWTException, FindFailed, CommandException {
        String filename = (param.getArgs().length == 2) ? param.getArgs()[1] : param.getArgs()[0];
        FileUtility.deleteIfExists(PropertyUtility.getDefaultDownloadPath()
                + "/" + filename + ((filename.contains(".pdf"))? "" : ".pdf"));
        Robot robot = new Robot();
        if(!"-skipCtrlP".equalsIgnoreCase(param.getArgs()[0])){
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_P);
            robot.getAutoDelay();
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_P);
        }
        //Print screen Start
        Screen screen = new Screen();
        //Pattern pdfOptionImg = new Pattern(FileUtility.getAbsolutePath("images//PdfOption.png"));
        Pattern buttonImg = new Pattern(FileUtility.getAbsolutePath("images/PrintSaveButton.png"));
        // Pattern dropDownImg = null;
        //for (int itr = 0; itr < winDropDownImages.length; itr++) {
        // try {
        //    dropDownImg = new Pattern(FileUtility.getAbsolutePath(winDropDownImages[itr]));
        screen.wait(buttonImg);
        //screen.wait(buttonImg, 2);
        //if(itr !=1) {
        // screen.click(dropDownImg);
        //  screen.click(pdfOptionImg);
        // }
        screen.click(buttonImg);
        System.out.println("For Loop - Find image successfully to SaveAsPDF....");
        // break;
        //} catch (FindFailed ff) {
        //    ff.printStackTrace();
        // }
        // }
        //screen.resetScreens();
        new WaitCmd().execute(new CommandParam(null,null, new String[]{"-second","2000"}, null, null));
        //Print screen End
        //Save dialog window Start
        screen = new Screen();
        Pattern linuxAddressBar = new Pattern(FileUtility.getAbsolutePath("images/LinuxAddressBar.png"));
        Pattern linuxSaveButton = new Pattern(FileUtility.getAbsolutePath("images/LinuxSaveButton.png"));
        screen.wait(linuxAddressBar, 5);
        //Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_A);
        robot.getAutoDelay();
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyPress(KeyEvent.VK_BACK_SPACE);
        robot.keyRelease(KeyEvent.VK_BACK_SPACE);
        screen.type(linuxAddressBar, PropertyUtility.getDefaultDownloadPath() + "/" + param.getArgs()[0] + ((param.getArgs()[0].contains(".pdf"))? "" : ".pdf"));
        screen.wait(linuxSaveButton, 5);
        screen.click(linuxSaveButton);
        //Save dialog window End
    }

    private void winSaveAsPDF(CommandParam param) throws InterruptedException, IOException, AWTException, FindFailed, CommandException {

        String filename = (param.getArgs().length == 2) ? param.getArgs()[1] : param.getArgs()[0];
        FileUtility.deleteIfExists(PropertyUtility.getDefaultDownloadPath()
                + "/" + filename + ((filename.contains(".pdf"))? "" : ".pdf"));
        Robot robot = new Robot();
        if(!"-skipCtrlP".equalsIgnoreCase(param.getArgs()[0])){
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_P);
            robot.getAutoDelay();
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_P);
        }
        //Print screen Start
        Screen screen = new Screen();
        //Pattern dropDownImg = new Pattern(FileUtility.getAbsolutePath("images//DropDown.png"));
        //Pattern pdfOptionImg = new Pattern(FileUtility.getAbsolutePath("images//SaveAsPdf3.png"));
        //Pattern buttonImg = new Pattern(FileUtility.getAbsolutePath("images//PrintSaveButton.png"));
        Pattern printPattern = null;
        //for (int itr = 0; itr < winDropDownImages.length; itr++) {

        //Step-1   Click Drop down to select print option
        //String[] printDropDownImages = FileUtility.getAllFiles("images/saveaspdf/dropdown");
        printPattern = ImageUtil.matchImagePatterns(screen, "saveaspdf/dropdown");
        screen.click(printPattern);
        //Step-2  select option as SaveAsPDF
        //String[] printOptionsImages = FileUtility.getAllFiles("images/saveaspdf/optiontosavepdf");
        printPattern = ImageUtil.matchImagePatterns(screen, "saveaspdf/optiontosavepdf");
        screen.click(printPattern);
        //Step-3  click save PDF button in the print screen which opens the dialog box
        //String[] saveBtnImages = FileUtility.getAllFiles("images/saveaspdf/savebutton");
        printPattern = ImageUtil.matchImagePatterns(screen, "saveaspdf/savebutton");
        screen.click(printPattern);
        new WaitCmd().execute(new CommandParam(null,null, new String[]{"2000"}, null, null));

        //Step-4  click text-box in the dialog box to type the file name
        screen = new Screen();
        //String[] saveTextBoxImages = FileUtility.getAllFiles("images/saveaspdf/savetextbox");
        printPattern = ImageUtil.matchImagePatterns(screen, "saveaspdf/savetextbox");
        StringSelection selection = new StringSelection((PropertyUtility.getDefaultDownloadPath()
                + "/" + filename + ((filename.contains(".pdf"))? "" : ".pdf")).replaceAll("/","\\\\"));
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        robot = new Robot();

        // Press CTRL+V to paste
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        /* robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_A);
        robot.getAutoDelay();
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        *//*robot.keyPress(KeyEvent.VK_DELETE);
        robot.keyRelease(KeyEvent.VK_DELETE);*//*
        robot.keyPress(KeyEvent.VK_BACK_SPACE);
        robot.keyRelease(KeyEvent.VK_BACK_SPACE);
        screen.type(printPattern, (PropertyUtility.getDefaultDownloadPath()
                + "/" + filename + ((filename.contains(".pdf"))? "" : ".pdf")).replaceAll("/","\\\\"));*/
        //Step-5  click save button in the save dialog box
        //String[] saveBtnDialogBoxImage = FileUtility.getAllFiles("images/saveaspdf/savebutton");
        printPattern = ImageUtil.matchImagePatterns(screen, "saveaspdf/savebutton");
        screen.click(printPattern);

        /*for (String file : FileUtility.getAllFiles("images/saveaspdf/optiontosavepdf")) {
            try {
                dropDownImg = new Pattern(FileUtility.getAbsolutePath(file));
                screen.wait(dropDownImg);
                //screen.wait(dropDownImg, 2);
                //filePath = file;
                //if(itr !=1 && itr !=2) {
                //if(!file.contains("SaveAsPDF.png")){
                LogUtil.logTestCaseMsg("Image Loop - Find image successfully to select option for SaveAsPDF....");
                LogUtil.logTestCaseMsg("optiontosavepdf image matched: " + file);
                break;
            } catch (FindFailed ff) {
                LogUtil.logTestCaseMsg("SaveAsPDF optiontosavepdf image not matched: " + file);
                try {
                    dropDownImg = matchToolProfilePattern(screen, file);
                    LogUtil.logTestCaseMsg("optiontosavepdf === " + dropDownImg.toString());
                    if (dropDownImg.isValid() == true) break;
                }catch (Exception ex){}
            }
        }
        screen.click(dropDownImg);*/


       /* Pattern buttonImg = null; //new Pattern(FileUtility.getAbsolutePath("images//PrintSaveButton.png"));
        for (String file : FileUtility.getAllFiles("images/saveaspdf/savebutton")) {
            try {
                buttonImg = new Pattern(FileUtility.getAbsolutePath(file));
                screen.wait(buttonImg);
                //screen.wait(buttonImg, 2);
                LogUtil.logTestCaseMsg("Image Loop - Find image successfully to click save PDF button in the print screen....");
                LogUtil.logTestCaseMsg("savebutton image matched: " + file);
                break;
            } catch (FindFailed ff) {
                LogUtil.logTestCaseMsg("savebutton image in the print not matched: " + file);
                buttonImg = matchToolProfilePattern(screen, file);
                if (buttonImg.isValid() == true) break;
            }
        }
        screen.click(buttonImg);


        new WaitCmd().execute(new CommandParam(null,null, new String[]{"2000"}, null, null));*/
        //Print screen End
        //Save dialog window Start
        //setAddressBarPath(param);
        /*screen = new Screen();
        Pattern saveFileTextBox = null; //new Pattern(FileUtility.getAbsolutePath("images//savetextbox//SaveTextBox.png"));
        for (String file : FileUtility.getAllFiles("images/saveaspdf/savetextbox")) {
            try {
                saveFileTextBox = new Pattern(FileUtility.getAbsolutePath(file));
                screen.wait(saveFileTextBox);
                //screen.wait(saveFileTextBox, 5);
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_A);
                robot.getAutoDelay();
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.keyRelease(KeyEvent.VK_A);
                robot.keyPress(KeyEvent.VK_BACK_SPACE);
                robot.keyRelease(KeyEvent.VK_BACK_SPACE);
                *//*screen.type(saveFileTextBox, (PropertyUtility.getDefaultDownloadPath()
                        + "/" + filename + ((filename.contains(".pdf"))? "" : ".pdf")).replaceAll("/","\\\\"));*//*
                LogUtil.logTestCaseMsg("Image Loop - Find image successfully to type in textbox to SaveAsPDF....");
                LogUtil.logTestCaseMsg("savetextbox image matched: " + file);
                break;
            } catch (FindFailed ff) {
                LogUtil.logTestCaseMsg("SaveAsPDF savetextbox image not matched: " + file);
                saveFileTextBox = matchToolProfilePattern(screen, file);
                if (saveFileTextBox.isValid() == true) break;
            }
        }
        screen.type(saveFileTextBox, (PropertyUtility.getDefaultDownloadPath()
                + "/" + filename + ((filename.contains(".pdf"))? "" : ".pdf")).replaceAll("/","\\\\"));*/

        /*Pattern saveButton = null; //new Pattern(FileUtility.getAbsolutePath("images//savebutton//SaveButton.png"));
        for (String file : FileUtility.getAllFiles("images/saveaspdf/savebutton")) {
            try {
                saveButton = new Pattern(FileUtility.getAbsolutePath(file));
                screen.wait(saveButton);
                //screen.wait(saveButton, 2);
                LogUtil.logTestCaseMsg("Image Loop - Find image successfully to click save button for SaveAsPDF....");
                LogUtil.logTestCaseMsg("savebutton image matched: " + file);
                break;
            } catch (FindFailed ff) {
                LogUtil.logTestCaseMsg("SaveAsPDF savebutton image not matched: " + file);
                saveButton = matchToolProfilePattern(screen, file);
                if (saveButton.isValid() == true) break;
            }
        }
        screen.click(saveButton);*/
        //Save dialog window End
    }

    /*private static Pattern findImagePattern(Screen screen, String imagePath) throws IOException {
        Pattern patter = null;
        String[] listOfImages = FileUtility.getAllFiles(imagePath);
        for (int ddlitr = 0; ddlitr < listOfImages.length; ddlitr++) {
            String file = listOfImages[ddlitr];
            String filePath = FileUtility.getAbsolutePath(file);
            try {
                patter = new Pattern(filePath);
                screen.wait(patter);
                //screen.wait(dropDownImg, 2);
                LogUtil.logTestCaseMsg("Image Loop - Find image/Pattern successfully to SaveAsPDF....");
                LogUtil.logTestCaseMsg("Image/Pattern matched: " + file);
                break;
            } catch (FindFailed ff) {
                LogUtil.logTestCaseMsg("Image not matched: " + filePath);
                //if(ddlitr == dropDownList.length - 1) {
                patter = ImageUtil.matchToolProfilePattern(screen, file);
                LogUtil.logTestCaseMsg("Image === " + patter.toString());
                if (patter.isValid() == true) break;
            }
        }
        return patter;
    }*/

    /*private Object[] findImage(Screen screen, String dirRelativePath) throws IOException {
        Object[] filePath = new Object[2];
        for (String file : FileUtility.getAllFiles(dirRelativePath)) {
            try {
                filePath[0] = new Pattern(FileUtility.getAbsolutePath(file));
                screen.wait(filePath[0], 2);
                filePath[1] = file;

                //if(itr !=1 && itr !=2) {
                *//*if(!file.contains("SaveAsPDF.png")){
                    screen.click(dropDownImg);
                    screen.click(pdfOptionImg);
                }
                screen.click(buttonImg);*//*
                System.out.println("Image Loop - Find image successfully to SaveAsPDF....");
                break;
            } catch (FindFailed ex) {
                //ff.printStackTrace();
            }
        }
        return filePath;
    }*/

    /*private void setAddressBarPath(CommandParam param) throws IOException, FindFailed, AWTException, InterruptedException {
        Screen screen = new Screen();
        *//*screen.click(new Pattern(FileUtility.getAbsolutePath("images/ViewListDownArrow.png")));
        screen.click(new Pattern(FileUtility.getAbsolutePath("images/ViewListOption.png")));*//*
        Pattern addressBar = null;
        Robot robot = new Robot();
        for (int itr = 0; itr < winAddressBarImages.length; itr++) {
            try {
                Pattern addressBarButn = new Pattern(FileUtility.getAbsolutePath("images/AddressBarRightArrow.png"));
                screen.wait(addressBarButn, 2);
                screen.click(addressBarButn);
                addressBar = new Pattern(FileUtility.getAbsolutePath(winAddressBarImages[itr]));
                screen.wait(addressBar, 10);
                robot.keyPress(KeyEvent.VK_BACK_SPACE);
                robot.keyRelease(KeyEvent.VK_BACK_SPACE);
                screen.type(addressBar, PropertyUtility.getDefaultDownloadPath());
                break;
            } catch (FindFailed ff) {}
        }
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        Thread.sleep(3000);
    }*/


   /* public static void main(String[] args) throws CommandException, AWTException {
        OpenUrlCmd openUrl = OpenUrlCmd.getInstance();
        openUrl.execute(new CommandParam("test","OpenUrl", new String[]{"https://mail.google.com/"}, null, null));
        *//*ExecuteJSCmd executeJs = ExecuteJSCmd.getInstance();
        executeJs.execute(new CommandParam("test","ExecuteJs", new String[]{"jsScriptKey-1"}, "{\"scripts\": { \"jsScriptKey-1\":[ { \"script\" : \"window.print()\" } ] } }", null));*//*
        SaveAsPDFCmd obj = getInstance();
        obj.execute(new CommandParam("test","Upload", new String[]{"","sandeep"}, null, null));
        driver.quit();
    }*/

    private static void printWithSelenium4(WebDriver driver, String outputPath) {
        try {
            verifyPrintDialog();
            DriverManager.getInstance().focusCurrentWindow();
            // Cast driver to correct type
            ChromeDriver chromeDriver = (ChromeDriver) driver;

            // Configure print options
            PrintOptions printOptions = new PrintOptions();
            printOptions.setPageRanges("1-1"); // Print first page only
            printOptions.setBackground(true);
            //printOptions.setOrientation(PrintOptions.Orientation.PORTRAIT);
            //printOptions.setScale(100);
            //printOptions.setPageSize(new PageSize(15.00,15.27));
            printOptions.setPageMargin(new PageMargin(0,0,0,0));
            //printOptions.setShrinkToFit(false);

            // Print the page to PDF
            Pdf pdf = chromeDriver.print(printOptions);

            if(null != pdf) {
                // Decode the Base64 string and save to file
                byte[] decoded = Base64.getDecoder().decode(pdf.getContent());
                try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                    fos.write(decoded);
                }
            } else {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_ESCAPE);
                robot.keyRelease(KeyEvent.VK_ESCAPE);
            }

        } catch (IOException e) {
            LogUtil.logTestCaseErrorMsg("SaveAsPdf - printWithSelenium4() - ", e);
            e.printStackTrace();
        } catch (AWTException e) {
            LogUtil.logTestCaseErrorMsg("SaveAsPdf - Try to verify and Escape any OS popup windows - ", e);
        }
    }

    private static void  verifyPrintDialog() {
       // for(int itr=0; itr < 2;itr++) {
            try {
                Robot robot = new Robot();
                robot.keyPress(KeyEvent.VK_ESCAPE);
                robot.keyRelease(KeyEvent.VK_ESCAPE);
            } catch (AWTException e) {
                LogUtil.logTestCaseErrorMsg("SaveAsPDFCmd - Try to verify and Escape any OS popup windows - ", e);
            }
           /* try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            /*WinDef.HWND hwnd = User32.INSTANCE.FindWindow(null, "Print");
            if (hwnd != null) {
                System.out.println("Print dialog is opened.");
                try {
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_ESCAPE);
                    robot.keyRelease(KeyEvent.VK_ESCAPE);
                } catch (AWTException e) {
                    LogUtil.logTestCaseErrorMsg("SaveAsPDFCmd - Try to verify and Escape any OS popup windows - ", e);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Print dialog is NOT present.");
            }*/
        //}
    }
}


