package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.domain.TestJsonInputData;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.logging.LogUtil;
import org.openqa.selenium.WebDriver;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sandeep Agrawal
 */
public class CompareDateCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static CompareDateCmd compareDateCmd = new CompareDateCmd();

    public CompareDateCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static CompareDateCmd getInstance() {
        //TODO Fix Me for concurrent access
        return compareDateCmd;
    }

    //CompareDate -globalVariable Varialble1 -globalVariable Varialble2 -operator >

    /**
     * @param param
     * @return
     * @throws CommandException
     */
    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException {
        try {
            if(null == param.getArgs() || param.getArgs().length < 6)
                throw new CommandException("Command argument is missing: provide Command argument (propertyKey).  ");

            String dateVariableName1 = param.getArgs()[1];
            String dateVariableName2 = param.getArgs()[3];
            String dateStr1 = String.valueOf(TestContext.getTestContext("").getGlobalVariable().get(dateVariableName1));
            String dateStr2 = String.valueOf(TestContext.getTestContext("").getGlobalVariable().get(dateVariableName2));

            TestJsonInputData inputParam = getTestJsonInputData(param);
            /*Date date1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:ms", Locale.ENGLISH).parse(dateStr1);
            Date date2 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:ms", Locale.ENGLISH).parse(dateStr2);*/
            Date date1 = new SimpleDateFormat(String.valueOf(inputParam.getDateFormat().get(dateVariableName1))).parse(dateStr1);
            Date date2 = new SimpleDateFormat(String.valueOf(inputParam.getDateFormat().get(dateVariableName2))).parse(dateStr2);
            /*Date date1 = new Date(dateStr1);
            Date date2 = new Date(dateStr2);*/

            LogUtil.logTestCaseMsg(dateVariableName1 + " = " + date1);
            LogUtil.logTestCaseMsg(dateVariableName2 + " = " + date2);


            boolean isSuccess = false;
            switch (param.getArgs()[5]) {
                case ">":
                    isSuccess = date1.compareTo(date2) > 0;
                    assertThat(isSuccess).as("Assertion Failed: Date " + dateVariableName1 + ": " + date1
                            + " is not after " + dateVariableName2 + ": " + date2).isEqualTo(true);
                    break;
                case "<":
                    isSuccess = date1.compareTo(date2) < 0;
                    assertThat(isSuccess).as("Assertion Failed: Date " + dateVariableName1 + ": " + date1
                            + " is not before " + dateVariableName2 + ": " + date2).isEqualTo(true);
                    break;
                case "=":
                    isSuccess = date1.compareTo(date2) == 0;
                    assertThat(isSuccess).as("Assertion Failed: Date " + dateVariableName1 + ": " + date1
                            + " is not equal to " + dateVariableName2 + ": " + date2).isEqualTo(true);
                    break;
                default:
                    LogUtil.logTestCaseErrorMsg("Operator not supported", null);
                    return new CommandResponse("Operator not supported", false);
            }

        } catch (Exception ex) {
            throw new CommandException("Failed to execute command'"
                    + param.getCommand() + "' with argument '" + param.getArgs()[0] + "'. "+ ex.getMessage(), ex);
        }
        return new CommandResponse("(Success)", true);
    }

}


