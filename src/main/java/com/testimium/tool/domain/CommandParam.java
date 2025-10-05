package com.testimium.tool.domain;

import com.testimium.tool.context.TestContext;

import java.util.Arrays;

/**
 * @author Sandeep Agrawal
 */
public class CommandParam implements IRequest {
    String testCaseName;
    String command;
    String[] args;
    String  inputParam;
    String assertParam;

    boolean nestedNodeEnabled = false;

    public CommandParam() {
    }

    public CommandParam(String[] args) {
        this.args = args;
    }

    public CommandParam(String testCaseName, String command, String[] args, String inputParam, String assertParam) {
        this.testCaseName = testCaseName;
        this.command = command;
        this.args = args;
        this.inputParam = inputParam;
        this.assertParam = assertParam;
        TestContext.getTestContext("").setTestInputJson(this.inputParam);
        TestContext.getTestContext("").setTestExpectedJson(this.assertParam);
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String getInputParam() {
        return inputParam;
    }

    public String getAssertParam() {
        return assertParam;
    }

    public boolean isNestedNodeEnabled() {
        return nestedNodeEnabled;
    }

    public void setNestedNodeEnabled(boolean nestedNodeEnabled) {
        this.nestedNodeEnabled = nestedNodeEnabled;
    }

    @Override
    public String toString() {
        return "CommandParam{" +
                "testCaseName='" + testCaseName + '\'' +
                ", command='" + command + '\'' +
                ", args=" + Arrays.toString(args) +
                ", inputParam='" + inputParam + '\'' +
                ", assertParam='" + assertParam + '\'' +
                ", addToNodeEnabled=" + nestedNodeEnabled +
                '}';
    }
}
