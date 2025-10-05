package com.testimium.tool.verifier.util;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.reader.PropertyReader;
import com.testimium.tool.verifier.domain.UiTableDetail;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ConvertElementListToMap {

    public static Map<String, Object> getSingleRowMap(UiTableDetail uiTableDetail, int index){

            // Getting specific row with each iteration
            String[] args = PropertyReader.getProperty(uiTableDetail.getCommandParam().getArgs()[0]).split(",");
            //List<WebElement> allColumnsEle = rowElement.findElements(By.tagName("div"));
            List<WebElement> allColumnsEle = null;
            //String[] divSplit = null;
            if(args.length == 5) {
                if(args[4].contains("/")) {
                    allColumnsEle = uiTableDetail.getRows().get(index + uiTableDetail.getSkipRow()).findElements(By.xpath(args[1] + "[" + index + uiTableDetail.getSkipRow() +"]" + args[4]));
                }
                else
                    allColumnsEle = uiTableDetail.getRows().get(index + uiTableDetail.getSkipRow()).findElements(By.tagName(args[4]));
            }

            //allTableData.add(getColumnValueMap(allHeaderNames, skipColumn, allColumnsEle));
            return getColumnValueMap(uiTableDetail.getAllHeaderNames(), uiTableDetail.getSkipColumn(), allColumnsEle);
    }

    public static List<Map<String, Object>> getListOfRows(UiTableDetail uiTableDetail) {
        List<Map<String, Object>> allTableData = new ArrayList<Map<String, Object>>();
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getInstance().getWebDriver();
        int[] rowCount = {1};
        uiTableDetail.getRows().stream().forEach(rowElement->{
            //if(!skipRow.contains((rowCount[0]))) {
            if(null != uiTableDetail.getSkipRow() && rowCount[0] > uiTableDetail.getSkipRow()) {
                // Getting specific row with each iteration
                String[] args = PropertyReader.getProperty(uiTableDetail.getCommandParam().getArgs()[0]).split(",");
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

                //allTableData.add(getColumnValueMap(allHeaderNames, skipColumn, allColumnsEle));
                Map<String, Object> actualMap = getColumnValueMap(uiTableDetail.getAllHeaderNames(), uiTableDetail.getSkipColumn(), allColumnsEle);
                allTableData.add(actualMap);
                /*if(!uiTableDetail.isMatchingCount()) {
                    System.out.println("DefaultVerification for row-" + rowCount[0] + ": - " + actualMap.toString());
                    assertThat(actualMap)
                            .as(MessageFormat.format("\nAssertion Failed...\n Expected param does not match with actual Result",
                                    new Object[]{TestContext.getTestContext("").getTestInputMap().toString(), actualMap}))
                            .containsAllEntriesOf(TestContext.getTestContext("").getTestInputMap());
                }*/
            }
            rowCount[0]++;
        });
        return allTableData;
    }

    private static Map<String, Object> getColumnValueMap(List<WebElement> allHeaderNames, Integer skipColumn, List<WebElement> allColumnsElement) {
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
