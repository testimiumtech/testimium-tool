package com.testimium.tool.reader;

import com.testimium.tool.exception.FileReaderException;
import com.testimium.tool.parser.IDataParser;

public class ExcelFileReader extends AbstractFileReader {

    private String filePath;
    //private FileInputStream inputStream;
    @Override
    protected Object read(IDataParser parser, String filePath) throws FileReaderException {
            //this.filePath = filePath;
            //File file =	new File(this.filePath);
            //Create an object of FileInputStream class to read excel file
            //inputStream = new FileInputStream(file);

            if(null != parser) {
                return parser.parse(filePath);
            }
        /*} catch (IOException e) {
            throw new FileReaderException("Excel file not found in given location: "
                    + this.filePath + "\nException: " + e.getMessage());
        } *//*finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/


        return null;
    }
}
