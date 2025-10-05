package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.locator.LocatorFactory;
import com.testimium.tool.utility.FileUtility;
import com.testimium.tool.utility.ImageUtil;
import com.testimium.tool.utility.PropertyUtility;
import org.apache.commons.lang3.RandomUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.http.HttpTimeoutException;
import java.util.Base64;

/**
 * @author Sandeep Agrawal
 */
public class VerifyCanvasAsImageCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static VerifyCanvasAsImageCmd verifyElement = new VerifyCanvasAsImageCmd();

    public VerifyCanvasAsImageCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static VerifyCanvasAsImageCmd getInstance() {
        //TODO Fix Me for concurrent access
        return verifyElement;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length != 1)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey).  ");

            WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));

            JavascriptExecutor js = (JavascriptExecutor)driver;
            String imageData = js.executeScript("return arguments[0].toDataURL('image/png').substring(22);", element).toString();
            System.out.println(imageData);
            byte[] canvas_img = Base64.getDecoder().decode(imageData);

            FileUtility.createDirectory(PropertyUtility.getDefaultDownloadPath());
            String actualFilePath = FileUtility.createFile(PropertyUtility.getDefaultDownloadPath() + "/CanvasAsImage" + RandomUtils.nextInt() + ".png", canvas_img);

            Thread.sleep(2000);


            Color imgColor = Color.MAGENTA;
            BufferedImage actualImage = ImageIO.read(new File(actualFilePath));
            BufferedImage expectedImage = ImageIO.read(new File(
                    FileUtility.getAbsolutePath(PropertyUtility.getExternalDirPath().trim()) + "/"
                            + TestContext.getTestContext("").getAssertParameter().getFirst().getFileName()));
            int randomNum = RandomUtils.nextInt();
            String diffFile = FileUtility.getAbsolutePath(PropertyUtility.getScreenshotLocation()) + "/Canvas_diff" + randomNum +".png";
            boolean isImageMatch = ImageUtil.compareAndHighlight(actualImage, expectedImage,diffFile,true, imgColor.getRGB());

            if(!isImageMatch) {
                return new CommandResponse("Verification Failed! \n Found difference in actual and expected images. \n" +  "<img src=\"../" + PropertyUtility.getScreenshotLocation() + "/Canvas_diff" + randomNum +".png" + "\" " + "alt=\"Italian Trulli\"> \n " +
                        "\n<a href='../../"+ PropertyUtility.getScreenshotLocation() + "/Canvas_diff" + randomNum +".png" + "'>Click to open the Canvas content difference in new tab.</a> \n");
            }

        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowAssertion(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


