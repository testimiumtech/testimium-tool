package com.testimium.tool.writer;


import com.testimium.tool.action.ParserType;
import com.testimium.tool.action.WriterEnum;
import com.testimium.tool.action.WriterType;
import com.testimium.tool.exception.FileWriterException;
import com.testimium.tool.logging.LogUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;

/**
 * Reader Factory class, single approach to read the provided file.
 * @author Sandeep Agrawal
 */
public class FileWriterFactory {

    private static FileWriterFactory fileWriterFactory = new FileWriterFactory();
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

    public static FileWriterFactory getInstance() {
        if(null == fileWriterFactory)
            fileWriterFactory = new FileWriterFactory();

        return fileWriterFactory;
    }

    /**
     * Method provides different file writing approach.
     * @param action input param
     * @param filePath input param
     * @param data input param
     * @return object
     * @throws FileWriterException If any exception
     */
    public Object writeFile(WriterType action, String filePath, Object data) throws FileWriterException {
        return action.getInstance().write(null, filePath, data);
    }

    /**
     * Write file with default writer object
     * @param fileType input param
     * @param filePath input param
     * @param data input param
     * @return object
     * @throws FileNotFoundException If file not found
     * @throws FileWriterException If any exception
     */
    public Object writeFile(String fileType, String filePath, Object data) throws FileNotFoundException, FileWriterException {
        return writeFile(fileType, null, filePath, data);
    }

    /**
     * Read file by passing customized parser
     * @param fileType input param
     * @param writerType input param
     * @param filePath input param
     * @param data input param
     * @return object
     * @throws FileWriterException If any exception
     * @throws FileNotFoundException If file not found
     */
    public Object writeFile(String fileType, String writerType, String filePath, Object data) throws FileWriterException, FileNotFoundException {
        LogUtil.logToolMsg("FileWriterFactory fileType : " + fileType);
        LogUtil.logToolMsg("FileWriterFactory parserName : " + fileType);
        LogUtil.logToolMsg("FileWriterFactory filePath : " + filePath);

        if(StringUtils.isEmpty(fileType))
            throw new FileWriterException(fileType + " file type is not supported by tool!");

        if(StringUtils.isEmpty(filePath)) {
            throw new FileNotFoundException(fileType + " filePath and name not provided: "+ filePath);
        }

        if(StringUtils.isEmpty(writerType))
            writerType = "Default";

        IDataWriter writer = ParserType.valueOf(WriterEnum.class, fileType.toUpperCase() + "_" + writerType.toUpperCase() + "_WRITER").getInstance();

        return WriterType.valueOf(WriterType.class,
                fileType + "_WRITER").getInstance().write(writer, filePath, data);
    }
}
