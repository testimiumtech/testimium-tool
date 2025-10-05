import com.testimium.tool.action.ComparatorType;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.ComparatorResponse;
import com.testimium.tool.exception.ToolComparatorException;
import com.testimium.tool.utility.FileUtility;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class SearchWordFromExcel {
    public static void main(String[] args) throws ToolComparatorException {
        Scanner scanner = new Scanner(System.in);
        ComparatorResponse compRes = null;
        TestContext.getTestContext("").setTestCaseName("SearchWordFromExcel");

        // Directory containing Excel files
        System.out.println("\n\nEnter the location of excel file: ");
        String directoryPath = scanner.nextLine();
                //"D:\\testimium\\POC\\Selenium\\codebase\\SmartTestingToolFramework\\sttf\\SmartTestingFramework\\testcases\\POSRegression"; // Update with your directory path
        System.out.println("\nEnter the word to search: ");
        String searchTerm = scanner.nextLine();
                //"Category"; // Word or sentence to search

        /*File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".xlsx"));*/
        try {
           String[] listOfFiles = FileUtility.getAllFiles(directoryPath);
            Object[] listArray = Arrays.stream(listOfFiles).filter(file->file.endsWith(".xlsx")).toArray();
            if (listArray != null) {
                for (Object fileStr : listArray) {
                    //System.out.println(fileStr);
                    File file = new File(String.valueOf(fileStr));
                    compRes = (ComparatorResponse) ComparatorType.valueOf(ComparatorType.class,
                                    "EXCEL_SEARCH_COMPARATOR").getInstance()
                            .compare(file.getAbsolutePath(), searchTerm, "");
                    System.out.println(compRes.toString());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
