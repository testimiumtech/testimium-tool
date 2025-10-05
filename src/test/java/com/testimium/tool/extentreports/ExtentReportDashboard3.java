package com.testimium.tool.extentreports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportDashboard3 {
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
        htmlContent.append("<html><head><title>Test Dashboard</title>");
        htmlContent.append("<style>");
        htmlContent.append("body { font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; }");
        htmlContent.append(".container { width: 80%; margin: auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0px 0px 10px gray; }");
        htmlContent.append("h1 { color: #333; }");
        htmlContent.append(".stats { display: flex; justify-content: space-around; padding: 10px; }");
        htmlContent.append(".stat-box { background: #fff; padding: 15px; border-radius: 8px; box-shadow: 0px 0px 5px gray; }");
        htmlContent.append(".passed { color: green; }");
        htmlContent.append(".failed { color: red; }");
        htmlContent.append("ul { list-style-type: none; padding: 0; }");
        htmlContent.append("li { padding: 8px; font-size: 18px; }");
        htmlContent.append("a { text-decoration: none; color: #007BFF; font-weight: bold; }");
        htmlContent.append("</style>");
        htmlContent.append("</head><body>");
        htmlContent.append("<div class='container'>");
        htmlContent.append("<h1>Test Report Dashboard</h1>");
        htmlContent.append("<div class='stats'>");
        htmlContent.append("<div class='stat-box'><strong>Start Time:</strong> " + startTimeStr + "</div>");
        htmlContent.append("<div class='stat-box'><strong>End Time:</strong> " + endTimeStr + "</div>");
        htmlContent.append("<div class='stat-box'><strong>Total Duration:</strong> " + totalTime + " seconds</div>");
        htmlContent.append("</div>");
        htmlContent.append("<div class='stats'>");
        htmlContent.append("<div class='stat-box'><strong>Total Tests:</strong> " + totalTests + "</div>");
        htmlContent.append("<div class='stat-box passed'><strong>Passed:</strong> " + passedTests + "</div>");
        htmlContent.append("<div class='stat-box failed'><strong>Failed:</strong> " + failedTests + "</div>");
        htmlContent.append("</div>");
        htmlContent.append("<h2>Test Reports</h2>");
        htmlContent.append("<ul>");

        if (reportFiles != null) {
            for (File report : reportFiles) {
                htmlContent.append("<li><a href='" + report.getName() + "'>" + report.getName() + "</a></li>");
            }
        }

        htmlContent.append("</ul>");
        htmlContent.append("</div>");
        htmlContent.append("</body></html>");

        try (FileWriter writer = new FileWriter(reportDir + "/index.html")) {
            writer.write(htmlContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}