package com.testimium.tool.command;

import com.aventstack.extentreports.Status;
import com.testimium.tool.base.DriverManager;
import com.testimium.tool.datasource.connector.register.DataSourceRegistry;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.reader.PropertyReader;
import com.testimium.tool.report.generator.ReportGenerator;
import com.testimium.tool.utility.FileUtility;
import com.testimium.tool.utility.OSValidator;
import com.testimium.tool.utility.PropertyUtility;
import org.openqa.selenium.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Sandeep Agrawal
 */
public class ExecuteBatchScriptCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static ExecuteBatchScriptCmd batchScriptCmd = new ExecuteBatchScriptCmd();

    public ExecuteBatchScriptCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static ExecuteBatchScriptCmd getInstance() {
        //TODO Fix Me for concurrent access
        return batchScriptCmd;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException {
        InputStream is = null;
        FileOutputStream fop = null;
        try {
            if(null == param.getArgs() || param.getArgs().length < 1)
                throw new CommandException("Command argument is missing: missing batch file name. ");

            File logPath = new File(PropertyUtility.getLoggerPath() +  File.separator + PropertyReader.getProperty("tool.active.profile"));
            if (!logPath.exists()){
                logPath.mkdirs();
            }


            DateFormat df = new SimpleDateFormat("dd-MM-yyyy.HH.mm.ss");
            Calendar calobj = Calendar.getInstance();

            String batchFileName = FileUtility.getAbsolutePath(PropertyUtility.getExternalDirPath()) + "/" + ((param.getArgs().length == 1) ? param.getArgs()[0] : param.getArgs()[1]);
            if(!OSValidator.isWindows()) {
                batchFileName = batchFileName.replaceAll("\\W*\\.+(bat)\\W*", ".sh");
                Process exeProcess  = Runtime.getRuntime().exec("sh -c sudo chmod -R 777 " + batchFileName);
                String processInfo = exeProcess.info().toString();
                ReportGenerator.getReportInstance().getChildNodeLevel1()
                        .log(Status.INFO, "Start batch script " + batchFileName + "<br> " + processInfo);
                printShFailedResult(batchFileName, exeProcess);
            } else {
                ReportGenerator.getReportInstance().getChildNodeLevel1()
                        .log(Status.INFO, "Start batch script " + batchFileName);
            }

            ProcessBuilder processBuilder = new ProcessBuilder(
                    batchFileName,
                    FileUtility.getAbsolutePath(PropertyUtility.getExternalDirPath()),
                    logPath.getAbsolutePath(),
                    df.format(calobj.getTime()));
            Process processResult = processBuilder.start();

            int exitcode = processResult.waitFor();
            System.out.println("Exit code: " + exitcode);
            if (exitcode == 0) {
                ReportGenerator.getReportInstance().getChildNodeLevel1()
                        .log(Status.INFO,"Batch processing Success!");
            } else {
                printShFailedResult(batchFileName, processResult);
            }

            is = processResult.getInputStream();
            /*InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);*/
            fop = new FileOutputStream(new File(logPath.getAbsolutePath() +  File.separator + "batchScript" + df.format(calobj.getTime()) + ".log"));
            int readIndex = 0;
            while( (readIndex = is.read() ) != -1) {
                //System.out.print((char)i);
                fop.write((char)readIndex);
            }
            int exitStatus = processResult.waitFor();
            if(exitStatus != 0) {
                ReportGenerator.getReportInstance().getChildNodeLevel1()
                        .log(Status.FAIL,"Batch execution is not successful for command: ' exitStatus=" + exitStatus + " "
                                + param.getCommand() + " with batch file '" + ((param.getArgs().length == 1) ? param.getArgs()[0] : param.getArgs()[1]) + "'. " +
                                "Please see the generated log for detail: " + logPath.getAbsolutePath() + "/batchScript" + df.format(calobj.getTime()) + ".log");
                throw new CommandException("Batch execution is not successful for command: ' exitStatus=" + exitStatus + " "
                        + param.getCommand() + " with batch file '" + ((param.getArgs().length == 1) ? param.getArgs()[0] : param.getArgs()[1]) + "'. " +
                        "Please see the generated log for detail: " + logPath.getAbsolutePath() + "/batchScript" + df.format(calobj.getTime()) + ".log");
            }

            /*fop.close();
            is.close();*/
            ReportGenerator.getReportInstance().getChildNodeLevel1()
                    .log(Status.INFO,"Finished processing batch file: " + FileUtility.getAbsolutePath(PropertyUtility.getExternalDirPath()) +  File.separator
                    + ((param.getArgs().length == 1) ? param.getArgs()[0] : param.getArgs()[1]));

            if(param.getArgs().length == 2 && "-reloadDatabase".equalsIgnoreCase(param.getArgs()[0])) {
                ReportGenerator.getReportInstance().getChildNodeLevel1()
                        .log(Status.INFO,"Reload database connection");
                DataSourceRegistry.reBuildConnectorMap();
            }

        } catch (Exception ex) {
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + ((param.getArgs().length == 1) ? param.getArgs()[0] : param.getArgs()[1]) + "'. "+ ex.getMessage(), ex);
        } finally {
            try {
                fop.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new CommandResponse("(Success)", true);
    }

    private void printShFailedResult(String batchFileName, Process processResult) throws IOException {
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(processResult.getInputStream()));
        String output;
        StringBuilder msg = new StringBuilder();
        while ((output = stdInput.readLine()) != null)
        {
            msg.append(output + " ");
        }
        processResult.destroyForcibly();
        ReportGenerator.getReportInstance().getChildNodeLevel1()
                .log(Status.INFO,"Batch processing failed: " + batchFileName + "<br> " + msg.toString());

    }
}


