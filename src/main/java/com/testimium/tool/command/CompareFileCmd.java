package com.testimium.tool.command;

import com.testimium.tool.action.ComparatorType;
import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.domain.ComparatorResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.executor.ActionHelper;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.reader.PropertyReader;
import com.testimium.tool.utility.DateUtility;
import com.testimium.tool.utility.FileUtility;
import com.testimium.tool.utility.PropertyUtility;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sandeep Agrawal
 */

/*  CompareFile command support now to search current/today date in the PDF. No change in command syntax but nee to pass the below assert json with "dateToSearch".

        Command Syntax --->  CompareFile DateSearchSample.pdf verifyExpected-1 PDF SEARCHWORD

        Assert JSON :

        {
            "assertParams": {
                "SearchCurrentDate-key": {
                "wordsToSearch": [""],
                "dateToSearch":[{"dateFormat":"YYYY-MM-dd", "numberOfDays": "0"}]
                }
            }
        }
 */
public class CompareFileCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static CompareFileCmd compareFileCmd = new CompareFileCmd();

    public CompareFileCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static CompareFileCmd getInstance() {
        //TODO Fix Me for concurrent access
        return compareFileCmd;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object

    /**
     * Command accepts 4 parameter:
     * File1, File2, fileType and comparision mode
     * @param param
     * @return
     * @throws CommandException
     */
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException {
        ComparatorResponse compRes = null;
        String dateSuffix = "";
        String assertKey = null;
        String actualAbsolutePath = null;
        String expectedFilePath = null;
        try {
            if (null == param.getArgs() || param.getArgs().length < 4)
                throw new CommandException("Command argument is missing: actualFile, expectedFile, fileType(TEXT, PDF, EXCEL, DOCX, JSON, CSV), compareMode(EXACT or PARTIAL or SEARCH), assertKey(Optional) ");

            //FileUtility.getFile(
            String actualFilePath = "";
            //TODO fix this
            //-pickLatestFile
            String compareType = "EXACT";
            String fileType = "PDF";
            if (!param.getArgs()[0].contains("-pickLatestFile")) {
                fileType  = param.getArgs()[2];
                compareType = param.getArgs()[3].toUpperCase();
                if (!param.getArgs()[0].contains("${") && StringUtils.isNotEmpty(PropertyUtility.getDefaultDownloadPath()))
                    actualFilePath = PropertyUtility.getDefaultDownloadPath() + "/" + param.getArgs()[0];
                else
                    actualFilePath = param.getArgs()[0];

                actualFilePath = ifContainsDollarBracket(param.getArgs()[0], actualFilePath);
                expectedFilePath = param.getArgs()[1];
            } else if (param.getArgs()[0].contains("-pickLatestFile")){
                fileType  = param.getArgs()[3];
                compareType = param.getArgs()[4].toUpperCase();
                actualFilePath = param.getArgs()[1];
                actualFilePath = ifContainsDollarBracket(param.getArgs()[1], actualFilePath);
                actualFilePath = FileUtility.getNewestFileFromDirectory(actualFilePath);
                expectedFilePath = param.getArgs()[2];
            }



            switch (compareType) {
                case "EXACT":
                case "PARTIAL":
                    expectedFilePath = FileUtility.getAbsolutePath(PropertyUtility.getExternalDirPath()) + "/" + expectedFilePath;
            }

            if (expectedFilePath != null && param.getArgs()[1].contains("${")) {
                //String expPath = expectedFilePath;
                int propCount = StringUtils.countMatches(expectedFilePath, "${");
                String prop = null;
                while (propCount > 0) {
                    prop = expectedFilePath.substring(expectedFilePath.lastIndexOf("${") + 2, expectedFilePath.lastIndexOf("}"));
                    expectedFilePath = expectedFilePath.replace("${" + prop + "}", PropertyReader.getProperty(prop));
                    propCount = StringUtils.countMatches(expectedFilePath, "${");
                }
                //expectedFilePath = expPath;
            }

            if (param.getArgs().length > 4) {
                for (int itr = 4; itr < param.getArgs().length; itr++) {
                    switch (param.getArgs()[itr]) {
                        case "-dateAsSuffix":
                            //ACTUAL,dd-MM-YYYY,<numberOfDays>,<>
                            if (param.getArgs().length >= itr + 1) {
                                String dateParam = param.getArgs()[itr + 1];
                                String[] optionValue = dateParam.split(",");
                                dateSuffix = DateUtility.getDate(optionValue[1], (optionValue.length < 3) ? 0 : Integer.valueOf(optionValue[2]));
                                String[] fileArray = null;
                                if ("ACTUAL".equalsIgnoreCase(optionValue[0])) {
                                    fileArray = FileUtility.getSeparatedFileName(actualFilePath);
                                    actualFilePath = fileArray[0] + fileArray[1] + dateSuffix + fileArray[2];
                                } else if ("EXPECTED".equalsIgnoreCase(optionValue[0])) {
                                    fileArray = FileUtility.getSeparatedFileName(expectedFilePath);
                                    expectedFilePath = fileArray[0] + fileArray[1] + dateSuffix + fileArray[2];
                                }

                            }
                            break;
                        case "-assertKey":
                            assertKey = param.getArgs()[itr + 1];
                            break;
                    }
                }
            }

            actualAbsolutePath = FileUtility.getAbsolutePath(actualFilePath);
            LogUtil.logTestCaseMsg("Actual File Location : " + actualAbsolutePath);
            LogUtil.logTestCaseMsg("Expected File Location : " + expectedFilePath);

           /* compRes = (ComparatorResponse) ComparatorType.valueOf(ComparatorType.class,
                            param.getArgs()[2] + "_"
                                    + param.getArgs()[3]
                                    + "_COMPARATOR").getInstance()
                    .compare(FileUtility.getAbsolutePath(actualFilePath), expectedFilePath, assertKey);*/
            compRes = (ComparatorResponse) ComparatorType.valueOf(ComparatorType.class,
                    fileType + "_"
                            + compareType
                            + "_COMPARATOR").getInstance()
                    .compare(FileUtility.getAbsolutePath(actualFilePath), expectedFilePath, assertKey);
            /*(param.getArgs().length == 5) ? param.getArgs()[4] : null*/
        } catch (Exception ex) {
            LogUtil.logTestCaseErrorMsg("Actual File Location : " + actualAbsolutePath, null);
            LogUtil.logTestCaseErrorMsg("Expected File Location : " + expectedFilePath, null);
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + ActionHelper.printParams("CompareFile", param.getArgs()) + "'. " + ex.getMessage(), ex);
        }

        assertThat(compRes.getResult().toUpperCase()).as("Assertion Failed: " + compRes).isEqualTo("PASS");

        return new CommandResponse(compRes.getMessge() + "<br>" + compRes.getObject(), true);
    }

    /**
     *
     * @param param
     * @param actualFilePath
     * @return
     */
    private String ifContainsDollarBracket(String param, String actualFilePath) {
        if (param.contains("${")) {
                /*String prop = actualFileLocation.substring(actualFileLocation.indexOf("${") + 2, actualFileLocation.indexOf("}"));
                actualFileLocation = actualFileLocation.replace("${" + prop + "}", PropertyReader.getProperty(prop));*/
            int propCount = StringUtils.countMatches(actualFilePath, "${");
            String prop = null;
            while (propCount > 0) {
                prop = actualFilePath.substring(actualFilePath.lastIndexOf("${") + 2, actualFilePath.lastIndexOf("}"));
                actualFilePath = actualFilePath.replace("${" + prop + "}", PropertyReader.getProperty(prop));
                propCount = StringUtils.countMatches(actualFilePath, "${");
            }
        }
        return actualFilePath;
    }
}


