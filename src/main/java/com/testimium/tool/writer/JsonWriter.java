package com.testimium.tool.writer;

import com.testimium.tool.exception.FileWriterException;

import java.io.File;

public class JsonWriter extends AbstractFileWriter<Boolean> {

    @Override
    protected Boolean write(IDataWriter writer, String filePath, Object data) throws FileWriterException {
        if(null == writer)
            writer = new DefaultJsonWriter();

        if(null != writer)
            return (Boolean) writer.write(new File(filePath), data);
        return null;
    }
}
