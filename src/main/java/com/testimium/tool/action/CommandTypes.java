package com.testimium.tool.action;


import com.testimium.tool.command.*;

import java.util.function.Supplier;

/**
 * @author Sandeep Agrawal
 */
public enum CommandTypes {
    OPENURL(OpenUrlCmd::new),
    CLICK(ClickCmd::new),
    DOUBLECLICK(DoubleClickCmd::new),
    SETELEMENT(SetElementCmd::new),
    //GETELEMENT(GetElementCmd::new),
    WAIT(WaitCmd::new),
    SETELEMENTS(SetElementsCmd::new),
    SWITCHTAB(SwitchTabCmd::new),
    CLEAR(ClearCmd::new),
    SETELEMENTSELECT(SetElementSelectCmd::new),
    EXECUTEJS(ExecuteJSCmd::new),
    EXECUTESQL(ExecuteSQLCmd::new),
    VERIFYELEMENTBYJS(VerifyElementByJSCmd::new),
    VERIFYHTMLTABLE(VerifyHtmlTableCmd::new),
    CLOSEBROWSERTAB(CloseBrowserTabCmd::new),
    VERIFYELEMENTISDISPLAYED(VerifyElementIsDisplayedCmd::new),
    VERIFYELEMENTISENABLED(VerifyElementIsEnabledCmd::new),
    VERIFYELEMENTISSELECTED(VerifyElementIsSelectedCmd::new),
    VERIFYELEMENTISNOTDISPLAYED(VerifyElementIsNotDisplayedCmd::new),
    VERIFYELEMENTISNOTENABLED(VerifyElementIsNotEnabledCmd::new),
    VERIFYELEMENTISNOTSELECTED(VerifyElementIsNotSelectedCmd::new),
    VERIFYELEMENT(VerifyElementCmd::new),
    VERIFYELEMENTS(VerifyElementsCmd::new),
    VERIFYELEMENTSELECT(VerifyElementSelectCmd::new),
    VERIFYELEMENTISPRESENT(VerifyElementIsPresentCmd::new),
    VERIFYELEMENTISNOTPRESENT(VerifyElementIsNotPresentCmd::new),
    VERIFYSQLRESPONSE(VerifySQLResponseCmd::new),
    UPLOADFILE(UploadFileCmd::new),
    PRESSKEY(PressKeyCmd::new),
    HANDLEFAILOVER(HandleFailOverCmd::new),
    SELECTHTMLTABLEROWS(SelectHtmlTableRowsCmd::new),
    EXECUTEBATCHSCRIPT(ExecuteBatchScriptCmd::new),
    COMPAREFILE(CompareFileCmd::new),
    CLEARBROWSERCACHE(ClearBrowserCacheCmd::new),
    TERMINATE(TerminateCmd::new),
    OPENNEWBROWSERTAB(OpenNewBrowserTabCmd::new),
    LOGMESSAGE(LogMessageCmd::new),
    SAVEASPDF(SaveAsPDFCmd::new),
    IF(IfCmd::new),
    EXECUTETEST(ExecuteTestCmd::new),
    COPY(CopyCmd::new),
    VERIFYELEMENTBYCSS(VerifyElementByCSSCmd::new),
    CLICKICON(ClickIconCmd::new),
    VERIFYBROWSERTAB(VerifyBrowserTabCmd::new),
    VERIFYCANVASASIMAGE(VerifyCanvasAsImageCmd::new),
    CLOSEALLBROWSERTAB(CloseAllBrowserTabCmd::new),
    SETGLOBALVARIABLE(SetGlobalVariableCmd::new),
    CLICKOSELEMENT(ClickOSElementCmd::new),
    SETBROWSERTIMEZONE(SetBrowserTimezoneCmd::new),
    RESETBROWSERTIMEZONE(ResetBrowserTimezoneCmd::new),
    COPYFILE(CopyFileCmd::new),
    LOOP(LoopCmd::new),
    SWITCHTOIFRAME(SwitchToIFrameCmd::new),
    SETIFRAMEELEMENT(SetIFrameElementCmd::new),
    ENDIFRAME(EndIFrameCmd::new),
    SAVEASIMAGE(SaveAsImageCmd::new),
    COMPAREDATE(CompareDateCmd::new),
    REFRESHBROWSER(RefreshBrowser::new);
    //InternalOperations
    //INVALID("Invalid Operation");

    private Supplier<Command> parserInstantiator;

    public Command getInstance() {
        return parserInstantiator.get();
    }

    CommandTypes(Supplier<Command> parserInstantiator) {
        this.parserInstantiator = parserInstantiator;
    }

}
