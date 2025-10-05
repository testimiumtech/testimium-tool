package com.testimium.tool.helper;

import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.exception.CommandException;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.io.IOException;
import java.net.http.HttpTimeoutException;

public class ExceptionHelper {

    /**
     *
     * @param message
     * @param param
     * @param ex
     * @throws CommandException
     * @throws HttpTimeoutException
     */
    public void checkAndThrowException(String message, CommandParam param, Exception ex) throws CommandException, HttpTimeoutException {
        checkException(param, ex);
        //NoSuchSessionException

        if(null != ex) {
            if (ex instanceof NoSuchSessionException) {
                throw new NoSuchSessionException("NoSuchWindowException - Communication with Browser is broken/disconnected, '"
                        + param.getCommand() + "' with argument '" + ((null == param.getArgs()) ? "nothing" : param.getArgs()[0]) + "'. " + ex.getMessage(), ex);
            } else if (ex instanceof NoSuchWindowException) {
                throw new NoSuchWindowException("NoSuchWindowException - Communication with Browser is broken/disconnected, '"
                        + param.getCommand() + "' with argument '" + ((null == param.getArgs()) ? "nothing" : param.getArgs()[0]) + "'. " + ex.getMessage(), ex);
            } else if (ex instanceof UnreachableBrowserException) {
                throw new UnreachableBrowserException("UnreachableBrowserException - Communication with Browser is broken/disconnected, '"
                        + param.getCommand() + "' with argument '" + ((null == param.getArgs()) ? "nothing" : param.getArgs()[0]) + "'. " + ex.getMessage(), ex);
            } else if (ex instanceof HttpTimeoutException) {
                throw new HttpTimeoutException("HttpTimeoutException - Communication with Browser is broken/disconnected, '"
                        + param.getCommand() + "' with argument '" + ((null == param.getArgs()) ? "nothing" : param.getArgs()[0]) + "'. " + ex.getMessage());
            } else if (ex instanceof WebDriverException) {
                if (null != ex.getMessage() && (ex.getMessage().contains("failed to check if window was closed: disconnected: not connected to DevTools")
                        || ex.getMessage().contains("disconnected: not connected to DevTools")
                        || ex.getMessage().contains("disconnected"))) {
                    throw new WebDriverException("WebDriverException - Communication with Browser is broken/disconnected, '"
                            + param.getCommand() + "' with argument '" + ((null == param.getArgs()) ? "nothing" : param.getArgs()[0]) + "'. " + ex.getMessage());
                }
            } else if (ex instanceof IOException) {
                if (null != ex.getMessage() && ex.getMessage().contains("An existing connection was forcibly closed by the remote host")) {
                    throw new WebDriverException("IOException - Communication with Browser is broken/disconnected, '"
                            + param.getCommand() + "' with argument '" + ((null == param.getArgs()) ? "nothing" : param.getArgs()[0]) + "'. " + ex.getMessage());
                }
            }

            throw new CommandException((null != message && !message.isEmpty()) ? message : "Failed to execute command '"
                    + param.getCommand() + "' with argument '" + ((null == param.getArgs()) ? "nothing" : param.getArgs()[0]) + "'. " + ex.getMessage(), ex);
        } else {
            throw new CommandException("Failed to execute command '"
                    + param.getCommand() + "' with argument '" + ((null == param.getArgs()) ? "nothing" : param.getArgs()[0]));
        }
    }

    /**
     *
     * @param message
     * @param param
     * @param ex
     * @throws AssertionError
     * @throws HttpTimeoutException
     */
    public void checkAndThrowAssertion(String message, CommandParam param, Exception ex) throws AssertionError, HttpTimeoutException {

        checkException(param, ex);
        //NoSuchSessionException
        if(null != ex) {
            if (ex instanceof NoSuchSessionException) {
                throw new NoSuchSessionException("NoSuchWindowException - Communication with Browser is broken/disconnected, '"
                        + param.getCommand() + "' with argument '" + ((null == param.getArgs()) ? "nothing" : param.getArgs()[0]) + "'. " + ex.getMessage(), ex);
            } else if (ex instanceof NoSuchWindowException) {
                throw new NoSuchWindowException("NoSuchWindowException - Failed to execute command'"
                        + param.getCommand() + "' with argument '" + ((null == param.getArgs()) ? "nothing" : param.getArgs()[0]) + "'. " + ex.getMessage(), ex);
            } else if (ex instanceof UnreachableBrowserException) {
                throw new UnreachableBrowserException("UnreachableBrowserException - Failed to execute command '"
                        + param.getCommand() + "' with argument '" + ((null == param.getArgs()) ? "nothing" : param.getArgs()[0]) + "'. " + ex.getMessage(), ex);
            } else if (ex instanceof HttpTimeoutException) {
                throw new HttpTimeoutException("HttpTimeoutException - Failed to execute command '"
                        + param.getCommand() + "' with argument '" + ((null == param.getArgs()) ? "nothing" : param.getArgs()[0]) + "'. " + ex.getMessage());
            } else if (ex instanceof WebDriverException) {
                if (null != ex.getMessage() && (ex.getMessage().contains("failed to check if window was closed: disconnected: not connected to DevTools")
                        || ex.getMessage().contains("disconnected: not connected to DevTools")
                        || ex.getMessage().contains("disconnected"))) {
                    throw new WebDriverException("WebDriverException - Communication with Browser is disconnected, '"
                            + param.getCommand() + "' with argument '" + ((null == param.getArgs()) ? "nothing" : param.getArgs()[0]) + "'. " + ex.getMessage());
                }
            } else if (ex instanceof IOException) {
                if (null != ex.getMessage() && ex.getMessage().contains("An existing connection was forcibly closed by the remote host")) {
                    throw new WebDriverException("IOException - Communication with Browser is broken/disconnected, '"
                            + param.getCommand() + "' with argument '" + ((null == param.getArgs()) ? "nothing" : param.getArgs()[0]) + "'. " + ex.getMessage());
                }
            }

            throw new AssertionError("<br>Verification failed for command '"
                    + param.getCommand() + "' with argument '" + ((null == param.getArgs())?"nothing":param.getArgs()[0]) + "'. "+ ex.getMessage(), ex);
        } else {
            throw new AssertionError("<br>Verification failed for command '"
                    + param.getCommand() + "' with argument '" + ((null == param.getArgs())?"nothing":param.getArgs()[0]));
        }
    }

    private static void checkException(CommandParam param, Exception ex) throws HttpTimeoutException {
        if(null != ex
                && ((null != ex.getMessage()
                && ((ex.getMessage().contains("org.openqa.selenium.TimeoutException: java.net.http.HttpTimeoutException: request timed out"))
                || (ex.getMessage().contains("java.net.http.HttpTimeoutException: request timed out"))
                || (ex.getMessage().contains("org.openqa.selenium.TimeoutException: java.util.concurrent.TimeoutException"))))
                || (null != ex.getCause() && null != ex.getCause().getMessage()
                &&  (((ex.getCause().getMessage().contains("org.openqa.selenium.TimeoutException: java.net.http.HttpTimeoutException: request timed out"))
                || (ex.getCause().getMessage().contains("java.net.http.HttpTimeoutException: request timed out"))
                || (ex.getCause().getMessage().contains("org.openqa.selenium.TimeoutException: java.util.concurrent.TimeoutException"))))))){
            throw new HttpTimeoutException("HttpTimeoutException - Communication with Browser is broken/disconnected, '"
                    + param.getCommand() + "' with argument '" + ((null == param.getArgs())?"nothing": param.getArgs()[0]) + "'. "+ ex.getMessage());
        }
    }
}
