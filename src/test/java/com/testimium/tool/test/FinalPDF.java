package com.testimium.tool.test;

import com.testimium.tool.comparator.pdf.CompareMode;
import com.testimium.tool.comparator.pdf.PDFUtil;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FinalPDF {
    private static String Actual_ROI403LPRD_P60 = "Actual_ROI403LPRD_P60.pdf";
    private static String Expected_ROI403LPRD_P60 = "Expected_ROI403LPRD_P60.pdf";
    private static String Actual_ROI404LPRD_P60 = "Actual_ROI404LPRD_P60.pdf";
    private static String Expected_ROI404LPRD_P60 = "Expected_ROI404LPRD_P60.pdf";
    private static String sample = "_20161108_102115.pdf";

    public static void main(String[] args) throws IOException {
        //same files

        //  comparePDF(Actual_ROI403LPRD_P60,Expected_ROI403LPRD_P60,true,false);
        //same files compare pdf as image and xml is true
        //  comparePDF(Actual_ROI403LPRD_P60,Expected_ROI403LPRD_P60,true,true);
        //same number of pages but content is differ
        comparePDF(Actual_ROI403LPRD_P60,Expected_ROI404LPRD_P60,false,true);
        //same number of pages but content is differ and compare as image is true
        //  comparePDF(Actual_ROI403LPRD_P60,Expected_ROI404LPRD_P60,true,false);
        //same number of pages but content is differ and compare as image and xml is true
        //  comparePDF(Actual_ROI403LPRD_P60,Expected_ROI404LPRD_P60,true,true);
        //differ in number of pages
        //  comparePDF(Actual_ROI403LPRD_P60,sample,false,false);
        //null files
        //  comparePDF(null,Actual_ROI404LPRD_P60,false,false);
    }

    public static void comparePDF(String expectedPDF, String actualPDF, boolean comparePdfAsImage, boolean convertPDFToXml) throws IOException{

        String actPdfData = "";
        String expPdfData = "";
        boolean comparePdfAsImages = false;
        boolean convertToXml = false;

        if(!comparePdfAsImages){
            comparePdfAsImages = comparePdfAsImage;
        }
        if(!convertToXml){
            convertToXml = convertPDFToXml;
        }
        List<String> expListData = new ArrayList<String>();
        List<String> actListData = new ArrayList<String>();

        System.setErr(new PrintStream(new NullOutputStream()));

        if((expectedPDF  == null || actualPDF ==null)){
            System.out.println("Please provide the file names");
            return;
        }

        //Creating the object for the files
        PDDocument expPdfDocument = PDDocument.load(new File(expectedPDF));
        PDDocument actPdfDocument = PDDocument.load(new File(actualPDF));

        if(!(expPdfDocument.getNumberOfPages() == actPdfDocument.getNumberOfPages())){
            System.out.println("Both the files pages are not equal, first file page count is "+expPdfDocument.getNumberOfPages()+" and second file is "+actPdfDocument.getNumberOfPages());
            return;
        }

        //Gets the PDF document data and stores them into strings
        if (!expPdfDocument.isEncrypted() && !actPdfDocument.isEncrypted()) {
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);
            PDFTextStripper Tstripper = new PDFTextStripper();
            expPdfData = Tstripper.getText(expPdfDocument);
            actPdfData = Tstripper.getText(actPdfDocument);
        }

        if(expPdfData.equals(actPdfData)){
            System.out.println("Both the files are having same data");
        }

        else{
            //System.out.println("Data in the files are not same");
            StringTokenizer stExpPdf1 = new StringTokenizer(expPdfData,"\n");
            StringTokenizer stActPdf1 = new StringTokenizer(actPdfData,"\n");
            while(stExpPdf1.hasMoreTokens()){
                expListData.add(stExpPdf1.nextToken());
            }

            while(stActPdf1.hasMoreTokens()){
                actListData.add(stActPdf1.nextToken());
            }

            for(int i=0;i<actListData.size();i++){
                if((!actListData.get(i).equals(expListData.get(i))) && comparePdfAsImages == false){
                    System.out.println("PDF FILE ACT "+actListData.get(i)+"PDF FILE EXP "+expListData.get(i));
                }

                if((!actListData.get(i).equals(expListData.get(i))) && comparePdfAsImages == true){
                    PDFUtil pdfUtil = new PDFUtil();
                    pdfUtil.setCompareMode(CompareMode.VISUAL_MODE);
                    pdfUtil.setImageDestinationPath(System.getProperty("user.dir"));
                    pdfUtil.highlightPdfDifference(true);
                    pdfUtil.compare(expectedPDF, actualPDF, 0, pdfUtil.getPageCount(expectedPDF), true, true);
                }
            }
        }

        if(convertToXml == true){
            Runtime rt = Runtime.getRuntime();
            Process epr = rt.exec("cmd /c pdf2txt.py -o "+System.getProperty("user.dir")+"//"+expectedPDF+"_expected_output.xml"+" "+expectedPDF);
            Process apr = rt.exec("cmd /c pdf2txt.py -o "+System.getProperty("user.dir")+"//"+actualPDF+"_actual_output.xml"+" "+actualPDF);
        }
    }
}
