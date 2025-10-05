package com.testimium.tool.exception;

/**
 * @author Sandeep Agrawal
 */
public class ConditionNotSuportedException extends Exception {

    public ConditionNotSuportedException(String condition, String supportedCondition) {
        super(String.format("'%s' condition is not supported! Given conditions are supported: \n %s", condition, supportedCondition));
    }
}
