package com.testimium.tool.writer;

import com.testimium.tool.exception.FileWriterException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class DefaultJsonWriter implements IDataWriter<File, Object, Boolean>{

    @Override
    public Boolean write(File file, Object data) throws FileWriterException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.writeValue(file, data);
        } catch (Exception ex) {
            throw new FileWriterException("", ex.getMessage());
        }
        return true;
    }
}
