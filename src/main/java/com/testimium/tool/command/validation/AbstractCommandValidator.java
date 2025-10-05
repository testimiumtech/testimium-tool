package com.testimium.tool.command.validation;

import com.testimium.tool.domain.CommandResponse;
import com.testimium.tool.exception.CommandException;

import java.net.http.HttpTimeoutException;
import java.util.List;

/**
 * This is  abstract validator class
 * @param <R> return type
 * @param <T> request type
 */
public abstract class AbstractCommandValidator<R, T> {
    /**
     * This is used to validate abstract command params
     * @param t request type
     * @return return type R
     * @throws CommandException if the command validation fails or is invalid
     * @throws java.net.http.HttpTimeoutException if the HTTP request times out
     */
    public abstract R validate(T t) throws CommandException, HttpTimeoutException;

    /**
     * @param t T
     * @param abstractCommandValidators List
     * @param <R> the return type of the validation result
     * @return R
     * @throws CommandException if the command validation fails or is invalid
     * @throws java.net.http.HttpTimeoutException if the HTTP request times out
     */
    public <R extends CommandResponse> R  validateCommandWithLocator(T t, List<AbstractCommandValidator> abstractCommandValidators) throws CommandException, HttpTimeoutException {
        R r = null;
        if(null != abstractCommandValidators){
            for(int itr = 0; itr < abstractCommandValidators.size(); itr++) {
                r = (R) abstractCommandValidators.get(itr).validate(t);
            }
        }
        return r;
    }
}
