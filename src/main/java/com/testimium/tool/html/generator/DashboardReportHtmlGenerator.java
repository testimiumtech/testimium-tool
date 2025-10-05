package com.testimium.tool.html.generator;

import com.testimium.tool.exception.FileReaderException;
import com.testimium.tool.html.IHtmlGenerator;
import com.testimium.tool.reader.FileReaderFactory;
import com.testimium.tool.report.IReportData;
import com.testimium.tool.report.domain.BatchData;
import com.testimium.tool.report.domain.DashboardData;
import com.testimium.tool.utility.FileUtility;
import com.testimium.tool.utility.PropertyUtility;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DashboardReportHtmlGenerator implements IHtmlGenerator {

    private String reportDir = PropertyUtility.getReportLocation();
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
    public void generateHtml() throws IOException {
        DashboardData dashboardData = new DashboardData();
        dashboardData.setName("Report-Dashboard");
        dashboardData.setLocation(reportDir);
        List<IReportData> batchDataList = new ArrayList<>();
        File file = new File(FileUtility.getAbsolutePath(reportDir));
        if(!file.exists()) file.mkdirs();
        String[] dirs = FileUtility.getAllDirectory(file);
        for(int itr = 0; itr < dirs.length; itr++) {
            File jsonFile = new File(reportDir + "/" + dirs[itr] + "/batch.json");
            System.out.println(jsonFile.getName());
            try {
                if(jsonFile.exists()) {
                    BatchData batchData = (BatchData) FileReaderFactory.getInstance().readFile("JSON", "Report", jsonFile.getAbsolutePath());
                    batchData.setTotalPassedRate();
                    batchDataList.add(batchData);
                    if (itr == 0) {
                        dashboardData.setTestStartTime(batchData.getTestStartTime());
                    }
                    //if (itr == dirs.length - 1) {
                    dashboardData.setTestEndTime(batchData.getTestEndTime());
                    //}

                    dashboardData.setTotalFailed(dashboardData.getTotalFailed() + batchData.getTotalFailed());
                    dashboardData.setTotalPassed(dashboardData.getTotalPassed() + batchData.getTotalPassed());
                    dashboardData.setTotalTest(dashboardData.getTotalTest() + batchData.getTotalTest());
                    dashboardData.setTotalSkipped(dashboardData.getTotalSkipped() + batchData.getTotalSkipped());
                   /* batchData.setTestEndTime();
                    batchData.setTotalDuration();*/
                }

            } catch (FileReaderException e) {
                e.printStackTrace();
            }
        }
        dashboardData.setTotalPassedRate();
        dashboardData.setReportDataList(batchDataList);
        setData(dashboardData);
        generate();
        /*IHtmlGenerator htmlGenerator = new TestReportHtmlGenerator();
        htmlGenerator.setData(dashboardData);
        htmlGenerator.generateHtml();*/
    }


    private void generate()  {
        String template = null;
        try {
            template = FileUtils.readFileToString(new File(PropertyUtility.getTemplateLocation() + "/Testsuite-Report.html"), "utf-8");
            String htmlOutput = template.replace("${TESTNAME}", data.getName());
            htmlOutput = htmlOutput.replace("${TOTALTESTCOUNT}", String.valueOf(data.getTotalTest()));
            htmlOutput = htmlOutput.replace("${TOTALPASSCOUNT}", String.valueOf(data.getTotalPassed()));
            htmlOutput = htmlOutput.replace("${TOTALFAILCOUNT}", String.valueOf(data.getTotalFailed()));
            htmlOutput = htmlOutput.replace("${TOTALSKIPCOUNT}", String.valueOf(data.getTotalSkipped()));
            htmlOutput = htmlOutput.replace("${PASSPERCENT}", data.getTotalPassedRate() + "%");
            htmlOutput = htmlOutput.replace("${TESTSTARTTIME}", String.valueOf(data.getTestStartTime()));
            htmlOutput = htmlOutput.replace("${TESTENDTIME}", String.valueOf(data.getTestEndTime()));
            htmlOutput = htmlOutput.replace("${TESTTOTALDURATION}", String.valueOf(data.getTotalDuration()));

            DashboardData dashboardData = (DashboardData)  this.getData();
            List<IReportData> reportDataList = dashboardData.getReportDataList();
            StringBuilder rows = new StringBuilder();
            for (int itr = 0; itr < reportDataList.size(); itr++) {
                rows.append(replace(reportDataList.get(itr)));
                rows.append("\n");
            }
            htmlOutput = htmlOutput.replace("${TABLEROWS}", rows);

            /*FileWriter writer = new FileWriter("./Reports" + "/"+ data.getName() + ".html");
            writer.write(htmlOutput.toString());
            writer.close();*/
            FileUtility.createFile(reportDir + "/"+ data.getName() + ".html", htmlOutput.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String replace(IReportData reportData){
        return "<tr>"
                +  " <td><a href='" + "./" + reportData.getLocation().replace(reportDir,"") + "/" + reportData.getName().replace(PropertyUtility.getToolProfile() + "/", "") + ".html' target='_blank' rel='noopener noreferrer'>" + reportData.getLocation().replace(reportDir + "/","") +"</a></td>"
                +  "  <td class='passed'>"+ reportData.getTotalPassed() + "</td>"
                +  "  <td class='failed'>"+ reportData.getTotalFailed() +"</td>"
                +  "  <td class='skipped'>"+ reportData.getTotalSkipped() +"</td>"
                +  "  <td>"+ reportData.getTotalPassedRate() +"%</td>"
                +  "</tr>";
    }

   /* public static void main(String[] args) throws IOException {
        new DashboardReportHtmlGenerator().generateHtml();
    }*/
}
