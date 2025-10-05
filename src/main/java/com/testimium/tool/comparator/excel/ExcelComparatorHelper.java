package com.testimium.tool.comparator.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.util.*;

/**
 * Utility to compare Excel File Contents cell by cell for all sheets.
 *
 * <p>This utility will be used to compare Excel File Contents cell by cell for all sheets programmatically.</p>
 *
 * <p>Below are the list of Attribute comparison supported in this version.</p>
 *
 * <ul>
 * <li>Cell Alignment</li>
 * <li>Cell Border Attributes</li>
 * <li>Cell Data</li>
 * <li>Cell Data-Type</li>
 * <li>Cell Fill Color</li>
 * <li>Cell Fill pattern</li>
 * <li>Cell Font Attributes</li>
 * <li>Cell Font Family</li>
 * <li>Cell Font Size</li>
 * <li>Cell Protection</li>
 * <li>Name of the sheets</li>
 * <li>Number of Columns</li>
 * <li>Number of Rows</li>
 * <li>Number of Sheet</li>
 * </ul>
 *
 * <p>(Some of the above attribute comparison only work for *.xlsx format currently. In future it can be enhanced.)</p>
 *
 * <p><b>Usage:</b></p>
 *
 * <pre>
 * {@code
 *  Workbook wb1 = WorkbookFactory.create(new File("workBook1.xls"));
 *  Workbook wb2 = WorkbookFactory.create(new File("workBook2.xls"));
 *  List<String> listOfDifferences = ExcelComparator.compare(wb1, wb2);
 *  for (String differences : listOfDifferences)
 *      System.out.println(differences);
 *  System.out.println("DifferenceFound = "+ excelFileDifference.isDifferenceFound);
 *  }
 * </pre>
 */
public class ExcelComparatorHelper {

    private String comparisonMode = "EXACT";
    private boolean enableCompareNumberOfSheets = true;
    private boolean enableCompareSheetNames = true;
    private boolean enableCompareSheetData = true;
    private boolean enableCompareRowCountInSheets = true;
    private boolean enableCompareColumnCountInSheets = true;
    private boolean enableCompareDataInAllSheets = true;
    private boolean enableCompareForSelectedRows = false;
    private boolean enableCellFillPatternMatches = true;
    private boolean enableCellAlignmentMatches = true;
    private boolean enableCellHiddenMatches = true;
    private boolean enableCellLockedMatches = true;
    private boolean enableCellFontFamilyMatches = true;
    private boolean enableCellFontSizeMatches = true;
    private boolean enableCellFontBoldMatches = true;
    private boolean enableCellUnderLineMatches = true;
    private boolean enableCellFontItalicsMatches = true;
    private boolean enableCellTopBorderMatches = true;
    private boolean enableCellBottomBorderMatches = true;
    private boolean enableCellLeftBorderMatches = true;
    private boolean enableCellRightBorderMatches = true;
    //All border above top, bottom, left and right
    private boolean enableCellAllBorderMatches = true;
    private boolean enableCellFillBackGroundMatches = true;

    private String[] compareExcelSheetsByNames = null;
    private Integer[] compareRows = null;

    private static final String CELL_DATA_DOES_NOT_MATCH = "<B>Cell Data does not Match ::</B>";
    private static final String CELL_FONT_ATTRIBUTES_DOES_NOT_MATCH = "<B>Cell Font Attributes does not Match ::</B>";
    private List<String> listOfDifferences = new ArrayList<String>();

    private static class Locator {
        Workbook workbook;
        Sheet sheet;
        Row row;
        Cell cell;
    }

    public ExcelComparatorHelper() {

    }

    public ExcelComparatorHelper(String comparisonMode) {
        this.comparisonMode = comparisonMode;
    }

