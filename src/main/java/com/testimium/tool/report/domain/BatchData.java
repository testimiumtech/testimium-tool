package com.testimium.tool.report.domain;

import com.testimium.tool.report.IReportData;
import com.testimium.tool.utility.DateUtility;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchData implements IReportData {

    private String name;
    private int totalTest;
    private int totalPassed;
    private int totalFailed;
    private int totalSkipped;
    private int totalPassedRate;
    private String testStartTime;
    private String testEndTime;
    private String totalDuration;
    private String location;
    private List<TestSuiteData> reportDataList;

    public BatchData() {
        setTestStartTime();
    }


    public String getName() {
        return name;
    }
    /**
     * Set Name
     * @param name input param
     */
    public void setName(String name) {
        this.name = name;
    }

    public int getTotalTest() {
        return totalTest;
    }
    /**
     * Set total number of test
     * @param totalTest input param
     */
    public void setTotalTest(int totalTest) {
        this.totalTest = totalTest;
    }

    public int getTotalPassed() {
        return totalPassed;
    }

    /**
     * Set total passed
     * @param totalPassed input param
     */
    public void setTotalPassed(int totalPassed) {
        this.totalPassed = totalPassed;
    }

    public int getTotalFailed() {
        return totalFailed;
    }

    /**
     * Set total failed
     * @param totalFailed input param
     */
    public void setTotalFailed(int totalFailed) {
        this.totalFailed = totalFailed;
    }

    public int getTotalSkipped() {
        return totalSkipped;
    }

    /**
     * Set total test skipped
     * @param totalSkipped input param
     */
    public void setTotalSkipped(int totalSkipped) {
        this.totalSkipped = totalSkipped;
    }

    public int getTotalPassedRate() {
        return this.totalPassedRate;
    }

    /**
     * set total passed rate
     */
    public void setTotalPassedRate() {
        if(getTotalTest() != 0) {
            this.totalPassedRate = (getTotalPassed() / getTotalTest()) * 100;
        }
    }

    public String getTestStartTime() {
        return testStartTime;
    }

    /**
     * Set test start time
     */
    public void setTestStartTime() {
        this.testStartTime = DateUtility.getDate("yyyy-MM-dd HH:mm:ss", 0);
    }

    public String getTestEndTime() {
        return testEndTime;
    }
    /**
     * Set test end time
     */
    public void setTestEndTime() {
        this.testEndTime = DateUtility.getDate("yyyy-MM-dd HH:mm:ss", 0);
    }

    public String getTotalDuration() {
        return totalDuration;
    }
    /**
     * Set test total time
     */
    public void setTotalDuration() {
        if(null != getTestStartTime() && null != getTestEndTime()) {
            this.totalDuration = DateUtility.getDuration(
                    "yyyy-MM-dd HH:mm:ss", getTestStartTime(), getTestEndTime()
            );
        }
        /*"H'h' mm'm' ss's'"*/
    }

    public List<TestSuiteData> getReportDataList() {
        return reportDataList;
    }

    /**
     * Set report data list
     * @param reportDataList input param
     */
    public void setReportDataList(List<TestSuiteData> reportDataList) {
        this.reportDataList = reportDataList;
    }

    public String getLocation() {
        return location;
    }

    /**
     * Set Location
     * @param location input param
     */
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "BatchData{" +
                "name='" + name + '\'' +
                ", totalTest=" + totalTest +
                ", totalPassed=" + totalPassed +
                ", totalFailed=" + totalFailed +
                ", totalSkipped=" + totalSkipped +
                ", totalPassedRate=" + totalPassedRate +
                ", testStartTime='" + testStartTime + '\'' +
                ", testEndTime='" + testEndTime + '\'' +
                ", totalDuration='" + totalDuration + '\'' +
                ", folderLocation='" + location + '\'' +
                ", testSuiteDataList=" + reportDataList +
                '}';
    }
}
