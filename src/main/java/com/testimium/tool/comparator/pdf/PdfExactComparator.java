package com.testimium.tool.comparator.pdf;

import com.testimium.tool.comparator.IToolComparator;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.AssertParameter;
import com.testimium.tool.domain.ComparatorResponse;
import com.testimium.tool.domain.assertions.Assertions;
import com.testimium.tool.exception.ToolComparatorException;
import com.testimium.tool.utility.JsonParserUtility;
import com.testimium.tool.utility.PropertyUtility;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class PdfExactComparator extends IToolComparator<String, String, ComparatorResponse> {

    private Assertions assertions;
    private PDFUtil pdfutil;

    @Override
    public ComparatorResponse compare(String actualFilePath, String expectedFilePath, String assertkey) throws ToolComparatorException {
        ComparatorResponse response = null;
        pdfutil = new PDFUtil();
        pdfutil.setCompareMode(CompareMode.VISUAL_MODE);
        pdfutil.highlightPdfDifference(true);
        pdfutil.setImageDestinationPath(PropertyUtility.getScreenshotLocation());

        try {
            /*AssertParameter assertParam = new JsonParserUtility<AssertParameter>()
                    .parse(TestContext.getTestContext("").getTestExpectedJson(), AssertParameter.class);*/
            AssertParameter assertParam = null;
            if(TestContext.getTestContext("").getTestExpectedJson().isEmpty()) {
                assertParam = new AssertParameter();
            } else {
                assertParam = new JsonParserUtility<AssertParameter>()
                        .parse(TestContext.getTestContext("").getTestExpectedJson(), AssertParameter.class);
            }

            if(StringUtils.isNotEmpty(assertkey) && null != assertParam && null != assertParam.getAssertParams()) {
                this.assertions = assertParam.getAssertParams().get(assertkey);
                response = getComparatorResponse(actualFilePath, expectedFilePath);
            } else if(null != assertParam && null != assertParam.getAssertParams()) {
                for(Map.Entry<String, Assertions> entry: assertParam.getAssertParams().entrySet()) {
                    try {
                        this.assertions = entry.getValue();
                        response = getComparatorResponse(actualFilePath, expectedFilePath);
                        if("FAIL".equals(response.getResult())) {
                            break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ToolComparatorException("PDF Exact Comparison Failed: " + ex.getMessage(), ex);
        }
        return response;
    }

    private ComparatorResponse getComparatorResponse(String actualFilePath, String expectedFilePath) throws IOException {
        ComparatorResponse response;
        boolean result = pdfutil.compare(actualFilePath, expectedFilePath, (null==this.assertions)? 0 : this.assertions.getStartPage(), (null==this.assertions)? 0 : this.assertions.getEndPage());
        response = new ComparatorResponse((result)? "PASS" : "FAIL",
                (result)? "Comparison Successful" : "Comparison Failed",
                (!result) ? Arrays.asList(pdfutil.getDiffFileName()).toString() : "");
        return response;
    }
}
