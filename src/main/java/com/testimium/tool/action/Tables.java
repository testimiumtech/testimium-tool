package com.testimium.tool.action;

public enum Tables {
    JQGRIDTABLE("JqGridTable"),
    SLICKGRIDTABLE("SlickGridTable"),
    REACTGRIDTABLE("ReactGridTable"),
    HTMLTABLE("HtmlTable"),
    SQLTABLE("SQLTable"),
    INVALID("Invalid Table");

    private String value;

    /**
     *
     * @param value
     */
    private Tables(String value) {
        this.value = value;
    }

    /**
     *
     * @return String
     */
    public String getTable() {
        return value;
    }

    /**
     * Validate if given options support by system and return the type else return default INVALID Option.
     * @param identifier
     * @return
     */
    public static Tables identifyOperation(String identifier) {
        if (identifier != null) {
            for (Tables optValue : Tables.values()) {
                if (identifier.equalsIgnoreCase(optValue.toString())) {
                    return optValue;
                }
            }
        }
        return INVALID;
    }
}
