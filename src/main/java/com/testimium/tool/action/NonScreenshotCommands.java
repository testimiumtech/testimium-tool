package com.testimium.tool.action;
/**
 * @author Sandeep Agrawal
 */
public enum NonScreenshotCommands {
    WAIT("Wait"),
    //SWITCHTAB("SwitchTab"),
    EXECUTESQL("ExecuteSQL"),
    //CLOSEBROWSERTAB("CloseBrowserTab"),
    VERIFYSQLRESPONSE("VerifySQLResponse"),
    EXECUTEBATCHSCRIPT("ExecuteBatchScript"),
    COMPAREFILE("CompareFile"),
    TERMINATE("Terminate"),
    LOGMESSAGE("LogMessage"),
    //EXECUTETEST("ExecuteTest"),
    SETGLOBALVARIABLE("SetGlobalVariable"),
    SETBROWSERTIMEZONE("SetBrowserTimezone"),
    RESETBROWSERTIMEZONE("ResetBrowserTimezone"),
    COPYFILE("CopyFile"),
    COMPAREDATE("CompareDate"),
    //InternalOperations
    INVALID("Invalid Operation");

    private String value;

    /**
     *
     * @param value
     */
    private NonScreenshotCommands(String value) {
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
    public static NonScreenshotCommands identifyOperation(String identifier) {
        if (identifier != null) {
            for (NonScreenshotCommands optValue : NonScreenshotCommands.values()) {
                if (identifier.equalsIgnoreCase(optValue.toString())) {
                    return optValue;
                }
            }
        }
        return INVALID;
    }
}
