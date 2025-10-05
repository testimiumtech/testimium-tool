package com.testimium.tool.action;

import com.testimium.tool.writer.AbstractFileWriter;
import com.testimium.tool.writer.JsonWriter;

import java.util.function.Supplier;

/**
 * Enum based factory to maintain and responsible to handle parser service instantiation.
 * @author Sandeep Agrawal
 */
public enum WriterType {
    JSON_WRITER(JsonWriter::new);
    /*JSON_DEFAULT_WRITER(DefaultJsonWriter::new);*/


    private Supplier<AbstractFileWriter> parserInstantiator;

    public AbstractFileWriter getInstance() {
        return parserInstantiator.get();
    }

    WriterType(Supplier<AbstractFileWriter> parserInstantiator) {
        this.parserInstantiator = parserInstantiator;
    }
}
