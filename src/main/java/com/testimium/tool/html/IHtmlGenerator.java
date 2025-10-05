package com.testimium.tool.html;

import com.testimium.tool.report.IReportData;

import java.io.IOException;

public interface IHtmlGenerator {

    void setData(IReportData data);
    IReportData getData();
    void generateHtml() throws IOException;
}
