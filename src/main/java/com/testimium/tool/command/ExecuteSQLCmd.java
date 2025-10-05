package com.testimium.tool.command;

import com.aventstack.extentreports.Status;
import com.testimium.tool.base.DriverManager;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.datasource.connector.factory.DBConnector;
import com.testimium.tool.datasource.connector.register.DataSourceRegistry;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.domain.TestJsonInputData;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.reader.FileReaderFactory;
import com.testimium.tool.report.generator.ReportGenerator;
import com.testimium.tool.utility.FileUtility;
import com.testimium.tool.utility.PropertyUtility;
import com.testimium.tool.utility.StringUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.openqa.selenium.WebDriver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Sandeep Agrawal
 */
public class ExecuteSQLCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static ExecuteSQLCmd executeSQLCmd = new ExecuteSQLCmd();

    public ExecuteSQLCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static ExecuteSQLCmd getInstance() {
        //TODO Fix Me for concurrent access
        return executeSQLCmd;
    }

    //ExecuteSQL
    //ExecuteSQL <Json Script Key>
    //ExecuteSQL -file
    //ExecuteSQL -file <file Path json key>
    //ExecuteSQL -globalVariable
    //ExecuteSQL <Json Script Key>
    //ExecuteSQL -storeValueToGlobalVariable <Variable Name>
    //ExecuteSQL -storeValueToGlobalVariable <Variable Name> <Json Script Key>

    /*Input Param Json:
    Example-1:
    {
        "dbScript": {
        "filePath": "sample/insertSQLFile.txt",
        "secondFilePath": "sample/insertSQLFile2.txt"
    }

    Example-2: to replace global/Copied variable
    {
	"dbScript": {
			"sqlQuery": "UPDATE TB_MODULE SET IS_USED='N' WHERE MODULE_NAME='<%=GVariable=%>' AND MODULE_NAME='<%=GV2=%>'"
		},
        "useGlobalVariable":true
     }
    }*/

    //TODO Have to decide either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException {

        boolean isSqlFile = false;
        String globalVariableName = null;
        try {
            if (null != param.getArgs() && param.getArgs().length > 3)
                throw new CommandException("Argument not required for command " + param.getCommand());

            if(PropertyUtility.isDBConnectionEnabled()) {
                boolean isStoreValueToGlobalVariable = false;
                DataSourceRegistry.getConnectorMap();
                List<String> sqlQueries = null;
                //TestJsonInputData inputParam = new JsonParserUtility<TestJsonInputData>().parse(param.getInputParam(), TestJsonInputData.class);
                TestJsonInputData inputParam = getTestJsonInputData(param);
               // String[] fileExt = FileUtility.getSeparatedFileName(file.getAbsolutePath());

                if (param.getArgs().length == 3 && "-storeValueToGlobalVariable".equals(param.getArgs()[0]) && "-sqlQuery".equals(param.getArgs()[1])) {
                    isStoreValueToGlobalVariable = true;
                    sqlQueries = new ArrayList<>();
                    sqlQueries.add(inputParam.getDbScript().get(param.getArgs()[2]));
                } else if (param.getArgs().length == 1 && "-storeValueToGlobalVariable".equals(param.getArgs()[0])) {
                    isStoreValueToGlobalVariable = true;
                    sqlQueries = new ArrayList<>();
                    sqlQueries.add(inputParam.getDbScript().get("sqlQuery"));
                } else if (param.getArgs().length == 2 && "-storeValueToGlobalVariable".equals(param.getArgs()[0])) {
                    isStoreValueToGlobalVariable = true;
                    globalVariableName = param.getArgs()[1];
                    sqlQueries = new ArrayList<>();
                    sqlQueries.add(inputParam.getDbScript().get("sqlQuery"));
                } else if (param.getArgs().length == 3 && "-storeValueToGlobalVariable".equals(param.getArgs()[0])) {
                    isStoreValueToGlobalVariable = true;
                    globalVariableName = param.getArgs()[1];
                    sqlQueries = new ArrayList<>();
                    sqlQueries.add(inputParam.getDbScript().get(param.getArgs()[2]));
                } else if (param.getArgs().length >= 1 && "-file".equals(param.getArgs()[0])) {
                    String filePath = PropertyUtility.getExternalDirPath() + "/" +  inputParam.getDbScript().get((param.getArgs().length == 2) ? param.getArgs()[1] : "filePath");
                    String[] fileExt = FileUtility.getSeparatedFileName(filePath);
                    isSqlFile = ".sql".equalsIgnoreCase(fileExt[2]);
                    if(isSqlFile) {
                        //sqlQueries = new ArrayList<>();
                        //sqlQueries.add((String)FileReaderFactory.getInstance().readFile("SQL", "DEFAULT", filePath));
                       //verify(FileReaderFactory.getInstance().readFile("SQL", "DEFAULT", filePath));
                        executeSqlFile(filePath);
                    } else {
                        sqlQueries = (List<String>) FileReaderFactory.getInstance().readFile("TEXT", "DEFAULT", filePath);
                    }
                } else {
                    sqlQueries = new ArrayList<>();
                    sqlQueries.add(inputParam.getDbScript().get((null==param.getArgs() || param.getArgs().length == 0) ? "sqlQuery" : param.getArgs()[0]));
                   // sqlQueries.add(prepareSqlQuery(inputParam, param));
                }

                if(!isSqlFile && (null == sqlQueries || sqlQueries.isEmpty()))
                    throw new CommandException("SQL Query not provided in the input parameter or as file");

                if(!isSqlFile) {
                    prepareSqlQuery(sqlQueries, inputParam);
                    DBConnector dbConnector = TestContext.getTestContext("").getMainDBConnector();
                    for (int ikr = 0; ikr < sqlQueries.size(); ikr++) {
                        List<Map<String, Object>> recordList = null;
                        if(isStoreValueToGlobalVariable && sqlQueries.get(ikr).substring(0,10).toUpperCase().contains("SELECT "))  {
                            recordList = dbConnector.executeSQL(sqlQueries.get(ikr),TestContext.getTestContext("").getMainConnectorKey());
                            processRecords(recordList, globalVariableName, param);
                        } else {
                            dbConnector.executeUpdate(sqlQueries.get(ikr), TestContext.getTestContext("").getMainConnectorKey());
                        }
                    }
               }
                /*DataSourceRegistry.getConnectorMap();
                int result = TestContext.getTestContext("").getMainDBConnector().executeUpdate(
                        String.valueOf(new JsonParserUtility<TestJsonInputData>().parse(param.getInputParam(),
                                TestJsonInputData.class).getDbScript().get((null==param.getArgs() || param.getArgs().length == 0) ? "sqlQuery" : param.getArgs()[0])),
                        //.replace("%[", "BEGIN").replace("]%", "END"),
                        TestContext.getTestContext("").getMainConnectorKey());*/
                /*if(result <= 0)
                    throw new CommandException("zero rows effected!!");*/
            } else {
                throw new CommandException("Database connection is not established because database configuration is not enabled.");
            }

        } catch (Exception ex) {
            throw new CommandException("Failed to execute command "
                    + param.getCommand() + ": "+ ex.getMessage(), ex);
        }
        return new CommandResponse("(Success)", true);
    }

    private void processRecords(List<Map<String, Object>> recordList, String globalVariableName, CommandParam commandParam) throws CommandException {
        if(null != recordList && recordList.size() > 0) {
            Set<Map.Entry<String, Object>> entrySt = recordList.get(0).entrySet();
            for (Map.Entry<String, Object> entry : entrySt) {
                if (StringUtils.isNotEmpty(globalVariableName)) {
                    TestContext.getTestContext("").getGlobalVariable().put(globalVariableName, String.valueOf(entry.getValue()));
                } else {
                    TestContext.getTestContext("").getGlobalVariable().put(entry.getKey(), String.valueOf(entry.getValue()));
                }
                //new SetGlobalVariableCmd().execute(new CommandParam(commandParam.getTestCaseName(), "SetGlobalVariable", new String[]{globalVariableName, String.valueOf(entry.getValue())}, commandParam.getInputParam(), commandParam.getAssertParam()));
                ReportGenerator.getReportInstance().getChildNodeLevel1().info("ExecuteSql set result in Global Variable - " + globalVariableName + " = " + TestContext.getTestContext("").getGlobalVariable().get(globalVariableName));
                LogUtil.logTestCaseMsg("ExecuteSql set result in Global Variable - " + globalVariableName + " = " + TestContext.getTestContext("").getGlobalVariable().get(globalVariableName));
            }
        }
    }

    /*private void verify(Object readerRes) throws CommandException {
        Boolean isSuccess = (Boolean)readerRes;
        if(!isSuccess.booleanValue()) {
            throw new CommandException("SQL file execution failed");
        }

    }*/

    /**
     * Prepare the sql query based on replacing global and copied variable placeholders
     * @param sqlQueries
     * @param inputParam
     */
    private void prepareSqlQuery(List<String> sqlQueries, TestJsonInputData inputParam) {
        //String sqlQuery = inputParam.getDbScript().get((null == commandParam.getArgs() || commandParam.getArgs().length == 0) ? "sqlQuery" : commandParam.getArgs()[0]);

        for (int ktr = 0; ktr < sqlQueries.size(); ktr++) {

            String sqlQuery = sqlQueries.get(ktr);
            List<String> replaceHolders = new StringUtility().getPlaceHoldersBetweenStrings("<%=", "=%>", sqlQuery);

            if (inputParam.isUseGlobalVariable()) {
                for (int itr = 0; itr < replaceHolders.size(); itr++) {
                    sqlQuery = sqlQuery.replaceAll("<%=" + replaceHolders.get(itr) + "=%>",
                            String.valueOf(TestContext.getTestContext("").getGlobalVariable().get(replaceHolders.get(itr))));
                }
                sqlQueries.set(ktr, sqlQuery);
            }

            if (inputParam.isUseCopiedVariable()) {
                for (int itr = 0; itr < replaceHolders.size(); itr++) {
                    sqlQuery = sqlQuery.replaceAll("<%=" + replaceHolders.get(itr) + "=%>",
                            String.valueOf(TestContext.getTestContext("").getCopiedVariable().get(replaceHolders.get(itr))));
                }
                sqlQueries.set(ktr, sqlQuery);
            }
        }
        ReportGenerator.getReportInstance().getChildNodeLevel1().log(Status.INFO,
                "<br><font color='green'> SQL Queries: " + sqlQueries.toString() + "</font>");
        LogUtil.logTestCaseMsg("SQL Queries: " + sqlQueries);
    }

    private void executeSqlFile(String filePath) throws SQLException, FileNotFoundException, ClassNotFoundException {

        //Map<ConnectorKey, Object> connectorMap  =  DataSourceRegistry.getConnectorMap();
        //connectorMap.get(TestContext.getTestContext("").getMainConnectorKey());
        /*Stream<ConnectorKey> connectorKey = connectorMap
                .entrySet()
                .stream()
                .filter(entry -> TestContext.getTestContext("").getMainConnectorKey().equals(entry.getKey()))
                .map(Map.Entry::getKey);*/

        /*ConnectorKey connectorKey = DataSourceRegistry.getMatchedConnectorKey();
        //Registering the Driver
        java.sql.DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
        //Getting the connection
        String mysqlUrl = "jdbc:jtds:sqlserver://localhost:1433/testimium";
        Connection con = java.sql.DriverManager.getConnection(mysqlUrl, "wrmsdb", "masterkey");
        System.out.println("Connection established......");*/
        //Initialize the script runner
        ScriptRunner sr = new ScriptRunner(DataSourceRegistry.getSqlConnectionObj());
        sr.setSendFullScript(false);
        //sr.setRemoveCRs(true);
        sr.setStopOnError(true);
        //sr.setEscapeProcessing(true);
        //Creating a reader object
        Reader reader = new BufferedReader(new FileReader(filePath));
        //Running the script
        sr.setAutoCommit(true);
        sr.runScript(reader);
        sr.closeConnection();

        //PrintWriter printWriter = new PrintWriter(TestContext.getTestContext("").);
       // sr.setErrorLogWriter(printWriter);
        //System.out.println("Error======" + printWriter.toString());

    }
}
