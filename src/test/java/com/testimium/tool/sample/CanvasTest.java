package com.testimium.tool.sample;


import com.testimium.tool.base.DriverManager;
import com.testimium.tool.utility.ImageUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class CanvasTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        WebDriver driver = DriverManager.getInstance().getWebDriver();
        driver.get("https://kitchen.applitools.com/ingredients/canvas");
        WebElement canvas = driver.findElement(By.xpath("//*[@id=\"burger_canvas\"]"));

        //Javascript executor
        JavascriptExecutor js = (JavascriptExecutor)driver;
        String pngData = js.executeScript("return arguments[0].toDataURL('image/png').substring(22);", canvas).toString();
        System.out.println(pngData);
        byte[] canvas_png = Base64.getDecoder().decode(pngData);
        FileOutputStream fos = null;
        File imgFile = null;
        try {
            imgFile = new File("C:\\testimium\\Testing\\images\\image.png");
            fos = new FileOutputStream(imgFile);
            fos.write(canvas_png);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        Thread.sleep(3000);
        Color imgColor = Color.MAGENTA;
        BufferedImage actualImage = ImageIO.read(imgFile);
        BufferedImage expectedImage = ImageIO.read(new File("C:\\testimium\\Testing\\images\\canvas.png"));
        ImageUtil.compareAndHighlight(actualImage, expectedImage,"C:\\testimium\\Testing\\images\\final.png",true, imgColor.getRGB());
        driver.quit();

        /*try {
            ITesseract tesseractImage = new Tesseract();
            String result = tesseractImage.doOCR(imgFile);
            System.out.print(result);
        } catch (TesseractException ex) {
            ex.printStackTrace();
        }*/
       /*Ocr ocr = new Ocr(); // create a new OCR engine
       ocr.startEngine("eng" , Ocr.SPEED_FAST); // English*/
    }

    private void startOCR(){
        //OCR library will read the text from image
        /*Ocr ocr = new Ocr(); // create a new OCR engine
        ocr.startEngine("eng" , Ocr.SPEED_SLOW); // English*/
        // path of the image
    /*    String s = ocr.recognize(new File[] { new File("C:\\testimium\\Testing\\images\\image.png")},
                Ocr.RECOGNIZE_TYPE_TEXT, Ocr.OUTPUT_FORMAT_PLAINTEXT);
        System.out.println(s);
        ocr.stopEngine(); // Stop OCR engine*/
    }
}
