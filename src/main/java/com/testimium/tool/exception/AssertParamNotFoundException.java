package com.testimium.tool.exception;

import com.testimium.tool.context.TestContext;

/**
 * @author Sandeep Agrawal
 */
public class AssertParamNotFoundException extends Exception {

    public AssertParamNotFoundException() {
        super(String.format("Assert parameter is not found for command " + TestContext.getTestContext("").getCommand()));
}
    }