    public ExcelComparatorHelper(Map<String, Object> flags, String comparisonMode) {
        this.comparisonMode = comparisonMode;
        if(null != flags) {
               this.enableCompareNumberOfSheets = getFlagValue("COMPARE_SHEET_COUNT", flags);
               this.enableCompareSheetNames = getFlagValue("COMPARE_SHEET_NAMES", flags);
               this.enableCompareSheetData = getFlagValue("COMPARE_SHEET_DATA", flags);
               this.enableCompareRowCountInSheets = getFlagValue("COMPARE_ROW_COUNT_IN_SHEET", flags);
               this.enableCompareColumnCountInSheets = getFlagValue("COMPARE_COLUMNS_COUNT_IN_SHEET", flags);
               this.enableCompareDataInAllSheets = getFlagValue("COMPARE_DATA_IN_ALL_SHEETS", flags);
               this.enableCompareForSelectedRows = getFlagValue("COMPARE_DATA_FOR_SELECTED_ROWS", flags);
               this.enableCellFillPatternMatches = getFlagValue("COMPARE_CELL_FILL_PATTERN", flags);
               this.enableCellAlignmentMatches = getFlagValue("COMPARE_CELL_ALIGNMENT", flags);
               this.enableCellHiddenMatches = getFlagValue("COMPARE_CELL_HIDDEN", flags);
               this.enableCellLockedMatches = getFlagValue("COMPARE_CELL_LOCKED", flags);
               this.enableCellFontFamilyMatches = getFlagValue("COMPARE_CELL_FONT_FAMILY", flags);
               this.enableCellFontSizeMatches = getFlagValue("COMPARE_CELL_FONT_SIZE", flags);
               this.enableCellFontBoldMatches = getFlagValue("COMPARE_CELL_FONT_BOLD", flags);
               this.enableCellUnderLineMatches = getFlagValue("COMPARE_CELL_UNDER_LINE", flags);
               this.enableCellFontItalicsMatches = getFlagValue("COMPARE_CELL_FONT_ITALIC", flags);
               this.enableCellTopBorderMatches = getFlagValue("COMPARE_CELL_TOP_BORDER", flags);
               this.enableCellBottomBorderMatches = getFlagValue("COMPARE_CELL_BOTTOM_BORDER", flags);
               this.enableCellLeftBorderMatches = getFlagValue("COMPARE_CELL_LEFT_BORDER", flags);
               this.enableCellRightBorderMatches = getFlagValue("COMPARE_CELL_RIGHT_BORDER", flags);
        }
    }

    public void setCompareExcelSheetsByNames(String[] compareExcelSheetsByNames) {
        this.compareExcelSheetsByNames = compareExcelSheetsByNames;
    }

    public void setCompareRows(Integer[] compareRows) {
        this.compareRows = compareRows;
    }

    /**
     * Utility to compare Excel File Contents cell by cell for all sheets.
     * @param wb1 the workbook1
     * @param wb2 the workbook2
     * @return the Excel file difference containing a flag and a list of differences
     */
    public List<String> compare(Workbook wb1, Workbook wb2) {
        ExcelComparatorHelper.Locator loc1 = new ExcelComparatorHelper.Locator();
        ExcelComparatorHelper.Locator loc2 = new ExcelComparatorHelper.Locator();
        loc1.workbook = wb1;
        loc2.workbook = wb2;

        /*ExcelComparatorHelper excelComparator = new ExcelComparatorHelper();
        excelComparator.compareNumberOfSheets(loc1, loc2 );
        excelComparator.compareSheetNames(loc1, loc2);
        excelComparator.compareSheetData(loc1, loc2);*/
        //return excelComparator.listOfDifferences;
        return compareBasedOnMode(loc1, loc2);
    }

    /**
     * Compare based on MODE
     * @param loc1
     * @param loc2
     * @return
     */
    private List<String> compareBasedOnMode(Locator loc1, Locator loc2) {
        //ExcelComparatorHelper excelComparator = new ExcelComparatorHelper();
        if(this.comparisonMode.equals("EXACT")) {
            this.compareNumberOfSheets(loc1, loc2);
            this.compareSheetNames(loc1, loc2);
            this.compareSheetData(loc1, loc2);
        } else {
            compareBasedOnPartialMode(loc1, loc2);
        }

        return this.listOfDifferences;
    }

