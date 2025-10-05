package com.testimium.tool.comparator;

import com.testimium.tool.exception.ToolComparatorException;
import com.testimium.tool.utility.DateUtility;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class IToolComparator<S, T, R> {
    private String outputFileName = "FileDifference";

    //R compare(S s, T t) throws ToolComparatorException;
    public abstract R compare(S s, T t, String assertkey) throws ToolComparatorException;

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    protected String[] mergeDateExcludePattern(String[] excludePattern, List<Map<String, Object>> excludeDatePattern) {
        String[] newExcludedPattern = null;
        int excludePatternSize = 0;
        if(null != excludeDatePattern && excludeDatePattern.size() > 0) {
            if(null != excludePattern && excludePattern.length > 0) {
                excludePatternSize = excludePattern.length;
                newExcludedPattern = new String[excludePatternSize + excludeDatePattern.size()];

                for (int i = 0; i < excludePatternSize; i++) {
                    newExcludedPattern[i] = excludePattern[i];
                }
            } else {
                newExcludedPattern = new String[excludeDatePattern.size()];
            }
            //excludePatternSize = excludePatternSize + 1;
            for(int itr = 0; itr < excludeDatePattern.size(); itr++) {
                if(null != excludeDatePattern.get(itr) &&  excludeDatePattern.get(itr).size() > 0) {
                    newExcludedPattern[excludePatternSize] = DateUtility.getDate(
                            String.valueOf(excludeDatePattern.get(itr).get("dateFormat")),
                            Integer.valueOf(String.valueOf(excludeDatePattern.get(itr).get("numberOfDays"))),
                            String.valueOf(excludeDatePattern.get(itr).get("isPastOrFutureDate")));
                }
                excludePatternSize = excludePatternSize + 1;
            }
            excludePattern = newExcludedPattern;
        }
        return excludePattern;
    }

    /**
     * Append date into word to search object.
     * @param wordToSearch
     * @param dateToSearch
     * @return String[]
     */
    protected String[] appendDateToSearch(String[] wordToSearch, List<Map<String, Object>> dateToSearch){

        if(null != dateToSearch && dateToSearch.size() > 0) {
            List<String> wordToSearchList = new LinkedList<String>(Arrays.asList(wordToSearch));
            //if(null != this.assertions && null != this.assertions.getDateToSearch()) {
            for(int itr = 0; itr < dateToSearch.size(); itr++) {
                String dateStr = DateUtility.getDate(
                        String.valueOf(dateToSearch.get(itr).get("dateFormat")),
                        Integer.valueOf(String.valueOf(dateToSearch.get(itr).get("numberOfDays"))),
                        String.valueOf(dateToSearch.get(itr).get("isPastOrFutureDate")));
                wordToSearchList.add(dateStr);
            }
            //Convert List<String> to String[]
            String array[] = new String[wordToSearchList.size()];
            for(int j =0;j<wordToSearchList.size();j++){
                array[j] = wordToSearchList.get(j);
            }
            return array;
            //}
        }

        return wordToSearch;
    }
}
