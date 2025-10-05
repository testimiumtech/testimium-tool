package com.testimium.tool.reader;

import com.testimium.tool.exception.FileReaderException;
import com.testimium.tool.parser.DefaultJsonParser;
import com.testimium.tool.parser.IDataParser;

import java.io.File;


public class JsonFileReader extends AbstractFileReader{
    private String filePath;

    public JsonFileReader(String filePath) {
        this.filePath = filePath;
    }
    public JsonFileReader(){}

    @Override
    protected Object read(IDataParser parser, String filePath) throws FileReaderException {

        this.filePath = filePath;
        File file = new File(this.filePath);

        if(null == parser)
            parser = new DefaultJsonParser();

        if(null != parser)
            return parser.parse(file);
       /* TestExpectedJsonOutput test = null;
        try {
            this.filePath = filePath;
            File file = new File(this.filePath);
            //File file = new File(FileUtility.getAbsolutePath("testfiles/Sample/test.json"));
            Reader reader = new FileReader(file);
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(reader);
            JSONArray data = (JSONArray) obj;
            System.out.println(data.toJSONString());

            test = new JsonParserUtility<TestExpectedJsonOutput>()
                    .parse("{ \"jsonFileData\":" + data.toJSONString() + "}", TestExpectedJsonOutput.class);
            if(null == test.getJsonFileData() || test.getJsonFileData().isEmpty())
                throw new FileReaderException("Input data not found:- '%s'", filePath);

            if(null != parser)
                return parser.parse(test.getJsonFileData());

            //System.out.println("JSON File Data: \n" + listOfMap.toString());
        } catch (FileNotFoundException fnf) {
            throw new FileReaderException("JSON file not found in given location: "
                    + this.filePath + "\nException: " + fnf.getMessage());
        } catch (IOException ioe) {
            throw new FileReaderException("System not able to parse given JSON file: "
                    + this.filePath + "\nException: " + ioe.getMessage());
        } catch (ParseException e) {
            throw new FileReaderException("System not able to parse given JSON file: "
                    + this.filePath + "\nException: " + e.getMessage());
        } catch (JsonParsingException e) {
            throw new FileReaderException("System not able to parse given JSON file: "
                    + this.filePath + "\nException: " + e.getMessage());
        }
        return test.getJsonFileData();*/
       return null;
    }

    /*public static void main(String[] args) throws FileReaderException {
        new JsonReader("").read();
    }*/
}
