package com.testimium.tool.comparator.text;

import com.testimium.tool.comparator.IToolComparator;
import com.testimium.tool.domain.ComparatorResponse;
import com.testimium.tool.exception.ToolComparatorException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.text.diff.StringsComparator;

import java.io.File;

/**
 *
 */
public class TextExactComparator extends IToolComparator<String, String, ComparatorResponse> {

    String outputFileName = "TextExactDiff";

    @Override
    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    @Override
    public ComparatorResponse compare(String actualFilePath, String expectedFilePath, String assertkey) throws ToolComparatorException {
        ComparatorResponse response = null;
        try {
            int modifyCharCount = 0;
            File file1 = new File(actualFilePath);
            File file2 = new File(expectedFilePath);
            LineIterator actualFile = FileUtils.lineIterator(file1, "utf-8");
            LineIterator expectedFile = FileUtils.lineIterator(file2, "utf-8");

            // Initialize visitor.
            TextFileVisitor textFileHelper = new TextFileVisitor();

            // Read file line by line so that comparison can be done line by line.
            while (actualFile.hasNext() || expectedFile.hasNext()) {
                /*
                 * In case both files have different number of lines, fill in with empty
                 * strings. Also append newline char at end so next line comparison moves to
                 * next line.
                 */
                String left = (actualFile.hasNext() ? actualFile.nextLine() : "") + "\n";
                String right = (expectedFile.hasNext() ? expectedFile.nextLine() : "") + "\n";

                // Prepare diff comparator with lines from both files.
                StringsComparator comparator = new StringsComparator(left, right);

                if (comparator.getScript().getLCSLength() > (Integer.max(left.length(), right.length()) * 0.4)) {
                    /*
                     * If both lines have atleast 40% commonality then only compare with each other
                     * so that they are aligned with each other in final diff HTML.
                     */
                    comparator.getScript().visit(textFileHelper);
                } else {
                    /*
                     * If both lines do not have 40% commanlity then compare each with empty line so
                     * that they are not aligned to each other in final diff instead they show up on
                     * separate lines.
                     */
                    StringsComparator leftComparator = new StringsComparator(left, "\n");
                    leftComparator.getScript().visit(textFileHelper);
                    StringsComparator rightComparator = new StringsComparator("\n", right);
                    rightComparator.getScript().visit(textFileHelper);
                }
                modifyCharCount += comparator.getScript().getModifications();
            }
            response = new ComparatorResponse((modifyCharCount == 0)? "PASS" : "FAIL",
                    (modifyCharCount == 0)? "Comparison Successful" : "Comparison Failed",
                    (modifyCharCount > 0) ? textFileHelper.generateHTML(this.outputFileName) : "");
            //return textFileHelper.generateHTML("ExactTextDiff");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ToolComparatorException("Exact Comparison Failed Exception: " + ex.getMessage(), ex);
        }

        return response;
    }

    /*@Override
    public String getOutputFileName() {
        return this.outputFileName;
    }*/
}
