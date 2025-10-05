package com.testimium.tool.command;

import com.aventstack.extentreports.ExtentTest;
import com.testimium.tool.base.DriverManager;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.domain.InputParameter;
import com.testimium.tool.domain.testcase.ExcelTestCase;

import com.testimium.tool.exception.*;
import com.testimium.tool.executor.ActionExecutor;
import com.testimium.tool.helper.TestCaseHelper;
import com.testimium.tool.reader.FileReaderFactory;
import com.testimium.tool.report.generator.ReportGenerator;
import com.testimium.tool.utility.FileUtility;
import com.testimium.tool.utility.JsonParserUtility;
import com.testimium.tool.utility.PropertyUtility;
import com.testimium.tool.utility.StringUtility;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Sandeep Agrawal
 */
public class LoopCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static LoopCmd loopCmd = new LoopCmd();
    private String supportedConditions = "ElementDisplayed(), ElementEnabled(), ElementSelected(), ElementValue(), ElementSelectValue(), OSName(WINDOWS/UNIX)" +
            "";

    public LoopCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static LoopCmd getInstance() {
        //TODO Fix Me for concurrent access
        return loopCmd;
    }

    /**
     * 1. Example
     * LOOP COUNT IN 2
     * ExecuteTest testcases/AllLogin.xlsx POSLogin;
     * ExecuteTest testcases/AllLogin.xlsx POSLogout;
     * END-LOOP
     *
     * 2. Example
     * LOOP COUNT IN 2
     * ExecuteTest testcases/AllLogin.xlsx POSLogin;
     * ExecuteTest testcases/AllLogin.xlsx POSLogout;
     * IF ElementDisplayed(UserId)==true THEN
     * ExecuteTest testcases/AllLogin.xlsx BOLogin;
     * ENDIF
     * IF ElementDisplayed(UserId)==false THEN
     * ExecuteTest testcases/AllLogin.xlsx BOLogout;
     * ENDIF
     * ExecuteTest testcases/AllLogin.xlsx POSLogin;
     * IF ElementDisplayed(UserId)==false THEN
     * ExecuteTest testcases/AllLogin.xlsx POSLogout;
     * ENDIF
     * END-LOOP
     *
     *
     **/
    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 1)
                throw new CommandException("LOOP block is missing. ");


            processLoop("LOOP", param.getArgs()[0], param);

            //TestContext.getTestContext("").getTest().pass("END-LOOP");
            ExtentTest initialExtentTestChild = ReportGenerator.getReportInstance().getChildNodeLevel1();
            initialExtentTestChild.createNode("END-LOOP");
            //ReportGenerator.getReportInstance().getChildNodeLevel1().info("END-LOOP");

        } catch (Exception ex) {
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);
        }
        return new CommandResponse("(Success)", true, param.getCommand());
    }


    /**
     * This method is used to process Loop block N times. It is the heart of Loop command.
     *
     * ExcelTestCase class making below data as statement to process LOOP which is parsed by this method:
     *
     *  Example-1
     *      LOOP#@#COUNT#@#IN#@#2#@##@#
     *            ExecuteTest#@#testcases/AllLogin.xlsx#@#POSLogin;#@##@#
     *            ExecuteTest#@#testcases/AllLogin.xlsx#@#POSLogout;#@##@#
     *      END-LOOP
     *  Example-2
     *       LOOP#@#COUNT#@#IN#@#2#@##@#
     *           ExecuteTest#@#testcases/AllLogin.xlsx#@#POSLogin;#@##@#
     *           ExecuteTest#@#testcases/AllLogin.xlsx#@#POSLogout;#@##@#
     *           IF#@#ElementDisplayed(UserId)==true#@#THEN#@#ExecuteTest#@#testcases/AllLogin.xlsx#@#BOLogin;#@#ENDIF#@##@#
     *           IF#@#ElementDisplayed(UserId)==false#@#THEN#@#ExecuteTest#@#testcases/AllLogin.xlsx#@#BOLogout;#@#ENDIF#@##@#
     *           ExecuteTest#@#testcases/AllLogin.xlsx#@#POSLogin;#@##@#
     *           IF#@#ElementDisplayed(UserId)==false#@#THEN#@#ExecuteTest#@#testcases/AllLogin.xlsx#@#POSLogout;#@#ENDIF#@##@#
     *       END-LOOP
     *
     * Example-3
     *          LOOP#@#COUNT#@#IN#@#-dateFromFile#@##@#
     *
     * @param prefix
     * @param statement
     * @param commandParam
     * @return CommandResponse
     * @throws ShutdownTestExecution
     * @throws HandleFailOverTestExecution
     */
    private CommandResponse processLoop(String prefix, String statement, CommandParam commandParam) throws ShutdownTestExecution, HandleFailOverTestExecution, CommandException, TestException, IOException, FileReaderException, InputParamNotFoundException, JsonParsingException, RecoverBrokenTestExecutionException {
        System.out.println("LOOP === " + statement);
        CommandResponse commandResponse = null;
        statement = statement.replace("END-LOOP", "");
        //LOOP COUNT IN 5#@##@#
        if(statement.contains("#@##@#")) {
            String[] splitStatement = statement.split("#@##@#");
            System.out.println("First #@##@#=== " + splitStatement[0]);
            ReportGenerator.getReportInstance().getChildNodeLevel1().getModel().setName(prefix + " " +splitStatement[0].replaceAll("#@#"," "));
            String[] firstPart = splitStatement[0].split("#@#");
            if(firstPart.length < 3) {
                throw new CommandException("LOOP Syntax is not valid, correct syntax format is -> <B>LOOP COUNT IN 100</B> (to iterate the statement 100 times)");
            }

            if("-dataFromFile".equals(firstPart[2])) {
                commandResponse = processFileData(commandParam, commandResponse, splitStatement);
            } else {
                int count = Integer.valueOf(firstPart[2]).intValue();
                commandResponse = executeInsideLoopStatement(commandParam, count, splitStatement, commandResponse);
            }
        } else {
            throw new CommandException("LOOP Syntax is not valid, correct syntax format is -> <B>LOOP COUNT IN 100</B> (to iterate the statement 100 times)");
        }

        return commandResponse;
    }

    private CommandResponse processFileData(CommandParam commandParam, CommandResponse commandResponse, String[] splitStatement) throws JsonParsingException, InputParamNotFoundException, IOException, FileReaderException, ShutdownTestExecution, TestException, RecoverBrokenTestExecutionException {
        InputParameter inputParameter = setInputParameter(commandParam.getInputParam());
        String expectedFilePath = FileUtility.getAbsolutePath(PropertyUtility.getExternalDirPath()) + "/" + inputParameter.getFileName();
        String fileExt = (null != inputParameter && null != inputParameter.getFileType()) ? inputParameter.getFileType().trim() : expectedFilePath.split(".")[1];
        List<Map<String, Object>> expected = (List<Map<String, Object>>)FileReaderFactory.getInstance().readFile(fileExt, expectedFilePath);

        if(null != expected && expected.size() > 0) {
            //List<Integer> recordDataList = null;
            ExtentTest initialExtentTestChild = ReportGenerator.getReportInstance().getChildNodeLevel1();
            if (null != inputParameter && null != inputParameter.getRecordNums() && inputParameter.getRecordNums().length > 0) {
                //recordDataList = new ArrayList<>();
                //Arrays.stream(inputParameter.getRecordNums()).forEach(recordDataList::add);
                int rowItr = 1;
                for (Integer rowNm : inputParameter.getRecordNums()) {
                    makeGlobalData(expected.get(rowNm - 1));
                    ExtentTest nestedChild = initialExtentTestChild.createNode(" Data Driven Iteration - " + rowItr, "File Data Row-" + (rowNm - 1) + " = " + expected.get((rowNm - 1)).toString());
                    ReportGenerator.getReportInstance().setChildNodeLevel1(nestedChild);
                    commandResponse = executeInsideLoopStatement(commandParam, 1, splitStatement, commandResponse);
                    ReportGenerator.getReportInstance().setChildNodeLevel1(initialExtentTestChild);
                    rowItr++;
                }
            } else {
                for (int itr = 0; itr < expected.size(); itr++) {
                    makeGlobalData(expected.get(itr));
                    ExtentTest nestedChild = initialExtentTestChild.createNode(" Data Driven Iteration - " + (itr + 1), "File Data Row-" + (itr + 1) + " = " + expected.get(itr).toString());
                    ReportGenerator.getReportInstance().setChildNodeLevel1(nestedChild);
                    String[] splitStatementCopy = new StringUtility().deepCopy(splitStatement);
                    commandResponse = executeInsideLoopStatement(commandParam, 1, splitStatementCopy, commandResponse);
                    //nestedChild.getModel().end();
                    ReportGenerator.getReportInstance().setChildNodeLevel1(initialExtentTestChild);
                }
            }
        } else {

        }
        return commandResponse;
    }

    private void makeGlobalData(Map<String, Object> row){
        for(Map.Entry<String, Object> entry:  row.entrySet()) {
            TestContext.getTestContext("").getGlobalVariable().put(entry.getKey(), entry.getValue());
        }
    }

    private InputParameter setInputParameter(String inputJson)
            throws JsonParsingException, InputParamNotFoundException {
        if(StringUtils.isNotEmpty(inputJson)) {
            return new JsonParserUtility<InputParameter>().parse(inputJson, InputParameter.class);
        } else
            throw new InputParamNotFoundException();
    }

    private CommandResponse executeInsideLoopStatement(CommandParam commandParam, int count, String[] splitStatement, CommandResponse commandResponse) throws ShutdownTestExecution, TestException, RecoverBrokenTestExecutionException {
        for (int itr = 0; itr < count; itr++) {
            for (int ikr = 1; ikr < splitStatement.length; ikr++) {
                if (!splitStatement[ikr].startsWith("IF")) {
                    splitStatement[ikr] = splitStatement[ikr].replace(";", "");
                    splitStatement[ikr] = splitStatement[ikr].replace("#@#", " ");
                } else {
                    splitStatement[ikr] = splitStatement[ikr].replaceFirst("#@#", " ");
                }
                commandResponse = processStatement(splitStatement[ikr], commandParam);
            }
            //TODO Handle Exception like HandleFailOverTestExecution
        }
        return commandResponse;
    }

    /**
     * Execute statement/command inside IF/ELSEIF/ELSE block with help of ActionExecutor
     * @param statement
     * @param commandParam
     * @return CommandResponse
     * @throws ShutdownTestExecution
     * @throws HandleFailOverTestExecution
     */
    private CommandResponse processStatement(String statement, CommandParam commandParam) throws ShutdownTestExecution, TestException, RecoverBrokenTestExecutionException {
        CommandResponse commandResponse = null;
        if(!statement.isEmpty() && !statement.isBlank()) {
            System.out.println("Colon Statement === " + statement);
            try {
                if (statement.startsWith("#"))
                    return commandResponse;

                commandResponse = new ActionExecutor().execute(getCommandParam(statement, commandParam));
            } catch (HandleFailOverTestExecution ex) {
                ExcelTestCase excelTestCase = new ExcelTestCase();
                excelTestCase.setTestCaseName(commandParam.getTestCaseName());
                excelTestCase.setInputParam(commandParam.getInputParam());
                excelTestCase.setAssertParam(commandParam.getAssertParam());
                commandResponse = new TestCaseHelper().handleFailOverTestExecution(ex, excelTestCase);
            }
        }
        return commandResponse;
    }
   /* private CommandResponse processStatement(String statement, CommandParam commandParam) throws ShutdownTestExecution, HandleFailOverTestExecution {
        CommandResponse commandResponse = null;
        String[] splitStatement = statement.split(";");
        for (int i = 0; i < splitStatement.length; i++) {
            if(!splitStatement[i].isEmpty() && !splitStatement[i].isBlank()) {
                System.out.println("Colon Statement === " + splitStatement[i]);
                commandResponse = new ActionExecutor().execute(getCommandParam(splitStatement[i], commandParam));
            }
        }
        return commandResponse;
    }*/

    private CommandParam getCommandParam (String statement, CommandParam commandParam){
        String[] operationStep = statement.trim().split("\\s+");
        String[] parms = new String[operationStep.length - 1];
        for (int i = 1; i < operationStep.length; i++) {
            parms[i - 1] = operationStep[i];
        }
        CommandParam cmd = new CommandParam("TestCase Name", operationStep[0], parms, commandParam.getInputParam(), commandParam.getAssertParam());
        cmd.setNestedNodeEnabled(true);
        return cmd;
    }

}


