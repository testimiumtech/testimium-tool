package com.testimium.tool.domain.testcase;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Sandeep Agrawal
 */
public class ExcelTestCase {

    private String testCaseId;
    private String testCaseName;
    private String description;
    private String testCaseType;
    private boolean isAutomated;
    private String module;
    private String subModule;
    private String dependency;
    private String preCondition;
    private String manualTestStep;
    private String expectedResult;
    private String dbVerification;
    //private String testStep;
    private List<TestStep> testSteps;
    private String inputParam;
    private String assertParam;
    private boolean isDisabled;
    private String status;
    private String comments;
    private boolean isFailOverTest;

    public ExcelTestCase() {
    }

    /**
     *
     * @param colValue
     */
    public ExcelTestCase(Object[] colValue) {
        this.setTestCaseId(String.valueOf(colValue[0]));
        this.setTestCaseName(String.valueOf(colValue[1]));
        this.setDescription(String.valueOf(colValue[2]));
        this.setTestCaseType(String.valueOf(colValue[3]));
        this.setAutomated(String.valueOf(colValue[4]));
        this.setModule(String.valueOf(colValue[5]));
        this.setSubModule(String.valueOf(colValue[6]));
        this.setDependency(String.valueOf(colValue[7]));
        this.setPreCondition(String.valueOf(colValue[8]));
        this.setManualTestStep(String.valueOf(colValue[9]));
        this.setExpectedResult(String.valueOf(colValue[10]));
        this.setDbVerification(String.valueOf(colValue[11]));
        //TestStep testStep = new TestStep(String.valueOf(colValue[5]));
        //this.setTestSteps(this.getTestSteps());
        if(this.isAutomated)
            this.addTestSteps(String.valueOf(colValue[12]));

        this.setInputParam(String.valueOf(colValue[13]));
        this.setAssertParam(String.valueOf(colValue[14]));
        this.setDisabled(String.valueOf(colValue[15]));
        this.setStatus(String.valueOf(colValue[16]));
        this.setComments(String.valueOf(colValue[17]));
    }

    /**
     *
     * @param testStepStr
     */
    /*public ExcelTestCase(String testStepStr) {
        this.process(testStepStr);
    }*/

    private void addTestSteps(String testStepStr){
        StringBuilder builder = null;
        String[] steps = testStepStr.split("\n");
        if(StringUtils.isNotEmpty(testStepStr)) {
            builder = new StringBuilder();
            this.testSteps = new ArrayList<>();
            boolean isLoopBlockStart = false;
            boolean isIfBlockStart = false;
            //boolean isIfBlockEnd = false;
            for (int itr = 0; itr < steps.length; itr++) {
                if(steps[itr].startsWith("#"))
                    continue;
                //IF Block Starts
                if(isIfBlockStart) {
                    if(steps[itr].startsWith("ENDIF")){
                        builder.append(steps[itr].trim());
                        isIfBlockStart = false;
                        steps[itr] = builder.toString();
                        //replace #@# after IF
                        steps[itr] = steps[itr].trim().replaceFirst("#@#", " ");
                        if(isLoopBlockStart) {
                            builder.append("#@##@#");
                            continue;
                        }
                    } else {
                        builder.append(steps[itr].trim().replaceAll("\\s+", "#@#"));
                        builder.append("#@#");
                        continue;
                    }
                } else if(steps[itr].startsWith("IF")){
                    isIfBlockStart = true;
                    builder.append(steps[itr].trim().replaceAll("\\s+", "#@#"));
                    builder.append("#@#");
                    continue;
                }
                //IF Block Ends
                //Loop Block Starts
                if(isLoopBlockStart) {
                    if(steps[itr].startsWith("END-LOOP")){
                        builder.append(steps[itr].trim());
                        isLoopBlockStart = false;
                        steps[itr] = builder.toString();
                        //replace #@# after LOOP
                        steps[itr] = steps[itr].trim().replaceFirst("#@#", " ");
                        //System.out.println(steps[itr]);
                    } else {
                        builder.append(steps[itr].trim().replaceAll("\\s+", "#@#"));
                        //builder.append("#@#");
                        builder.append("#@##@#");
                        continue;
                    }
                } else if(steps[itr].startsWith("LOOP")){
                    isLoopBlockStart = true;
                    builder.append(steps[itr].trim().replaceAll("\\s+", "#@#"));
                    builder.append("#@##@#");
                    continue;
                }
                System.out.println("==final==" + builder.toString());
                //Loop Block Ends
                builder = new StringBuilder();
                //isIfBlockStart = false;
                //isIfBlockEnd = false;
                //End IF
                TestStep testStep = new TestStep();
                //testStep.setFullStep(testStepStr);
                // String[] firstSplit = steps[itr].trim().split("'");
                //TODO change the strategy to take value from command/operation step
                String[] operationStep = steps[itr].trim().split("\\s+");
                testStep.setOperation(operationStep[0]);
                testStep.setFullStep(testStepStr);
                //Arrays.stream(operationStep).filter(indx-> !indx.contains("@")).map(indx-> indx).toArray()
                String[] parms = new String[operationStep.length - 1];
                for (int i = 1; i < operationStep.length; i++) {
                    parms[i - 1] = operationStep[i];
                }
                testStep.setParams(parms);
                testSteps.add(testStep);
            }
        }
    }

