package com.testimium.tool.comparator.text;

import com.testimium.tool.comparator.IToolComparator;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.AssertParameter;
import com.testimium.tool.domain.ComparatorResponse;
import com.testimium.tool.domain.assertions.Assertions;
import com.testimium.tool.exception.JsonParsingException;
import com.testimium.tool.exception.ToolComparatorException;
import com.testimium.tool.utility.HtmlGenerator;
import com.testimium.tool.utility.JsonParserUtility;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Map;
import java.util.stream.Stream;

public class TextPartialComparator extends IToolComparator<String, String, ComparatorResponse> {

    private static final String UNMATCHED = "<span style=\"background-color: #FB504B\">${text}</span>";
    int modificationCount;
    String expectedLine;
    String actualLine;
    String[] excludePattern;
    private String outputFile = "TextPartialDiff";

    /*@Override
    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }*/

    @Override
    public ComparatorResponse compare(String actualFilePath, String expectedFilePath, String assertKey) throws ToolComparatorException {
        ComparatorResponse response = null;
        StringBuilder actualBuilder = new StringBuilder();
        StringBuilder expectedBuilder = new StringBuilder();
        try {
            //Setting default filename
            if(null == getOutputFileName() || getOutputFileName().isEmpty() || getOutputFileName().isBlank()) setOutputFileName(this.outputFile);

            LineIterator actual = FileUtils.lineIterator(new File(actualFilePath), "utf-8");
            LineIterator expected = FileUtils.lineIterator(new File(expectedFilePath), "utf-8");
            prepareExcludedPatter(assertKey);
            int lineCount = 1;
            while (expected.hasNext() || actual.hasNext()) {
                expectedLine = (expected.hasNext() ? expected.nextLine() : "");
                actualLine = (actual.hasNext() ? actual.nextLine() : "");
                replacePatternWithEmpty();
                String[] expectedWords = expectedLine.split(" ");
                for (int itr = 0; itr < expectedWords.length; itr++) {
                    if (!actualLine.contains(expectedWords[itr])) {
                        //expectedMap.put("Line-" + lineCount, UNMATCHED.replace("${text}", expectedWords[itr]));
                        expectedLine = expectedLine.replace(expectedWords[itr], UNMATCHED.replace("${text}", expectedWords[itr]));
                        modificationCount++;
                    }
                }
                actualBuilder.append("<span style=\"color: #45EA85\">Line-" + lineCount + "-   </span>" + actualLine);
                actualBuilder.append("<br/>");
                expectedBuilder.append("<span style=\"color: #45EA85\">Line-" + lineCount + "-   </span>" + expectedLine);
                expectedBuilder.append("<br/>");
                lineCount++;
            }
            response = new ComparatorResponse((modificationCount == 0)? "PASS" : "FAIL",
                    (modificationCount == 0)? "Comparison Successful" : "Comparison Failed",
                    (modificationCount > 0) ? new HtmlGenerator().generateTextComparisonHTML(getOutputFileName(), actualBuilder.toString(), expectedBuilder.toString()) : "");
            ;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ToolComparatorException("Partial Comparison Failed Exception: " + ex.getMessage(), ex);
        }

        return response;
    }

    private void replacePatternWithEmpty(){
        if(null!=this.excludePattern && this.excludePattern.length>0){
            for(int i=0; i<this.excludePattern.length; i++){
                if(null != this.excludePattern[i]) {
                    actualLine = actualLine.replaceAll(this.excludePattern[i], "");
                    expectedLine = expectedLine.replaceAll(this.excludePattern[i], "");
                }
            }
        }
    }

    private void prepareExcludedPatter(String assertKey) throws JsonParsingException {
        //if(StringUtils.isNotEmpty(assertKey)) {
        AssertParameter assertParam = null;
        try {
            /*assertParam = new JsonParserUtility<AssertParameter>()
                    .parse(TestContext.getTestContext("").getTestExpectedJson(), AssertParameter.class);*/
            if(TestContext.getTestContext("").getTestExpectedJson().isEmpty()) {
                assertParam = new AssertParameter();
            } else {
                assertParam = new JsonParserUtility<AssertParameter>()
                        .parse(TestContext.getTestContext("").getTestExpectedJson(), AssertParameter.class);
            }
        } catch (JsonParsingException ex) {
            throw new JsonParsingException("Not able to parse Assert Param json");
        }
            Assertions assertions = null;
            if (StringUtils.isNotEmpty(assertKey) && null != assertParam && null != assertParam.getAssertParams()) {
                assertions = assertParam.getAssertParams().get(assertKey);
                assertions.setExcludePattern(mergeDateExcludePattern(assertions.getExcludePattern(), assertions.getExcludeDatePattern()));
                this.excludePattern = assertions.getExcludePattern();
            } else if (null != assertParam && null != assertParam.getAssertParams()) {
                int count = 0;
                for (Map.Entry<String, Assertions> entry : assertParam.getAssertParams().entrySet()) {
                    assertions = entry.getValue();
                    assertions.setExcludePattern(mergeDateExcludePattern(assertions.getExcludePattern(), assertions.getExcludeDatePattern()));
                    if (count == 0)
                        this.excludePattern = assertions.getExcludePattern();
                    merge(assertions.getExcludePattern());
                    count++;
                }
            }
        //}
    }

    private String[] merge(String[] patter)
    {
        return Stream.of(this.excludePattern, patter)
                .flatMap(Stream::of)		// or Arrays::stream
                .toArray(String[]::new);
    }

    public void setExcludePattern(String[] excludePattern) {
        this.excludePattern = excludePattern;
    }
}
