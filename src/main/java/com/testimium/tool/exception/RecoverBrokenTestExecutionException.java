package com.testimium.tool.exception;

import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.testcase.ExcelTestCase;

/**
 * @author Sandeep Agrawal
 */
public class RecoverBrokenTestExecutionException extends Exception {

	private String message;
	private CommandParam commandParam;
    private ExcelTestCase excelTestCase;
    private boolean isRecoverBrokenTest;
    private String lastExecuteUrl;

    public RecoverBrokenTestExecutionException(String message) {
        super(String.format("Handling the fail over test execution: ", message));
        this.message = message;
    }


    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public RecoverBrokenTestExecutionException(String message, CommandParam commandParam) {
        super(String.format("Handling the fail over test execution: ", message));
        this.message = message;
        this.commandParam = commandParam;
    }

    public RecoverBrokenTestExecutionException(String message, ExcelTestCase excelTestCase, boolean isRecoverBrokenTest, String lastExecuteUrl) {
        super(String.format("Handling the fail over test execution for broken test: ", message));
        this.message = "Starts Healing Broken test \n" + message;
        this.excelTestCase = excelTestCase;
        this.isRecoverBrokenTest = isRecoverBrokenTest;
        this.lastExecuteUrl = lastExecuteUrl;
    }

    public String getMessage() {
        return message;
    }

    public CommandParam getCommandParam() {
        return commandParam;
    }

    public ExcelTestCase getExcelTestCase() {
        return excelTestCase;
    }

    public boolean isRecoverBrokenTest() {
        return isRecoverBrokenTest;
    }

    public String getLastExecuteUrl() {
        return lastExecuteUrl;
    }
}
