package com.testimium.tool.command;

import com.testimium.tool.context.TestContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * @author Sandeep Agrawal
 */
@Deprecated
public class VerifyCmd {
  private WebDriver driver;
     /* private static VerifyCmd verifyCommand = new VerifyCmd();

    public VerifyCmd() {
        this.driver = DriverManager.getInstance().getWebDriver();
    }

    public static VerifyCmd getInstance() {
        //TODO Fix Me for concurrent access
        return verifyCommand;
    }

    //TODO Have to decide either ConmmandResponse/WebElement/Dynamic as response object
    @Override
    public CommandResponse execute(CommandParam param) {
        DriverManager.getInstance().focusCurrentWindow();
        WebElement rows = driver.findElement(By.xpath("//*[@class='jqgrid-listscreen ui-jqgrid-btable ui-common-table']/tbody"));
        List<Map<String, Object>> dataTableMap = prepareTableMap(rows, Arrays.asList(new String[]{"1"}), null);
        if(null == dataTableMap || dataTableMap.isEmpty()){
            //TODO accurate message or information can also be tracked in the report even it is success.
            System.err.println("No record populated for verification");
        }
        return null;
    }*/

    /**
     *  TODO This method has to be implemented in a dynamic way
     * @param rows
     * @param skipRow
     * @param skipColumn
     * @return
     */
    private List<Map<String, Object>> prepareTableMap(WebElement rows, List<String> skipRow, List<String> skipColumn) {
        List<Map<String, Object>> dataRows = null;
        //To locate rows of table.
        List <WebElement> rows_table = rows.findElements(By.tagName("tr"));
        List<WebElement> columns = driver.findElements(By.xpath("//*[@class='ui-jqgrid-htable ui-common-table ']/thead/tr/th"));
        //To calculate no of rows In table.
        int rows_count = rows_table.size();
        //Loop will execute for all the rows of the table
        if(rows_count > 0)
            dataRows = new ArrayList<>();

        for (int row = 0; row < rows_count; row++) {
            //(null != skipRow && skipRow.contains(String.valueOf(row)))
            if(row == 0)
                continue;
            //To locate columns(cells) of that specific row.
            List < WebElement > Columns_row = rows_table.get(row).findElements(By.tagName("td"));
            //To calculate no of columns(cells) In that specific row.
            int columns_count = Columns_row.size();
            //System.out.println("Number of cells In Row " + row + " are " + columns_count);
            //Loop will execute till the last cell of that specific row.
            Map<String, Object> singleRow = new HashMap<String, Object>();

            for (int column = 0; column < columns_count; column++) {
                if(null != skipColumn && skipColumn.contains(String.valueOf(row)))
                    continue;
                //To retrieve text from the cells.
                String celltext = Columns_row.get(column).getText();
                System.out.println(columns.get(column).getText()+" Cell Value Of row number " + row + " and column number " + column + " Is " + celltext);
                singleRow.put(columns.get(column).getText().replaceAll(" ", "").trim(), celltext);
            }
            assertThat(singleRow).containsAllEntriesOf(TestContext.getTestContext("").getTestInputMap());
            dataRows.add(singleRow);
        }
        return dataRows;
    }
}
