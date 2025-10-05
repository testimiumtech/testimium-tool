package com.testimium.tool.domain;

import com.testimium.tool.domain.assertions.Assertions;

import java.util.List;
import java.util.Map;

public class TestExpectedJsonOutput {

    private Map<String, Assertions> assertParams;
    private List<Map<String, Object>> jsonFileData;

    public Map<String, Assertions> getAssertParams() {
        return assertParams;
    }

    public void setAssertParams(Map<String, Assertions> assertParams) {
        this.assertParams = assertParams;
    }

    public List<Map<String, Object>> getJsonFileData() {
        return jsonFileData;
    }

    public void setJsonFileData(List<Map<String, Object>> jsonFileData) {
        this.jsonFileData = jsonFileData;
    }

    /*public static void main(String[] args) throws JsonProcessingException, CommandException, JsonParsingException {
        String json = "{  \"assertParams\": {   \"verifyExpected-1\": {    \"totalRowCount\": 2   },   \"verifyExpected-2\": {    \"matchingCount\": 1,    \"totalRowCount\": 2,    \"expectedParams\": {      \"Last Name\": \"AGRAWAL\",      \"First Name\": \"SANDEEP\",      \"Email\": \"sandeepagarwal0305@gmail.com\"     }   },   \"verifyExpected-3\": {    \"matchingCount\": 1,    \"expectedParams\": {     \"Last Name\": \"AGRAWAL\",     \"First Name\": \"AYAAN\",     \"Email\": \"aglr26.sandeep@gmail.com\"    }   }  } }";

        TestExpectedJsonOutput assertParam = new JsonParser<TestExpectedJsonOutput>().parse(json, TestExpectedJsonOutput.class);
        System.out.println(assertParam.getAssertParams().toString());

        //Predicate<Assertions> isTotalRowCount = asst -> null != asst.getTotalRowCount() && asst.getTotalRowCount() == 3;
       // Predicate<Assertions> isTotalRowCount = asst -> null != asst.getTotalRowCount() && asst.getTotalRowCount() == 3;
        assertParam.getAssertParams().entrySet().stream().filter(entry-> null !=entry.getValue()).
                                forEach(entry->{
            System.out.println(entry.getValue().getExpectedParams().toString());
        });

    }*/
}
