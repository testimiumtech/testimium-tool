package com.testimium.tool.action;

import com.testimium.tool.schedular.ITaskScheduler;
import com.testimium.tool.schedular.ReportGeneratorTask;

import java.util.function.Supplier;

public enum TaskType {

    REPORT_GENERATOR_TASK (ReportGeneratorTask::getInstance);

    private Supplier<ITaskScheduler> readerInstantiator;

    public ITaskScheduler getInstance() {
        return readerInstantiator.get();
    }

    TaskType(Supplier<ITaskScheduler> readerInstantiator) {
        this.readerInstantiator = readerInstantiator;
    }
}
