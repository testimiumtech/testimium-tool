package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.domain.TestJsonInputData;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.exception.VerificationException;
import com.testimium.tool.utility.FileUtility;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class CopyFileCmd implements ExternalCommand<CommandParam, CommandResponse> {

    private WebDriver driver;
    private static CopyFileCmd CopyFileCmd = new CopyFileCmd();

    public CopyFileCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static CopyFileCmd getInstance() {
        //TODO Fix Me for concurrent access
        return CopyFileCmd;
    }

    //CopyFile       //InputParam must be defined
    //CopyFile -files
    //CopyFile -from <path json key> -to <path json key>
    //Used to copy all the files under specific folder
    //CopyFile -files -from <directory path json key> -to <directory path json key>
    //key value may contain ${<property key>}

    /*Here is the new command called CopyFile implemented, you can use below syntax :

            1. CopyFile

    Input Param:
    {
        "script": {
        "copyFrom": "C:/testimium/printing/user1/Receipt.pdf",
                "copyTo": "C:/testimium1/printing/sandy1/Receipt.pdf"
    }
    }


  2. CopyFile -files

    Input Param:

    {
        "script": {
        "copyFrom": "testfiles/sample/",
                "copyTo": "C:/testimium1/sample"
    }
    }


  3. CopyFile -from copyFrom1 -to copyTo1

    Input Param:

    {
        "script": {
        "copyFrom": "C:\\testimium\\printing\\user1\\Receipt.pdf",
                "copyTo": "C:\\testimium\\printing\\sandy1\\Receipt.pdf",
                "copyFom1": "sample/insertSQLFile.txt",
                "copyTo1": "sample/insertSQLFile.sql.txt"
    }
    }


  4. Used to copy all the files from specific directory to given directory
    CopyFile -files -from <directory path json key> -to <directory path json key>*/
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, VerificationException {

        try {
            if (null != param.getArgs() && param.getArgs().length > 4)
                throw new CommandException("Argument" + param.getArgs()[1] + "not required for command " + param.getCommand());

            TestJsonInputData inputParam = getTestJsonInputData(param);
            if(null == inputParam || null == inputParam.getScript() || inputParam.getScript().size() <= 0)
                throw new CommandException("System not able to find the From and To path for file/directory as input param");

            String copyFrom = null;
            String copyTo = null;
            boolean isFiles = false;
            if(null != param.getArgs()  && param.getArgs().length > 0) {
                for (int itr = 0; itr < param.getArgs().length; itr++) {
                    switch (param.getArgs()[itr]) {
                        case "-files":
                            isFiles = true;
                            copyFiles(param, inputParam);
                            break;
                        case "-from":
                            itr += 1;
                            copyFrom =  null != inputParam.getScript().get(param.getArgs()[itr]) ? FileUtility.getPathByReplaceWithProperty(String.valueOf(inputParam.getScript().get(param.getArgs()[itr]))) : "";
                            break;
                        case "-to":
                            itr += 1;
                            copyTo = null != inputParam.getScript().get(param.getArgs()[itr]) ? FileUtility.getPathByReplaceWithProperty(String.valueOf(inputParam.getScript().get(param.getArgs()[itr]))) : "";
                            break;
                    }
                }
            }



            if(!isFiles) {
                if( null == param.getArgs() || param.getArgs().length == 0) {
                    copyFrom =  null != inputParam.getScript().get("copyFrom") ? FileUtility.getPathByReplaceWithProperty(String.valueOf(inputParam.getScript().get("copyFrom"))) : "";
                    copyTo = null != inputParam.getScript().get("copyTo") ? FileUtility.getPathByReplaceWithProperty(String.valueOf(inputParam.getScript().get("copyTo"))) : "";
                }

                if (copyFrom.isEmpty() || copyTo.isEmpty())
                    throw new CommandException("System not able to find the either/both From/To path for file/directory as input param");


                FileUtility.copyFile(new File(FileUtility.getAbsolutePath(copyFrom)), new File(copyTo));
            }


        } catch (Exception ex) {
            throw new CommandException("Failed to execute command "
                    + param.getCommand() + ": "+ ex.getMessage(), ex);
        }
        return new CommandResponse("(Success)", true);
    }


/*    public static void copyFile(String from, String to) throws IOException{
        Path src = Paths.get(from);
        Path dest = Paths.get(to);
        Files.copy(src.toFile(), dest.toFile());
    }*/

    private void copyFiles(CommandParam param, TestJsonInputData inputParam) throws IOException, CommandException {
        String copyFrom[] = null;
        String copyTo = "";

        for (int itr = 0; itr < param.getArgs().length; itr++) {
            switch (param.getArgs()[itr]) {
                case "-files":
                    break;
                case "-from":
                    itr += 1;
                    copyFrom =  null != inputParam.getScript().get(param.getArgs()[itr]) ? FileUtility.getAllFiles(FileUtility.getPathByReplaceWithProperty(String.valueOf(inputParam.getScript().get(param.getArgs()[itr])))) : new String[]{""};
                    break;
                case "-to":
                    itr += 1;
                    copyTo = null != inputParam.getScript().get(param.getArgs()[itr]) ? FileUtility.getPathByReplaceWithProperty(String.valueOf(inputParam.getScript().get(param.getArgs()[itr]))) : "";
                    break;
            }
        }

        if(param.getArgs().length == 1) {
            copyFrom =  null != inputParam.getScript().get("copyFrom") ? FileUtility.getAllFiles(FileUtility.getPathByReplaceWithProperty(String.valueOf(inputParam.getScript().get("copyFrom")))) : new String[]{""};
            copyTo = null != inputParam.getScript().get("copyTo") ? FileUtility.getPathByReplaceWithProperty(String.valueOf(inputParam.getScript().get("copyTo"))) : "";
        }

        if(null == copyFrom || (copyFrom.length == 1 && copyFrom[0].isEmpty()) || copyTo.isEmpty()) {
            throw new CommandException("copyFrom path is not provided");
        }

        for (int itr = 0; itr < copyFrom.length; itr++) {
            File file = new File(FileUtility.getAbsolutePath(copyFrom[itr]));
            String fileName = file.getName();
            FileUtility.copyFile(file, new File(copyTo + "/" + fileName));
        }
    }


}
