package com.testimium.tool.parser;

import com.aventstack.extentreports.Status;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.exception.FileReaderException;
import com.testimium.tool.logging.LogUtil;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;

public class DefaultSQLParser implements IDataParser<File, String> {


    @Override
    public String parse(File file) throws FileReaderException {
        System.out.println("Start reading text file.....");
        StringBuilder result = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line == null || line.isEmpty())
                    continue;
                //System.out.println(line);
                result.append(line).append(System.lineSeparator());

            }

            if(result.length() == 0)
                throw new FileReaderException("SQL statement not found:- '%s'", file.getAbsolutePath());

            System.out.println("End reading text file.....");

        } catch (AccessDeniedException ex) {
            throw new FileReaderException("Please provide the valid file name:- '%s'", file.getAbsolutePath());
        } catch (NoSuchFileException ex) {
            throw new FileReaderException("File not found in given location:- '%s'", file.getAbsolutePath());
        }  catch (IOException ex) {
            ex.printStackTrace();
            throw new FileReaderException(ex.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return result.toString();
    }

    /**
     * TODO with sqlcmd cmd command
     * @param file
     * @return
     * @throws FileReaderException
     */
    public Boolean parseExample(File file) throws FileReaderException {
        BufferedReader input = null;
        Boolean isSuccess = Boolean.FALSE;
        try {
            //String[] fileExt = FileUtility.getSeparatedFileName(file.getAbsolutePath());
           /* Process p = Runtime.getRuntime().exec
                    ("psql -U username -d dbname -h serverhost -f scripfile.sql");*/
            /*Process p = Runtime.getRuntime().exec
                    ("sqlcmd -E -S " + PropertyUtility.getMainDBHostAddress() + " -v database=" + PropertyUtility.getMainDBServiceName() + " -e -i " + file.getAbsolutePath());*/
            ProcessBuilder processBuilder = new ProcessBuilder("cmd","sqlcmd -S localhost -v database=testimium -e -i " + file.getAbsolutePath());
            Process p = processBuilder.start();
            int exitCode = p.waitFor();
            String line;
            if(exitCode == 0) {
                input = new BufferedReader (new InputStreamReader(p.getInputStream()));
                while ((line = input.readLine()) != null) {
                    TestContext.getTestContext("").getTest()
                            .log(Status.INFO, "<br><font color='green'> " + line + "</font>");
                    LogUtil.logTestCaseMsg(line);
                }
                isSuccess = Boolean.TRUE;
            } else {
                input =new BufferedReader(new InputStreamReader(p.getErrorStream()));
                while ((line = input.readLine()) != null) {
                    TestContext.getTestContext("").getTest()
                            .log(Status.ERROR, "<br><font color='green'> " + line + "</font>");
                    LogUtil.logTestCaseErrorMsg(line, null);
                }
            }

            input.close();
        }
        catch (Exception err) {
            LogUtil.logTestCaseErrorMsg("Failed to execute SQl file " + err.getMessage(), err);
            throw new FileReaderException("Failed to execute SQl file " + err.getMessage(), err);
        }

        return isSuccess;
    }

}
