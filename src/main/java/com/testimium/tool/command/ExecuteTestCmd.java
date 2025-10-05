package com.testimium.tool.command;

import com.aventstack.extentreports.ExtentTest;
import com.testimium.tool.base.DriverManager;
import com.testimium.tool.base.MasterTestCase;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.domain.testcase.ExcelTestCase;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.exception.TestException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.parser.ExcelParser;
import com.testimium.tool.reader.FileReaderFactory;
import com.testimium.tool.reader.PropertyReader;
import com.testimium.tool.report.generator.ReportGenerator;
import com.testimium.tool.utility.FileUtility;
import org.openqa.selenium.WebDriver;

import java.net.http.HttpTimeoutException;
import java.util.List;

/**
 * @author Sandeep Agrawal
 */
public class ExecuteTestCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static ExecuteTestCmd executeTestCmd = new ExecuteTestCmd();
    public ExecuteTestCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static ExecuteTestCmd getInstance() {
        //TODO Fix Me for concurrent access
        return executeTestCmd;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, TestException, HttpTimeoutException {
        ExtentTest initialExtentTestChild = ReportGenerator.getReportInstance().getChildNodeLevel1();
        try {

            //TestContext.getTestContext("").getExecuteTestCmdHierarchy().add(TestContext.getTestContext("").getTestCaseName());
            if (null == param.getArgs() || param.getArgs().length < 1)
                throw new CommandException("Command argument is missing: provide Command argument (TestSuite Path, with/without Sheet Name).  ");

            String filePath = null;
            if(param.getArgs()[0].contains("${")) {
                filePath = PropertyReader.getProperty(param.getArgs()[0].substring(param.getArgs()[0].indexOf("${") + 2, param.getArgs()[0].indexOf("}")));
            } else {
                filePath = param.getArgs()[0];
            }

            String sheetName = "";
            String columnName = "";
            if( param.getArgs().length > 1 && "-globalVariable".equals(param.getArgs()[1])) {
                if(param.getArgs().length < 3)
                    throw new CommandException("Command argument is missing: provide Command argument (propertyKey, -globalVariable, name (global Variable key/name)). ");
                sheetName = String.valueOf(TestContext.getTestContext("").getGlobalVariable().get(param.getArgs()[2]));
                columnName = param.getArgs()[2];
            } else if( param.getArgs().length > 1) {
                sheetName = param.getArgs()[1];
            }
            ExtentTest nestedChild = initialExtentTestChild.createNode("Testsuite: " + filePath.substring(filePath.lastIndexOf("/")) + 1 + ((columnName.isEmpty()) ? sheetName : " = " + sheetName));
            ExcelParser excelParser = (ExcelParser) FileReaderFactory.getInstance().readFile("EXCEL", "TESTCASE", FileUtility.getAbsolutePath(filePath));

            ReportGenerator.getReportInstance().setChildNodeLevel1(nestedChild);
            if(param.getArgs().length >= 2) {
                //MasterTestCase.processTest((ExcelParser) FileReaderFactory.getInstance().readFile("EXCEL", FileUtility.getAbsolutePath(filePath)), param.getArgs()[1], true);
               // ExcelParser excelParser = (ExcelParser) FileReaderFactory.getInstance().readFile("EXCEL", "TESTCASE", FileUtility.getAbsolutePath(filePath));
                List<ExcelTestCase> testCases = excelParser.getTestCasesBySheetName(sheetName.trim());
                if(null != testCases && testCases.size() > 0) {
                    MasterTestCase.processTest(testCases, excelParser.getFileName(), sheetName, true);
                }
            } else if (param.isNestedNodeEnabled()) {
                MasterTestCase.process(excelParser, true);
            } else {
                MasterTestCase.process(excelParser);
            }

           /* List<String> executeTestCmdHierarchy = TestContext.getTestContext("").getExecuteTestCmdHierarchy();
            TestContext.getTestContext("").setTestCaseName(executeTestCmdHierarchy.get(executeTestCmdHierarchy.size() -1));
            executeTestCmdHierarchy.remove(executeTestCmdHierarchy.size() -1);*/
            ReportGenerator.getReportInstance().setChildNodeLevel1(initialExtentTestChild);
        } catch (Exception ex) {
            ReportGenerator.getReportInstance().setChildNodeLevel1(initialExtentTestChild);
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}


