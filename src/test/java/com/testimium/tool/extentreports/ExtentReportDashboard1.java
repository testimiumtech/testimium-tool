package com.testimium.tool.extentreports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExtentReportDashboard1 {
    public static void main(String[] args) {
        String reportDir = "./reports";
        new File(reportDir).mkdirs();

        // Generate multiple test reports
        generateReport(reportDir + "/TestSuite1.html", "Test Suite 1");
        generateReport(reportDir + "/TestSuite2.html", "Test Suite 2");

        // Generate dashboard
        generateDashboard(reportDir);
    }

    public static void generateReport(String filePath, String suiteName) {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(filePath);
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        ExtentTest test = extent.createTest(suiteName + " - Test Case 1");
        test.pass("Test case passed");

        extent.flush();
    }

    public static void generateDashboard(String reportDir) {
        File folder = new File(reportDir);
        File[] reportFiles = folder.listFiles((dir, name) -> name.endsWith(".html"));

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><head><title>Test Dashboard</title></head><body>");
        htmlContent.append("<h1>Test Report Dashboard</h1><ul>");

        if (reportFiles != null) {
            for (File report : reportFiles) {
                htmlContent.append("<li><a href='" + report.getName() + "'>" + report.getName() + "</a></li>");
            }
        }

        htmlContent.append("</ul></body></html>");

        try (FileWriter writer = new FileWriter(reportDir + "/index.html")) {
            writer.write(htmlContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}