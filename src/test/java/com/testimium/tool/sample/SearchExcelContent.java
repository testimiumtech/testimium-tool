package com.testimium.tool.sample;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class SearchExcelContent {
    public static void main(String[] args) {
        // Directory containing Excel files
        String directoryPath = "path/to/your/excel-files"; // Update with your directory path
        String searchTerm = "yourWordOrSentence"; // Word or sentence to search

        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".xlsx"));

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                searchInExcelFile(file, searchTerm);
            }
        } else {
            System.out.println("No Excel files found in the directory.");
        }
    }

    private static void searchInExcelFile(File file, String searchTerm) {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            System.out.println("Searching in file: " + file.getName());

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                Iterator<Row> rowIterator = sheet.iterator();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        if (cell.getCellType() == CellType.STRING) {
                            String cellValue = cell.getStringCellValue();
                            if (cellValue.contains(searchTerm)) {
                                System.out.println("Found '" + searchTerm + "' in file '" + file.getName() +
                                        "', sheet '" + workbook.getSheetName(i) +
                                        "', row " + (row.getRowNum() + 1) +
                                        ", column " + (cell.getColumnIndex() + 1));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + file.getName());
            e.printStackTrace();
        }
    }
}
