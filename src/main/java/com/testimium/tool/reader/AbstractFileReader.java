package com.testimium.tool.reader;

import com.testimium.tool.exception.FileReaderException;
import com.testimium.tool.parser.IDataParser;

/**
 *  Abstract class which can be used to implement different file reading approach.
 * @author Sandeep Agrawal
 */
public abstract class AbstractFileReader<T> {

    /**
     *
     * @param parser input parameter
     * @param filePath input parameter
     * @return response type T
     * @throws FileReaderException if file readers fails
     */
    protected abstract T read(IDataParser parser, String filePath) throws FileReaderException;

}
