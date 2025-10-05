package com.testimium.tool.domain;

import java.util.List;
import java.util.Map;
/**
 * @author Sandeep Agrawal
 */
public class TestJsonInputData {
    private Map<String, List<Map<String, String>>> formElements;
    private Map<String, List<Map<String, Object>>> scripts;
    private Map<String, String> dbScript;
    private Map<String, Object> script;
    private Map<String, Object> dateFormat;
    private int skipNumberOfNextTestCases;
    private int failNumberOfNextTestCases;
    private boolean useGlobalVariable = false;
    private boolean useCopiedVariable = false;

    public Map<String, List<Map<String, String>>> getFormElements() {
        return formElements;
    }

    public void setFormElements(Map<String, List<Map<String, String>>> formElements) {
        this.formElements = formElements;
    }

    public Map<String, List<Map<String, Object>>> getScripts() {
        return scripts;
    }

    public void setScripts(Map<String, List<Map<String, Object>>> scripts) {
        this.scripts = scripts;
    }

    public Map<String, String> getDbScript() {
        return dbScript;
    }

    public void setDbScript(Map<String, String> dbScript) {
        this.dbScript = dbScript;
    }

    public Map<String, Object> getScript() {
        return script;
    }

    public void setScript(Map<String, Object> script) {
        this.script = script;
    }

    public Map<String, Object> getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(Map<String, Object> dateFormat) {
        this.dateFormat = dateFormat;
    }

    public int getSkipNumberOfNextTestCases() {
        return skipNumberOfNextTestCases;
    }

    public void setSkipNumberOfNextTestCases(int skipNumberOfNextTestCases) {
        this.skipNumberOfNextTestCases = skipNumberOfNextTestCases;
    }

    public int getFailNumberOfNextTestCases() {
        return failNumberOfNextTestCases;
    }

    public void setFailNumberOfNextTestCases(int failNumberOfNextTestCases) {
        this.failNumberOfNextTestCases = failNumberOfNextTestCases;
    }

    public boolean isUseGlobalVariable() {
        return useGlobalVariable;
    }

    public void setUseGlobalVariable(boolean useGlobalVariable) {
        this.useGlobalVariable = useGlobalVariable;
    }

    public boolean isUseCopiedVariable() {
        return useCopiedVariable;
    }

    public void setUseCopiedVariable(boolean useCopiedVariable) {
        this.useCopiedVariable = useCopiedVariable;
    }

    @Override
    public String toString() {
        return "TestJsonInputData{" +
                "formElements=" + formElements +
                ", scripts=" + scripts +
                ", dbScript=" + dbScript +
                ", script=" + script +
                ", skipNumberOfNextTestCases=" + skipNumberOfNextTestCases +
                ", failNumberOfNextTestCases=" + failNumberOfNextTestCases +
                ", useGlobalVariable=" + useGlobalVariable +
                ", useCopiedVariable=" + useCopiedVariable +
                '}';
    }

    /*public static void main(String[] args) throws JsonProcessingException, CommandException {
        String json = "{\"formElements\": {\"CUSTBASICMultiSearch\":[ {\"TestStep\" : \"SetElement  LastName id   AGRAWAL\" }, {\"TestStep\" : \"SetElement  //select[@name='Date']  XPath   WTD_LY  Select\"}, {\"TestStep\" : \"Click duplicatesCheck id\" }]} }";

        TestJsonInputData inputData = new JsonParser<TestJsonInputData>().parse(json, TestJsonInputData.class);
        System.out.println(inputData.toString());
        inputData.getFormElements().entrySet().stream()
                .filter(key-> key.getKey().equalsIgnoreCase("CUSTBASICMultiSearch"))
                .forEach(map ->{
                    System.out.println("value: " + map.getValue());
                    map.getValue().stream().forEach(innerM->{
                        innerM.entrySet().stream().forEach(innerMap-> {
                            if(innerMap.getKey().equalsIgnoreCase("TestStep")) {
                                System.out.println("value1: " + innerMap.getValue());
                                    //TestStep testStep = new TestStep();
                                    String[] operationStep = innerMap.getValue().trim().split("\\s+");
                                    //testStep.setOperation(operationStep[0]);
                                    String[] parms = new String[operationStep.length-1];
                                    for (int i = 1; i < operationStep.length; i++) {
                                        parms[i-1]  = operationStep[i];
                                    }
                                    //perform(props, operationStep[0].trim(), operationStep);
                            }
                        });
                    });
                });
    }*/
}
