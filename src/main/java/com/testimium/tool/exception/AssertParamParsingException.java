package com.testimium.tool.exception;

import com.testimium.tool.context.TestContext;

/**
 * @author Sandeep Agrawal
 */
public class AssertParamParsingException extends Exception {

    public AssertParamParsingException() {
        super(String.format("For given command: " + TestContext.getTestContext("").getCommand() +
                "Not able to parse assert parameter: " + TestContext.getTestContext("").getTestExpectedJson()));
    }

    public AssertParamParsingException(Throwable cause) {
        super(String.format("For given command: " + TestContext.getTestContext("").getCommand() +
                "Not able to parse assert parameter: " + TestContext.getTestContext("").getTestExpectedJson()), cause);
    }
}
