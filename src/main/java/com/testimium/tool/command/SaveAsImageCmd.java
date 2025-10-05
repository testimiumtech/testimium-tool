package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.utility.PropertyUtility;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 *
 */
public class SaveAsImageCmd implements ExternalCommand<CommandParam, CommandResponse> {
    /**
     * Web driver object
     */
    private WebDriver driver;
    /**
     * Static object of SaveAsImageCmd
     */
    private static SaveAsImageCmd saveAsImageCmd = new SaveAsImageCmd();

    public SaveAsImageCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }
    /**
     * Get the static instance
     * @return response SaveAsImageCmd
     */
    public static SaveAsImageCmd getInstance() {
        //TODO Fix Me for concurrent access
        return saveAsImageCmd;
    }

    /**
     * Syntax: SaveAsImage Locator-Property File-Name File-Format
     *
     * @param param in put param
     * @return command response
     * @throws CommandException if command exception
     * @throws HttpTimeoutException if http timeout exception
     */
    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 2)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey/locator).  ");

            WebElement element = wait(param);
            //WebElement element = driver.findElement(LocatorFactory.getByLocatorProperty(param.getArgs()[0]));
            String imgSrc = element.getAttribute("src");
            URL imageURL = new URL(imgSrc);
            BufferedImage saveImage = ImageIO.read(imageURL);
            String imgFormat = "PNG";
            if(param.getArgs().length >= 3) {
                imgFormat = param.getArgs()[2];
            }
            ImageIO.write(saveImage, imgFormat.toLowerCase(), new File(PropertyUtility.getDefaultDownloadPath() + "/" + param.getArgs()[1]));

        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


