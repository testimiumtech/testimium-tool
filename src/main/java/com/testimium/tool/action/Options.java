package com.testimium.tool.action;

public enum Options {
    SCROLLTOELEMENT("ScrollToElement"),
    MOVETOELEMENT("MoveToElement"),
    INVALID("Invalid Option");

    private String value;

    /**
     *
     * @param value
     */
    private Options(String value) {
        this.value = value;
    }

    /**
     *
     * @return String
     */
    public String getOption() {
        return value;
    }

    /**
     * Validate if given options support by system and return the type else return default INVALID Option.
     * @param identifier
     * @return
     */
    public static Options identifyOperation(String identifier) {
        if (identifier != null) {
            for (Options optValue : Options.values()) {
                if (identifier.equalsIgnoreCase(optValue.toString())) {
                    return optValue;
                }
            }
        }
        return INVALID;
    }
}
