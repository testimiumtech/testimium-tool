package com.testimium.tool.verifier;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.command.WaitCmd;
import com.testimium.tool.domain.CommandParam;

import com.testimium.tool.exception.*;
import com.testimium.tool.locator.LocatorFactory;
import com.testimium.tool.reader.PropertyReader;
import com.testimium.tool.verifier.domain.UiTableDetail;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UiTableVerification extends AbstractVerificationType {

    private static UiTableVerification verification = new UiTableVerification();

    public static UiTableVerification getInstance() {
        //TODO Fix Me for concurrent access
        return verification;
    }

    @Override
    public void verify(CommandParam commandParam) throws VerificationException {
        if(null == commandParam.getArgs() || commandParam.getArgs().length < 2)
            throw new VerificationException("Both row and column locator expected as argument for command " + commandParam.getCommand() + "SkilGridTable Verification");
        try {
            new WaitCmd().execute(new CommandParam(null,null, new String[]{"2000"}, null, null));
            List<WebElement> rows = DriverManager.getInstance().getWebDriver().findElements(LocatorFactory.getByLocatorProperty(commandParam.getArgs()[0]));
            List <WebElement> allHeaderNames = DriverManager.getInstance().getWebDriver().findElements(LocatorFactory.getByLocatorProperty(commandParam.getArgs()[1]));

           /* List<WebElement> rows = findElements(LocatorFactory.getByLocatorProperty(commandParam.getArgs()[0]));
            List <WebElement> allHeaderNames = findElements(LocatorFactory.getByLocatorProperty(commandParam.getArgs()[1]));*/

           /* DriverManager.getInstance().getFluentWait().until(
                    ExpectedConditions.visibilityOfElementLocated(LocatorFactory.getByLocatorProperty(commandParam.getArgs()[1])));*/
            //TODO Fix me should be common for all type of tables, so each table should inherit its logic and must implement in AbstractTableVerification.java
            Integer skipRow = 0;
            Integer skipColumn = 0;
            String sqlJsonKey = null;
            if(commandParam.getArgs().length > 2) {
                for (int itr = 2; itr < commandParam.getArgs().length; itr++) {
                    switch(commandParam.getArgs()[itr]) {
                        case "-skipFirstRow":
                            skipRow = 1;
                            break;
                        case "-skipRow":
                            //itr += 1;
                            if(commandParam.getArgs().length > itr) {
                                try {
                                    skipRow = Integer.valueOf(commandParam.getArgs()[itr + 1]);
                                } catch (NumberFormatException nfe){
                                    throw new VerificationException("Expected numeric value with option -skipRow ");
                                }
                            } else
                                throw new VerificationException("value is missing for option -skipRow. \nProvide numeric value as total number of rows required to skip from top.");
                            break;
                        case "-skipFirstColumn":
                            skipColumn = 1;
                            break;
                        case "-skipColumn":
                            if(commandParam.getArgs().length > itr) {
                                try {
                                    skipColumn = Integer.valueOf(commandParam.getArgs()[itr + 1]);
                                } catch (NumberFormatException nfe){
                                    throw new VerificationException("Expected numeric value with option -skipColumn ");
                                }
                            } else
                                throw new VerificationException("value is missing for option -skipColumn. \nProvide numeric value as total number of columns required to skip from top.");
                            break;

                            //TODO how to handle slick grid rows verification by automatically scroll into each non visible rows.
                            // Solution is, pass the row number by using below statement
                            //   executeJavaScript("grid.scrollRowIntoView(" + 100 + ",true)", "");
                    }
                }
            }
            this.setAssertParameter(commandParam.getAssertParam());
            this.assertTest(new UiTableDetail(rows, allHeaderNames, skipRow, skipColumn, commandParam));
            //this.assertTest(getListOfRows(rows, allHeaderNames, skipRow, skipColumn, commandParam));

        } catch (PropertyKeyNotFoundException | LocatorNotFoundException ex){
            throw new VerificationException("Failed to execute command'"
                    + commandParam.getCommand() + "' with param '" + commandParam.toString() + "'. " + ex.getMessage(), ex);
        }  catch (ArrayIndexOutOfBoundsException ex) {
            throw new VerificationException(commandParam.getCommand() + "command accepts either 2 (rows and columns property Key) or 3 (rows and column property Key and expected result json key) arguments.");
        } catch (AssertParamNotFoundException e) {
            throw new VerificationException("AssertParamNotFoundException: " + e.getMessage(), e);
        } catch (JsonParsingException ex) {
            throw new VerificationException("JsonParsingException: " + ex.getMessage(), ex);
        } catch (CommandException ex) {
            throw new VerificationException("WaitCmd CommandException: " + ex.getMessage(), ex);
        }
    }

    private List<Map<String, Object>> getListOfRows(List<WebElement> rows, List<WebElement> allHeaderNames, Integer skipRow, Integer skipColumn, CommandParam commandParam) {
        List<Map<String, Object>> allTableData = new ArrayList<Map<String, Object>>();
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getInstance().getWebDriver();
        int[] rowCount = {1};
        rows.stream().forEach(rowElement->{
            //if(!skipRow.contains((rowCount[0]))) {
            if(null != skipRow && rowCount[0] > skipRow) {
                // Getting specific row with each iteration
                String[] args = PropertyReader.getProperty(commandParam.getArgs()[0]).split(",");
                //List<WebElement> allColumnsEle = rowElement.findElements(By.tagName("div"));
                List<WebElement> allColumnsEle = null;
                //String[] divSplit = null;
                if(args.length == 5) {
                    if(args[4].contains("/")) {
                        allColumnsEle = rowElement.findElements(By.xpath(args[1] + "[" + rowCount[0] +"]" + args[4]));
                    }
                    else
                        allColumnsEle = rowElement.findElements(By.tagName(args[4]));
                }

                allTableData.add(getColumnValueMap(allHeaderNames, skipColumn, allColumnsEle));
            }
            rowCount[0]++;
        });
        return allTableData;
    }

    private Map<String, Object> getColumnValueMap(List<WebElement> allHeaderNames, Integer skipColumn, List<WebElement> allColumnsElement) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getInstance().getWebDriver();
        // Creating a map to store key-value pair data. It will be created for each
        // iteration of row
        Map<String, Object> eachRowData = new LinkedHashMap<>();
        for (int j = skipColumn.intValue(); j < allColumnsElement.size(); j++) {
            // Getting cell value
            // We will put in to map with header name and value with iteration
            // Get jth index value from allHeaderNames and jth cell value of row
            js.executeScript("arguments[0].scrollIntoView();",allHeaderNames.get(j));
            eachRowData.put(allHeaderNames.get(j).getText().trim(), allColumnsElement.get(j).getText().trim());
        }
        return eachRowData;
    }

}
