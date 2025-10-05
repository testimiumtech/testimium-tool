package com.testimium.tool.utility;

import com.testimium.tool.context.TestContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtility {

    /**
     * Provide list of placeholders between given start and end character from provided string
     * @param sqlQuery
     * @return
     */
    public List<String> getPlaceHoldersBetweenStrings(String startStr, String endStr, String sqlQuery) {
        Pattern pattern = Pattern.compile(startStr + "(.*?)" + endStr, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(sqlQuery);
        List<String> replaceHolders =  new ArrayList<>();

        while (matcher.find()) {
            System.out.println(matcher.group(1));
            replaceHolders.add(matcher.group(1));
        }
        return replaceHolders;
    }

    public String getPlaceHolderBetweenStrings(String startStr, String endStr, String text) {
        Pattern pattern = Pattern.compile(startStr + "(.*?)" + endStr, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
             return matcher.group(1);
        }
        return text;
    }

    /**
     * Provide placeholder between given start and end character from provided string, return the global variable value
     * @param startStr
     * @param endStr
     * @param text
     * @return
     */
    public String getGlobalVariableValue(String startStr, String endStr, String text) {
        Pattern pattern = Pattern.compile(startStr + "(.*?)" + endStr, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return String.valueOf(TestContext.getTestContext("").getGlobalVariable().get(matcher.group(1)));
        }
        return ("null".equalsIgnoreCase(text)) ? null : text;
    }

    /**
     * This method will be return the deep copy of an string array object
     * @param original
     * @return
     */
    public String[] deepCopy(String[] original) {
        if (original == null) {
            return null;
        }

        String[] copy = new String[original.length];
        for (int i = 0; i < original.length; i++) {
            if (original[i] != null) {
                copy[i] = new String(original[i]);
            }
        }
        return copy;
    }


    /**
     * Shot the alphanumeric string array
     * @param names
     */
    public void smartShot(String[] names){
        Arrays.sort(names, new Comparator<>() {
            @Override
            public int compare(String s1, String s2) {
                NumericName n1 = splitPrefix(s1);
                NumericName n2 = splitPrefix(s2);

                // Both have numbers
                if (n1.number != null && n2.number != null) {
                    int cmp = Integer.compare(n1.number, n2.number);
                    if (cmp != 0) return cmp;
                    return n1.rest.compareTo(n2.rest);
                }

                // One has number
                if (n1.number != null) return -1; // numbers come first
                if (n2.number != null) return 1;

                // Neither has number – pure lexicographical
                return s1.compareTo(s2);
            }

            class NumericName {
                Integer number;
                String rest;
                NumericName(Integer number, String rest) {
                    this.number = number;
                    this.rest = rest;
                }
            }

            private NumericName splitPrefix(String s) {
                int i = 0;
                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                    i++;
                }
                if (i == 0) {
                    return new NumericName(null, s);
                } else {
                    Integer num = Integer.parseInt(s.substring(0, i));
                    return new NumericName(num, s.substring(i));
                }
            }
        });

        //System.out.println(Arrays.toString(names));
    }
}
