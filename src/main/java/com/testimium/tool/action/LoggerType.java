package com.testimium.tool.action;

import com.testimium.tool.logging.ILogger;
import com.testimium.tool.logging.custom.TestCaseErrorLogger;
import com.testimium.tool.logging.custom.TestCaseLogger;
import com.testimium.tool.logging.custom.ToolErrorLogger;
import com.testimium.tool.logging.custom.ToolLogger;

import java.util.function.Supplier;

public enum LoggerType {
    //MAIN_LOGGER,
    //SQL_LOGGER,
    TESTCASE_LOGGER (TestCaseLogger::getInstance),
    TESTCASE_ERROR_LOGGER (TestCaseErrorLogger::getInstance),
    TOOL_LOGGER (ToolLogger::getInstance),
    TOOL_ERROR_LOGGER (ToolErrorLogger::getInstance);

    private Supplier<ILogger> readerInstantiator;

    public ILogger getInstance() {
        return readerInstantiator.get();
    }

    LoggerType(Supplier<ILogger> readerInstantiator) {
        this.readerInstantiator = readerInstantiator;
    }
}
