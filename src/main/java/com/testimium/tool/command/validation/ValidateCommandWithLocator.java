package com.testimium.tool.command.validation;

import com.testimium.tool.command.VerifyElementIsDisplayedCmd;
import com.testimium.tool.command.VerifyElementIsEnabledCmd;
import com.testimium.tool.command.VerifyElementIsPresentCmd;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;

import java.net.http.HttpTimeoutException;

public class ValidateCommandWithLocator extends AbstractCommandValidator<CommandResponse, CommandParam>{


    @Override
    public CommandResponse validate(CommandParam commandParam) throws CommandException, HttpTimeoutException {
        CommandResponse commandResponse = null;
        commandResponse = new VerifyElementIsPresentCmd().execute(commandParam);
        commandResponse = (commandResponse.isSuccess()) ? new VerifyElementIsEnabledCmd().execute(commandParam) : commandResponse;
        commandResponse = (commandResponse.isSuccess()) ? new VerifyElementIsDisplayedCmd().execute(commandParam): commandResponse;

        return commandResponse;
    }
}
