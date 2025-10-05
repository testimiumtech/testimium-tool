package com.testimium.tool.parser;

import com.testimium.tool.exception.FileReaderException;
import com.testimium.tool.exception.JsonParsingException;
import com.testimium.tool.report.domain.BatchData;
import com.testimium.tool.utility.JsonParserUtility;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReportJsonParser implements IDataParser<File, BatchData> {

    @Override
    public BatchData parse(File file) throws FileReaderException {
        BatchData test = null;
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
            /*Reader reader = new FileReader(file);
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(reader);
            JSONObject data = (JSONObject) obj;
            System.out.println(data.toJSONString());*/

            test = new JsonParserUtility<BatchData>()
                    .parse(new String(encoded, Charset.defaultCharset()), BatchData.class);

            if(null == test)
                throw new FileReaderException("Input data not found:- '%s'", file.getAbsolutePath());

            //System.out.println("JSON File Data: \n" + listOfMap.toString());
        } catch (FileNotFoundException fnf) {
            throw new FileReaderException("JSON file not found in given location: "
                    + file.getAbsolutePath() + "\nException: " + fnf.getMessage());
        } catch (IOException ioe) {
            throw new FileReaderException("System not able to parse given JSON file: "
                    + file.getAbsolutePath() + "\nException: " + ioe.getMessage());
        } /*catch (ParseException e) {
            throw new FileReaderException("System not able to parse given JSON file: "
                    + file.getAbsolutePath() + "\nException: " + e.getMessage());
        }*/ catch (JsonParsingException e) {
            throw new FileReaderException("System not able to parse given JSON file: "
                    + file.getAbsolutePath() + "\nException: " + e.getMessage());
        }
        return test;
    }
}
