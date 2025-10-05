package com.testimium.tool.command;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;
import com.testimium.tool.helper.ExceptionHelper;
import com.testimium.tool.reader.PropertyReader;
import com.testimium.tool.utility.PropertyUtility;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;

import java.net.http.HttpTimeoutException;

/**
 * @author Sandeep Agrawal
 */
public class OpenUrlCmd implements ExternalCommand<CommandParam, CommandResponse> {
    private WebDriver driver;
    private static OpenUrlCmd openUrlCommand = new OpenUrlCmd();

    public OpenUrlCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static OpenUrlCmd getInstance() {
        //TODO Fix Me for concurrent excess
        return openUrlCommand;
    }

    //TODO Have to finalize either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) throws CommandException, HttpTimeoutException {
        String webUrl = null;
        try {
            if( null == param || null == param.getArgs() || param.getArgs().length < 1) {
                if(StringUtils.isEmpty(PropertyUtility.getWebUrl()))
                    throw new CommandException("Missing web application URL configuration in tool property web.app.url");
                webUrl = PropertyUtility.getWebUrl();
            } else {
                webUrl = (null == PropertyReader.getProperty(param.getArgs()[0]) ? param.getArgs()[0] : PropertyReader.getProperty(param.getArgs()[0]))
                        + "" + ((param.getArgs().length == 2) ? param.getArgs()[1] : "");
            }
            new SetGlobalVariableCmd().execute(new CommandParam(new String[]{"lastUsedOpenAppUrl",webUrl}));
            this.driver = DriverManager.getInstance().getWebDriver();
            driver.get(webUrl);
            //waitForPageToLoad(this.driver, 30);
        } catch (Exception ex) {
            new ExceptionHelper().checkAndThrowException(null, param, ex);
        }
        return new CommandResponse("(Success)", true);
    }
}
