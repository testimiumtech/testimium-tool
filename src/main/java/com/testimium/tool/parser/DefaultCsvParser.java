package com.testimium.tool.parser;

import com.testimium.tool.exception.FileReaderException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCsvParser implements IDataParser<File, List<Map<String, Object>>> {

    private char splitBy = ',';

    @Override
    public List<Map<String, Object>> parse(File file) throws FileReaderException {
        /*List<Map<String, Object>> listOfMap = null;
        try {
            Reader reader = new FileReader(file);

            Iterator<Map<String, Object>> iterator = new CsvMapper()
                    .readerFor(Map.class)
                    .with(CsvSchema.emptySchema().withHeader().withColumnSeparator(splitBy))
                    .readValues(reader);

            listOfMap = new ArrayList<>();
            while (iterator.hasNext()) {
                Map<String, Object> keyVals = iterator.next();
                listOfMap.add(keyVals);
            }*/

        List<Map<String, Object>> listOfMap = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] headers = reader.readNext(); // First line as headers
            if (headers == null) {
                return listOfMap;
            }

            String[] line;
            while ((line = reader.readNext()) != null) {
                Map<String, Object> rowMap = new HashMap<>();
                for (int i = 0; i < headers.length && i < line.length; i++) {
                    rowMap.put(headers[i], line[i]);
                }
                listOfMap.add(rowMap);
            }

        if(listOfMap.isEmpty())
                throw new FileReaderException("Input data not found:- '%s'", file.getAbsolutePath());

        } catch (FileNotFoundException fnf) {
            throw new FileReaderException("CSV file not found in given location: "
                    + file.getAbsolutePath() + "\nException: " + fnf.getMessage());
        } catch (IOException ioe) {
            throw new FileReaderException("IOException - System not able to parse given CSV file: "
                    + file.getAbsolutePath() + "\nException: " + ioe.getMessage());
        } catch (CsvValidationException e) {
            throw new FileReaderException("CsvValidationException - System not able to parse given CSV file: "
                    + file.getAbsolutePath() + "\nException: " + e.getMessage());
        }

        return listOfMap;
    }
}
