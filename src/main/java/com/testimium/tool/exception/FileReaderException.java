package com.testimium.tool.exception;
/**
 * Exception class is used by custom file readers classes to generate excetion with proper messages.
 * @author Sandeep Agrawal
 */
public class FileReaderException extends Exception {

    public FileReaderException() {
    }

    public FileReaderException(String message) {
        super(message);
    }

    public FileReaderException(String message, String fileName) {
        super(String.format(message, fileName));
    }

    public FileReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileReaderException(Throwable cause) {
        super(cause);
    }

    public FileReaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
