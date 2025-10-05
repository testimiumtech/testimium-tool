package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;

import com.testimium.tool.exception.*;
import com.testimium.tool.executor.ActionExecutor;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.report.generator.ReportGenerator;
import com.testimium.tool.utility.OSValidator;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class IfCmd implements ExternalCommand<CommandParam, CommandResponse> {
    /**
     * Web driver object
     */
    private WebDriver driver;
    /**
     * Static object of IfCmd
     */
    private static IfCmd ifCmd = new IfCmd();
    private String supportedConditions = "ElementDisplayed(), ElementEnabled(), ElementSelected(), ElementValue(), ElementSelectValue(), OSName(WINDOWS/UNIX)";

    public IfCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }
    /**
     * Get the static instance
     * @return response IfCmd
     */
    public static IfCmd getInstance() {
        //TODO Fix Me for concurrent access
        return ifCmd;
    }

    /**
     * 1.  IF isElementSelected(locator-property) == true THEN
                LogMessage PASS Verified$$CheckBox$$Checked;
            ENDIF

       2.  IF isElementSelected(locator-property,  HTML-Element-Value) == true THEN
                LogMessage PASS VerifiedCheckBox$$Checked;
            ENDIF
     **/
    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 1)
                throw new CommandException("If block is missing. ");

            //boolean isConditionSuccess = false;
            CommandResponse commandResponse = null;
            if(param.getArgs()[0].contains("ELSEIF")) {
                String[] splitElseIf = param.getArgs()[0].split("ELSEIF");
                if (null != splitElseIf && splitElseIf.length > 0) {
                    for (int i = 0; i < splitElseIf.length; i++) {
                        System.out.println("ELSEIF === " + splitElseIf[i]);
                        if (i == 0) {
                            commandResponse = processCondition("IF", splitElseIf[i], param);
                        } else if (i < splitElseIf.length - 1) {
                            commandResponse = processCondition("ELSEIF", splitElseIf[i], param);
                        } else {
                            commandResponse = processElseCondition("ELSEIF", splitElseIf[i], param);
                        }

                        if(commandResponse.isSuccess()) {
                            ReportGenerator.getReportInstance().getChildNodeLevel1().pass("ENDIF");
                            return commandResponse;
                        }
                    }
                }
            } else if(param.getArgs()[0].contains("ELSE")) {
                processElseCondition("IF", param.getArgs()[0], param);
            } else {
                processCondition("IF", param.getArgs()[0], param);
            }

            ReportGenerator.getReportInstance().getChildNodeLevel1().pass("ENDIF");

        } catch (Exception ex) {
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);
        }
        return new CommandResponse("(Success)", true, param.getCommand());
    }

    private CommandResponse processElseCondition(String prefix, String statement, CommandParam commandParam) throws ShutdownTestExecution, PropertyKeyNotFoundException, LocatorNotFoundException, ConditionNotSuportedException, HandleFailOverTestExecution, CommandException, TestException, HttpTimeoutException, RecoverBrokenTestExecutionException {
        CommandResponse commandResponse = null;
        String[] splitElse = statement.split("ELSE");
        for (int i = 0; i < splitElse.length; i++) {
            System.out.println("ELSE === " + splitElse[i]);
            if(i == 0)
                commandResponse = processCondition(prefix , splitElse[i], commandParam);
            else
                commandResponse = processCondition("ELSE", splitElse[i], commandParam);
            //If false then continue to execute else part
            if(null == commandResponse || commandResponse.isSuccess())
                return commandResponse;

        }
        return null;
    }

    /**
     * This method is used to process each IF/ElseIf/Else block. It is the heart of IF command.
     * @param prefix input param
     * @param statement input param
     * @param commandParam input param
     * @return response CommandResponse
     * @throws ShutdownTestExecution If any unwanted exception
     * @throws PropertyKeyNotFoundException if property key not found
     * @throws LocatorNotFoundException if locator not found
     * @throws ConditionNotSuportedException if condition not supported
     * @throws HandleFailOverTestExecution If not able to handle fail over
     * @throws CommandException if command exception
     * @throws TestException if test fails
     * @throws HttpTimeoutException if http timeout
     * @throws RecoverBrokenTestExecutionException If not able to recover test
     */
    private CommandResponse processCondition(String prefix, String statement, CommandParam commandParam) throws ShutdownTestExecution, PropertyKeyNotFoundException, LocatorNotFoundException, ConditionNotSuportedException, HandleFailOverTestExecution, CommandException, TestException, HttpTimeoutException, RecoverBrokenTestExecutionException {
        CommandResponse commandResponse = null;
        statement = statement.replace("#@#ENDIF", "").replace("#@#", " ");
        if(!statement.contains("ELSE") && statement.contains("THEN")) {
            String[] splitStatement = statement.split("THEN");
            System.out.println("First Then 0=== " + splitStatement[0]);
            commandResponse = new CommandResponse("", null, checkCondition(splitStatement[0], commandParam));
            if (commandResponse.isSuccess()) {
                //TestContext.getTestContext("").getTest().pass(prefix + " " + splitStatement[0] + " THEN  <br>(Condition Matched)" );
                if(prefix.equalsIgnoreCase("IF")) ReportGenerator.getReportInstance().getChildNodeLevel1().getModel().setName(prefix + " " +splitStatement[0].replaceAll("#@#"," " + " THEN"));
                //ReportGenerator.getReportInstance().getChildNodeLevel1().pass(prefix + " " + splitStatement[0] + " THEN  <br>(Condition Matched)" );
                ReportGenerator.getReportInstance().getChildNodeLevel1().pass(prefix + " " + splitStatement[0] + " THEN  <br>(Condition Matched)" );
                System.out.println("First Then 1=== " + splitStatement[1]);
                commandResponse = processStatement(splitStatement[1], commandParam);
            } else {
                ReportGenerator.getReportInstance().getChildNodeLevel1().pass(prefix + " " + splitStatement[0] + " THEN  <br>(Condition Not Matched)");
                //TestContext.getTestContext("").getTest().pass(prefix + " " + splitStatement[0] + " THEN  <br>(Condition Not Matched)");
            }
            //commandResponse = new CommandResponse("",null, false);
        } else if(!statement.contains("THEN")){
            System.out.println("Not Then === " + statement);
            commandResponse = processStatement(statement, commandParam);
            //commandResponse = new CommandResponse("",null, false);
        }
        return commandResponse;
    }

    /**
     * Execute statement/command inside IF/ELSEIF/ELSE block with help of ActionExecutor
     * @param statement input param
     * @param commandParam input param
     * @return Command response
     * @throws ShutdownTestExecution If any unwanted exception
     * @throws HandleFailOverTestExecution If not able to handle fail over
     * @throws TestException if test fails
     * @throws RecoverBrokenTestExecutionException If not able to recover test
     */
    private CommandResponse processStatement(String statement, CommandParam commandParam) throws ShutdownTestExecution, HandleFailOverTestExecution, TestException, RecoverBrokenTestExecutionException {
        CommandResponse commandResponse = null;
        String[] splitStatement = statement.split(";");
        for (int i = 0; i < splitStatement.length; i++) {
            if(!splitStatement[i].isEmpty() && !splitStatement[i].isBlank()) {
                System.out.println("Colon Statement === " + splitStatement[i]);
                /*if(splitStatement[i].startsWith("#")){
                    continue;
                }*/
                commandParam = getCommandParam(splitStatement[i], commandParam);
                if(!commandParam.getCommand().startsWith("#")) {
                    LogUtil.logTestCaseMsg("Command = " + commandParam);
                    commandParam.setNestedNodeEnabled(true);
                    commandResponse = new ActionExecutor().execute(commandParam);
                }
            }
        }
        return commandResponse;
    }

    /**
     * Get Command Param
     * @param statement input param
     * @param commandParam input param
     * @return command param
     */
    private CommandParam getCommandParam (String statement, CommandParam commandParam){
        String[] operationStep = statement.trim().split("\\s+");
        String[] parms = new String[operationStep.length - 1];
        for (int i = 1; i < operationStep.length; i++) {
            parms[i - 1] = operationStep[i];
        }
        return new CommandParam("TestCase Name", operationStep[0], parms, commandParam.getInputParam(), commandParam.getAssertParam());
    }

    /**
     * Checking condition
     * @param statement input param
     * @return boolean value
     * @throws PropertyKeyNotFoundException if property key not found
     * @throws LocatorNotFoundException if locator not found
     */
    private boolean checkCondition(String statement, CommandParam commandParam) throws PropertyKeyNotFoundException, ConditionNotSuportedException, CommandException, HttpTimeoutException {
        String[] operator = statement.trim().trim().split("==");
        String condition = operator[0].substring(0, operator[0].indexOf("("));
        String property = operator[0].substring( operator[0].indexOf("(")+1, operator[0].indexOf(")"));
        String[] param = property.split(",");
        boolean isSuccess = false;
        WebElement element = null;
        try {
            if(!"OSNAME".equalsIgnoreCase(condition) && !condition.equalsIgnoreCase("GlobalVariable")) {
                 element = wait(param[0].trim());
                // element = driver.findElement(LocatorFactory.getByLocatorProperty(param[0].trim()));
                //element = findElement(commandParam);
            }
        } catch (LocatorNotFoundException | NoSuchElementException | ElementNotInteractableException |
                 TimeoutException ex) {
            ex.printStackTrace();
            LogUtil.logToolMsg("Locator Not Found - " + param[0].trim());
        }
        switch (condition.toUpperCase()){
            case "OSNAME":
                isSuccess = compareBoolean(OSValidator.isOsNameMatched(param[0].trim()), operator[1]);
                break;
            case "ELEMENTDISPLAYED":
            case "ISELEMENTDISPLAYED":
                isSuccess = null == element ? compareBoolean(false, operator[1]) : compareBoolean(element.isDisplayed(), operator[1]);
                break;
            case "ELEMENTENABLED":
            case "ISELEMENTENABLED":
                isSuccess = null == element ? compareBoolean(false, operator[1]) : compareBoolean(element.isEnabled(), operator[1]);
                break;
            case "ELEMENTSELECTED":
            case "ISELEMENTSELECTED":
                //isSuccess = null == element ? compareBoolean(false, operator[1]) : compareBoolean(element.isSelected(), operator[1]);
                String[] newCommandParam  = null;
                newCommandParam = (param.length == 2) ? new String[]{param[0], param[1],"-fromIf"} : new String[]{param[0], "-fromIf"};
                isSuccess = compareBoolean(new VerifyElementIsSelectedCmd().execute(new CommandParam(
                        statement,
                        "VerifyElementIsSelected",
                        newCommandParam,
                        commandParam.getInputParam(),
                        commandParam.getAssertParam())).isSuccess(), operator[1]);
                break;
            case "ELEMENTVALUE":
            case "ISEQUALS":
                //String[] param = property.split(",");
                String actualValue = (param.length == 1) ? element.getText() : element.getAttribute(param[1].trim());
                isSuccess = actualValue.trim().equals(operator[1].trim());
                break;
            case "ELEMENTSELECTVALUE":
            case "ISDROPDOWNVALUESELECTED":
                //Select dropdown = new Select(element);
                isSuccess = new Select(element).getFirstSelectedOption().getText().trim().equals(operator[1].trim());
                break;
            case "ISJSVALUEEQUALS":
                //TODO - Not tested yet
                isSuccess = compareBoolean(new VerifyElementByJSCmd().execute(new CommandParam(
                        statement,
                        "VerifyElementByJSCmd",
                        new String[]{param[0],param[1],param[2],"-fromIf"},
                        commandParam.getInputParam(),
                        commandParam.getAssertParam())).isSuccess(), operator[1]);
                break;
            case "GLOBALVARIABLE":
                isSuccess = operator[1].equals(TestContext.getTestContext("").getGlobalVariable().get(param[0]));
                break;
            default:
                throw new ConditionNotSuportedException(condition, supportedConditions);
        }
        return isSuccess;
    }

    /**
     * compare boolean
     * @param actual input param
     * @param expected input param
     * @return boolean value
     */
    private boolean compareBoolean(boolean actual, String expected){
        return Boolean.compare(actual, Boolean.parseBoolean(expected.trim())) == 0 ? true : false;
    }
}