    /**
     * Compare in PARTIAL mode
     * @param loc1
     * @param loc2
     */
    private void compareBasedOnPartialMode(Locator loc1, Locator loc2) {
        if(this.enableCompareNumberOfSheets) {
            this.compareNumberOfSheets(loc1, loc2);
        }
        if(this.enableCompareSheetNames) {
            this.compareSheetNames(loc1, loc2);
        }

        if(this.enableCompareSheetData) {
            this.compareSheetData(loc1, loc2);
        } /*else {
            TestContext.getTestContext("").getTest().log(Status.INFO, "To enable sheet data compare in assert param use COMPARE_SHEET_DATA:true");
        }*/
    }

    /**
     * Compare data in all sheets.
     * @param loc1
     * @param loc2
     */
    private void compareDataInAllSheets(Locator loc1, Locator loc2) {
        for (int i = 0; i < loc1.workbook.getNumberOfSheets(); i++) {
            if (loc2.workbook.getNumberOfSheets() <= i) return;

            loc1.sheet = loc1.workbook.getSheetAt(i);
            loc2.sheet = loc2.workbook.getSheetAt(i);

            compareDataInSheet(loc1, loc2);
        }
    }

    /**
     * Compare data in selected sheets only used by Partial Mode
     * @param loc1
     * @param loc2
     */
    private void compareDataInSelectedSheets(Locator loc1, Locator loc2) {
        for(int itr = 0; itr < this.compareExcelSheetsByNames.length; itr++){
            //for (int i = 0; i < loc1.workbook.getNumberOfSheets(); i++) {
                //if (loc2.workbook.getNumberOfSheets() <= i) return;

                int index1 = loc1.workbook.getSheetIndex(compareExcelSheetsByNames[itr].trim());
                if(this.compareExcelSheetsByNames[itr].equals(loc1.workbook.getSheetAt(index1).getSheetName())) {
                    loc1.sheet = loc1.workbook.getSheetAt(index1);
                   // break;
                }
            //}
           // for (int i = 0; i < loc2.workbook.getNumberOfSheets(); i++) {
                //if (loc1.workbook.getNumberOfSheets() <= i) return;
                int index2 = loc2.workbook.getSheetIndex(compareExcelSheetsByNames[itr].trim());
                if(this.compareExcelSheetsByNames[itr].equals(loc2.workbook.getSheetAt(index2).getSheetName())) {
                    loc2.sheet = loc2.workbook.getSheetAt(index2);
                    //break;
                }
            //}
            compareDataInSheet(loc1, loc2);
        }
    }

    private void compareDataInSheet(Locator loc1, Locator loc2) {
        if(this.enableCompareForSelectedRows) {
            for (int j = 0; j < this.compareRows.length; j++) {
                loc1.row = loc1.sheet.getRow(compareRows[j]-1);
                loc2.row = loc2.sheet.getRow(compareRows[j]-1);

                if ((loc1.row == null) || (loc2.row == null)) {
                    continue;
                }
                compareDataInRow(loc1, loc2);
            }
        } else {
            for (int j = 0; j < loc1.sheet.getPhysicalNumberOfRows(); j++) {
                if (loc2.sheet.getPhysicalNumberOfRows() <= j) return;

                loc1.row = loc1.sheet.getRow(j);
                loc2.row = loc2.sheet.getRow(j);

                if ((loc1.row == null) || (loc2.row == null)) {
                    continue;
                }

                compareDataInRow(loc1, loc2);
            }
        }
    }

    private void compareDataInRow(Locator loc1, Locator loc2) {
        for (int k = 0; k < loc1.row.getLastCellNum(); k++) {
            if (loc2.row.getPhysicalNumberOfCells() <= k) return;

            loc1.cell = loc1.row.getCell(k);
            loc2.cell = loc2.row.getCell(k);

            if ((loc1.cell == null) || (loc2.cell == null)) {
                continue;
            }

            compareDataInCell(loc1, loc2);
        }
    }

