package com.testimium.tool.exception;

import com.testimium.tool.context.TestContext;

/**
 * @author Sandeep Agrawal
 */
public class InputParamParsingException extends Exception {

    public InputParamParsingException() {
        super(String.format("For given command: " + TestContext.getTestContext("").getCommand() +
                "Not able to parse input parameter: " + TestContext.getTestContext("").getTestInputJson()));
    }

    public InputParamParsingException(Throwable cause) {
        super(String.format("For given command: " + TestContext.getTestContext("").getCommand() +
                "Not able to parse input parameter: " + TestContext.getTestContext("").getTestInputJson()), cause);
    }
}
