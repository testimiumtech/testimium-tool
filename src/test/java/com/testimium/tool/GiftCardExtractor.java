package com.testimium.tool;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GiftCardExtractor {

    public static void main(String[] args) {
        File file = new File("D:\\Projects\\Bopsen\\testimium-tool\\testfiles\\Sample\\Print Gift card.pdf");

        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            System.out.println("text: " + text);
            // Regex to match each GCard1 block and extract fields
            Pattern pattern = Pattern.compile(
                    "GCard1\\s+To redeem in\\-store present this gift card at checkout\\.\\s+([A-Z0-9\\-]{14})",
                    Pattern.MULTILINE
            );
            //\\s+(GIFT\d{8})\s+(Valid for .*?)\s+T&C's:.*

            Matcher matcher = pattern.matcher(text);

            while (matcher.find()) {
                String giftCode = matcher.group(1);
                //String giftId = matcher.group(2);
                //String validity = matcher.group(3);

                System.out.println("Gift Code:" + giftCode);
                //System.out.println("Gift ID: " + giftId);
                //System.out.println("Validity: " + validity);
                System.out.println("---------------------------");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

