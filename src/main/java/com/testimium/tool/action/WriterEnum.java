package com.testimium.tool.action;

import com.testimium.tool.writer.DefaultJsonWriter;
import com.testimium.tool.writer.IDataWriter;

import java.util.function.Supplier;

/**
 * Enum based factory to maintain and responsible to handle writer service instantiation.
 * @author Sandeep Agrawal
 */
public enum WriterEnum {
    JSON_DEFAULT_WRITER(DefaultJsonWriter::new);


    private Supplier<IDataWriter> parserInstantiator;

    public IDataWriter getInstance() {
        return parserInstantiator.get();
    }

    WriterEnum(Supplier<IDataWriter> parserInstantiator) {
        this.parserInstantiator = parserInstantiator;
    }
}
