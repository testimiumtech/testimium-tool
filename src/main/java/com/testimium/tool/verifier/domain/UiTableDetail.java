package com.testimium.tool.verifier.domain;

import com.testimium.tool.domain.CommandParam;
import org.openqa.selenium.WebElement;

import java.util.List;

public class UiTableDetail {

    private List<WebElement> rows;
    private List<WebElement> allHeaderNames;
    private Integer skipRow;
    private Integer skipColumn;
    private CommandParam commandParam;
    private boolean isMatchingCount;
    private boolean isWebElement;

    public UiTableDetail(List<WebElement> rows, List<WebElement> allHeaderNames, Integer skipRow, Integer skipColumn, CommandParam commandParam) {
        this.rows = rows;
        this.allHeaderNames = allHeaderNames;
        this.skipRow = skipRow;
        this.skipColumn = skipColumn;
        this.commandParam = commandParam;
    }

    public List<WebElement> getRows() {
        return rows;
    }

    public List<WebElement> getAllHeaderNames() {
        return allHeaderNames;
    }

    public Integer getSkipRow() {
        return skipRow;
    }

    public Integer getSkipColumn() {
        return skipColumn;
    }

    public CommandParam getCommandParam() {
        return commandParam;
    }

    public void setMatchingCount(boolean matchingCount) {
        isMatchingCount = matchingCount;
    }

    public boolean isMatchingCount() {
        return isMatchingCount;
    }

    public boolean isWebElement() {
        return isWebElement;
    }

    public void setWebElement(boolean webElement) {
        isWebElement = webElement;
    }

    @Override
    public String toString() {
        return "UiTableDetail{" +
                "rows=" + rows +
                ", allHeaderNames=" + allHeaderNames +
                ", skipRow=" + skipRow +
                ", skipColumn=" + skipColumn +
                ", commandParam=" + commandParam +
                ", isMatchingCount=" + isMatchingCount +
                ", isWebElement=" + isWebElement +
                '}';
    }
}
