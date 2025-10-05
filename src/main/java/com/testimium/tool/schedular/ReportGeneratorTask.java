package com.testimium.tool.schedular;

import com.testimium.tool.report.generator.ReportGenerator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReportGeneratorTask implements ITaskScheduler{

    private static ReportGeneratorTask reportGeneratorScheduler = null;
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public static synchronized ITaskScheduler getInstance() {
        if(null == reportGeneratorScheduler) {
            reportGeneratorScheduler = new ReportGeneratorTask();
        }
        return reportGeneratorScheduler;
    }

    @Override
    public void execute(Object obj) {
        try {
            Runnable task = () -> {
                System.out.println("Updating Report......");
                System.out.println("Updating Dashboard Report......");
                ReportGenerator.getReportInstance().regenerateReport(false);
            };

            //scheduler.schedule(task, 5, TimeUnit.MINUTES);
            scheduler.scheduleAtFixedRate(task, 1, 1, TimeUnit.MINUTES);
            //scheduler.awaitTermination(20, TimeUnit.MINUTES); // wait enough time
        } catch (Exception e) {
            e.printStackTrace();
            if(null != scheduler){
                scheduler.shutdownNow();
            }
        }
    }

    public void shutdown(){
        ReportGenerator.getReportInstance().endReport();
        scheduler.shutdownNow();
        boolean tasksFinished = false;
        try {
            tasksFinished = scheduler.awaitTermination(15, TimeUnit.SECONDS);

            if (tasksFinished) {
                System.out.println("All tasks completed within the timeout.");
            } else {
                System.out.println("Timeout occurred before all tasks finished.");
            }
        } catch (InterruptedException e) {
            System.out.println("Await termination interrupted.");
            Thread.currentThread().interrupt();
        }
    }

}
