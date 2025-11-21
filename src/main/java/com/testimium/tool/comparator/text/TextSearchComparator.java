package com.testimium.tool.comparator.text;

import com.testimium.tool.command.SetGlobalVariableCmd;
import com.testimium.tool.comparator.IToolComparator;
import com.testimium.tool.context.TestContext;
import com.testimium.tool.domain.AssertParameter;
import com.testimium.tool.domain.CommandParam;
import com.testimium.tool.domain.ComparatorResponse;
import com.testimium.tool.domain.assertions.Assertions;
import com.testimium.tool.exception.JsonParsingException;
import com.testimium.tool.exception.ToolComparatorException;
import com.testimium.tool.reader.FileReaderFactory;
import com.testimium.tool.utility.JsonParserUtility;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextSearchComparator extends IToolComparator<String, String, ComparatorResponse> {

    private String[] wordsToSearch;

    String outputFileName = "Text Search";

    @Override
    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    @Override
    public ComparatorResponse compare(String actualFilePath, String nothing, String assertKey) throws ToolComparatorException {
        ComparatorResponse response = null;
        try {
            List<String> actual = (List<String>) FileReaderFactory.getInstance().readFile("TEXT", "DEFAULT", actualFilePath);
            prepareExcludedPatter(assertKey);
            response = compare(actual, this.wordsToSearch);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ToolComparatorException(outputFileName + " Failed Exception: " + ex.getMessage(), ex);
        }
        return response;
    }

    @Deprecated
    public ComparatorResponse compare(List<String> actual, String[] words) {
        ComparatorResponse response;
        Map<String, Long> wordMap = new HashMap<>();
        for (int itr = 0; itr < words.length; itr++) {
            if (!wordMap.containsKey(words[itr])) {
                wordMap.put(words[itr].trim(), 0L);
            }
            for (int i = 0; i < actual.size(); i++) {
                String line1 = actual.get(i);
                if (line1.contains(words[itr])) {
                    Long count = wordMap.get(words[itr].trim());
                    count = count + 1;
                    wordMap.put(words[itr].trim(), count);
                }
            }
        }
        wordMap = wordMap.entrySet().stream().filter(map -> map.getValue() == 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        response = new ComparatorResponse((wordMap.size() == 0)? "PASS" : "FAIL",
                (wordMap.size() == 0)? "Comparison Successful" : "Comparison Failed",
                (wordMap.size() > 0) ? outputFileName + ":- following word not found : " + wordMap.toString(): "");
        return response;
    }

    public ComparatorResponse compare(String actualText, String[] words, String ketToMap) {
        ComparatorResponse response;
        Map<String, Long> wordMap = new HashMap<>();

        for (int itr = 0; itr < words.length; itr++) {
            long matchCount = 0;
            long keyMapCount = 0;
            Pattern pattern = Pattern.compile(words[itr], Pattern.MULTILINE);
            //\\s+(GIFT\d{8})\s+(Valid for .*?)\s+T&C's:.*
            Matcher matcher = pattern.matcher(actualText);
            while (matcher.find()) {
                String matchedText = isRegex(words[itr])? matcher.group(1) : matcher.group();
                if(null != ketToMap && !ketToMap.isEmpty()) {
                    TestContext.getTestContext("").getGlobalVariable().put(((keyMapCount==0) ? ketToMap : ketToMap + "-" + keyMapCount), matchedText);
                    keyMapCount++;
                }
                System.out.println("Text Matched: " + matchedText);
                System.out.println("---------------------------");
                matchCount++;
            }
            if(matchCount > 0) {
                wordMap.put(words[itr].trim(), matchCount);
            } else {
                wordMap.put(words[itr].trim(), 0L);
            }
        }
        System.out.println("Set GlobalVariable: " + TestContext.getTestContext("").getGlobalVariable());
        wordMap = wordMap.entrySet().stream().filter(map -> map.getValue() == 0).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        response = new ComparatorResponse((wordMap.size() == 0)? "PASS" : "FAIL",
                (wordMap.size() == 0)? "Comparison Successful" : "Comparison Failed",
                (wordMap.size() > 0) ? outputFileName + ":- following word not found : " + wordMap.toString(): "");
        return response;
    }

    private boolean isRegex(String input) {
        return input.matches(".*[.\\^$*+?{}\\[\\]\\\\|()].*");
    }

    private void prepareExcludedPatter(String assertKey) throws JsonParsingException {
        AssertParameter assertParam = new JsonParserUtility<AssertParameter>()
                .parse(TestContext.getTestContext("").getTestExpectedJson(), AssertParameter.class);
        Assertions assertions = null;
        if(StringUtils.isNotEmpty(assertKey) && null != assertParam && null != assertParam.getAssertParams()) {
            assertions = assertParam.getAssertParams().get(assertKey);
            this.wordsToSearch = assertions.getWordsToSearch();
            this.wordsToSearch = appendDateToSearch(this.wordsToSearch, assertions.getDateToSearch());
        } else if(null != assertParam && null != assertParam.getAssertParams()) {
            int count = 0;
            for(Map.Entry<String, Assertions> entry: assertParam.getAssertParams().entrySet()) {
                assertions = entry.getValue();
                if(count == 0)
                    this.wordsToSearch = assertions.getWordsToSearch();
                merge(assertions.getWordsToSearch(), assertions.getDateToSearch());
                count++;
            }
        }
    }

    private String[] merge(String[] words, List<Map<String, Object>> dateToSearch)
    {
        String[] wordsToSarch = Stream.of(this.wordsToSearch, words)
                .flatMap(Stream::of)		// or Arrays::stream
                .toArray(String[]::new);
        return appendDateToSearch(wordsToSarch, dateToSearch);

    }
}
