package com.testimium.tool.report;

public interface IReportData {

    String getName();


    String getLocation();


    int getTotalTest() ;


    int getTotalPassed();


    int getTotalFailed();


    int getTotalSkipped();


    int getTotalPassedRate();


    String getTestStartTime();


    String getTestEndTime();

    String getTotalDuration();

    /*@JsonIgnore
    List<Tes> getReportDataList();*/

}
