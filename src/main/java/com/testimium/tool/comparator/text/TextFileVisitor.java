package com.testimium.tool.comparator.text;

import com.testimium.tool.utility.HtmlGenerator;
import org.apache.commons.text.diff.CommandVisitor;

import java.io.IOException;

/*
 * Custom visitor for file comparison which stores comparison & also generates
 * HTML in the end.
 */
class TextFileVisitor implements CommandVisitor<Character> {

    // Spans with red & green highlights to put highlighted characters in HTML
    private static final String DELETION = "<span style=\"background-color: #FB504B\">${text}</span>";
    private static final String INSERTION = "<span style=\"background-color: #45EA85\">${text}</span>";

    private String left = "";
    private String right = "";

    @Override
    public void visitKeepCommand(Character c) {
        // For new line use <br/> so that in HTML also it shows on next line.
        String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
        // KeepCommand means c present in both left & right. So add this to both without
        // any
        // highlight.
        left = left + toAppend;
        right = right + toAppend;
    }

    @Override
    public void visitInsertCommand(Character c) {
        // For new line use <br/> so that in HTML also it shows on next line.
        String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
        // InsertCommand means character is present in right file but not in left. Show
        // with green highlight on right.
        right = right + INSERTION.replace("${text}", "" + toAppend);
    }

    @Override
    public void visitDeleteCommand(Character c) {
        // For new line use <br/> so that in HTML also it shows on next line.
        String toAppend = "\n".equals("" + c) ? "<br/>" : "" + c;
        // DeleteCommand means character is present in left file but not in right. Show
        // with red highlight on left.
        left = left + DELETION.replace("${text}", "" + toAppend);
    }

    public String generateHTML(String templateName) throws IOException {
        // Get template & replace placeholders with left & right variables with actual
        // comparison
        return new HtmlGenerator().generateTextComparisonHTML(templateName, left, right);
    }
}