package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.factory.VerificationTypeFactory;
import org.openqa.selenium.*;

/**
 * @author Sandeep Agrawal
 */
public class VerifySQLResponseCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static VerifySQLResponseCmd verifySQLResponseCmd = new VerifySQLResponseCmd();

    public VerifySQLResponseCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static VerifySQLResponseCmd getInstance() {
        //TODO Fix Me for concurrent access
        return verifySQLResponseCmd;
    }

    //TODO Have to decide either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException {
        try {
            if (null != param.getArgs() && param.getArgs().length > 2)
                throw new CommandException("Not supported more then two argument by command " + param.getCommand());

            /*if(null != PropertyReader.getProperty("enable.datasource.connection")
                    && "true".equalsIgnoreCase(PropertyReader.getProperty("enable.datasource.connection"))) {*/
                VerificationTypeFactory.getVerificationInstance("SQLTable").verify(param);
            /*} else {
                throw new CommandException("Database connection is not established because database configuration is not enabled.");
            }*/

        } catch (Exception ex) {
            /*throw new CommandException("Failed to execute command "
                    + param.getCommand() + ": <br>"+ ex.getMessage(), ex);*/
            //throw new CommandException("<br>"+ ex.getMessage(), ex);
            throw new AssertionError("<br>"+ ex.getMessage(), ex);
        }
        return new CommandResponse("(Success)", true);
    }
}
