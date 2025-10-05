package com.testimium.tool.report.domain;

import com.testimium.tool.report.IReportData;
import com.testimium.tool.utility.DateUtility;

public class TestSuiteData implements IReportData {

    private String name;
    private String batchName;
    private String location;
    private int totalTest;
    private int totalPassed;
    private int totalFailed;
    private int totalSkipped;
    private int totalPassedRate;
    private String testStartTime;
    private String testEndTime;
    private String totalDuration;
    private String filePath;

    public TestSuiteData() {
        setTestStartTime();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
        if(getTotalTest() > 0) {
            this.totalPassedRate = (getTotalPassed() / getTotalTest()) * 100;
        }
    }

    public String getTestStartTime() {
        return testStartTime;
    }

    public void setTestStartTime() {
        this.testStartTime = DateUtility.getDate("yyyy-MM-dd HH:mm:ss", 0);
    }

    public String getTestEndTime() {
        return testEndTime;
    }

    public void setTestEndTime() {
        this.testEndTime = DateUtility.getDate("yyyy-MM-dd HH:mm:ss", 0);
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration() {
        if(null != getTestStartTime() && null != getTestEndTime()) {
            this.totalDuration = DateUtility.getDuration(
                    "yyyy-MM-dd HH:mm:ss", getTestStartTime(), getTestEndTime());
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

   /* @Override
    public List<IReportData> getReportDataList() {
        return null;
    }*/

    @Override
    public String toString() {
        return "TestSuiteData{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", totalTest=" + totalTest +
                ", totalPassed=" + totalPassed +
                ", totalFailed=" + totalFailed +
                ", totalSkipped=" + totalSkipped +
                ", totalPassedRate=" + totalPassedRate +
                ", testStartTime='" + testStartTime + '\'' +
                ", testEndTime='" + testEndTime + '\'' +
                ", totalDuration='" + totalDuration + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