    /*private int CELL_TYPE_NUMERIC = 0;
    private int CELL_TYPE_STRING = 1;
    private int CELL_TYPE_FORMULA = 2;
    private int CELL_TYPE_BLANK = 3;
    private int CELL_TYPE_BOOLEAN = 4;
    private int CELL_TYPE_ERROR = 5;*/

    private void compareDataInCell(Locator loc1, Locator loc2) {
        //if (isCellTypeMatches(loc1, loc2)) {
        final CellType loc1cellType = loc1.cell.getCellType();
        //int loc1cellType = loc1.cell.getCellType();
        switch(loc1cellType) {
            case BLANK:
            case STRING:
            case ERROR:
                isCellContentMatches(loc1,loc2);
                break;
            case BOOLEAN:
                isCellContentMatchesForBoolean(loc1,loc2);
                break;
            case FORMULA:
                isCellContentMatchesForFormula(loc1,loc2);
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(loc1.cell)) {
                    isCellContentMatchesForDate(loc1,loc2);
                } else {
                    isCellContentMatchesForNumeric(loc1,loc2);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected cell type: " + loc1cellType);
        }
        //}
        if(this.enableCellFillPatternMatches)
            isCellFillPatternMatches(loc1,loc2);

        if(this.enableCellAlignmentMatches)
            isCellAlignmentMatches(loc1,loc2);

        if(this.enableCellHiddenMatches)
            isCellHiddenMatches(loc1,loc2);

        if(this.enableCellLockedMatches)
            isCellLockedMatches(loc1,loc2);

        if(this.enableCellFontFamilyMatches)
            isCellFontFamilyMatches(loc1,loc2);

        if(this.enableCellFontSizeMatches)
            isCellFontSizeMatches(loc1,loc2);

        if(this.enableCellFontBoldMatches)
            isCellFontBoldMatches(loc1,loc2);

        if(this.enableCellUnderLineMatches)
            isCellUnderLineMatches(loc1,loc2);

        if(this.enableCellFontItalicsMatches)
            isCellFontItalicsMatches(loc1,loc2);

        if(this.enableCellTopBorderMatches)
            isCellBorderMatches(loc1,loc2,'t');

        if(this.enableCellLeftBorderMatches)
            isCellBorderMatches(loc1,loc2,'l');

        if(this.enableCellBottomBorderMatches)
            isCellBorderMatches(loc1,loc2,'b');

        if(this.enableCellRightBorderMatches)
            isCellBorderMatches(loc1,loc2,'r');

        if(this.enableCellFillBackGroundMatches)
            isCellFillBackGroundMatches(loc1,loc2);
    }

    /**
     * Compare number of columns in sheets.
     */
    private void compareNumberOfColumnsInSheets(Locator loc1, Locator loc2) {
        for (int i = 0; i < loc1.workbook.getNumberOfSheets(); i++) {
            if (loc2.workbook.getNumberOfSheets() <= i) return;

            loc1.sheet = loc1.workbook.getSheetAt(i);
            loc2.sheet = loc2.workbook.getSheetAt(i);

            Iterator<Row> ri1 = loc1.sheet.rowIterator();
            Iterator<Row> ri2 = loc2.sheet.rowIterator();

            int num1 = (ri1.hasNext()) ? ri1.next().getPhysicalNumberOfCells() : 0;
            int num2 = (ri2.hasNext()) ? ri2.next().getPhysicalNumberOfCells() : 0;

            if (num1 != num2) {
                String str = String.format(Locale.ROOT, "%s\nActual Excel Sheet-> %s [%d Columns] not match with Expected Excel Sheet-> %s [%d Columns]\n",
                        "<B>Number Of Columns does not Match ::</B>",
                        loc1.sheet.getSheetName(), num1,
                        loc2.sheet.getSheetName(), num2
                );
                listOfDifferences.add(str);
            }
        }
    }

