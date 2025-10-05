package com.testimium.tool.utility;

import com.testimium.tool.context.TestContext;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class HtmlGenerator {

    public String generateTextComparisonHTML(String templateName, String actualText, String expectedText) throws IOException {
        // Get template & replace placeholders with left & right variables with actual
        // comparison
        String template = FileUtils.readFileToString(new File(PropertyUtility.getTemplateLocation() + "/TextDiffTemplate.html"), "utf-8");
        String out1 = template.replace("${left}", actualText.toString());
        String output = out1.replace("${right}", expectedText.toString());
        String fileName = null;
        File file = null;
        // Write file to disk.
        try {
            fileName = PropertyUtility.getScreenshotLocation() + "/" + TestContext.getTestContext("").getTestCaseName() + "-" + DateUtility.getDateForFileName() + ".html";
            file = new File(fileName);
            FileUtils.write(file, output, "utf-8");
            System.out.println("HTML diff generated.");
        } catch (Exception ex){
            //TODO Fix this
            ex.printStackTrace();
        }

        fileName = fileName.replace(PropertyUtility.getReportLocation(),"..");
        //<embed type="text/html" src="snippet.html"  width="500" height="200">
        return "<a href='"+ fileName + "'>Click to see the file comparison difference</a> \n" + file.getAbsolutePath() + "<br><br><embed type='text/html' src='" + fileName +"' width='800' height='300'>";
    }
}
