package com.testimium.tool.comparator.pdf;

import com.testimium.tool.comparator.IToolComparator;
import com.testimium.tool.comparator.text.TextSearchComparator;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.AssertParameter;
import com.testimium.tool.domain.ComparatorResponse;
import com.testimium.tool.domain.assertions.Assertions;
import com.testimium.tool.exception.ToolComparatorException;
import com.testimium.tool.utility.JsonParserUtility;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class PdfSearchComparator extends IToolComparator<String, String, ComparatorResponse> {
    private Assertions assertions;
    private PDFUtil pdfutil;

    @Override
    public ComparatorResponse compare(String actualFilePath, String nothing, String assertkey) throws ToolComparatorException {
        ComparatorResponse response = null;
        pdfutil = new PDFUtil();
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
                response = getComparatorResponse(actualFilePath);
            } else if(null != assertParam && null != assertParam.getAssertParams()) {
                for(Map.Entry<String, Assertions> entry: assertParam.getAssertParams().entrySet()) {
                    try {
                        this.assertions = entry.getValue();
                        response = getComparatorResponse(actualFilePath);
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
            throw new ToolComparatorException("PDF Search Comparison Failed Exception: " + ex.getMessage(), ex);
        }
        return response;
    }

    @Deprecated
    private ComparatorResponse getComparatorResponse(String actualFilePath, String expectedFilePath) throws IOException {
        ComparatorResponse resp = new TextSearchComparator().compare(
                Arrays.asList(pdfutil.getText(actualFilePath, (null==this.assertions)? 0 : this.assertions.getStartPage(), (null==this.assertions)? 0 : this.assertions.getEndPage()).split(" ")),
                appendDateToSearch(assertions.getWordsToSearch(), assertions.getDateToSearch()));
        return resp;
    }

    private ComparatorResponse getComparatorResponse(String actualFilePath) throws IOException {
        ComparatorResponse resp = new TextSearchComparator().compare(
                pdfutil.getText(actualFilePath, (null==this.assertions)? 0 : this.assertions.getStartPage(),
                        (null==this.assertions)? 0 : this.assertions.getEndPage()),
                appendDateToSearch(assertions.getWordsToSearch(), assertions.getDateToSearch()), assertions.getGlobalKey());
        return resp;
    }

    /*private String[] appendDateToSearch(){

        if(null != this.assertions.getDateToSearch()) {
            List<String> wordToSearchList = Arrays.asList(this.assertions.getWordsToSearch());
            if(null != this.assertions && null != this.assertions.getDateToSearch()) {
                this.assertions.getDateToSearch().forEach(dSearch->{
                    wordToSearchList.add(DateUtility.getDate(
                            String.valueOf(dSearch.get("dateFormat")),
                            Integer.valueOf(String.valueOf(dSearch.get("numberOfDays"))),
                            String.valueOf(dSearch.get("isPastOrFutureDate"))));
                });

                //Convert List<String> to String[]
                String array[] = new String[wordToSearchList.size()];
                for(int j =0;j<wordToSearchList.size();j++){
                    array[j] = wordToSearchList.get(j);
                }
                return array;
            }
        }

        return this.assertions.getWordsToSearch();
    }*/
}