    /**
     * Compare number of rows in sheets.
     */
    private void compareNumberOfRowsInSheets(Locator loc1, Locator loc2) {
        for (int i = 0; i < loc1.workbook.getNumberOfSheets(); i++) {
            if (loc2.workbook.getNumberOfSheets() <= i) return;

            loc1.sheet = loc1.workbook.getSheetAt(i);
            loc2.sheet = loc2.workbook.getSheetAt(i);

            int num1 = loc1.sheet.getPhysicalNumberOfRows();
            int num2 = loc2.sheet.getPhysicalNumberOfRows();

            if (num1 != num2) {
                String str = String.format(Locale.ROOT, "%s\nActual Excel Sheet-> %s [%d rows] not match with Expected Excel Sheet-> %s [%d rows]\n",
                        "<B>Number Of Rows does not Match ::</B>",
                        loc1.sheet.getSheetName(), num1,
                        loc2.sheet.getSheetName(), num2
                );
                listOfDifferences.add(str);
            }
        }

    }

    /**
     * Compare number of sheets.
     */
    private void compareNumberOfSheets(Locator loc1, Locator loc2) {
        int num1 = loc1.workbook.getNumberOfSheets();
        int num2 = loc2.workbook.getNumberOfSheets();
        if (num1 != num2) {
            String str = String.format(Locale.ROOT, "%s\nActual Excel total Sheets [%d Sheets] not match with Expected Excel total Sheets [%d Sheets]\n",
                    "<B>Number of Sheets do not match ::</B>",
                    num1, num2
            );

            listOfDifferences.add(str);

        }
    }

    /**
     * Compare sheet data.
     * @param loc1
     * @param loc2
     */
    private void compareSheetData(Locator loc1, Locator loc2) {
        if(this.enableCompareRowCountInSheets) {
            compareNumberOfRowsInSheets(loc1, loc2);
        }

        if(this.enableCompareColumnCountInSheets) {
            compareNumberOfColumnsInSheets(loc1, loc2);
        }

        if(this.enableCompareDataInAllSheets || (comparisonMode.equals("PARTIAL") && null == this.compareExcelSheetsByNames)) {
            compareDataInAllSheets(loc1, loc2);
        } if(comparisonMode.equals("PARTIAL")) {
            if(null == this.compareExcelSheetsByNames || this.compareExcelSheetsByNames.length == 0) {
               listOfDifferences.add("Sheet names not provided to compare for " + comparisonMode + " Mode");
            }
            compareDataInSelectedSheets(loc1, loc2);
        }
    }

    /**
     * Compare sheet names.
     * @param loc1
     * @param loc2
     */
    private void compareSheetNames(Locator loc1, Locator loc2) {
        for (int i = 0; i < loc1.workbook.getNumberOfSheets(); i++) {
            String name1 = loc1.workbook.getSheetName(i);
            String name2 = (loc2.workbook.getNumberOfSheets() > i) ? loc2.workbook.getSheetName(i) : "";

            if (!name1.equals(name2)) {
                String str = String.format("%s\nActual Excel Sheet-> Name [%s] not match with Expected Excel Sheet-> Name [%s]\n",
                        "<B>Name of the sheets do not match ::</B>",
                        name1, i+1,
                        name2, i+1
                );
                listOfDifferences.add(str);
            }
        }
    }

    /**
     * Formats the message.
     */
    private void addMessage(Locator loc1, Locator loc2, String messageStart, String value1, String value2) {
        String str =
                String.format(Locale.ROOT, "%s\nActual Excel Sheet -> %s -> %s [%s] not match with Expected Excel Sheet -> %s -> %s [%s]\n",
                        messageStart,
                        loc2.sheet.getSheetName(), new CellReference(loc1.cell).formatAsString(), value1,
                        loc2.sheet.getSheetName(), new CellReference(loc2.cell).formatAsString(), value2
                );
        //System.out.println(str);
        listOfDifferences.add(str);
    }

    /**
     * Checks if cell alignment matches.
     */
    private void isCellAlignmentMatches(Locator loc1, Locator loc2) {
        // TODO: check for NPE
        short align1 = loc1.cell.getCellStyle().getAlignment().getCode();
        short align2 = loc2.cell.getCellStyle().getAlignment().getCode();
        if (align1 != align2) {
            addMessage(loc1, loc2,
                    "<B>Cell Alignment does not Match ::</B>",
                    Short.toString(align1),
                    Short.toString(align2)
            );
        }
    }

