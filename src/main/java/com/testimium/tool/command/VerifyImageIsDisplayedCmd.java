package com.testimium.tool.command;

import org.openqa.selenium.By;
        import org.openqa.selenium.WebDriver;
        import org.openqa.selenium.WebElement;
        import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeOptions;

@Deprecated
public class VerifyImageIsDisplayedCmd{
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\testimium\\selenium\\driver\\chromedriver\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\testimium\\selenium\\driver\\chromedriver\\chrome-win64\\chrome.exe");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        String url = "https://www.tutorialspoint.com/index.htm";
        driver.get(url);
        //identify image
        WebElement i = driver.findElement
                (By.xpath("//img[@src='/static/images/home/hero-banner.png']"));
        // Javascript executor to check image
        Boolean p = (Boolean) ((JavascriptExecutor)driver) .executeScript("return arguments[0].complete " + "&& typeof arguments[0].naturalWidth != \"undefined\" " + "&& arguments[0].naturalWidth > 0", i);

        //verify if status is true
        if (p) {
            System.out.println("Logo present");
        } else {
            System.out.println("Logo not present");
        }
        driver.quit();
    }
}