    /*private void addTestSteps(String testStepStr){
        String[] steps = testStepStr.split("\n");
        if(StringUtils.isNotEmpty(testStepStr)) {
            this.testSteps = new ArrayList<>();
            for (int itr = 0; itr < steps.length; itr++) {
                TestStep testStep = new TestStep();
                //testStep.setFullStep(testStepStr);
                // String[] firstSplit = steps[itr].trim().split("'");
                //TODO change the strategy to take value from command/operation step
                String[] operationStep = steps[itr].trim().split("\\s+");
                testStep.setOperation(operationStep[0]);
                testStep.setFullStep(testStepStr);
                //Arrays.stream(operationStep).filter(indx-> !indx.contains("@")).map(indx-> indx).toArray()
                String[] parms = new String[operationStep.length - 1];
                for (int i = 1; i < operationStep.length; i++) {
                    parms[i - 1] = operationStep[i];
                }
                testStep.setParams(parms);
                testSteps.add(testStep);
            }
        }
    }*/

    public String getTestCaseId() {
        return testCaseId;
    }

    private void setTestCaseId(String testCaseId) {
        this.testCaseId = testCaseId;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public String getTestCaseType() {
        return testCaseType;
    }

    public void setTestCaseType(String testCaseType) {
        this.testCaseType = testCaseType;
    }

    public boolean isAutomated() {
        return isAutomated;
    }

    public void setAutomated(String automated) {
        isAutomated = false;
        if(automated.equalsIgnoreCase("Yes")){
            isAutomated = true;
        }
    }

    public String getModule() {
        return module;
    }

    private void setModule(String module) {
        this.module = module;
    }

    public String getSubModule() {
        return subModule;
    }

    private void setSubModule(String subModule) {
        this.subModule = subModule;
    }

    public String getDependency() {
        return dependency;
    }

    private void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public String getPreCondition() {
        return preCondition;
    }

    public void setPreCondition(String preCondition) {
        this.preCondition = preCondition.replaceAll("\n", "<br/>");;
    }

    public String getManualTestStep() {
        return manualTestStep;
    }

    public void setManualTestStep(String manualTestStep) {
        this.manualTestStep = manualTestStep.replaceAll("\n", "<br/>");
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult.replaceAll("\n", "<br/>");;
    }

    public String getDbVerification() {
        return dbVerification;
    }

    public void setDbVerification(String dbVerification) {
        this.dbVerification = dbVerification.replaceAll("\n", "<br/>");;
    }

    public List<TestStep> getTestSteps() {
        return testSteps;
    }

    private void setTestSteps(List<TestStep> testSteps) {
        this.testSteps = testSteps;
    }

    public String getInputParam() {
        return inputParam;
    }

    public void setInputParam(String inputParam) {
        this.inputParam = inputParam;
    }

    public String getAssertParam() {
        return assertParam;
    }

    public void setAssertParam(String assertParam) {
        this.assertParam = assertParam;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    private void setDisabled(String disabled) {
        isDisabled = false;
        if(disabled.equalsIgnoreCase("Yes") || disabled.equalsIgnoreCase("Y")){
            isDisabled = true;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments.replaceAll("\n", "<br/>");;
    }

    public boolean isFailOverTest() {
        return isFailOverTest;
    }

    public void setFailOverTest(boolean failOverTest) {
        isFailOverTest = failOverTest;
    }

    @Override
    public String toString() {
        String logFormat ="\n%1$-20s:%2$s";
        StringBuilder logString = new StringBuilder();
        //return "ExcelTestCase{",
        logString.append(String.format(logFormat, "testCaseId", testCaseId));
        logString.append(String.format(logFormat,"testCaseName", testCaseName));
        logString.append(String.format(logFormat,"description", description));
        logString.append(String.format(logFormat,"testCaseType", testCaseType));
        logString.append(String.format(logFormat,"isAutomated=", isAutomated));
        logString.append(String.format(logFormat,"module", module));
        logString.append(String.format(logFormat,"subModule", subModule));
        logString.append(String.format(logFormat,"dependency", dependency));
        logString.append(String.format(logFormat,"preCondition", preCondition));
        logString.append(String.format(logFormat,"manualTestStep", manualTestStep));
        logString.append(String.format(logFormat,"expectedResult", expectedResult));
        logString.append(String.format(logFormat,"dbVerification", dbVerification));
        //logString.append(String.format(logFormat,"testSteps=", testSteps));
        logString.append(String.format(logFormat,"inputParam", inputParam));
        logString.append(String.format(logFormat,"assertParam", assertParam));
        logString.append(String.format(logFormat,"isDisabled=", isDisabled));
        logString.append(String.format(logFormat,"status", status));
        logString.append(String.format(logFormat,"comments", comments));
        logString.append(String.format(logFormat,"isFailOverTest", isFailOverTest));
         //       '}';
        return logString.toString();
    }

    /*
    @Override
    public String toString() {
        return "ExcelTestCase{" +
                "testCaseId='" + testCaseId + '\'' +
                ", testCaseName='" + testCaseName + '\'' +
                ", description='" + description + '\'' +
                ", testCaseType='" + testCaseType + '\'' +
                ", isAutomated=" + isAutomated +
                ", module='" + module + '\'' +
                ", subModule='" + subModule + '\'' +
                ", dependency='" + dependency + '\'' +
                ", preCondition='" + preCondition + '\'' +
                ", manualTestStep='" + manualTestStep + '\'' +
                ", expectedResult='" + expectedResult + '\'' +
                ", dbVerification='" + dbVerification + '\'' +
                ", testSteps=" + testSteps +
                ", inputParam='" + inputParam + '\'' +
                ", assertParam='" + assertParam + '\'' +
                ", isDisabled=" + isDisabled +
                ", status='" + status + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
     */
}
