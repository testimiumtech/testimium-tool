package com.testimium.tool.parser;

import com.testimium.tool.exception.FileReaderException;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

public class DefaultTextParser implements IDataParser<File, List<String>> {

    public DefaultTextParser() {}

    @Override
    public List<String> parse(File file) throws FileReaderException {
        System.out.println("Start reading text file.....");
        List<String> result = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            result = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line == null || line.isEmpty())
                    continue;
                //System.out.println(line);
                result.add(line);
            }

            if(result.isEmpty())
                throw new FileReaderException("Input data not found:- '%s'", file.getAbsolutePath());

            System.out.println("End reading text file.....");

        } catch (AccessDeniedException ex) {
            throw new FileReaderException("Please provide the valid file name:- '%s'", file.getAbsolutePath());
        } catch (NoSuchFileException ex) {
            throw new FileReaderException("File not found in given location:- '%s'", file.getAbsolutePath());
        }  catch (IOException ex) {
            ex.printStackTrace();
            throw new FileReaderException(ex.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return result;
    }
}
