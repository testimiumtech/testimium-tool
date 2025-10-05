package com.testimium.tool.sample;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.exception.LocatorNotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v129.page.Page;

import java.io.FileOutputStream;
import java.util.Base64;
import java.util.Optional;

public class SaveAsPDFHeadless {

    private static WebDriver driver;
    public static void main(String[] args) throws LocatorNotFoundException {
        //sample();
        DriverManager driverManager = DriverManager.getInstance();
        driver = driverManager.getWebDriver();
        driver.get("http://localhost:8080/hrms");
        DevTools devTools = ((ChromeDriver)driver).getDevTools();
        saveAsPdf(devTools);
    }
    public static void saveAsPdf(DevTools devTools) {

        try {

            // Wait if needed, or ensure the page is fully loaded

            // Call the DevTools printToPDF command
            Page.PrintToPDFResponse pdf = devTools.send(Page.printToPDF(
                    Optional.of(false),  // landscape
                    Optional.empty(),    // scale
                    Optional.of(true),   // printBackground
                    Optional.empty(),    // paperWidth
                    Optional.empty(),    // paperHeight
                    Optional.empty(),    // marginTop
                    Optional.empty(),    // marginBottom
                    Optional.empty(),    // marginLeft
                    Optional.empty(),    // marginRight
                    Optional.empty(),    // pageRanges
                    Optional.empty(),    // headerTemplate
                    Optional.empty(),    // footerTemplate
                    Optional.empty(),    // preferCSSPageSize
                    Optional.empty(),    // transferMode
                    Optional.empty(),     // media
                    Optional.empty(),
                    Optional.of(false)
            ));
            /*Optional<Boolean> landscape,
            Optional<Boolean> displayHeaderFooter,
                    Optional<Boolean> printBackground,
                    Optional<Number> scale,
                    Optional<Number> paperWidth,
                    Optional<Number> paperHeight,
                    Optional<Number> marginTop,
                    Optional<Number> marginBottom,
                    Optional<Number> marginLeft,
                    Optional<Number> marginRight,
                    Optional<String> pageRanges,
                    Optional<String> headerTemplate,
                    Optional<String> footerTemplate,
                    Optional<Boolean> preferCSSPageSize,
                    Optional< Page.PrintToPDFTransferMode > transferMode,
                    Optional<Boolean> generateTaggedPDF,
                    Optional<Boolean> generateDocumentOutline*/

            // Decode base64 PDF content and save to file
            byte[] pdfBytes = Base64.getDecoder().decode(pdf.getData());
            try (FileOutputStream fileOutputStream = new FileOutputStream("output.pdf")) {
                fileOutputStream.write(pdfBytes);
            }

            System.out.println("PDF saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}

