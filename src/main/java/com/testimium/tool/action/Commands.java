package com.testimium.tool.action;
/**
 * @author Sandeep Agrawal
 */
public enum Commands {
    OPENURL("OpenUrl"),
    CLICK("Click"),
    DOUBLECLICK("DoubleClick"),
    SETELEMENT("SetElement"),
    GETELEMENT("GetElement"),
    WAIT("Wait"),
    SETELEMENTS("SetElements"),
    SWITCHTAB("SwitchTab"),
    CLEAR("Clear"),
    SETELEMENTSELECT("SetElementSelect"),
    EXECUTEJS("ExecuteJS"),
    EXECUTESQL("ExecuteSQL"),
    VERIFYELEMENTBYJS("VerifyElementByJS"),
    VERIFYHTMLTABLE("VerifyHtmlTable"),
    CLOSEBROWSERTAB("CloseBrowserTab"),
    VERIFYELEMENTISDISPLAYED("VerifyElementIsDisplayed"),
    VERIFYELEMENTISENABLED("VerifyElementIsEnabled"),
    VERIFYELEMENTISSELECTED("VerifyElementIsSelected"),
    VERIFYELEMENTISNOTDISPLAYED("VerifyElementIsNotDisplayed"),
    VERIFYELEMENTISNOTENABLED("VerifyElementIsNotEnabled"),
    VERIFYELEMENTISNOTSELECTED("VerifyElementIsNotSelected"),
    VERIFYELEMENT("VerifyElement"),
    VERIFYELEMENTS("VerifyElements"),
    VERIFYELEMENTSELECT("VerifyElementSelect"),
    VERIFYELEMENTISPRESENT("VerifyElementIsPresent"),
    VERIFYELEMENTISNOTPRESENT("VerifyElementIsNotPresent"),
    VERIFYSQLRESPONSE("VerifySQLResponse"),
    UPLOADFILE("UploadFile"),
    PRESSKEY("PressKey"),
    HANDLEFAILOVER("HandleFailOver"),
    SELECTHTMLTABLEROWS("SelectHtmlTableRows"),
    EXECUTEBATCHSCRIPT("ExecuteBatchScript"),
    COMPAREFILE("CompareFile"),
    CLEARBROWSERCACHE("ClearBrowserCache"),
    TERMINATE("Terminate"),
    OPENNEWBROWSERTAB("OpenNewBrowserTab"),
    LOGMESSAGE("LogMessage"),
    SAVEASPDF("SaveAsPDF"),
    IF("If"),
    EXECUTETEST("ExecuteTest"),
    COPY("Copy"),
    VERIFYELEMENTBYCSS("VerifyElementByCSS"),
    CLICKICON("ClickIcon"),
    VERIFYBROWSERTAB("VerifyBrowserTab"),
    VERIFYCANVASASIMAGE("VerifyCanvasAsImage"),
    CLOSEALLBROWSERTAB("CloseAllBrowserTab"),
    SETGLOBALVARIABLE("SetGlobalVariable"),
    CLICKOSELEMENT("ClickOSElement"),
    SETBROWSERTIMEZONE("SetBrowserTimezone"),
    RESETBROWSERTIMEZONE("ResetBrowserTimezone"),
    COPYFILE("CopyFile"),
    LOOP("Loop"),
    SWITCHTOIFRAME("SwitchToIFrame"),
    SETIFRAMEELEMENT("SetIFrameElement"),
    ENDIFRAME("EndIFrame"),
    SAVEASIMAGE("SaveAsImage"),
    COMPAREDATE("CompareDate"),
    REFRESHBROWSER("RefreshBrowser"),
    CLEARSELECT("ClearSelect"),
    CLEARCHECKBOX("ClearCheckbox"),
    //InternalOperations
    INVALID("Invalid Operation");

    private String value;

    /**
     *
     * @param value
     */
    private Commands(String value) {
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
    public static Commands identifyOperation(String identifier) {
        if (identifier != null) {
            for (Commands optValue : Commands.values()) {
                if (identifier.equalsIgnoreCase(optValue.toString())) {
                    return optValue;
                }
            }
        }
        return INVALID;
    }
}
