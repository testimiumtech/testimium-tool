package com.testimium.tool.exception;
/**
 * Exception class is used by custom file readers classes to generate excetion with proper messages.
 * @author Sandeep Agrawal
 */
public class FileWriterException extends Exception {

    public FileWriterException() {
    }

    public FileWriterException(String message) {
        super(message);
    }

    public FileWriterException(String message, String fileName) {
        super(String.format(message, fileName));
    }

    public FileWriterException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileWriterException(Throwable cause) {
        super(cause);
    }

    public FileWriterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
