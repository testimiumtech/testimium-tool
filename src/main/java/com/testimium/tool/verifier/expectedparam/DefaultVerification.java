package com.testimium.tool.verifier.expectedparam;

import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.assertions.Assertions;
import com.testimium.tool.exception.VerificationException;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.verifier.util.ConvertElementListToMap;
import com.testimium.tool.verifier.domain.UiTableDetail;
import com.testimium.tool.verifier.util.VerifyMethods;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultVerification implements ExpectedParamTypeVerification {
    private Assertions assertions;
    private List<Map<String, Object>> actual;
    private UiTableDetail uiTableDetail;
    private StringBuilder builder;

    public DefaultVerification(Assertions assertions, List<Map<String, Object>> actual) {
        this.uiTableDetail = null;
        this.assertions = assertions;
        this.actual = actual;
    }

    public DefaultVerification(Assertions assertions, UiTableDetail uiTableDetail) {
        this.actual = null;
        this.assertions = assertions;
        this.uiTableDetail = uiTableDetail;
    }

    @Override
    public void verify() throws VerificationException {
        LogUtil.logTestCaseMsg("Actual Map: " + ((null == this.uiTableDetail) ? this.actual.toString() : this.uiTableDetail.toString()));
        LogUtil.logTestCaseMsg("Expected Map" + assertions.toString());
        if(null != this.assertions.getTotalRowCount()) {
            int size = (null == this.uiTableDetail) ? this.actual.size() : this.uiTableDetail.getRows().size() - this.uiTableDetail.getSkipRow();
           /* assertThat(size).as(MessageFormat.format(
                    "\nAssertion Failed... \nNo Actual Record found", new Object[] {})
            ).isNotZero();*/
            //assertElement("","");

            /*assertThat(size).as(MessageFormat.format(
                    "<br>Verification Failed... <br>Expected row count should be {0} is not matching with actual row count {1}",
                    new Object[] {this.assertions.getTotalRowCount(), size})
            ).isEqualTo(this.assertions.getTotalRowCount());*/

            new VerifyMethods().assertElement((size == this.assertions.getTotalRowCount()) ? "Row/record count should be " + size : "Row/record count is " + size,
                    "Row/record count should be " + this.assertions.getTotalRowCount());

        }

        //TODO Fix Me remove syso and put logs
        if(null != this.assertions.getTableRowNum() && this.assertions.getTableRowNum().length > 0) {
            TestContext.getTestContext("").clearInputMap();
            TestContext.getTestContext("")
                    .getTestInputMap().putAll(this.assertions.getExpectedParams());
            specificRowVerification(this.assertions.getTableRowNum());
        } else if(null != this.assertions.getExpectedParams()) {
            TestContext.getTestContext("").clearInputMap();
            TestContext.getTestContext("")
                    .getTestInputMap().putAll(this.assertions.getExpectedParams());
            if(null != this.assertions.getMatchingCount()) {
                if(null != this.uiTableDetail) {
                    this.uiTableDetail.setMatchingCount(true);
                    this.actual = ConvertElementListToMap.getListOfRows(this.uiTableDetail);
                }
                //TODO - Need to fix the matching count and actual stream
                Long  matchCount = this.actual.stream().filter(map-> map.entrySet().containsAll(
                        TestContext.getTestContext("").getTestInputMap().entrySet()))
                        .count();
                this.actual.clear();

               /* assertThat(matchCount.intValue())
                        .as(MessageFormat.format(
                                "\nAssertion Failed... \nExpected matching count {0} does not match with actual record count {1} <br> Expected Map: " + new GsonBuilder().setPrettyPrinting().create().toJson(TestContext.getTestContext("").getTestInputMap().toString()),
                                new Object[] {this.assertions.getMatchingCount().intValue(), matchCount, TestContext.getTestContext("").getTestInputMap().toString()})
                        ).isEqualTo(this.assertions.getMatchingCount().intValue());*/
               /* TestContext.getTestContext("")
                        .getTestInputMap().putAll(this.assertions.getExpectedParams());
                List expectedList = new ArrayList<Map<String, Object>>();
                expectedList.add(TestContext.getTestContext("").getTestInputMap());
                if(!compareMatchingCount(expectedList, this.actual)) {
                    throw new VerificationException("<br>Verification failed - Found differences between actual and expected record : <br>" + builder.toString());
                }*/
                new VerifyMethods().assertElement((matchCount.intValue() == this.assertions.getMatchingCount().intValue()) ? "Matching rows/records should be " + matchCount.intValue() : matchCount.intValue() + " rows/records are matching",
                        "Matching rows/records should be " + this.assertions.getMatchingCount().intValue());

            } else if(null != this.uiTableDetail){
                this.uiTableDetail.setMatchingCount(false);
                this.actual = ConvertElementListToMap.getListOfRows(this.uiTableDetail);
                /*this.actual.stream().forEach(actualMap->
                                assertThat(actualMap)
                                        .as(MessageFormat.format("\nAssertion Failed...\n Expected param does not match with actual Result",
                                                new Object[]{TestContext.getTestContext("").getTestInputMap().toString(), actualMap}))
                                        .containsAllEntriesOf(TestContext.getTestContext("").getTestInputMap())
                        );*/

               /* assertThat(this.actual.stream().filter(map -> map.entrySet().containsAll(
                        TestContext.getTestContext("").getTestInputMap().entrySet())).count()).as(
                        MessageFormat.format("\nAssertion Failed...\n Expected param ({0}) does not match with actual Result",
                                new Object[]{TestContext.getTestContext("").getTestInputMap().toString()}))
                        .isEqualTo(this.actual.size());*/
                TestContext.getTestContext("")
                        .getTestInputMap().putAll(this.assertions.getExpectedParams());
                List expectedList = new ArrayList<Map<String, Object>>();
                expectedList.add(TestContext.getTestContext("").getTestInputMap());
                /*if(!compare(expectedList)) {
                    throw new VerificationException("<br>Verification failed - Found differences between actual and expected record : <br>" + builder.toString());
                }*/
                new VerifyMethods().assertElement(this.actual, expectedList);
            } else {
                /*this.actual.stream().forEach(actualMap->
                        assertThat(actualMap)
                                .as(MessageFormat.format("Assertion Failed... Expected result does not match with actual result. <br/><br/><B>Expected Value:</B> {0}\"",
                                        new Object[]{TestContext.getTestContext("").getTestInputMap().toString()}))
                                .containsAllEntriesOf(TestContext.getTestContext("").getTestInputMap())
                );*/
                List expectedList = new ArrayList<Map<String, Object>>();
                expectedList.add(TestContext.getTestContext("").getTestInputMap());
                /*if(!compare(expectedList)) {
                    throw new VerificationException("<br>Verification failed - Found differences between actual and expected record : <br>" + builder.toString());
                }*/
                new VerifyMethods().assertElement(this.actual, expectedList);
            }
        }
    }

    /**
     * Method is to verify the specific index of list of map
     * @param rowNum
     */
    private void specificRowVerification(Integer[] rowNum) throws VerificationException {
        List<Map<String, Object>> expectedList = new ArrayList<>();
        expectedList.add(TestContext.getTestContext("").getTestInputMap());
        List<Map<String, Object>> actualRowList = null;

        for (Integer rowNm : rowNum) {
            // After iterating row completely, add in to list.
            Map<String, Object> actualRow =
                    (null == this.uiTableDetail) ? this.actual.get(rowNm-1) : ConvertElementListToMap.getSingleRowMap(this.uiTableDetail, rowNm-1);
            LogUtil.logTestCaseMsg("specificRowVerification for row-" + (rowNm-1) + ": - " + actualRow.toString());
            //List actualList = new ArrayList<Map<String, Object>>();
            //this.actual = new ArrayList<>();
            //this.actual.add(actualRow);
            /*if(!compare(expectedList)) {
                throw new VerificationException("<br>Verification failed - Found differences between actual and expected record : <br>" + builder.toString());
            }*/
            actualRowList = new ArrayList<>();
            actualRowList.add(actualRow);
            new VerifyMethods().assertElement(actualRowList, expectedList);
            /*assertThat(actualRow).as(
                    MessageFormat.format("\nAssertion Failed...\n {0} Expected param does not match with actual Result {1}",
                            new Object[]{TestContext.getTestContext("").getTestInputMap().toString(), actualRow}))
                    .containsAllEntriesOf(TestContext.getTestContext("").getTestInputMap());*/
        }
    }


    private boolean compareMatchingCount(List<Map<String, Object>> expected, List<Map<String, Object>> actuals) {
        boolean isPass = true;
        boolean isSuccess = true;
        builder = new StringBuilder();
        builder.append("<HTML><BODY>" +
                "<TABLE border=1>" +
                "<TR align='center'>" +
                "<TH>#Row</TH><TH>Actual</TH><TH></TH><TH>Expected</TH></TR>" +
                "</TR><TR>");
        int totalMatchRowCount = 0;
        for(int itr=0; itr < expected.size(); itr++) {
            List<Map<String, Object>> ext = new ArrayList<Map<String, Object>>();
            ext.add(expected.get(itr));
            Stream.of(actuals).filter((Predicate<? super List<Map<String, Object>>>) ext).count();
            for(int actr=0; actr < actuals.size(); actr++) {
                Iterator<Map.Entry<String, Object>> ecpectedIterator = expected.get(itr).entrySet().iterator();
                Map<String, Object> actualRow = (null == actuals || actuals.size() == 0) ? new HashMap<>() : actuals.get(actr);
                String actualStr = actualRow.toString();
                String expectedStr = expected.get(itr).toString();
                while (ecpectedIterator.hasNext()) {
                    Map.Entry entry = ecpectedIterator.next();
                    //System.out.println("Key : " + entry.getKey() + " value : " + entry.getValue());
                    //Check if key present in actual
                    if (actualRow.containsKey(String.valueOf(entry.getKey()))) {
                        //TODO check value data type as well
                        if ((null != actualRow.get(entry.getKey()) && null == entry.getValue())
                                || (null != entry.getValue() && !entry.getValue().equals(actualRow.get(entry.getKey())))) {
                            isPass = false;
                            isSuccess = false;
                            expectedStr = expectedStr.replace(String.valueOf(entry.getKey()), "<mark title='" + entry.getKey() + " value does not match with actual record'>" + entry.getKey() + "</mark>");
                            //expectedStr = expectedStr.replace(String.valueOf(entry.getValue()), "<mark title='Key value does not match with actual record'>"+ entry.getValue() + "</mark>");
                            actualStr = actualStr.replace(String.valueOf(entry.getKey()), "<mark title='" + entry.getKey() + " value does not match with expected record'>" + entry.getKey() + "</mark>");
                            //actualStr = actualStr.replace(String.valueOf(actualRow.get(entry.getKey())), "<mark title='Key value does not match with expected record'>"+ actualRow.get(entry.getKey()) + "</mark>");
                        }
                    } else {
                        isPass = false;
                        isSuccess = false;
                        expectedStr = expectedStr.replace(String.valueOf(entry.getKey()), "<mark title='Actual record doest not contain this header/column/key'>" + entry.getKey() + "</mark>");
                    }
                }

                if(!isPass){
                    builder.append("<TD>" + (itr + 1) + "</TD>");
                    builder.append("<TD>" + actualStr + "</TD>");
                    builder.append("<TD>X</TD>");
                    builder.append("<TD>" + expectedStr + "</TD>");
                    isPass = true;
                }

                builder.append("</TR>");
            }
        }
        builder.append("</TABLE></BODY></HTML>");
        return isSuccess;
    }
}
