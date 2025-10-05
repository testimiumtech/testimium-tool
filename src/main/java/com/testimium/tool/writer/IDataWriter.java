package com.testimium.tool.writer;

import com.testimium.tool.exception.FileWriterException;

/**
 * Interface is use to implement different or single responsibility data writer which can write data in different format.
 * @param <T> Custom input type
 * @param <R> Custom response type
 * @author Sandeep Agrawal
 */
public interface IDataWriter<S, T, R> {
    R write(S s, T t) throws FileWriterException;
}