    /**
     * Checks if cell border bottom matches.
     */
    private void isCellBorderMatches(Locator loc1, Locator loc2, char borderSide) {
        if (!(loc1.cell instanceof XSSFCell)) return;
        XSSFCellStyle style1 = ((XSSFCell)loc1.cell).getCellStyle();
        XSSFCellStyle style2 = ((XSSFCell)loc2.cell).getCellStyle();
        boolean b1, b2;
        String borderName;
        switch (borderSide) {
            case 't': default:
                b1 = style1.getBorderTop().equals(BorderStyle.THIN);
                b2 = style2.getBorderTop().equals(BorderStyle.THIN);
                borderName = "TOP";
                break;
            case 'b':
                b1 = style1.getBorderBottom().equals(BorderStyle.THIN);
                b2 = style2.getBorderBottom().equals(BorderStyle.THIN);
                borderName = "BOTTOM";
                break;
            case 'l':
                b1 = style1.getBorderLeft().equals(BorderStyle.THIN);
                b2 = style2.getBorderLeft().equals(BorderStyle.THIN);
                borderName = "LEFT";
                break;
            case 'r':
                b1 = style1.getBorderRight().equals(BorderStyle.THIN);
                b2 = style2.getBorderRight().equals(BorderStyle.THIN);
                borderName = "RIGHT";
                break;
        }
        if (b1 != b2) {
            addMessage(loc1, loc2,
                    "<B>Cell Border Attributes does not Match ::</B>",
                    (b1 ? "" : "NOT ")+borderName+" BORDER",
                    (b2 ? "" : "NOT ")+borderName+" BORDER"
            );
        }
    }

    /**
     * Checks if cell content matches.
     * @param loc1
     * @param loc2
     */
    private void isCellContentMatches(Locator loc1, Locator loc2) {
        /*String str =
                String.format(Locale.ROOT, "%s\nworkbook1 -> %s -> %s [%s] != workbook2 -> %s -> %s [%s]",
                        "",
                        loc1.sheet.getSheetName(), new CellReference(loc1.cell).formatAsString(), "",
                        loc2.sheet.getSheetName(), new CellReference(loc2.cell).formatAsString(), ""
                );

        System.out.println(str);*/
        // TODO: check for null and non-rich-text cells
        String str1 = String.valueOf(loc1.cell.getRichStringCellValue());
        //String str2 = String.valueOf(loc2.cell.getRichStringCellValue());
        String str2 = String.valueOf(getCellValue(loc2));

        if (!str1.equals(str2)) {
            addMessage(loc1,loc2,CELL_DATA_DOES_NOT_MATCH,str1,str2);
        }
    }

    /**
     * Checks if cell content matches for boolean.
     */
    private void isCellContentMatchesForBoolean(Locator loc1, Locator loc2) {
        boolean b1 = loc1.cell.getBooleanCellValue();
        //boolean b2 = loc2.cell.getBooleanCellValue();
        boolean b2 = Boolean.valueOf(getCellValue(loc2));
        if (b1 != b2) {
            addMessage(loc1,loc2,CELL_DATA_DOES_NOT_MATCH,Boolean.toString(b1),Boolean.toString(b2));
        }
    }

    /**
     * Checks if cell content matches for date.
     */
    private void isCellContentMatchesForDate(Locator loc1, Locator loc2) {
        Date date1 = loc1.cell.getDateCellValue();
        //Date date2 = loc2.cell.getDateCellValue();
        String date2 = getCellValue(loc2);

        if (!date1.equals(date2)) {
            addMessage(loc1, loc2, CELL_DATA_DOES_NOT_MATCH, date1.toString(), date2);
        }
    }


