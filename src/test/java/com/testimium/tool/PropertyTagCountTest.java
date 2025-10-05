package com.testimium.tool;

import org.apache.commons.lang3.StringUtils;

public class PropertyTagCountTest {
    public static void main(String[] args) {

        String actualFileLocation = "A/B/C/${dirtest}/N/O";

        int propCount = StringUtils.countMatches(actualFileLocation, "${");
        while(propCount > 0){
            String prop = actualFileLocation.substring(actualFileLocation.lastIndexOf("${") + 2, actualFileLocation.lastIndexOf("}"));
            actualFileLocation = actualFileLocation.replace("${" + prop + "}", prop);
            propCount = StringUtils.countMatches(actualFileLocation, "${");
        }
        System.out.println(actualFileLocation);

    }
}
