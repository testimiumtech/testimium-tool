package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.AssertParameter;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.domain.assertions.Assertions;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.utility.JsonParserUtility;
import org.openqa.selenium.WebDriver;

import java.util.Map;

public class VerifyBrowserTabCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static VerifyBrowserTabCmd verifyBrowserTabCmd = new VerifyBrowserTabCmd();

    public VerifyBrowserTabCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static VerifyBrowserTabCmd getInstance() {
        //TODO Fix Me for concurrent access
        return verifyBrowserTabCmd;
    }

    //TODO Have to decide either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException {

        if(null == param.getArgs() || param.getArgs().length < 0)
            throw new CommandException("Command argument is missing: option -tabCount and a numeric value ");

        switch(param.getArgs()[0]) {
            case "-tabCount":
                int count = this.driver.getWindowHandles().size();
                    if(count != Integer.valueOf(param.getArgs()[1])) {
                        throw new CommandException(String.format("Browser Tab count: %s does not match with expected matchCount: %s", count, param.getArgs()[1]));
                    }
                break;
            default:
                count = this.driver.getWindowHandles().size();
                try {
                    AssertParameter assertParameter = JsonParserUtility.getAssertParameter();
                    if(null != assertParameter.getAssertParams()) {
                        for(Map.Entry<String, Assertions> entry:  assertParameter.getAssertParams().entrySet()) {
                            if("matchCount".equals(entry.getKey()) && (null == entry.getValue().getMatchCount() || count == entry.getValue().getMatchCount())){
                                throw new CommandException(String.format("Browser Tab count: %s does not match with expected matchCount: %s", count, entry.getValue().getMatchCount()));
                            }
                        }
                    }
                } catch (Exception ex) {
                    throw new CommandException(ex);
                }


        }
        return new CommandResponse("(Success)", true);
    }
}
