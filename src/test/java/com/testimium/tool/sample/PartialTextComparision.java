
package com.testimium.tool.sample;

import com.testimium.tool.utility.PropertyUtility;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.RandomUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PartialTextComparision {
    private static final String UNMATCHED = "<span style=\"background-color: #FB504B\">${text}</span>";
    private static StringBuilder actualBuilder = new StringBuilder();
    private static StringBuilder expectedBuilder = new StringBuilder();
    public static void main(String[]args) throws Exception{

        Map<String, String> expectedMap = new LinkedHashMap<>();
        Map<String, String> actualMap = new HashMap<>();
        LineIterator actual = FileUtils.lineIterator(new File("testfiles/sample/actual.txt"), "utf-8");
        //List<String>  actual = (List<String>)FileReaderFactory.getInstance().readFile("TEXT","DEFAULT", "testfiles/sample/file-1.txt");
        LineIterator expected = FileUtils.lineIterator(new File("testfiles/sample/expected.txt"), "utf-8");
        int lineCount = 1;
        while (expected.hasNext() || actual.hasNext()) {
            String expectedLine = (expected.hasNext() ? expected.nextLine() : "");
            String[] expectedWords = expectedLine.split(" ") ;
                String actualLine = (actual.hasNext() ? actual.nextLine() : "");
                for(int itr=0; itr < expectedWords.length; itr++) {
                    if(!actualLine.contains(expectedWords[itr])) {
                        //expectedMap.put("Line-" + lineCount, UNMATCHED.replace("${text}", expectedWords[itr]));
                        expectedLine = expectedLine.replace(expectedWords[itr], UNMATCHED.replace("${text}", expectedWords[itr]));
                    }
                }
            actualBuilder.append("<span style=\"color: #45EA85\">Line-"+lineCount + "-   </span>" + actualLine);
            actualBuilder.append("<br/>");
            expectedBuilder.append("<span style=\"color: #45EA85\">Line-"+lineCount + "-   </span>" + expectedLine);
            expectedBuilder.append("<br/>");
            lineCount++;
        }
        //System.out.println(expectedBuilder.toString());
        generateHTML("ExactTextDiff");
        /*
        wordMap.remove("Output1");
        wordMap.remove("(4,37.97,€816)");
        System.out.println(wordMap.entrySet().stream().filter(map -> map.getValue() == 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString());*/
    }

    public static String generateHTML(String templateName) throws IOException {

        // Get template & replace placeholders with left & right variables with actual
        // comparison
        String template = FileUtils.readFileToString(new File(PropertyUtility.getTemplateLocation() + "/" + templateName +"Template.html"), "utf-8");
        String out1 = template.replace("${left}", actualBuilder.toString());
        String output = out1.replace("${right}", expectedBuilder.toString());
        // Write file to disk.
        File file = new File(PropertyUtility.getScreenshotLocation() + "/TextFileExactDiff" + RandomUtils.nextInt() + ".html");
        FileUtils.write(file, output, "utf-8");
        System.out.println("HTML diff generated.");
        return "<a href='"+ file.getAbsolutePath() + "'>Click to see the Text file difference</a> \n" + file.getAbsolutePath();
    }
}
