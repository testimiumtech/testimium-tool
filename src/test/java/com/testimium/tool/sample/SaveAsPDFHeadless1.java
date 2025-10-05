package com.testimium.tool.sample;

import com.testimium.tool.base.DriverManager;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.print.PageMargin;
import org.openqa.selenium.print.PrintOptions;
import org.openqa.selenium.remote.CommandExecutor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SaveAsPDFHeadless1 {

    private static WebDriver driver;
    public static void main(String[] args) {
        // Set the path to your ChromeDriver
        //System.setProperty("webdriver.chrome.driver", "C:/testimium/selenium/driver/chromedriver/chromedriver-win64/chromedriver.exe");
        DriverManager driverManager = DriverManager.getInstance();
        driver = driverManager.getWebDriver();
        driver.get("http://localhost:8080/hrms");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //DevTools devTools = ((ChromeDriver)driver).getDevTools();
        // Configure Chrome options
        /*ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Required for PDF printing
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Initialize the WebDriver
        WebDriver driver = new ChromeDriver(options);*/

        try {
            // Navigate to the webpage you want to save as PDF
            //driver.get("http://localhost:8080/hrms");

            // Method 1: Using Selenium 4.x built-in print functionality
            printWithSelenium4(driver, "selenium4_output.pdf");

            // Method 2: Using Chrome DevTools Protocol (CDP)
            printWithCDP(driver, "cdp_output.pdf");

            System.out.println("PDF files saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the browser
            driver.quit();
        }
    }

    /**
     * Prints a page using Selenium 4's built-in print functionality
     */
    private static void printWithSelenium4(WebDriver driver, String outputPath) {
        try {
            // Cast driver to correct type
            org.openqa.selenium.chrome.ChromeDriver chromeDriver = (org.openqa.selenium.chrome.ChromeDriver) driver;

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


            // Decode the Base64 string and save to file
            byte[] decoded = Base64.getDecoder().decode(pdf.getContent());
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                fos.write(decoded);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints a page using Chrome DevTools Protocol (CDP)
     * Works with older Selenium versions too
     */
    private static void printWithCDP(WebDriver driver, String outputPath) {
        try {
            // Cast driver to ChromeDriver
            ChromeDriver chromeDriver = (ChromeDriver) driver;
            CommandExecutor executor = chromeDriver.getCommandExecutor();

            // Set up print parameters
            Map<String, Object> params = new HashMap<>();
            params.put("landscape", false);
            params.put("printBackground", true);
            params.put("paperWidth", 8.27); // A4 width in inches
            params.put("paperHeight", 15.00); // A4 height in inches
            params.put("marginTop", 0);  // 15mm in inches
            params.put("marginBottom", 0);  // 15mm in inches
            params.put("marginLeft", 0);  // 10mm in inches
            params.put("marginRight", 0);  // 10mm in inches
            params.put("pageRanges", "1");
            /*params.put("headerTemplate", 0);
            params.put("footerTemplate", 0);
            params.put("preferCSSPageSize", 0);
            params.put("transferMode", "");*/
            params.put("scale", 0.98);
            params.put("displayHeaderFooter", true);
            params.put("generateDocumentOutline", false);
            // Create CDP command
            /*Command command = new Command(
                    chromeDriver.getSessionId(),
                    "CDP",
                    Map.of("cmd", "Page.printToPDF", "params", params)
            );*/

            // Execute command
            //Response response = executor.execute(command);

            Map<String, Object> result = chromeDriver.executeCdpCommand("Page.printToPDF", params);

            // Extract and decode PDF data
            //Map<String, Object> result = (Map<String, Object>) response.getValue();
            String pdfData = (String) result.get("data");
            byte[] decoded = Base64.getDecoder().decode(pdfData);

            // Save to file
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                fos.write(decoded);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

