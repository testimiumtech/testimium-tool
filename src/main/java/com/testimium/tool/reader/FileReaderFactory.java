package com.testimium.tool.reader;


import com.testimium.tool.action.ParserType;
import com.testimium.tool.action.ReaderType;
import com.testimium.tool.exception.FileReaderException;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.parser.IDataParser;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;

/**
 * Reader Factory class, single approach to read the provided file.
 * @author Sandeep Agrawal
 */
public class FileReaderFactory {

    private static FileReaderFactory fileReaderFactory = new FileReaderFactory();
    /**
     *  Static method makes sure that single object will be created through out the JVM for FileReaderFactory class.
     * @return FileReaderFactory
     */
    //TODO Fix Me for Concurrent process or parallel execution
    /*public static FileReaderFactory getInstance(IDataParser parser) {
        if(null == fileReaderFactory)
            fileReaderFactory = new FileReaderFactory();

       // fileReaderFactory.parser = parser;

        return  fileReaderFactory;
    }*/

    public static FileReaderFactory getInstance() {
        if(null == fileReaderFactory)
            fileReaderFactory = new FileReaderFactory();

        return  fileReaderFactory;
    }

    /**
     * Method provides different file reading approach.
     * @param action Custom reader actions
     * @param filePath input file
     * @return Object Custom response
     * @throws FileReaderException
     */
    public Object readFile(ReaderType action, String filePath) throws FileReaderException {
        return action.getInstance().read(null, filePath);
    }

    /**
     * Read file with default parser
     * @param fileType
     * @param filePath
     * @return
     * @throws FileReaderException
     */
    public Object readFile(String fileType, String filePath) throws FileReaderException, FileNotFoundException {
        return readFile(fileType, null, filePath);
    }

    /**
     * Read file by passing customized parser
     * @param fileType
     * @param parserType
     * @param filePath
     * @return
     * @throws FileReaderException
     */
    public Object readFile(String fileType, String parserType, String filePath) throws FileReaderException, FileNotFoundException {
        LogUtil.logToolMsg("FileReaderFactory fileType : " + fileType);
        LogUtil.logToolMsg("FileReaderFactory parserName : " + fileType);
        LogUtil.logToolMsg("FileReaderFactory filePath : " + filePath);

        if(StringUtils.isEmpty(fileType))
            throw new FileReaderException(fileType + " file type is not supported by tool!");

        if(StringUtils.isEmpty(filePath)) {
            throw new FileNotFoundException(fileType + " file not found in given location: "+ filePath);
        }

        if(StringUtils.isEmpty(parserType))
            parserType = "Default";

        IDataParser parser = ParserType.valueOf(ParserType.class, fileType.toUpperCase() + "_" + parserType.toUpperCase() + "_PARSER").getInstance();

        return ReaderType.valueOf(ReaderType.class,
                "READ_" + fileType.toUpperCase()).getInstance().read(parser, filePath);
    }

    /*public static void main(String[] args) throws FileReaderException {
        IDataParser parser = ParserType.valueOf(ParserType.class,"CSV_DEFAULT_PARSER").getInstance();
        IDataParser parser1 = ParserType.valueOf(ParserType.class,"CSV_DEFAULT_PARSER").getInstance();
        IDataParser parser2 = ParserType.valueOf(ParserType.class,"CSV_DEFAULT_PARSER").getInstance();
        IDataParser parser3 = ParserType.valueOf(ParserType.class,"CSV_DEFAULT_PARSER").getInstance();

        System.out.println(parser.hashCode());
        System.out.println(parser1.hashCode());
        System.out.println(parser2.hashCode());
        System.out.println(parser3.hashCode());
    }*/
}
