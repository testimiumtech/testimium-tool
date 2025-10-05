package com.testimium.tool.comparator.excel;

import com.testimium.tool.comparator.IToolComparator;
import com.testimium.tool.domain.ComparatorResponse;
import com.testimium.tool.exception.ToolComparatorException;
import com.testimium.tool.logging.LogUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class ExeclSearchComparator extends IToolComparator<String, String, ComparatorResponse> {


    /**
     *
     * @param actualExcelPath
     * @param searchWord
     * @param assertkey
     * @return
     * @throws ToolComparatorException
     */
    @Override
    public ComparatorResponse compare(String actualExcelPath, String searchWord, String assertkey) throws ToolComparatorException {
        ComparatorResponse response = null;
        FileInputStream fis = null;
        Workbook workbook = null;
        boolean isFoundWord = false;
        try {
            File file = new File(actualExcelPath);
            fis = new FileInputStream(file);
            workbook = new XSSFWorkbook(fis);
            LogUtil.logTestCaseMsg("Searching in file: " + file.getName());
            StringBuilder stringBuilder = new StringBuilder();
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
                            if (cellValue.contains(searchWord)) {
                                isFoundWord = true;

                                stringBuilder.append("\nFound '" + searchWord + "' in file '" + file.getName() +
                                        "', sheet '" + workbook.getSheetName(i) +
                                        "', row " + (row.getRowNum() + 1) +
                                        ", column " + (cell.getColumnIndex() + 1));

                                LogUtil.logTestCaseMsg("\nFound '" + searchWord + "' in file '" + file.getName() +
                                        "', sheet '" + workbook.getSheetName(i) +
                                        "', row " + (row.getRowNum() + 1) +
                                        ", column " + (cell.getColumnIndex() + 1));
                            }
                        }
                    }
                }
            }
            if(isFoundWord) {
                response = new ComparatorResponse("PASS", "Comparison Successful", stringBuilder.toString());
            } else {
                response = new ComparatorResponse("FAIL", searchWord + " word not found in the excel file " + file.getName(), "");
            }

            workbook.close();
            fis.close();
        } catch (FileNotFoundException ex) {
            response = new ComparatorResponse("FAIL", ex.getMessage(), "");
            try {
                if(null != workbook) workbook.close();
                if(null != fis) fis.close();
            } catch (IOException e) {
                throw new ToolComparatorException(e);
            }
        } catch (Exception ex) {
            try {
                if(null != workbook) workbook.close();
                if(null != fis) fis.close();
            } catch (IOException e) {
                throw new ToolComparatorException("IOException - Excel word search Failed:  " + e);
            }
            throw new ToolComparatorException("Excel Partial Comparison Failed: " + ex.getMessage(), ex);
        }
        return response;
    }
}
