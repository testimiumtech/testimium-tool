package com.testimium.tool.extentreports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportDashboard4 {
    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;
    private static int skippedTests = 0;
    private static long startTime;
    private static long endTime;

    public static void main(String[] args) {
        String reportDir = "./reports";
        new File(reportDir).mkdirs();

        startTime = System.currentTimeMillis();

        // Generate multiple test reports
        generateReport(reportDir + "/TestSuite1.html", "Test Suite 1", Status.PASS);
        generateReport(reportDir + "/TestSuite2.html", "Test Suite 2", Status.FAIL);
        generateReport(reportDir + "/TestSuite3.html", "Test Suite 3", Status.SKIP);

        endTime = System.currentTimeMillis();

        // Generate dashboard
        generateDashboard(reportDir);
    }

    public static void generateReport(String filePath, String suiteName, Status status) {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(filePath);
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        ExtentTest test = extent.createTest(suiteName + " - Test Case 1");
        totalTests++;
        test.assignCategory(suiteName);
        switch (status) {
            case PASS:
                test.pass("Test case passed");
                passedTests++;
                break;
            case FAIL:
                test.fail("Test case failed");
                failedTests++;
                break;
            case SKIP:
                test.skip("Test case skipped");
                skippedTests++;
                break;
        }

        extent.flush();
    }

    public static void generateDashboard(String reportDir) {
        File folder = new File(reportDir);
        File[] reportFiles = folder.listFiles((dir, name) -> name.endsWith(".html") && !name.equals("index.html"));

        long totalTime = (endTime - startTime) / 1000;
        String startTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(startTime));
        String endTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(endTime));
        double passPercentage = (totalTests == 0) ? 0 : ((double) passedTests / totalTests) * 100;

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
        htmlContent.append(".skipped { color: orange; }");
        htmlContent.append("ul { list-style-type: none; padding: 0; }");
        htmlContent.append("li { padding: 8px; font-size: 18px; }");
        htmlContent.append("a { text-decoration: none; color: #007BFF; font-weight: bold; }");
        htmlContent.append("td, th {\n" +
                "        padding: 15px 5px;\n" +
                "        display: table-cell;\n" +
                "        text-align: left;\n" +
                "        vertical-align: middle;\n" +
                "        border-radius: 2px;\n" +
                "    }");
        htmlContent.append("</style>");
        htmlContent.append("</head><body>");
        htmlContent.append("<div class='container'>");
        htmlContent.append("<h1>Test Report Dashboard</h1>" );
        /*htmlContent.append("<div id='test-view-charts' class='subview-full'>\n" +
                "    <div id='charts-row' class='row nm-v nm-h'>\n" +
                "        <div class='col s12 m12 l12 np-h'>\n" +
                "            <div class='card-panel nm-v'>\n" +
                "                <div class='left panel-name'>Tests</div>\n" +
                "                <div class='chart-box' style=\"max-height:94px;\">\n" +
                "                    <canvas id='parent-analysis' width='90' height='70'></canvas>\n" +
                "                </div>\n" +
                "                <div class='block text-small'>\n" +
                "                    <span class='tooltipped' data-position='top' data-tooltip='35.897%'><span class='strong'>112</span> test(s) passed</span>\n" +
                "                </div>\n" +
                "                <div class='block text-small'>\n" +
                "                    <span class='strong tooltipped' data-position='top' data-tooltip='64.103%'>200</span> test(s) failed, <span class='strong tooltipped' data-position='top' data-tooltip='0%'>0</span> skipped\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>" +
                "</div>");*/
        htmlContent.append("<div class='stats'>");
        htmlContent.append("<div class='stat-box'><strong>Total Tests:</strong> " + totalTests + "</div>");
        htmlContent.append("<div class='stat-box passed'><strong>Passed:</strong> " + passedTests + "</div>");
        htmlContent.append("<div class='stat-box failed'><strong>Failed:</strong> " + failedTests + "</div>");
        htmlContent.append("<div class='stat-box skipped'><strong>Skipped:</strong> " + skippedTests + "</div>");
        htmlContent.append("<div class='stat-box'><strong>Pass Percentage:</strong> " + String.format("%.2f", passPercentage) + "%</div>");
        htmlContent.append("<div class='stat-box'><strong>Start Time:</strong> " + startTimeStr + "</div>");
        htmlContent.append("<div class='stat-box'><strong>End Time:</strong> " + endTimeStr + "</div>");
        htmlContent.append("<div class='stat-box'><strong>Total Duration:</strong> " + totalTime + " seconds</div>");
        htmlContent.append("</div>");
        htmlContent.append("<div class='stats'>");
        htmlContent.append("</div>");
        htmlContent.append("<h2>Test Reports</h2>");
        //htmlContent.append("<ul>");

        /*if (reportFiles != null) {
            for (File report : reportFiles) {
                htmlContent.append("<li><a href='" + report.getName() + "'>" + report.getName() + "</a></li>");
            }
        }*/
        htmlContent.append("<div class='stat-box'><table border=1>");
        htmlContent.append("<tr><th>Test Suite</th><th>Passed</th><th>Failed</th><th>Skipped</th><th>Pass %</th></tr>");

        if (reportFiles != null) {
            for (File report : reportFiles) {
                int suitePassed = (int) (Math.random() * 10);
                int suiteFailed = (int) (Math.random() * 10);
                int suiteSkipped = (int) (Math.random() * 5);
                int suiteTotal = suitePassed + suiteFailed + suiteSkipped;
                double suitePassPercentage = (suiteTotal == 0) ? 0 : ((double) suitePassed / suiteTotal) * 100;
                htmlContent.append("<tr>");
                htmlContent.append("<td><a href='" + report.getName() + "'>" + report.getName() + "</a></td>");
                htmlContent.append("<td class='passed'>" + suitePassed + "</td>");
                htmlContent.append("<td class='failed'>" + suiteFailed + "</td>");
                htmlContent.append("<td class='skipped'>" + suiteSkipped + "</td>");
                htmlContent.append("<td>" + String.format("%.2f", suitePassPercentage) + "%</td>");
                htmlContent.append("</tr>");
            }
        }

        htmlContent.append("</table></div>");

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