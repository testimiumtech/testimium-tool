package com.testimium.tool.html.generator;

import com.testimium.tool.html.IHtmlGenerator;
import com.testimium.tool.report.IReportData;
import com.testimium.tool.report.domain.BatchData;
import com.testimium.tool.report.domain.TestSuiteData;
import com.testimium.tool.utility.FileUtility;
import com.testimium.tool.utility.PropertyUtility;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestReportHtmlGenerator implements IHtmlGenerator {

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
            template = FileUtils.readFileToString(new File(PropertyUtility.getTemplateLocation() + "/Testsuite-Report.html"), "utf-8");
            String htmlOutput = template.replace("${TESTNAME}", data.getName());
            htmlOutput = htmlOutput.replace("${TOTALTESTCOUNT}", String.valueOf(data.getTotalTest()));
            htmlOutput = htmlOutput.replace("${TOTALPASSCOUNT}", String.valueOf(data.getTotalPassed()));
            htmlOutput = htmlOutput.replace("${TOTALFAILCOUNT}", String.valueOf(data.getTotalFailed()));
            htmlOutput = htmlOutput.replace("${TOTALSKIPCOUNT}", String.valueOf(data.getTotalSkipped()));
            htmlOutput = htmlOutput.replace("${PASSPERCENT}", String.valueOf(data.getTotalPassedRate()) + "%");
            htmlOutput = htmlOutput.replace("${TESTSTARTTIME}", String.valueOf(data.getTestStartTime()));
            htmlOutput = htmlOutput.replace("${TESTENDTIME}", String.valueOf(data.getTestEndTime()));
            htmlOutput = htmlOutput.replace("${TESTTOTALDURATION}", String.valueOf(data.getTotalDuration()));

            BatchData batchData = (BatchData)this.getData();
            List<TestSuiteData> reportDataList = batchData.getReportDataList();
            if(null != reportDataList) {
                StringBuilder rows = new StringBuilder();
                for (int itr = 0; itr < reportDataList.size(); itr++) {
                    rows.append(replace(reportDataList.get(itr)));
                    rows.append("\n");
                }
                htmlOutput = htmlOutput.replace("${TABLEROWS}", rows);
            }

           /* FileWriter writer = new FileWriter(data.getLocation() + "/"+ data.getName() + ".html");
            writer.write(htmlOutput.toString());
            writer.close();*/

            FileUtility.createFile(data.getLocation() + "/"+ data.getName() + ".html", htmlOutput.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String replace(IReportData reportData){
        return "<tr>"
                     +  " <td><a href='" + "./" + reportData.getName() + ".html' target='_blank' rel='noopener noreferrer'>" + reportData.getName() +"</a></td>"
                     +  "  <td class='passed'>"+ reportData.getTotalPassed() + "</td>"
                     +  "  <td class='failed'>"+ reportData.getTotalFailed() +"</td>"
                     +  "  <td class='skipped'>"+ reportData.getTotalSkipped() +"</td>"
                     +  "  <td>"+ reportData.getTotalPassedRate() +"%</td>"
                     +  "</tr>";
    }
}

