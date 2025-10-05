package com.testimium.tool.action;

import com.testimium.tool.comparator.IToolComparator;
import com.testimium.tool.comparator.csv.CsvExactComparator;
import com.testimium.tool.comparator.csv.CsvPartialComparator;
import com.testimium.tool.comparator.csv.CsvSearchComparator;
import com.testimium.tool.comparator.excel.ExeclExactComparator;
import com.testimium.tool.comparator.excel.ExeclPartialComparator;
import com.testimium.tool.comparator.excel.ExeclSearchComparator;
import com.testimium.tool.comparator.image.ImageExactComparator;
import com.testimium.tool.comparator.pdf.PdfExactComparator;
import com.testimium.tool.comparator.pdf.PdfPartialComparator;
import com.testimium.tool.comparator.pdf.PdfSearchComparator;
import com.testimium.tool.comparator.text.TextExactComparator;
import com.testimium.tool.comparator.text.TextPartialComparator;
import com.testimium.tool.comparator.text.TextSearchComparator;

import java.util.function.Supplier;

public enum ComparatorType {
    TEXT_PARTIAL_COMPARATOR(TextPartialComparator::new),
    TEXT_EXACT_COMPARATOR(TextExactComparator::new),
    TEXT_SEARCHWORD_COMPARATOR(TextSearchComparator::new),
    PDF_EXACT_COMPARATOR(PdfExactComparator::new),
    PDF_PARTIAL_COMPARATOR(PdfPartialComparator::new),
    PDF_SEARCHWORD_COMPARATOR(PdfSearchComparator::new),
    CSV_PARTIAL_COMPARATOR(CsvPartialComparator::new),
    CSV_EXACT_COMPARATOR(CsvExactComparator::new),
    CSV_SEARCHWORD_COMPARATOR(CsvSearchComparator::new),
    EXCEL_EXACT_COMPARATOR(ExeclExactComparator::new),
    EXCEL_PARTIAL_COMPARATOR(ExeclPartialComparator::new),
    EXCEL_SEARCH_COMPARATOR(ExeclSearchComparator::new),
    IMAGE_EXACT_COMPARATOR(ImageExactComparator::new);

    private Supplier<IToolComparator> compInstantiator;

    public IToolComparator getInstance() {
        return compInstantiator.get();
    }

    ComparatorType(Supplier<IToolComparator> compInstantiator) {
        this.compInstantiator = compInstantiator;
    }

}
