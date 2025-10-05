package com.testimium.tool.sample;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.command.CloseBrowserTabCmd;
import com.testimium.tool.exception.CommandException;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.http.HttpTimeoutException;

public class IdentifyWindowsPopup {

    public static void main(String[] args) throws AWTException, CommandException, HttpTimeoutException {
        WebDriver driver = DriverManager.getInstance().getWebDriver();
        driver.get("https://demoqa.com/buttons");
        driver.manage().window().maximize();
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_P);
        robot.getAutoDelay();
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_P);
       /*try {
            Screen screen = new Screen();

            Pattern componentImage1 = new Pattern(FileUtility.getAbsolutePath("images/WindowsPrintPopup/CancelBtn.png"));
            Pattern componentImage2 = new Pattern(FileUtility.getAbsolutePath("images/WindowsPrintPopup/WindowsPrintPopupBtns.png"));
            screen.wait(componentImage2, 2000);
            screen.click(componentImage1);
        } catch (FindFailed ff) {
                ff.printStackTrace();
        } catch (Exception ex) {
                ex.printStackTrace();
        }*/

        new CloseBrowserTabCmd().execute(null);


    }
}
