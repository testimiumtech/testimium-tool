package com.testimium.tool.reader;

import com.testimium.tool.exception.FileReaderException;
import com.testimium.tool.parser.DefaultCsvParser;
import com.testimium.tool.parser.IDataParser;

import java.io.*;

public class CsvFileReader extends AbstractFileReader{

    private String filePath;

    public CsvFileReader(String filePath) {
        this.filePath = filePath;
    }
    public CsvFileReader(){}
    /**
     * Read the file
     * @param parser input param
     * @param filePath input param
     * @throws FileReaderException if file reading issue
     */
    @Override
    protected Object read(IDataParser parser, String filePath) throws FileReaderException {

        this.filePath = filePath;
        File file = new File(this.filePath);

        if(null == parser)
            parser = new DefaultCsvParser();

        if(null != parser)
            return parser.parse(file);

        return null;
    }
}
