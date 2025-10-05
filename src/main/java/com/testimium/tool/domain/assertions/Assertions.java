package com.testimium.tool.domain.assertions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Assert object with attributes
 */
public class Assertions {
    private Integer assertId;
    private Integer totalRowCount;
    private Integer matchingCount;
    private Integer[] tableRowNum;
    /**
     * Verification type can be:
     * JSON
     * CSV
     * Excel
     */
    private String expectedParamType;
    private String fileName;
    private String[] wordsToSearch;
    private List<Map<String, Object>> dateToSearch;
    private String[] excludePattern;
    private List<Map<String, Object>> excludeDatePattern;
    private int startPage;
    private int endPage;
    private Map<String, Object> flags;
    private String[] compareExcelSheets;
    private Integer matchCount;
    private List<Object> exactMatch;
    private List<Object> matchAny;
    private String text;

    private Map<String, Object> expectedParams;

    /**
     *
     * @return response integer
     */
    public Integer getAssertId() {
        return assertId;
    }

    public void setAssertId(Integer assertId) {
        this.assertId = assertId;
    }

    /**
     *
     * @return response integer
     */
    public Integer getTotalRowCount() {
        return totalRowCount;
    }

    public void setTotalRowCount(Integer totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    /**
     *
     * @return response integer
     */
    public Integer getMatchingCount() {
        return matchingCount;
    }

    public void setMatchingCount(Integer matchingCount) {
        this.matchingCount = matchingCount;
    }

    /**
     *
     * @return response integer aaray
     */
    public Integer[] getTableRowNum() {
        return tableRowNum;
    }

    public void setTableRowNum(Integer[] tableRowNum) {
        this.tableRowNum = tableRowNum;
    }

    /**
     *
     * @return response String
     */
    public String getExpectedParamType() {
        return expectedParamType;
    }

    public void setExpectedParamType(String expectedParamType) {
        this.expectedParamType = expectedParamType;
    }

    /**
     *
     * @return response String
     */
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     *
     * @return response integer
     */
    public Map<String, Object> getExpectedParams() {
        return expectedParams;
    }

    public void setExpectedParams(Map<String, Object> expectedParams) {
        this.expectedParams = expectedParams;
    }

    /**
     *
     * @return response array of text
     */
    public String[] getWordsToSearch() {
        return wordsToSearch;
    }

    public void setWordsToSearch(String[] wordsToSearch) {
        this.wordsToSearch = wordsToSearch;
    }

    /**
     *
     * @return response list of map
     */
    public List<Map<String, Object>> getDateToSearch() {
        return dateToSearch;
    }

    public void setDateToSearch(List<Map<String, Object>> dateToSearch) {
        this.dateToSearch = dateToSearch;
    }

    /**
     *
     * @return response array of text
     */
    public String[] getExcludePattern() {
        return excludePattern;
    }

    public void setExcludePattern(String[] excludePattern) {
        this.excludePattern = excludePattern;
    }

    /**
     *
     * @return response list of map
     */
    public List<Map<String, Object>> getExcludeDatePattern() {
        return excludeDatePattern;
    }

    public void setExcludeDatePattern(List<Map<String, Object>> excludeDatePattern) {
        this.excludeDatePattern = excludeDatePattern;
    }

    /**
     *
     * @return response integer
     */
    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        if(this.startPage <= 0)
            this.startPage = -1;
        else
            this.startPage = startPage;
    }
    /**
     *
     * @return response integer
     */
    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        if(this.endPage <= 0)
            this.endPage = -1;
        else
            this.endPage = endPage;
    }
    /**
     *
     * @return response map
     */
    public Map<String, Object> getFlags() {
        return flags;
    }

    public void setFlags(Map<String, Object> flags) {
        this.flags = flags;
    }
    /**
     *
     * @return response array of text
     */
    public String[] getCompareExcelSheets() {
        return compareExcelSheets;
    }

    public void setCompareExcelSheets(String[] compareExcelSheets) {
        this.compareExcelSheets = compareExcelSheets;
    }
    /**
     *
     * @return response integer
     */
    public Integer getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(Integer matchCount) {
        this.matchCount = matchCount;
    }
    /**
     *
     * @return response integer
     */
    public List<Object> getExactMatch() {
        return exactMatch;
    }

    public void setExactMatch(List<Object> exactMatch) {
        this.exactMatch = exactMatch;
    }
    /**
     *
     * @return response list of object
     */
    public List<Object> getMatchAny() {
        return matchAny;
    }

    public void setMatchAny(List<Object> matchAny) {
        this.matchAny = matchAny;
    }

    /**
     *
     * @return response String
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return response String
     */
    @Override
    public String toString() {
        return "Assertions{" +
                "assertId=" + assertId +
                ", totalRowCount=" + totalRowCount +
                ", matchingCount=" + matchingCount +
                ", tableRowNum=" + Arrays.toString(tableRowNum) +
                ", expectedParamType='" + expectedParamType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", wordsToSearch=" + Arrays.toString(wordsToSearch) +
                ", excludePattern=" + Arrays.toString(excludePattern) +
                ", excludeDatePattern=" + excludeDatePattern +
                ", startPage=" + startPage +
                ", endPage=" + endPage +
                ", flags=" + flags +
                ", compareExcelSheets=" + Arrays.toString(compareExcelSheets) +
                ", matchCount=" + matchCount +
                ", matchList=" + exactMatch +
                ", matchAny=" + matchAny +
                ", text=" + text +
                ", expectedParams=" + expectedParams +
                '}';
    }
}
