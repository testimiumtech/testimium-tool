package com.testimium.tool.comparator.excel;

import com.testimium.tool.comparator.IToolComparator;
import com.testimium.tool.domain.ComparatorResponse;
import com.testimium.tool.exception.ToolComparatorException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ExeclExactComparator extends IToolComparator<String, String, ComparatorResponse> {

    @Override
    public ComparatorResponse compare(String actualExcelPath, String expectedFilePath, String assertkey) throws ToolComparatorException {
        ComparatorResponse response = null;
        Workbook wb1 = null;
        Workbook wb2 = null;
        try {
            wb1 = new XSSFWorkbook(new FileInputStream(new File(actualExcelPath)));
            wb2 = new XSSFWorkbook(new FileInputStream(new File(expectedFilePath)));
            List<String> listOfDifferences =  new ExcelComparatorHelper().compare(wb1, wb2);
            if(null != listOfDifferences && listOfDifferences.size() > 0) {
                response = new ComparatorResponse("FAIL", "Excel file content not matched", listOfDifferences);
            } else {
                response = new ComparatorResponse("PASS", "Comparison Successful", listOfDifferences);
            }
            wb1.close();
            wb2.close();
        } catch (FileNotFoundException ex) {
            response = new ComparatorResponse("FAIL", ex.getMessage(), "");

            try {
                if(null != wb1) wb1.close();
                if(null != wb2) wb2.close();
            } catch (IOException e) {
                throw new ToolComparatorException(e);
            }
        } catch (Exception ex) {
            try {
                if(null != wb1) wb1.close();
                if(null != wb2) wb2.close();
            } catch (IOException e) {
                throw new ToolComparatorException("IOException - Excel Exact Comparison Failed:  " + e);
            }
            throw new ToolComparatorException("Exception - Excel Exact Comparison Failed: " + ex.getMessage(), ex);
        }

        return response;
    }
}
