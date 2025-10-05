package com.testimium.tool.report.domain;

import com.testimium.tool.report.IReportData;
import com.testimium.tool.utility.DateUtility;

import java.util.List;

public class DashboardData implements IReportData{
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
    private List<IReportData> reportDataList;

    public DashboardData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalTest() {
        return totalTest;
    }

    public void setTotalTest(int totalTest) {
        this.totalTest = totalTest;
    }

    public int getTotalPassed() {
        return totalPassed;
    }

    public void setTotalPassed(int totalPassed) {
        this.totalPassed = totalPassed;
    }

    public int getTotalFailed() {
        return totalFailed;
    }

    public void setTotalFailed(int totalFailed) {
        this.totalFailed = totalFailed;
    }

    public int getTotalSkipped() {
        return totalSkipped;
    }

    public void setTotalSkipped(int totalSkipped) {
        this.totalSkipped = totalSkipped;
    }

    public int getTotalPassedRate() {
        return this.totalPassedRate;
    }

    public void setTotalPassedRate() {
        if(getTotalTest() != 0) {
            this.totalPassedRate = (getTotalPassed() / getTotalTest()) * 100;
        }
    }

    public String getTestStartTime() {
        return testStartTime;
    }

    public void setTestStartTime(String testStartTime) {
        this.testStartTime = testStartTime;
    }

    public String getTestEndTime() {
        return testEndTime;
    }

    public void setTestEndTime(String testEndTime) {
        this.testEndTime = testEndTime;
    }

    public String getTotalDuration() {
        if(null != getTestStartTime() && null != getTestEndTime()) {
            return this.totalDuration = DateUtility.getDuration("yyyy-MM-dd HH:mm:ss", getTestStartTime(), getTestEndTime());
        }
        /*"H'h' mm'm' ss's'"*/
        return "00:00:00";
    }


    public List<IReportData> getReportDataList() {
        return reportDataList;
    }

    public void setReportDataList(List<IReportData> reportDataList) {
        this.reportDataList = reportDataList;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "DashboardData{" +
                "name='" + name + '\'' +
                ", totalTest=" + totalTest +
                ", totalPassed=" + totalPassed +
                ", totalFailed=" + totalFailed +
                ", totalSkipped=" + totalSkipped +
                ", totalPassedRate=" + totalPassedRate +
                ", testStartTime='" + testStartTime + '\'' +
                ", testEndTime='" + testEndTime + '\'' +
                ", totalDuration='" + totalDuration + '\'' +
                ", location='" + location + '\'' +
                ", reportDataList=" + reportDataList +
                '}';
    }
}
