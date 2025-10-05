package com.testimium.tool.action;
/**
 * @author Sandeep Agrawal
 */
public enum NonReportableCommands {
    LOGMESSAGE("LogMessage"),
    IF("If"),
    LOOP("Loop"),
    //InternalOperations
    REPORTABLE_COMMAND("Reportable Command");

    private String value;

    /**
     *
     * @param value
     */
    private NonReportableCommands(String value) {
        this.value = value;
    }

    /**
     *
     * @return String
     */
    public String getOperation() {
        return value;
    }

    /**
     * Validate if given operation support by system and return the type else return default INVALID operation.
     * @param identifier
     * @return
     */
    public static NonReportableCommands identifyOperation(String identifier) {
        if (identifier != null) {
            for (NonReportableCommands optValue : NonReportableCommands.values()) {
                if (identifier.equalsIgnoreCase(optValue.toString())) {
                    return optValue;
                }
            }
        }
        return REPORTABLE_COMMAND;
    }
}
