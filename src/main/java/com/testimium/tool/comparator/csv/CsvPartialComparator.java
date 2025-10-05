package com.testimium.tool.comparator.csv;

import com.testimium.tool.action.ComparatorType;
import com.testimium.tool.comparator.IToolComparator;
import com.testimium.tool.comparator.text.TextPartialComparator;
import com.testimium.tool.domain.ComparatorResponse;
import com.testimium.tool.exception.ToolComparatorException;

public class CsvPartialComparator extends IToolComparator<String, String, ComparatorResponse> {
    @Override
    public ComparatorResponse compare(String actualFilePath, String expectedFilePath, String assertkey) throws ToolComparatorException {

        TextPartialComparator comparatorTool = (TextPartialComparator) ComparatorType.valueOf(ComparatorType.class,"TEXT_PARTIAL_COMPARATOR").getInstance();
        comparatorTool.setOutputFileName("CsvPartialDiff");
        //comparatorTool.setExcludePattern(this.excludePattern);
        ComparatorResponse compRes = null;
        try {
            compRes = comparatorTool.compare(actualFilePath, expectedFilePath, null);
        } catch (ToolComparatorException ex) {
            //TODO Exception Handling
            ex.printStackTrace();
            throw new ToolComparatorException("CSV Partial Comparison Failed Exception: " + ex.getMessage(), ex);
        }
        return compRes;
    }
}
