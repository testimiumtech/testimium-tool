package com.testimium.tool.utility;

import com.testimium.tool.reader.PropertyReader;
import org.apache.commons.lang3.StringUtils;

public class PropertyUtility {

    public static String getWebUrl() {
        return PropertyReader.getProperty("web.app.url");
    }

    public static boolean isDBConnectionEnabled() {
        return "true".equals(PropertyReader.getProperty("enable.datasource.connection").toLowerCase()) ? true:false;
    }

    public static String getMainDBServiceName() {
        return PropertyReader.getProperty("main.datasource.service.name");
    }

    public static String getMainDBConnectorName() {
        return PropertyReader.getProperty("main.datasource.connector.name");
    }

    public static String getLoggerPath() {
        String logsDir = StringUtils.isEmpty(PropertyReader.getProperty("test.logger.path")) ? "./logs" : PropertyReader.getProperty("test.logger.path");
        FileUtility.createDirectory(logsDir);

        return logsDir;
    }

    public static String getTestCasesPath() {
        return PropertyReader.getProperty("testcases.path");
    }

    public static String getExternalDirPath() {
        return PropertyReader.getProperty("external.files.directory.path");
    }

    public static boolean isMultiTestsuiteExecEnabled() {
        return "true".equals(PropertyReader.getProperty("enable.multi.testsuite.execution").toLowerCase()) ? true:false;
    }

    public static boolean isAllTestExecEnabled() {
        return "true".equals(PropertyReader.getProperty("enable.all.testcase.execution").toLowerCase()) ? true:false;
    }

    public static boolean isFailOverExecEnabled() {
        return "true".equals(PropertyReader.getProperty("enable.fail.over.execution").toLowerCase()) ? true:false;
    }

    public static String getTestsuiteFileName() {
        return PropertyReader.getProperty("testsuite.file.name");
    }

    public static String getTestsuiteSheetName() {
        return PropertyReader.getProperty("testsuite.sheet.name");
    }

    public static String getStartTestsuiteName() {
        return PropertyReader.getProperty("start.with.testsuite");
    }

    public static String getEndTestsuiteName() {
        return PropertyReader.getProperty("end.with.testsuite");
    }

    public static String getTestType() {
        return PropertyReader.getProperty("perform.test.type");
    }

    public static String getTemplateLocation()  {
        return "templates";
    }

    public static String getReportLocation()  {
        return "./Reports/" + getToolProfile();
    }

    public static String getScreenshotLocation()  {
        FileUtility.createDirectory(getReportLocation()+"/screenshot");
        return getReportLocation()+"/screenshot";
    }

    public static String getDefaultDownloadPath(){
        String dPath = FileUtility.getPathByReplaceWithProperty(PropertyReader.getProperty("default.download.path"));
        FileUtility.createDirectory(dPath);
        return dPath;
    }

    public static String getWebBrowser() {
        return StringUtils.isEmpty(PropertyReader.getProperty("web.browser.driver"))? "CHROME": PropertyReader.getProperty("web.browser.driver");
    }

    public static String getBrowserTimezone() {
        return PropertyReader.getProperty("tool.browser.timezone");
    }

    public static String getLocalTimezone() {
        return PropertyReader.getProperty("tool.local.timezone");
    }

    public static String getLocatorLoadingTimeout() {
        return PropertyReader.getProperty("locator.loading.timeout");
    }

    public static String getChromeBrowserExePath() {
        return PropertyReader.getProperty("chrome.browser.exe.path");
    }

    public static String getToolProfile() {
        return PropertyReader.getProperty("tool.active.profile");
    }

    public static boolean isSikuliExecutionEnabled() {
        return "true".equals(PropertyReader.getProperty("enable.sikuli.api.execution").toLowerCase()) ? true:false;
    }
}
