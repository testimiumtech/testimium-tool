package com.testimium.tool.logging;

import com.testimium.tool.action.LoggerType;
/**
 * @author sandeep
 *
 */
/**
* A factory for creating Log objects.
*/
public abstract class LogFactory {
	/*private Map<String, TestCaseLogger> loggerMap = new HashMap<String, TestCaseLogger>();*/

	public static ILogger getLogger(LoggerType loggerType){
		return loggerType.getInstance();
	}
	
	//public abstract ILogger getLogger(boolean isErrorLog, String appenderName, String loggerName, String rootDir);
		
}