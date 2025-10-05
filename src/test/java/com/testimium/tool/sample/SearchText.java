package com.testimium.tool.sample;

import com.testimium.tool.reader.FileReaderFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchText {
    public static void main(String[]args) throws Exception{

        Map<String, Integer> wordMap = new HashMap<>();
        //LineIterator file1 = FileUtils.lineIterator(new File("testfiles/sample/file-1.txt"), "utf-8");
        List<String>  file1 = (List<String>)FileReaderFactory.getInstance().readFile("TEXT","DEFAULT", "testfiles/sample/file-1.txt");
        String searchWord = "Sample Output1";
        String[] searchSplit = searchWord.split(" ");
        for(int itr=0; itr < searchSplit.length; itr++) {
            if (!wordMap.containsKey(searchSplit[itr])) {
                wordMap.put(searchSplit[itr].trim(), 0);
            }
            for(int i=0; i < file1.size(); i++) {
                //int count = 0;
                String line1 = file1.get(i);
                if (line1.contains(searchSplit[itr])) {
                    //count += 1;
                    Integer count = wordMap.get(searchSplit[itr].trim());
                    count = count + 1;
                    wordMap.put(searchSplit[itr].trim(), count);
                }
            }
        }
        System.out.println(wordMap.toString());
        /*wordMap.remove("Output1");
        wordMap.remove("(4,37.97,€816)");*/
        System.out.println(wordMap.entrySet().stream().filter(map -> map.getValue() == 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)).toString());
    }
}