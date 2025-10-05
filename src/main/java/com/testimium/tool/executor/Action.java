package com.testimium.tool.executor;

import com.testimium.tool.exception.HandleFailOverTestExecution;
import com.testimium.tool.exception.RecoverBrokenTestExecutionException;
import com.testimium.tool.exception.ShutdownTestExecution;
import com.testimium.tool.exception.TestException;

/**
 *  Allow Actions
 * @author Sandeep Agrawal
 */
@FunctionalInterface
public interface Action<T, R> {
    /**
     * Perform action
     * @param t input param
     * @return object R
     * @throws ShutdownTestExecution If any unwanted exception
     * @throws HandleFailOverTestExecution If not able to handle fail over
     * @throws TestException If not able to execute test
     * @throws RecoverBrokenTestExecutionException If not able to recover test
     */
    R perform(T t) throws ShutdownTestExecution, HandleFailOverTestExecution, TestException, RecoverBrokenTestExecutionException;
}
