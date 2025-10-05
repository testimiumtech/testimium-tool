package com.testimium.tool.logging;

/**
 * @author sandeep
 *
 */
public class LogFactoryImpl extends LogFactory{
	
	//private Map<String, TestCaseLogger> loggerMap = new HashMap<String, TestCaseLogger>();

	/*public ILogger getLogger(boolean isErrorLog, String appenderName, String loggerName, String rootDir) {
		if(!loggerMap.containsKey(loggerName)) {
			loggerMap.put(loggerName,
					new TestCaseLogger(isErrorLog, appenderName, loggerName, rootDir));
		}
		return loggerMap.get(loggerName);
	}

	public ILogger getMainLogger(boolean isErrorLog, String appenderName, String loggerName, String rootDir) {
		if(!loggerMap.containsKey(loggerName)) {
			loggerMap.put(loggerName,
					new TestCaseLogger(isErrorLog, appenderName, loggerName, rootDir));
		}
		return loggerMap.get(loggerName);
	}*/
}
