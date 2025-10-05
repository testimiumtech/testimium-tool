package com.testimium.tool.sample;

import com.testimium.tool.base.DriverManager;
import com.testimium.tool.exception.LocatorNotFoundException;
import com.testimium.tool.locator.LocatorFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SlickGridInteraction {
    public static void main(String[] args) throws LocatorNotFoundException {
        //sample();
        WebDriver driver = DriverManager.getInstance().getWebDriver();
        driver.get("http://mleibman.github.io/SlickGrid/examples/example12-fillbrowser.html");
        List<WebElement> rows =  driver.findElements(LocatorFactory.getByLocator("XPath", "//*[@class=\"grid-canvas\"]/div"));
        List <WebElement> allHeaderNames = driver.findElements(LocatorFactory.getByLocator("XPath", "//*[@class=\"slick-header-columns\"]/div/span[1]"));
        List<Map<String, Object>> rowList = getListOfRows(rows, allHeaderNames,5, 0);
        //System.out.println(rowList.toString());
    }

    private static List<Map<String, Object>> getListOfRows(List<WebElement> rows, List<WebElement> allHeaderNames, Integer skipRow, Integer skipColumn) {
        List<Map<String, Object>> allTableData = new ArrayList<Map<String, Object>>();
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getInstance().getWebDriver();
        //js.executeScript("grid.scrollRowIntoView("+1020+",true)","");
        int[] rowCount = {1};
        rows.stream().forEach(rowElement->{
            //if(!skipRow.contains((rowCount[0]))) {
            if(null != skipRow && rowCount[0] > skipRow) {
                // Getting specific row with each iteration
                // String specificRowLoc = "//*[@class='jqgrid-listscreen ui-jqgrid-btable ui-common-table']/tbody/tr[" + i + "]";
                // Locating only cells of specific row.
                js.executeScript("grid.scrollRowIntoView("+(rowCount[0]+1)+",true)","");
                List<WebElement> allColumnsEle = rowElement.findElements(By.tagName("div"));
                // After iterating row completely, add in to list.
                allTableData.add(getColumnValueMap(allHeaderNames, skipColumn, allColumnsEle));
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
        // Iterating each cell
        //for (int j = 0; j < allColumnsEle.size(); j++) {
        //int[] columnCount = {0};
        //allColumnsEle.stream().forEach(columnElement -> {
        for (int j = skipColumn.intValue(); j < allColumnsElement.size(); j++) {
            //try {
            // Getting cell value
            // We will put in to map with header name and value with iteration
            // Get jth index value from allHeaderNames and jth cell value of row
            //if(TestServiceContext.getServiceContext("").getTestInputMap().containsKey((allHeaderNames.get(j)).getText().trim()))
            //js.executeScript("grid.scrollRowIntoView("+j+")","");
            js.executeScript("arguments[0].scrollIntoView();",allHeaderNames.get(j));

            /*if (isDefaultVerify)
                eachRowData.put(allHeaderNames.get(j).getText().replaceAll(" ", "").trim(), allColumnsElement.get(j).getText().trim());
            else*/
            eachRowData.put(allHeaderNames.get(j).getText().trim(), allColumnsElement.get(j).getText().trim());
           /*} catch (StaleElementReferenceException ex) {
                       ex.printStackTrace();
                   }*/
        }
        System.out.println(eachRowData.toString());
        return eachRowData;
    }



    private static void sample() {
        // TODO Auto-generated method stub

        /*System.setProperty("webdriver.chrome.driver","D:\\SeleniumRef\\chromedriver.exe");*/
        WebDriver driver = DriverManager.getInstance().getWebDriver();
        //driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);

        driver.get("http://mleibman.github.io/SlickGrid/examples/example3-editing.html");
        System.out.println("Title :"+ driver.getTitle());

        //Javascript executor
        JavascriptExecutor js = (JavascriptExecutor)driver;

        //Returns the DIV element matching class grid-canvas, which contains every data row currently being rendered in the DOM.
        WebElement rowCount = (WebElement) js.executeScript("return grid.getCanvasNode();");

        //Read column title and count
        ArrayList<Map<String, Object>> ColString = (ArrayList<Map<String, Object>>) js.executeScript("return grid.getColumns();");
        int colCount = ColString.size();
        System.out.println("Number of Columns:"+colCount);
        for (int i = 0; i < ColString.size(); i++) {
            Map<String, Object> s = (Map<String, Object>) ColString.get(i);
            System.out.println("Column Name :"+s.get("name"));
        }

        // Get the total number of data rows being rendered in the DOM.
        long rowCnt =  (long) js.executeScript("return $(grid.getCanvasNode()).children().length;");
        System.out.println("Rows :"+ rowCnt);


        //To update a cell in slickgrid.
        js.executeScript("grid.gotoCell(0,1,true)");
        driver.findElement(By.tagName("textarea")).sendKeys("Updated...");
        driver.findElement(By.xpath("//button[contains(text(),'Save')]")).click();

        //Read cell values, column index can be parameterized which is hard coded now.
        for (int i=0; i<rowCnt; i++){
            js.executeScript("grid.scrollRowIntoView("+i+",false)", "");
            WebElement dom =(WebElement) js.executeScript("return grid.getCellNode("+i+",0);");
            System.out.println("--->"  +dom.getText());
        }

        driver.close();
    }
}
