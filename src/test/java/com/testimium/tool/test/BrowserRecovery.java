package com.testimium.tool.test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BrowserRecovery {
    WebDriver driver;

    public void startBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
        driver.get("https://example.com");
    }

    public void runTest() {
        try {
            // Attempt to interact
            driver.findElement(By.tagName("body")).click();
        } catch (WebDriverException e) {
            System.out.println("Browser probably closed. Restarting browser...");
            restartBrowser();
        }
    }

    public void restartBrowser() {
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception ignored) {}

        startBrowser();
        // Re-do steps to restore state if needed
    }

    public static void main(String[] args) {
        BrowserRecovery test = new BrowserRecovery();
        test.startBrowser();
        test.runTest();
    }
}

