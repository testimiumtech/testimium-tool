package com.testimium.tool.html.generator;

import com.testimium.tool.context.TestContext;
import com.testimium.tool.html.IHtmlGenerator;
import com.testimium.tool.report.IReportData;
import com.testimium.tool.report.generator.ReportGenerator;
import com.testimium.tool.utility.PropertyUtility;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class TestStepErrorLogHtmlGenerator implements IHtmlGenerator {

    IReportData data;

    @Override
    public void setData(IReportData data) {
        this.data = data;
    }

    @Override
    public IReportData getData() {
        return data;
    }

    @Override
    public void generateHtml()  {
        String template = null;
        try {
            template = FileUtils.readFileToString(new File(PropertyUtility.getTemplateLocation() + "/TestStepErrorLog.html"), "utf-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String htmlOutput = template.replace("${logText}", TestContext.getTestContext("").getTestStepErrorLog().toString());

        ReportGenerator.getReportInstance().getChildNodeLevel1()
                .fail("<br><font color='red'>" + htmlOutput + "</font>");
    }
}
