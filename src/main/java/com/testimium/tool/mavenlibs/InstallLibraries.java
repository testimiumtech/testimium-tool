package com.testimium.tool.mavenlibs;

import com.testimium.tool.logging.LogUtil;

import java.io.IOException;

public class InstallLibraries implements IMaven {
    @Override
    public void execute(String cmdStatement) {
        LogUtil.logToolMsg("Started Loading Libraries");
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(cmdStatement);
        Process processResult = null;
        try {
            processResult = processBuilder.start();
            int exitcode = processResult.waitFor();
            LogUtil.logToolMsg("END Loading Libraries");
        } catch (IOException ex) {
            LogUtil.logToolErrorMsg("IOException while load libraries: ", ex);
        } catch (InterruptedException ex) {
            LogUtil.logToolErrorMsg("InterruptedException while load libraries: ", ex);
        }
    }
}
