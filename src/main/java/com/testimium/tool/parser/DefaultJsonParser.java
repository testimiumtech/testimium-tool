package com.testimium.tool.parser;

import com.testimium.tool.domain.TestExpectedJsonOutput;
import com.testimium.tool.exception.FileReaderException;
import com.testimium.tool.exception.JsonParsingException;
import com.testimium.tool.utility.JsonParserUtility;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.List;
import java.util.Map;

public class DefaultJsonParser implements IDataParser<File, List<Map<String, Object>>> {

    @Override
    public List<Map<String, Object>> parse(File file) throws FileReaderException {
        TestExpectedJsonOutput test = null;
        try {
            Reader reader = new FileReader(file);
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(reader);
            JSONArray data = (JSONArray) obj;
            System.out.println(data.toJSONString());

            test = new JsonParserUtility<TestExpectedJsonOutput>()
                    .parse("{ \"jsonFileData\":" + data.toJSONString() + "}", TestExpectedJsonOutput.class);

            if(null == test.getJsonFileData() || test.getJsonFileData().isEmpty())
                throw new FileReaderException("Input data not found:- '%s'", file.getAbsolutePath());

            //System.out.println("JSON File Data: \n" + listOfMap.toString());
        } catch (FileNotFoundException fnf) {
            throw new FileReaderException("JSON file not found in given location: "
                    + file.getAbsolutePath() + "\nException: " + fnf.getMessage());
        } catch (IOException ioe) {
            throw new FileReaderException("System not able to parse given JSON file: "
                    + file.getAbsolutePath() + "\nException: " + ioe.getMessage());
        } catch (ParseException e) {
            throw new FileReaderException("System not able to parse given JSON file: "
                    + file.getAbsolutePath() + "\nException: " + e.getMessage());
        } catch (JsonParsingException e) {
            throw new FileReaderException("System not able to parse given JSON file: "
                    + file.getAbsolutePath() + "\nException: " + e.getMessage());
        }
        return test.getJsonFileData();
    }
}
