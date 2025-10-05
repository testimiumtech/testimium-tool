package com.testimium.tool.comparator.excel;

import com.testimium.tool.comparator.IToolComparator;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.AssertParameter;
import com.testimium.tool.domain.ComparatorResponse;
import com.testimium.tool.domain.assertions.Assertions;
import com.testimium.tool.exception.JsonParsingException;
import com.testimium.tool.exception.ToolComparatorException;
import com.testimium.tool.utility.JsonParserUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExeclPartialComparator extends IToolComparator<String, String, ComparatorResponse> {

    private String[] compareExcelSheets;
    private Integer[] compareRows;
    /**
     *
     * @param actualExcelPath
     * @param expectedFilePath
     * @param assertkey
     * @return
     * @throws ToolComparatorException
     */
    @Override
    public ComparatorResponse compare(String actualExcelPath, String expectedFilePath, String assertkey) throws ToolComparatorException {
        ComparatorResponse response = null;
        Workbook wb1 = null;
        Workbook wb2 = null;
        try {
            wb1 = new XSSFWorkbook(new FileInputStream(new File(actualExcelPath)));
            wb2 = new XSSFWorkbook(new FileInputStream(new File(expectedFilePath)));

            ExcelComparatorHelper excelComparatorHelper =  new ExcelComparatorHelper(getFlags(assertkey), "PARTIAL");
            excelComparatorHelper.setCompareExcelSheetsByNames(compareExcelSheets);
            excelComparatorHelper.setCompareRows(compareRows);
            List<String> listOfDifferences =  excelComparatorHelper.compare(wb1, wb2);
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
                throw new ToolComparatorException("IOException - Excel Partial Comparison Failed:  " + e);
            }
            throw new ToolComparatorException("Excel Partial Comparison Failed: " + ex.getMessage(), ex);
        }

        return response;
    }

    /**
     *
     * @param assertKey
     * @return
     * @throws JsonParsingException
     */
    private Map<String, Object> getFlags(String assertKey) throws JsonParsingException {
        /*AssertParameter assertParam = new JsonParserUtility<AssertParameter>()
                .parse(TestContext.getTestContext("").getTestExpectedJson(), AssertParameter.class);*/
        AssertParameter assertParam = null;
        if(TestContext.getTestContext("").getTestExpectedJson().isEmpty()) {
            assertParam = new AssertParameter();
        } else {
            assertParam = new JsonParserUtility<AssertParameter>()
                    .parse(TestContext.getTestContext("").getTestExpectedJson(), AssertParameter.class);
        }
        Assertions assertions = null;
        if (StringUtils.isNotEmpty(assertKey) && null != assertParam && null != assertParam.getAssertParams()) {
            assertions = assertParam.getAssertParams().get(assertKey);
            compareExcelSheets = assertions.getCompareExcelSheets();
            compareRows = assertions.getTableRowNum();
            return assertions.getFlags();
        } else if (null != assertParam && null != assertParam.getAssertParams() && assertParam.getAssertParams().size() > 0) {
            for (Map.Entry<String, Assertions> entry : assertParam.getAssertParams().entrySet()) {
                assertions = entry.getValue();
                break;
            }
            if(null != assertions) {
                compareExcelSheets = assertions.getCompareExcelSheets();
                compareRows = assertions.getTableRowNum();
                return assertions.getFlags();
            }
        }
        return null;
    }
}