    /**
     * Checks if cell content matches for formula.
     */
    private void isCellContentMatchesForFormula(Locator loc1, Locator loc2) {
        // TODO: actually evaluate the formula / NPE checks
        String form1 = loc1.cell.getCellFormula();
        String form2 = loc2.cell.getCellFormula();
        if (!form1.equals(form2)) {
            addMessage(loc1, loc2, CELL_DATA_DOES_NOT_MATCH, form1, form2);
        }
    }

    /**
     * Checks if cell content matches for numeric.
     */
    private void isCellContentMatchesForNumeric(Locator loc1, Locator loc2) {
        // TODO: Check for NaN
        double num1 = loc1.cell.getNumericCellValue();
        //double num2 = loc2.cell.getNumericCellValue();
        String num2 = getCellValue(loc2);
        if (Double.toString(num1).equals(num2)) {
            addMessage(loc1, loc2, CELL_DATA_DOES_NOT_MATCH, Double.toString(num1), num2);
        }
    }

    private String getCellFillBackground(Locator loc) {
        Color col = loc.cell.getCellStyle().getFillForegroundColorColor();
        return (col instanceof XSSFColor) ? ((XSSFColor)col).getARGBHex() : "NO COLOR";
    }

    /**
     * Checks if cell file back ground matches.
     */
    private void isCellFillBackGroundMatches(Locator loc1, Locator loc2) {
        String col1 = getCellFillBackground(loc1);
        String col2 = getCellFillBackground(loc2);
        if (!col1.equals(col2)) {
            addMessage(loc1, loc2, "<B>Cell Fill Color does not Match ::</B>", col1, col2);
        }
    }
    /**
     * Checks if cell fill pattern matches.
     */
    private void isCellFillPatternMatches(Locator loc1, Locator loc2) {
        // TOOO: Check for NPE
        short fill1 = loc1.cell.getCellStyle().getFillPattern().getCode();
        short fill2 = loc2.cell.getCellStyle().getFillPattern().getCode();
        if (fill1 != fill2) {
            addMessage(loc1, loc2,
                    "<B>Cell Fill pattern does not Match ::</B>",
                    Short.toString(fill1),
                    Short.toString(fill2)
            );
        }
    }

    /**
     * Checks if cell font bold matches.
     */
    private void isCellFontBoldMatches(Locator loc1, Locator loc2) {
        if (!(loc1.cell instanceof XSSFCell)) return;
        boolean b1 = ((XSSFCell)loc1.cell).getCellStyle().getFont().getBold();
        boolean b2 = ((XSSFCell)loc2.cell).getCellStyle().getFont().getBold();
        if (b1 != b2) {
            addMessage(loc1, loc2,
                    CELL_FONT_ATTRIBUTES_DOES_NOT_MATCH,
                    (b1 ? "" : "NOT ")+"BOLD",
                    (b2 ? "" : "NOT ")+"BOLD"
            );
        }
    }

    /**
     * Checks if cell font family matches.
     */
    private void isCellFontFamilyMatches(Locator loc1, Locator loc2) {
        // TODO: Check for NPEs
        if (!(loc1.cell instanceof XSSFCell)) return;
        String family1 = ((XSSFCell)loc1.cell).getCellStyle().getFont().getFontName();
        String family2 = ((XSSFCell)loc2.cell).getCellStyle().getFont().getFontName();
        if (!family1.equals(family2)) {
            addMessage(loc1, loc2, "<B>Cell Font Family does not Match ::</B>", family1, family2);
        }
    }

    /**
     * Checks if cell font italics matches.
     */
    private void isCellFontItalicsMatches(Locator loc1, Locator loc2) {
        if (!(loc1.cell instanceof XSSFCell)) return;
        boolean b1 = ((XSSFCell)loc1.cell).getCellStyle().getFont().getItalic();
        boolean b2 = ((XSSFCell)loc2.cell).getCellStyle().getFont().getItalic();
        if (b1 != b2) {
            addMessage(loc1, loc2,
                    CELL_FONT_ATTRIBUTES_DOES_NOT_MATCH,
                    (b1 ? "" : "NOT ")+"ITALICS",
                    (b2 ? "" : "NOT ")+"ITALICS"
            );
        }
    }

