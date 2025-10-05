package com.testimium.tool.action;


import com.testimium.tool.parser.*;

import java.util.function.Supplier;

/**
 * Enum based factory to maintain and responsible to handle parser service instantiation.
 * @author Sandeep Agrawal
 */
public enum ParserType {
    TEXT_DEFAULT_PARSER(DefaultTextParser::new),
    TXT_DEFAULT_PARSER(DefaultTextParser::new),
    CSV_DEFAULT_PARSER(DefaultCsvParser::new),
    JSON_DEFAULT_PARSER(DefaultJsonParser::new),
    EXCEL_TESTCASE_PARSER(ExcelParser::new),
    XLSX_DEFAULT_PARSER(ExcelParser::new),
    EXCEL_DEFAULT_PARSER(ExcelParser::new),
    SQL_DEFAULT_PARSER(DefaultSQLParser::new),
    JSON_REPORT_PARSER(ReportJsonParser::new);


    private Supplier<IDataParser> parserInstantiator;

    public IDataParser getInstance() {
        return parserInstantiator.get();
    }

    ParserType(Supplier<IDataParser> parserInstantiator) {
        this.parserInstantiator = parserInstantiator;
    }
}
