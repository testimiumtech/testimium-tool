package com.testimium.tool.extentreports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportDashboard2 {
    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;
    private static long startTime;
    private static long endTime;

    public static void main(String[] args) {
        String reportDir = "./reports";
        new File(reportDir).mkdirs();

        startTime = System.currentTimeMillis();

        // Generate multiple test reports
        generateReport(reportDir + "/TestSuite1.html", "Test Suite 1", true);
        generateReport(reportDir + "/TestSuite2.html", "Test Suite 2", false);

        endTime = System.currentTimeMillis();

        // Generate dashboard
        generateDashboard(reportDir);
    }

    public static void generateReport(String filePath, String suiteName, boolean isPassed) {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(filePath);
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        ExtentTest test = extent.createTest(suiteName + " - Test Case 1");
        totalTests++;
        if (isPassed) {
            test.pass("Test case passed");
            passedTests++;
        } else {
            test.fail("Test case failed");
            failedTests++;
        }

        extent.flush();
    }

    public static void generateDashboard(String reportDir) {
        File folder = new File(reportDir);
        File[] reportFiles = folder.listFiles((dir, name) -> name.endsWith(".html") && !name.equals("index.html"));

        long totalTime = (endTime - startTime) / 1000;
        String startTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(startTime));
        String endTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(endTime));

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><head><title>Test Dashboard</title></head><body>");
        htmlContent.append("<h1>Test Report Dashboard</h1>");
        htmlContent.append("<p><strong>Start Time:</strong> " + startTimeStr + "</p>");
        htmlContent.append("<p><strong>End Time:</strong> " + endTimeStr + "</p>");
        htmlContent.append("<p><strong>Total Duration:</strong> " + totalTime + " seconds</p>");
        htmlContent.append("<p><strong>Total Tests:</strong> " + totalTests + "</p>");
        htmlContent.append("<p><strong>Passed:</strong> " + passedTests + "</p>");
        htmlContent.append("<p><strong>Failed:</strong> " + failedTests + "</p>");
        htmlContent.append("<h2>Test Reports</h2><ul>");

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
