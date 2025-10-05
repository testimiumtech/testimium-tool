package com.testimium.tool;

import com.testimium.tool.base.DriverManager;
import org.openqa.selenium.WebDriver;
import org.sikuli.basics.Settings;
import org.sikuli.script.Finder;
import org.sikuli.script.Match;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

public class CompareImages {

    public static void main(String[] args) throws InterruptedException {
        isTextExistsInImage();
    }

    private static void exactMatch() {
        Pattern actual = new Pattern("C:\\Sandeep\\Projects\\testimium\\CodeBase\\testimium-tool\\screenshot\\screenshot_14087898.png");
        Pattern expected = new Pattern("C:\\Sandeep\\Projects\\testimium\\CodeBase\\testimium-tool\\screenshot\\screenshot_14087898.png");

        Finder f = new Finder(actual.getImage());
        f.find(expected);
        if(f.hasNext()){
            Match m = f.next();
            if(m.getScore() == 1)
                System.out.println("Match Found: " + m.getScore());
        }
    }

    private static void verifyExpectedImageInScreen() {
        Screen screen = new Screen();
        Pattern expected = new Pattern("C:\\Sandeep\\Projects\\testimium\\CodeBase\\testimium-tool\\screenshot\\screenshot_14087898.png");

        Finder f = new Finder(screen.capture().getImage());
        f.find(expected);
        if(f.hasNext()){
            Match m = f.next();
            if(m.getScore() == 1)
                System.out.println("Match Found: " + m.getScore());
        }
        //We can also check if pattern objects exist in the current screen or not
        if(screen.exists(expected) != null) {
            System.out.println("Image exists in current screen");
        }
    }

    private static void isTextExistsInImage() throws InterruptedException {
        //Pattern actual = new Pattern("C:\\Sandeep\\Projects\\testimium\\CodeBase\\testimium-tool\\screenshot\\screenshot_14087898.png");
        WebDriver driver = DriverManager.getInstance().getWebDriver();
        driver.get("https://www.tataplay.com/dth/set-top-box/tata-play-hd-digital?utm_source=google&utm_medium=cpc&utm_campaign=Search+%7C+IMB+%7C+BMM+%7C+Rest+of+cities&utm_id=6674897505&utm_campaignid=6674897505&pl=&kw=best%20dth%20service&ch=g&mrktparam=&&source=google_search_ads&medium=cpc&campaign=%7bcampaign%7d&gclid=Cj0KCQjwidSWBhDdARIsAIoTVb2jz5VdvPCl1eNrzffPDhoP3BQiLnFP31LHcIFIC3B97qpga4QdBsgaAnWtEALw_wcB&gclsrc=aw.ds");
        Thread.sleep(2000);

        Settings.OcrTextSearch = true;
        Settings.OcrTextRead = true;
        Screen screen = new Screen();

        if(screen.exists("1299") != null) {
            System.out.println("Text Found");
        }

        driver.close();
    }

}
