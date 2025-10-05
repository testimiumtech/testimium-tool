package com.testimium.tool.action;


import com.testimium.tool.reader.*;

import java.util.function.Supplier;

/**
 * Enum based factory to maintain and responsible to handle the file reader service instantiation.
 * @author Sandeep Agrawal
 */
public enum ReaderType {
    //READ_TXT(TextFileReader::getInstance),
    READ_TEXT(TextFileReader::new),
    READ_TXT(TextFileReader::new),
    READ_CSV(CsvFileReader::new),
    READ_JSON(JsonFileReader::new),
    READ_XLSX(ExcelFileReader::new),
    READ_EXCEL(ExcelFileReader::new);
    //READ_SQL(SQLFileReader::new);

    private Supplier<AbstractFileReader> readerInstantiator;

    public AbstractFileReader getInstance() {
        return readerInstantiator.get();
    }

    ReaderType(Supplier<AbstractFileReader> readerInstantiator) {
        this.readerInstantiator = readerInstantiator;
    }
}
