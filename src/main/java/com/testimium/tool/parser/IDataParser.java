package com.testimium.tool.parser;

import com.testimium.tool.exception.FileReaderException;

/**
 * Interface is use to implement different or single responsibility data parsers which can generate different result format.
 * @param <T> Custom input type
 * @param <R> Custom response type
 * @author Sandeep Agrawal
 */
public interface IDataParser<T, R> {
    R parse(T t) throws FileReaderException;
}
