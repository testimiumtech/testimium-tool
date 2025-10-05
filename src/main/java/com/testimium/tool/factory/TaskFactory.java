package com.testimium.tool.factory;

import com.testimium.tool.action.TaskType;
import com.testimium.tool.exception.TaskNotFoundException;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.schedular.ITaskScheduler;

/**
 * @author Sandeep Agrawal
 *
 */
public class TaskFactory {

    public static synchronized ITaskScheduler getTaskInstance(String taskName) throws TaskNotFoundException {
        synchronized (taskName) {
            ITaskScheduler task = null;
            try {
                task = TaskType.valueOf(TaskType.class, taskName.toUpperCase().trim() + "_TASK").getInstance();
            } catch (Exception ex) {
                LogUtil.logTestCaseErrorMsg(taskName + " Not Found Exception ", ex);
                new TaskNotFoundException(taskName);
            }

            return task;
        }
    }
}
