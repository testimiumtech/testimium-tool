package com.testimium.tool.context;

import com.aventstack.extentreports.ExtentTest;
import com.testimium.tool.datasource.connector.dto.ConnectorKey;
import com.testimium.tool.datasource.connector.factory.DBConnector;
import com.testimium.tool.domain.AssertParameter;
import com.testimium.tool.exception.JsonParsingException;
import com.testimium.tool.report.generator.ReportGenerator;
import com.testimium.tool.utility.JsonParserUtility;
import com.testimium.tool.utility.PropertyUtility;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author Sandeep Agrawal
 */
public class TestContext {

    private static TestContext testContext = new TestContext();
    private Map<String, Object> testInputMap = new HashMap<>();
    private String testInputJson;
    private String testExpectedJson;
    private ExtentTest test;
    private String currentModule;
    private boolean isTestStepFailed;
    private boolean isHandleFailOver;
    private Map<String, Object> failOver = new HashMap<>();
    private DBConnector mainDBConnector;
    private ConnectorKey mainConnectorKey;
    private List<String> testTypes;
    private Map<String, Object> copiedVariable = new HashMap<>();
    private Map<String, Object> globalVariable = new HashMap<>();

    //Hold command name
    private String command;
    private String testSuiteName;
    private String testCaseName;
    private String assigneCatogary;

    private ReportGenerator reportGenerator;
   //private List<String> executeTestCmdHierarchy = new ArrayList<>();

    private StringBuilder testStepErrorLog;
    private String currentUrl;

    public static TestContext getTestContext(String activity) {
        if(null == testContext)
            testContext = new TestContext();
        return testContext;
    }

    public void setTestInputMap(Map<String, Object> testInputMap) {
        this.testInputMap = testInputMap;
    }

    public Map<String, Object> getTestInputMap() {
        return testInputMap;
    }

    public String getTestInputJson() {
        return testInputJson;
    }

    public void setTestInputJson(String testInputJson) {
        this.testInputJson = testInputJson;
    }

    public String getTestExpectedJson() {
        return testExpectedJson;
    }

    public void setTestExpectedJson(String testExpectedJson) {
        this.testExpectedJson = testExpectedJson;
    }

    public ExtentTest getTest() {
        return ReportGenerator.getReportInstance().getCurrentTest();
    }

    public void setTest(ExtentTest test) {
        this.test = test;
    }

    public String getCurrentModule() {
        return currentModule;
    }

    public void setCurrentModule(String currentModule) {
        this.currentModule = currentModule;
    }

    public Map<String, Object> getFailOver() {
        return failOver;
    }

    public void setFailOver(Map<String, Object> failOver) {
        this.failOver = failOver;
    }

    public void clearInputMap(){
        testInputMap.clear();
    }

    public boolean isTestStepFailed() {
        return isTestStepFailed;
    }

    public void setTestStepFailed(boolean testStepFailed) {
        isTestStepFailed = testStepFailed;
    }

    public boolean isHandleFailOver() {
        return isHandleFailOver;
    }

    public void setHandleFailOver(boolean handleFailOver) {
        isHandleFailOver = handleFailOver;
    }

    public DBConnector getMainDBConnector() {
        return mainDBConnector;
    }

    public void setMainDBConnector(DBConnector mainDBConnector) {
        this.mainDBConnector = mainDBConnector;
    }

    public ConnectorKey getMainConnectorKey() {
        return mainConnectorKey;
    }

    public void setMainConnectorKey() {
        ConnectorKey mainDSConnector = new ConnectorKey();
        mainDSConnector.setServiceName(PropertyUtility.getMainDBServiceName());
        mainDSConnector.setConnectorName(PropertyUtility.getMainDBConnectorName());
        this.mainConnectorKey = mainDSConnector;
    }

    public List<String> getTestTypes() {
        return this.testTypes;
    }

    public void setTestTypes(String testTypes) {
        if(StringUtils.isNotEmpty(testTypes))
            this.testTypes = Arrays.asList(testTypes);
        else {

        }
    }

    /*public static TestContext getTestContext() {
        return testContext;
    }

    public static void setTestContext(TestContext testContext) {
        TestContext.testContext = testContext;
    }*/

    public Map<String, Object> getCopiedVariable() {
        return copiedVariable;
    }

    public void setCopiedVariable(Map<String, Object> copiedVariable) {
        this.copiedVariable = copiedVariable;
    }

    public Map<String, Object> getGlobalVariable() {
        return globalVariable;
    }

    public void setGlobalVariable(String key, Object value) {
        this.globalVariable.put(key, value);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getTestSuiteName() {
        return testSuiteName;
    }

    public void setTestSuiteName(String testSuiteName) {
        this.testSuiteName = testSuiteName;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public String getAssigneCatogary() {
        return assigneCatogary;
    }

    public void setAssigneCatogary(String assigneCatogary) {
        this.assigneCatogary = assigneCatogary;
    }

    /**
     *
     * @return
     * @throws JsonParsingException
     */
    public AssertParameter getAssertParameter() throws JsonParsingException {
        return new JsonParserUtility<AssertParameter>()
                .parse(getTestExpectedJson(), AssertParameter.class);
    }

    public ReportGenerator getReportGenerator() {
        return reportGenerator;
    }

    public void setReportGenerator(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    /*public List<String> getExecuteTestCmdHierarchy() {
        return executeTestCmdHierarchy;
    }

    public void setExecuteTestCmdHierarchy(List<String> executeTestCmdHierarchy) {
        this.executeTestCmdHierarchy = executeTestCmdHierarchy;
    }*/

    public StringBuilder getTestStepErrorLog() {
        return testStepErrorLog;
    }

    public void setTestStepErrorLog(StringBuilder testStepErrorLog) {
        this.testStepErrorLog = testStepErrorLog;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }
}
