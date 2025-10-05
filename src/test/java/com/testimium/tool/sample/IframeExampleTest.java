package com.testimium.tool.sample;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class IframeExampleTest {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("WebDriver.chrome.driver","C:/testimium/selenium/driver/chromedriver/chromedriver-win64/chromedriver.exe");
        WebDriver driver = null;
        try {


            driver = new ChromeDriver();
            driver.manage().window().maximize();
            driver.get("http://localhost:8081/hrms");
            Thread.sleep(3000);
            driver.findElement(By.xpath("//*[@id=\"machineID\"]")).sendKeys("100");
            driver.findElement(By.xpath("//*[@id=\"btnOK\"]")).click();
            Thread.sleep(3000);
            driver.findElement(By.cssSelector("input[id='userid']")).sendKeys("user1");
            driver.findElement(By.xpath("//*[@id=\"passwd\"]")).sendKeys("1234567a");
            driver.findElement(By.id("btnLogin")).click();
            Thread.sleep(20000);
            driver.findElement(By.xpath("//*[@id=\"sidebar-menu\"]/li[3]/a/i")).click();
            Thread.sleep(3000);
            driver.findElement(By.xpath("//*[@id=\"sidebar-menu\"]/li[3]/div/ul/li[1]/a")).click();
            Thread.sleep(20000);
            WebElement searchButton = driver.findElement(By.id("Search"));
            (new Actions(driver)).moveToElement(driver.findElement(By.id("Search"))).build().perform();
            //((JavascriptExecutor) DriverManager.getInstance().getWebDriver()).executeScript("arguments[0].scrollIntoView(true);", searchButton);
            //WebElement getAPractices=driver.findElement(RelativeLocator.with(By.tagName("button")).above(Test));
            searchButton.click();
            Thread.sleep(3000);
        }catch (Exception ex) {
            //ClickEdit
            Thread.sleep(3000);
            driver.findElement(By.xpath("//*[@id=\"0\"]")).click();
           driver.wait(5000);
            driver.findElement(By.xpath("//*[@id=\"topNav\"]/div[3]/div/a")).click();
            driver.findElement(By.xpath("//ul[@id=\"actionButtonMenu\"]/li[1]/*[@id=\"Edit\"]")).click();
//             driver.wait(3000);
            //EcommerceTab
            driver.findElement(By.xpath("//*[@id=\"navTab-ECommerceOptions\"]")).click();
           //Checkbox prepare for web
            driver.findElement(By.xpath("//*[@id=\"tab-ECommerceOptions\"]/div[1]/fieldset/section/div[1]/div[2]/label")).click();
           //SetWebBtn
            driver.findElement(By.xpath("//*[@id=\"setweb\"]")).click();

            //Frame
            //WebElement Frame1 = driver.findElement(By.xpath("//*[@id=\"cke_1_contents\"]/iframe"));
            //driver.switchTo().frame("Frame1");
            //driver.findElement(By.id("")).sendKeys("Hello");

            WebElement iframeMsg = driver.findElement(By.xpath("//*[contains(@class, 'wysiwyg_frame')]"));
            driver.switchTo().frame(iframeMsg);
            WebElement body = driver.findElement(By.cssSelector("body"));
            System.out.println(body.getText());
            driver.quit();
        }
    }
}