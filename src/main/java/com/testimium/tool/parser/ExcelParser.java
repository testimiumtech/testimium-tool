package com.testimium.tool.parser;

import com.aventstack.extentreports.Status;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.testcase.ExcelTestCase;
import com.testimium.tool.exception.FileReaderException;
import com.testimium.tool.exception.TestException;
import com.testimium.tool.logging.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelParser implements IDataParser <String, ExcelParser>{

    private String fileName;
    private String sheetName;
    private Workbook workbook = null;

    @Override
    public ExcelParser parse(String filePath) throws FileReaderException {
        FileInputStream inputStream = null;
        File file = null;
        try {
            file =	new File(filePath);
            if(!file.exists())
                throw new FileNotFoundException("Excel file not found in given location: "+ file.getAbsolutePath());
            this.fileName = file.getName();
            //Create an object of FileInputStream class to read excel file
            inputStream = new FileInputStream(file);
            this.workbook = new XSSFWorkbook(inputStream);
        }  catch (FileNotFoundException fnf) {
            LogUtil.logToolErrorMsg(fnf.getMessage(), fnf);
            TestContext.getTestContext("").getTest().log(Status.FAIL, fnf.getMessage());
            throw new FileReaderException("\nException: " + fnf.getMessage());
        } catch (IOException ioe) {
            throw new FileReaderException("System not able to parse given Excel file: " + file.getAbsolutePath() + "\nException: " + ioe.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                LogUtil.logToolErrorMsg(e.getMessage(), e);
                e.printStackTrace();
            }
        }
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public Sheet getSheet() {
        return workbook.getSheet(sheetName);
    }

    /**
     * Get the excel rows as list
     * @param sheetName input param
     * @return iteration object array
     */
    public Iterator<Object[]> getRowsAsList(String sheetName)  {
        {
            this.sheetName = sheetName;
            Sheet sheet = getSheet();
            if(null == sheet) LogUtil.logTestCaseErrorMsg("Sheet name "+ sheetName +" not found in the given excel file " + getFileName(), new NullPointerException());
            List<Object[]> rows = new ArrayList<Object[]>();
            List<Object> singleRow=null;//Find number of rows in excel file
            int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
            /*System.out.println("------  --------------------------------------------" );
            System.out.println("Row  -    Columns" );
            System.out.println("------  --------------------------------------------" );
            System.out.println();*/
            for (int i = 0; i < rowCount; i++) {
                boolean isEmptyRow = true;
                singleRow = new ArrayList<Object>();
                //Loop over all the rows
                Row row = sheet.getRow(i+1);
                //Create a loop to print cell values in a row
                //for (int j = 0; j < row.getLastCellNum(); j++) {
                //System.out.print(i + " - ");

                for (int j = 0; j < 18; j++) {
                    //Print excel data in console
                    //System.out.print(j + ", ");
                    try {
                        if(null != row.getCell(j) && StringUtils.isNotEmpty(row.getCell(j).toString())) {
                            isEmptyRow = false;
                        }
                        singleRow.add(String.valueOf((null == row.getCell(j)) ? "" : row.getCell(j)));
                    } catch (NullPointerException npe) {
                        //TODO FIX ME
                    }
                }
                if(isEmptyRow)
                    break;
                rows.add(singleRow.toArray());
            }
            return rows.iterator();
        }
    }

    /**
     *
     * @param sheetName
     * @return
     * @throws IOException
     */
    public List<ExcelTestCase> getTestCasesBySheetName(String sheetName) throws IOException, TestException {
        System.out.println("Process Sheet Name: "+ sheetName.toLowerCase());
        //PropertyReader.load(FileUtility.getPropertyFiles(sheetName));
        Iterator<Object[]> iterator = getRowsAsList(sheetName);
        List<ExcelTestCase> tcList = new ArrayList<>();
        while(iterator.hasNext()) {
            Object[] colValue = iterator.next();

            if( !"failOverSteps".equals(colValue[1]) && ("null".equalsIgnoreCase(String.valueOf(colValue[3]))
                    || "".equalsIgnoreCase(String.valueOf(colValue[3]).trim())))
                throw new TestException("Test type is missing for testcase name '" + colValue[1] + "' to perform the test for "+sheetName);

            if ((null != TestContext.getTestContext("").getTestTypes()
                    && !"ALL".equalsIgnoreCase(TestContext.getTestContext("").getTestTypes().get(0)))
                    && (TestContext.getTestContext("").getTestTypes().stream()
                    .filter(val -> String.valueOf(colValue[3]).contains(val)).count() <= 0))
                continue;

            ExcelTestCase tc = new ExcelTestCase(colValue);

            if("failOverSteps".equals(tc.getTestCaseName()))
                TestContext.getTestContext("").getFailOver().put("TestSuiteLevel", tc);
            else if((null != tc.getTestSteps() && tc.getTestSteps().size() > 0))
                   /* && (PropertyUtility.isAllTestExecEnabled() || !tc.isDisabled())
                    && tc.isAutomated())*/ {
                tcList.add(tc);
            }
        }
        return tcList;
    }

    public List<String> getAllSheets() {
        List<String> sheetList = new ArrayList<>();
        for (int itr = 0; itr < this.workbook.getNumberOfSheets(); itr++) {
            sheetList.add(workbook.getSheetName(itr));
        }
        return sheetList;
    }
}