    /**
     * Checks if cell font size matches.
     */
    private void isCellFontSizeMatches(Locator loc1, Locator loc2) {
        if (!(loc1.cell instanceof XSSFCell)) return;
        short size1 = ((XSSFCell)loc1.cell).getCellStyle().getFont().getFontHeightInPoints();
        short size2 = ((XSSFCell)loc2.cell).getCellStyle().getFont().getFontHeightInPoints();
        if (size1 != size2) {
            addMessage(loc1, loc2,
                    "<B>Cell Font Size does not Match ::</B>",
                    Short.toString(size1),
                    Short.toString(size2)
            );
        }
    }

    /**
     * Checks if cell hidden matches.
     */
    private void isCellHiddenMatches(Locator loc1, Locator loc2) {
        boolean b1 = loc1.cell.getCellStyle().getHidden();
        boolean b2 = loc1.cell.getCellStyle().getHidden();
        if (b1 != b2) {
            addMessage(loc1, loc2,
                    "<B>Cell Visibility does not Match ::</B>",
                    (b1 ? "" : "NOT ")+"HIDDEN",
                    (b2 ? "" : "NOT ")+"HIDDEN"
            );
        }
    }

    /**
     * Checks if cell locked matches.
     */
    private void isCellLockedMatches(Locator loc1, Locator loc2) {
        boolean b1 = loc1.cell.getCellStyle().getLocked();
        boolean b2 = loc1.cell.getCellStyle().getLocked();
        if (b1 != b2) {
            addMessage(loc1, loc2,
                    "<B>Cell Protection does not Match ::</B>",
                    (b1 ? "" : "NOT ")+"LOCKED",
                    (b2 ? "" : "NOT ")+"LOCKED"
            );
        }
    }

    /**
     * Checks if cell type matches.
     */
   /* private boolean isCellTypeMatches(Locator loc1, Locator loc2) {
        CellType type1 = loc1.cell.get;
        CellType type2 = loc2.cell.getCellTypeEnum();
        if (type1 == type2) return true;
        addMessage(loc1, loc2,
                "Cell Data-Type does not Match in :: ",
                type1.name(), type2.name()
        );
        return false;
    }*/

    /**
     * Checks if cell under line matches.
     * @param loc1
     * @param loc2
     */
    private void isCellUnderLineMatches(Locator loc1, Locator loc2) {
        // TOOO: distinguish underline type
        if (!(loc1.cell instanceof XSSFCell)) return;
        byte b1 = ((XSSFCell)loc1.cell).getCellStyle().getFont().getUnderline();
        byte b2 = ((XSSFCell)loc2.cell).getCellStyle().getFont().getUnderline();
        if (b1 != b2) {
            addMessage(loc1, loc2,
                    CELL_FONT_ATTRIBUTES_DOES_NOT_MATCH,
                    (b1 == 1 ? "" : "NOT ")+"UNDERLINE",
                    (b2 == 1 ? "" : "NOT ")+"UNDERLINE"
            );
        }
    }

    private String getCellValue(Locator loc) {
        //if (isCellTypeMatches(loc1, loc2)) {
        final CellType loc1cellType = loc.cell.getCellType();
        //int loc1cellType = loc1.cell.getCellType();
        switch (loc1cellType) {
            case BLANK:
            case STRING:
            case ERROR:
                return String.valueOf(loc.cell.getRichStringCellValue());
            case BOOLEAN:
                return String.valueOf(loc.cell.getBooleanCellValue());
            case FORMULA:
                return String.valueOf(loc.cell.getCellFormula());
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(loc.cell)) {
                    return String.valueOf(loc.cell.getDateCellValue());
                } else {
                    return String.valueOf(loc.cell.getNumericCellValue());
                }
            default:
                throw new IllegalStateException("Unexpected cell type: " + loc1cellType);
        }
    }

    private boolean getFlagValue(String mapKey, Map<String, Object> flags){
        return (null != flags.get(mapKey)) ? (Boolean) flags.get(mapKey) : false;
    }
}
