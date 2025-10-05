package com.testimium.tool.reader;

import com.testimium.tool.exception.FileReaderException;
import com.testimium.tool.parser.DefaultTextParser;
import com.testimium.tool.parser.IDataParser;

import java.io.File;


public class TextFileReader extends AbstractFileReader{
    private String filePath;

    public TextFileReader() {}

    /**
     * This method is used to read the Text file
     * @return
     * @throws FileReaderException
     */
    @Override
    protected Object read(IDataParser parser, String filePath) throws FileReaderException {
        this.filePath = filePath;
        File file = new File(this.filePath);
        if(null == parser)
            parser = new DefaultTextParser();

        if(null != parser)
            return parser.parse(file);

        return null;
    }

    /*public static void main(String[] args) throws FileReaderException {
            new TextFileReader("input/sampleInput.txt").read();
    }*/
}
