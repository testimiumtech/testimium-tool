package com.testimium.tool.command.verification;

import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.assertions.Assertions;
import com.testimium.tool.exception.JsonParsingException;
import com.testimium.tool.exception.VerificationException;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.utility.StringUtility;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public interface AssertMethods {

    private Assertions getAssertion(String args[]) throws JsonParsingException {
        if(null != args && args.length > 1) {
            for (int itr = 0; itr < args.length; itr++) {
                switch (args[itr]) {
                    case "-assertKey":
                        return TestContext.getTestContext("").getAssertParameter().getByAssetKey(args[itr+1]);
                }
            }

        }
        return null;
    }

    default boolean assertElement(String actual, String expected, String args[]) throws VerificationException, JsonParsingException {
        String message = "";
        if(null != args && args.length > 1) {
            for (int itr = 0; itr < args.length; itr++) {
                switch (args[itr]) {
                    case "-removeSpace":
                        actual = actual.trim();
                        break;
                    case "-copiedVariable":
                        expected = String.valueOf(TestContext.getTestContext("").getCopiedVariable().get(args[itr+1])).replace("''","").replaceAll("(\\$\\$|##|@@)", " ").trim();
                        break;
                    case "-globalVariable":
                        expected = String.valueOf(TestContext.getTestContext("").getGlobalVariable().get(args[itr+1])).replace("''","").replaceAll("(\\$\\$|##|@@)", " ").trim();
                        break;
                    case "-matchAny":
                        Assertions assertions = getAssertion(args);
                        boolean isMatch = (null != assertions) ? assertions.getMatchAny().contains(actual)
                                : TestContext.getTestContext("").getAssertParameter().getFirst().getMatchAny().contains(actual);
                        expected = TestContext.getTestContext("").getAssertParameter().getFirst().getMatchAny().toString();
                        message = "Actual does not match with any of the expected values/list.";
                        if(isMatch) {
                            actual = "matched";
                            expected = "matched";
                        }
                        break;
                    case "-exactMatch":
                        Assertions assertion = getAssertion(args);
                        boolean isListMatch = (null != assertion) ? assertion.getExactMatch().get(0).equals(actual)
                                : TestContext.getTestContext("").getAssertParameter().getFirst().getExactMatch().get(0).equals(actual);
                        expected = TestContext.getTestContext("").getAssertParameter().getFirst().getExactMatch().toString();
                        message = "Actual not exactly match with the expected list of values.";
                        if(isListMatch) {
                            actual = "matched";
                            expected = "matched";
                        }
                        break;
                }
            }
        }
        return assertElement(actual, expected, message);
    }


    default boolean assertElement(String actual, String expected) throws VerificationException {
        return assertElement(actual, expected, "");
    }

    default boolean assertElement(String actual, String expected, String message) throws VerificationException {
        LogUtil.logTestCaseMsg("Actual Value = " + actual);
        LogUtil.logTestCaseMsg("Expected Value = " + expected);
        LogUtil.logTestCaseMsg("Message = " + message);
        String actualStr = null;
        String expectedStr = null;
        boolean isPass = true;
        boolean isActualEmpty = StringUtils.isEmpty(actual);
        boolean isExpectedEmpty = StringUtils.isEmpty(expected);

        if(isActualEmpty && !isExpectedEmpty) {
            isPass = false;
            actualStr = "Value is Null/Empty";
            expectedStr = expected;
        } else if(isExpectedEmpty && !isActualEmpty) {
            isPass = false;
            actualStr = actual;
            expectedStr = "Value is Null/Empty";
        } else if(!actual.equals(expected)) {
            isPass = false;
            actualStr = actual;
            expectedStr = expected;
        }

        if(!isPass){
            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<HTML><BODY>" +
                    (("".equals(message))? "": "<p>" + message + "</p><br>") +
                    "<TABLE border=1>" +
                    "<TR align='center' style='background-color: lightblue'>" +
                    "<TH><H6>#Row</H6></TH><TH><H6>Actual</H6></TH><TH></TH><TH><H6>Expected</H6></TH></TR>" +
                    "</TR><TR>");
            htmlBuilder.append("<TD style='background-color: lightgrey'>" + 1 + "</TD>");
            htmlBuilder.append("<TD>" + actualStr + "</TD>");
            htmlBuilder.append("<TD><img src='../../../../images/symbols/not-equal-sign.png' width='42' height='42' style='vertical-align:middle'></TD>");
            htmlBuilder.append("<TD>" + expectedStr + "</TD>");
            htmlBuilder.append("</TR>");
            htmlBuilder.append("</TABLE></BODY></HTML>");
            throw new VerificationException("<br>Verification failed - Found differences between actual and expected record : <br>" + htmlBuilder.toString());
        }
        return isPass;
    }


    /**
     * T
     * @param actual
     * @param expected
     * @return
     * @throws VerificationException
     */
    default boolean assertElement(List<Map<String, Object>> actual, List<Map<String, Object>> expected) throws VerificationException {
        boolean isPass = true;
        boolean isSuccess = true;
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<HTML><BODY>" +
                "<TABLE border=1>" +
                "<TR align='center' style='background-color: lightblue'>" +
                "<TH><H6>#Row</H6></TH><TH><H6>Actual</H6></TH><TH></TH><TH><H6>Expected</H6></TH></TR>" +
                "</TR><TR>");
        //int totalMatchRowCount = 0;
        for(int itr=0; itr < expected.size(); itr++) {
            Iterator<Map.Entry<String, Object>> ecpectedIterator = expected.get(itr).entrySet().iterator();
            Map<String, Object> actualRow = (null == actual || actual.size() == 0) ? new HashMap<>() : actual.get(itr);
            String actualStr = actualRow.toString();
            String expectedStr = expected.get(itr).toString();
            List<String> differences = new ArrayList<>();
            int diffCount = 1;
            while (ecpectedIterator.hasNext()) {
                String diffStr = "";
                Map.Entry expectedEntry = ecpectedIterator.next();
                //System.out.println("Key : " + entry.getKey() + " value : " + entry.getValue());
                //Check if key present in actual
                String placeHolderKey = new StringUtility().getPlaceHolderBetweenStrings("<%=","=%>", String.valueOf(expectedEntry.getValue()));
                Object expectedValue = (null == expectedEntry.getValue()) ? null :
                        ((null != TestContext.getTestContext("").getGlobalVariable().get(placeHolderKey))
                        ? String.valueOf(TestContext.getTestContext("").getGlobalVariable().get(placeHolderKey))
                                : expectedEntry.getValue());


                /*String expectedValue = (null != TestContext.getTestContext("").getGlobalVariable().get(placeHolderKey))
                        ? String.valueOf(TestContext.getTestContext("").getGlobalVariable().get(placeHolderKey)) : String.valueOf(expectedEntry.getValue());*/
                Set<String> actualKeys = actualRow.keySet();
                String expectedKey = String.valueOf(expectedEntry.getKey()).trim();
                if(actualKeys.contains(expectedKey)){
                    /*if( (null !=actualRow.get(expectedEntry.getKey()) &&  null == expectedValue)
                            || (null != expectedValue && !expectedValue.equals(actualRow.get(expectedEntry.getKey())))) {*/
                    if( (null !=actualRow.get(expectedKey) &&  null == expectedValue)
                            || (null != expectedValue && null == actualRow.get(expectedKey))
                            || (null != expectedValue && !expectedValue.equals(actualRow.get(expectedKey)))) {
                        //System.out.print(expectedKey + " , ");
                        if(diffCount ==1) {
                            expectedStr = expectedStr + "<br> <H6>Differences are:</H6> ";
                            actualStr = actualStr + "<br> <H6>Differences are:</H6> ";
                        }
                        isPass = false;
                        isSuccess = false;
                        String expDataTypeMismatch = "";
                        String actDataTypeMismatch = "";
                        //Check value data type as well
                        if(null == actualRow.get(expectedKey) && null != expectedValue) {
                            actDataTypeMismatch = "<br>  <b>Data Type</b>: Value is NULL";
                            expDataTypeMismatch = "<br>  <b>Data Type</b>: " + expectedValue.getClass().getName();
                        } else if(null != actualRow.get(expectedKey) && null == expectedValue) {
                            actDataTypeMismatch = "<br>  <b>Data Type</b>: " + actualRow.get(expectedKey).getClass();
                            expDataTypeMismatch = "<br>  <b>Data Type</b>: Value is NULL";
                        } else if(!expectedValue.getClass().equals(actualRow.get(expectedKey).getClass())){
                            actDataTypeMismatch = "<br>  <b>Data Type</b>: " + actualRow.get(expectedKey).getClass().getName();
                            expDataTypeMismatch = "<br>  <b>Data Type</b>: " + expectedValue.getClass().getName();
                        }

                        expectedStr = expectedStr.replace(expectedKey + "=", "<mark title='"+ expectedKey + " value does not match with actual record" + expDataTypeMismatch + "'>"+ expectedKey + "=</mark>");
                        expectedStr = expectedStr.replace("<%=" + placeHolderKey + "=%>", "" + expectedValue);
                        //expectedStr = expectedStr.replace(String.valueOf(entry.getValue()), "<mark title='Key value does not match with actual record'>"+ entry.getValue() + "</mark>");
                        actualStr = actualStr.replace(expectedKey  + "=", "<mark title='" + expectedKey + " value does not match with expected record" + actDataTypeMismatch + "'>"+ expectedKey + "=</mark>");
                        //actualStr = actualStr.replace(String.valueOf(actualRow.get(entry.getKey())), "<mark title='Key value does not match with expected record'>"+ actualRow.get(entry.getKey()) + "</mark>");

                        expectedStr = expectedStr + "<br>" + diffCount + ". <p><b>" + expectedKey + "</b> value does not match with actual record. " + expDataTypeMismatch + "</p>";
                        actualStr = actualStr + "<br>" + diffCount + ". <p><b>" + expectedKey + "</b> value does not match with expected record." + actDataTypeMismatch + "</p>";
                        diffCount++;
                    }
                    expectedStr = expectedStr.replace("<%=" + placeHolderKey + "=%>", "" + expectedValue);
                } else {
                    if(diffCount == 1) {
                        expectedStr = expectedStr + "<br> <H6>Differences are:</H6> ";
                        actualStr = actualStr + "<br> <H6>Differences are:</H6> ";
                    }
                    isPass = false;
                    isSuccess = false;
                    expectedStr = expectedStr.replace(String.valueOf(expectedKey), "<mark title='Actual record doest not contain this header/column/key'>"+ expectedKey + "</mark>");
                    expectedStr = expectedStr + "<br>" + diffCount + ". <p><b>" + expectedKey + "</b> Actual record doest not contain this header/column/key. " + "</p>";
                    actualStr = actualStr + "<br>" + diffCount + ". <p><b>" + expectedKey + "</b> Actual record doest not contain this header/column/key. " + "</p>";
                }
            }
            if(!isPass){
                htmlBuilder.append("<TD style='background-color: lightgrey'>" + (itr + 1) + "</TD>");
                htmlBuilder.append("<TD>" + actualStr + "</TD>");
                htmlBuilder.append("<TD><img src='../../../../images/symbols/not-equal-sign.png' width='42' height='42' style='vertical-align:middle'></TD>");
                htmlBuilder.append("<TD>" + expectedStr + "</TD>");
                isPass = true;
            }

            htmlBuilder.append("</TR>");
        }
        htmlBuilder.append("</TABLE></BODY></HTML>");
        if(!isSuccess)
            throw new VerificationException("<br>Verification failed - Found differences between actual and expected record : <br>" + htmlBuilder.toString());

        return isSuccess;
    }


    default boolean assertElement(List<Object> actualList, List<Object> expectedList, String args[]) throws VerificationException, JsonParsingException {
        String actual = actualList.toString();
        String expected = null;
        String message = "";
        boolean isMatch = false;
        if(null != args && args.length > 1) {
            for (int itr = 0; itr < args.length; itr++) {
                switch (args[itr]) {
                    case "-copiedVariable":
                        expected = String.valueOf(TestContext.getTestContext("").getCopiedVariable().get(args[itr + 1]));
                        isMatch = actualList.contains(expected);
                        message = "Actual does not match with expected value.";
                        break;
                    case "-globalVariable":
                        expected = String.valueOf(TestContext.getTestContext("").getGlobalVariable().get(args[itr+1]));
                        //expectedList = new ArrayList<Object>(Collections.singleton(String.valueOf(TestContext.getTestContext("").getGlobalVariable().get(args[itr+1]))));
                        isMatch = actualList.contains(expected);
                        message = "Actual does not match with expected value.";
                        break;
                    case "-matchAny":
                        Assertions assertions = getAssertion(args);
                        isMatch = (null != assertions) ? assertions.getMatchAny().contains(actualList)
                                : TestContext.getTestContext("").getAssertParameter().getFirst().getMatchAny().contains(actualList);
                        expected = TestContext.getTestContext("").getAssertParameter().getFirst().getMatchAny().toString();
                        message = "Actual does not match with any of the expected values/list.";
                        break;
                    case "-exactMatch":
                        Assertions assertion = getAssertion(args);
                        isMatch = (null != assertion) ? assertion.getExactMatch().containsAll(actualList)
                                : TestContext.getTestContext("").getAssertParameter().getFirst().getExactMatch().containsAll(actualList);
                        expected = TestContext.getTestContext("").getAssertParameter().getFirst().getExactMatch().toString();
                        message = "Actual not exactly match with the expected list of values.";
                        break;
                }
            }
        }

        if(isMatch) {
            actual = "matched";
            expected = "matched";
            message = "";
        }

        return assertElement(actual, expected, message);
    }
}
