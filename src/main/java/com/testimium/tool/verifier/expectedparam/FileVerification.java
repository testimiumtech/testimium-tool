package com.testimium.tool.verifier.expectedparam;

import com.testimium.tool.domain.assertions.Assertions;
import com.testimium.tool.exception.FileReaderException;
import com.testimium.tool.exception.VerificationException;
import com.testimium.tool.reader.FileReaderFactory;
import com.testimium.tool.utility.FileUtility;
import com.testimium.tool.utility.PropertyUtility;
import com.testimium.tool.verifier.util.ConvertElementListToMap;
import com.testimium.tool.verifier.domain.UiTableDetail;
import com.testimium.tool.verifier.util.VerifyMethods;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FileVerification implements ExpectedParamTypeVerification {
    private Assertions assertions;
    private List<Map<String, Object>> actual;
    private UiTableDetail uiTableDetail;
    private StringBuilder builder;

    public FileVerification(Assertions assertions, List<Map<String, Object>> actual) {
        this.uiTableDetail = null;
        this.assertions = assertions;
        this.actual = actual;
    }

    public FileVerification(Assertions assertions, UiTableDetail uiTableDetail) {
        this.actual = null;
        this.assertions = assertions;
        this.uiTableDetail = uiTableDetail;
    }

    @Override
    public void verify() throws VerificationException {

        int size = (null == this.uiTableDetail) ? this.actual.size() : this.uiTableDetail.getRows().size() - this.uiTableDetail.getSkipRow();

        if(null != this.assertions.getTotalRowCount()) {
            /*assertThat(size).as(MessageFormat.format(
                    "\nAssertion Failed... \nExpected result row count is {0} which is not match with actual count {1}",
                    new Object[] {this.assertions.getTotalRowCount(), size})
            ).isEqualTo(this.assertions.getTotalRowCount());*/
            new VerifyMethods().assertElement((size == this.assertions.getTotalRowCount()) ? "Row/record count should be " + size : "Row/record count is " + size,
                    "Row/record count should be " + this.assertions.getTotalRowCount());
        }

        try {
            List<Map<String, Object>> expected = (List<Map<String, Object>>)FileReaderFactory.getInstance().readFile(
                    this.assertions.getExpectedParamType().toUpperCase(), FileUtility.getAbsolutePath(
                            PropertyUtility.getExternalDirPath() + "//" + this.assertions.getFileName()));

            /*assertThat(size).as(
                    MessageFormat.format("\nAssertion Failed...\n Expected param count does not match with actual count", ""))
                    .isEqualTo(expected.size());*/
            new VerifyMethods().assertElement((size == expected.size()) ? "Row/record count should be " + size : "Row/record count is " + size,
                    "Row/record count should be " + expected.size());

            if(null != this.uiTableDetail) {
                this.uiTableDetail.setMatchingCount(true);
                this.actual = ConvertElementListToMap.getListOfRows(this.uiTableDetail);
            }
            /*expected.stream().forEach(expectedMap->
                    assertThat(this.actual).as(
                            MessageFormat.format("\nAssertion Failed...\n Expected param does not match with actual Result",""))
                            .contains(expectedMap)
            );*/
           /* expected.forEach(expectdmap->{
               *//* this.actual.stream().forEach(actualMap->
                        assertThat(actualMap)
                                .as(MessageFormat.format("\nAssertion Failed...\n Expected param does not match with actual Result",
                                        new Object[]{TestContext.getTestContext("").getTestInputMap().toString(), actualMap}))
                                .containsAllEntriesOf(expectdmap)
                );*//*

                assertThat(this.actual)
                        .as(MessageFormat.format("\nAssertion Failed...\n Expected param does not match with actual Result. <br/><br/><B>Expected Value:</B> {0}",  expectdmap))
                        .contains(expectdmap);
            });*/
            //assertThat(compare(expected)).isEqualTo(true);
           /* if(!compare(expected)) {
                throw new VerificationException("<br>Verification failed - Found differences between actual and expected record : <br>" + builder.toString());
            }*/
            new VerifyMethods().assertElement(this.actual, expected);

            //TODO Show the difference in table
            //compare(expected);
            this.actual.clear();

        } catch (FileReaderException ex) {
            throw new VerificationException("VerificationException: " + ex.getMessage(), ex);
        } /*catch (NotSupportedException nex) {
            throw new VerificationException("VerificationException: " + nex.getMessage(), nex);
        }*/ catch (IOException e) {
            throw new VerificationException("VerificationException: " + e.getMessage(), e);
        }
    }

    /*private boolean compare(List<Map<String, Object>> expected) {
        boolean isPass = true;
        boolean isSuccess = true;
        builder = new StringBuilder();
        builder.append("<HTML><BODY>" +
                "<TABLE border=1>" +
                "<TR align='center'>" +
                    "<TH>#Row</TH><TH>Actual</TH><TH></TH><TH>Expected</TH></TR>" +
                "</TR><TR>");
        int actualCount = 0;
        for(int itr=0; itr < expected.size(); itr++) {
            Iterator<Map.Entry<String, Object>> ecpectedIterator = expected.get(itr).entrySet().iterator();
            Map<String, Object> actualRow = (null == this.actual || this.actual.size() == 0) ? new HashMap<>() : this.actual.get(itr);
            String actualStr = actualRow.toString();
            String expectedStr = expected.get(itr).toString();
            while (ecpectedIterator.hasNext()) {
                Map.Entry expectedEntry = ecpectedIterator.next();
                //System.out.println("Key : " + entry.getKey() + " value : " + entry.getValue());
                //Check if key present in actual
                if(actualRow.containsKey(String.valueOf(expectedEntry.getKey()))){
                      if( (null !=actualRow.get(expectedEntry.getKey()) &&  null == expectedEntry.getValue())
                              || (null != expectedEntry.getValue() && !expectedEntry.getValue().equals(actualRow.get(expectedEntry.getKey())))) {
                          isPass = false;
                          isSuccess = false;
                          String expDataTypeMismatch = "";
                          String actDataTypeMismatch = "";
                          //Check value data type as well
                          if(!expectedEntry.getValue().getClass().equals(actualRow.get(expectedEntry.getKey()).getClass())){
                              actDataTypeMismatch = "\nData type mismatch with expected value" +
                                      "\n" + actualRow.get(expectedEntry.getKey()).getClass();
                              expDataTypeMismatch = "\nData type mismatch with actual value" +
                                      "\n" + expectedEntry.getValue().getClass();
                          }

                          expectedStr = expectedStr.replace(String.valueOf(expectedEntry.getKey()), "<mark title='"+ expectedEntry.getKey() + " value does not match with actual record" + expDataTypeMismatch + "'>"+ expectedEntry.getKey() + "</mark>");
                          //expectedStr = expectedStr.replace(String.valueOf(entry.getValue()), "<mark title='Key value does not match with actual record'>"+ entry.getValue() + "</mark>");
                          actualStr = actualStr.replace(String.valueOf(expectedEntry.getKey()), "<mark title='" + expectedEntry.getKey() + " value does not match with expected record" + actDataTypeMismatch + "'>"+ expectedEntry.getKey() + "</mark>");
                          //actualStr = actualStr.replace(String.valueOf(actualRow.get(entry.getKey())), "<mark title='Key value does not match with expected record'>"+ actualRow.get(entry.getKey()) + "</mark>");
                      }
                } else {
                    isPass = false;
                    isSuccess = false;
                    expectedStr = expectedStr.replace(String.valueOf(expectedEntry.getKey()), "<mark title='Actual record doest not contain this header/column/key'>"+ expectedEntry.getKey() + "</mark>");
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
        builder.append("</TABLE></BODY></HTML>");
        return isSuccess;
    }*/

   /* public static void main(String[] args) throws FileReaderException, NotSupportedException {
        Stream<Map<String, Object>> stream1 = FileReaderFactory.getInstance().readFile("CSV", "C:\\Sandeep\\Projects\\testimium\\POC\\Selenium\\codebase\\SmartTestingToolFramework\\sttf\\SmartTestingFramework\\testfiles\\test.csv")
                .getListOfMap().stream();
        Stream<Map<String, Object>> stream2 = FileReaderFactory.getInstance().readFile("CSV", "C:\\Sandeep\\Projects\\testimium\\POC\\Selenium\\codebase\\SmartTestingToolFramework\\sttf\\SmartTestingFramework\\testfiles\\test.csv")
                .getListOfMap().stream();

        //System.out.print(stream1.filter(two -> stream2.allMatch(e -> e.entrySet()))).collect(Collectors.toList()).size());
    }*/
}
