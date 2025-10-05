package com.testimium.tool.sample;

import com.testimium.tool.base.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v129.page.Page;

import java.io.FileOutputStream;
import java.util.Base64;
import java.util.Optional;

public class SaveAsPDFHeadless2 {
    public static void main(String[] args) throws Exception {
        //ChromeOptions options = new ChromeOptions();
        /*options.addArguments("--headless=new"); // Required for PDF printing
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");*/
        DriverManager driverManager = DriverManager.getInstance();
        WebDriver driver = driverManager.getWebDriver();
        driver.get("http://localhost:8080/hrms");
        //ChromeDriver driver = new ChromeDriver(options);
        DevTools devTools = ((ChromeDriver)driver).getDevTools();
        //devTools.createSession();

        driver.get("http://localhost:8080/hrms/");

        Page.PrintToPDFResponse pdf = devTools.send(Page.printToPDF(
                Optional.empty(),Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()
        ));

        byte[] pdfData = Base64.getDecoder().decode(pdf.getData());
        try (FileOutputStream fos = new FileOutputStream("page.pdf")) {
            fos.write(pdfData);
        }

        driver.quit();
        System.out.println("PDF saved successfully.");
    }
}

