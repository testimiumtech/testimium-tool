package com.testimium.tool.logging;

import com.testimium.tool.action.LoggerType;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.reader.PropertyReader;
import com.testimium.tool.utility.PropertyUtility;

import java.io.File;

public class LogUtil {

	/*public static void info(String message){
		LogFactory.getFactory().getLogger(false,
				TestContext.getTestContext("").getTestCaseName(),
				TestContext.getTestContext("").getTestCaseName(),
				TestContext.getTestContext("").getTestSuiteName() + "-TestSuite").info(message);
	}

	public static void error(String message, Throwable ex){
		LogFactory.getFactory().getLogger(true,
				TestContext.getTestContext("").getTestCaseName(),
				TestContext.getTestContext("").getTestCaseName(),
				TestContext.getTestContext("").getTestSuiteName() + "-TestSuite").error(message, ex);
	}*/

	public static void logToolMsg(String message){
		LogFactory.getLogger(LoggerType.TOOL_LOGGER).info(message);
	}

	public static void logToolErrorMsg(String message, Throwable ex){
		LogFactory.getLogger(LoggerType.TOOL_ERROR_LOGGER).error(message, ex);
	}

	public static void logTestCaseMsg(String message){
		LogFactory.getLogger(LoggerType.TESTCASE_LOGGER).info(message);
	}

	public static void logTestCaseErrorMsg(String message, Throwable ex){
		LogFactory.getLogger(LoggerType.TESTCASE_ERROR_LOGGER).error(message, ex);
	}

	public static void removeLogTestCase(){
		LogFactory.getLogger(LoggerType.TESTCASE_LOGGER).removeAppander();
	}

	public static void removeAllAppenders() {
		LogFactory.getLogger(LoggerType.TOOL_LOGGER).removeAllAppender();
		LogFactory.getLogger(LoggerType.TOOL_ERROR_LOGGER).removeAllAppender();
		LogFactory.getLogger(LoggerType.TESTCASE_LOGGER).removeAllAppender();
		LogFactory.getLogger(LoggerType.TESTCASE_ERROR_LOGGER).removeAllAppender();
	}

	public static String getCurrentErrorLogFilePath() {
		return PropertyUtility.getLoggerPath() + File.separator
				+ PropertyReader.getProperty("tool.active.profile") + File.separator
				+ TestContext.getTestContext("").getTestSuiteName() + File.separator
				+ "Error" + File.separator
				+ TestContext.getTestContext("").getTestCaseName() + "-error.log";
	}

	public static String getCurrentLogFilePath() {
		return PropertyUtility.getLoggerPath() + File.separator
				+ PropertyReader.getProperty("tool.active.profile") + File.separator
				+ TestContext.getTestContext("").getTestSuiteName() + File.separator
				+ TestContext.getTestContext("").getTestCaseName() + ".log";
	}

}