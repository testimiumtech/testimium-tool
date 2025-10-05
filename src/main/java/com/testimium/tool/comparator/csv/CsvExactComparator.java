package com.testimium.tool.comparator.csv;

import com.testimium.tool.action.ComparatorType;
import com.testimium.tool.comparator.IToolComparator;
import com.testimium.tool.comparator.text.TextExactComparator;
import com.testimium.tool.domain.ComparatorResponse;
import com.testimium.tool.exception.ToolComparatorException;

public class CsvExactComparator extends IToolComparator<String, String, ComparatorResponse> {
    @Override
    public ComparatorResponse compare(String actualFilePath, String expectedFilePath, String assertkey) throws ToolComparatorException {

        TextExactComparator comparatorTool = (TextExactComparator) ComparatorType.valueOf(ComparatorType.class,"TEXT_EXACT_COMPARATOR").getInstance();
        comparatorTool.setOutputFileName("CsvExactDiff");
        //comparatorTool.setExcludePattern(this.excludePattern);
        ComparatorResponse compRes = null;
        try {
            compRes = comparatorTool.compare(actualFilePath, expectedFilePath, null);
        } catch (ToolComparatorException ex) {
            //TODO Exception Handling
            ex.printStackTrace();
            throw new ToolComparatorException("CSV Exact Comparison Failed Exception: " + ex.getMessage(), ex);
        }
        return compRes;
    }
}
