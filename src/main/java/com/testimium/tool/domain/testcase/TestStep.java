package com.testimium.tool.domain.testcase;

import java.util.Arrays;

/**
 * @author Sandeep Agrawal
 */
public class TestStep {
    private String fullStep;
    private String operation;
    private String[] params;

    public TestStep(){}

    public String getFullStep() {
        return fullStep;
    }

    public void setFullStep(String fullStep) {
        this.fullStep = fullStep;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "TestStep{" +
                "fullStep='" + fullStep + '\'' +
                ", operation='" + operation + '\'' +
                ", params=" + ((null == params) ? null : Arrays.toString(params)) +
                '}';
    }

    public String logCommand() {
        return operation + " " + ((null == params) ? null : Arrays.toString(params));
    }
}
