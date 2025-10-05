package com.testimium.tool.sample;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.command.WaitCmd;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.exception.CommandException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SaveImageTest {
    /**
     * 1. Find the image element in a certain way (I'm using cssSelector)
     * 2. Get the src attribute
     * 3. Create a java URL
     * 4. Create a BufferedImage using ImageIO java class
     * 5. Use ImageIO to save the image with a preferred image extension and a location
     * @param args
     */
    public static void main(String[] args) throws IOException, CommandException {
        WebDriver driver = DriverManager.getInstance().getWebDriver();
        driver.get("http://localhost:8086/hrms/");

        WebElement machId = driver.findElement(By.xpath("//*[@id=\"machineID\"]"));
        machId.sendKeys("100");

        WebElement macOk = driver.findElement(By.xpath("//*[@id=\"btnOK\"]"));
        macOk.click();

        WebElement usernameEle = driver.findElement(By.xpath("//*[@id=\"userid\"]"));
        usernameEle.sendKeys("user1");

        WebElement pass = driver.findElement(By.xpath("//*[@id=\"passwd\"]"));
        pass.sendKeys("1234567a");

        new WaitCmd().execute(new CommandParam(null,null, new String[]{"2000"}, null, null));

        WebElement loginBtn = driver.findElement(By.xpath("//*[@id=\"btnLogin\"]"));
        pass.click();

        new WaitCmd().execute(new CommandParam(null,null, new String[]{"8000"}, null, null));

        WebElement img = driver.findElement(By.xpath("//*[@id=\"header-logo_cum\"]/img"));
        String logoSRC = img.getAttribute("src");

        URL imageURL = new URL(logoSRC);
        BufferedImage saveImage = ImageIO.read(imageURL);

        ImageIO.write(saveImage, "png", new File("C:\\testimium\\Testing\\downloads\\logo-forum.png"));
    }
}
