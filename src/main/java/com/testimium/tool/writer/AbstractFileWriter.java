package com.testimium.tool.writer;

import com.testimium.tool.exception.FileWriterException;


/**
 *  Abstract class which can be used to implement different file reading approach.
 * @author Sandeep Agrawal
 * @param <T> abstract type T
 */
public abstract class AbstractFileWriter<T> {

    /**
     *
     * @param writer Input param
     * @param filePath Input param
     * @param data Input param
     * @return response T
     * @throws FileWriterException If fails to write
     */
    protected abstract T write(IDataWriter writer, String filePath, Object data) throws FileWriterException;

}
