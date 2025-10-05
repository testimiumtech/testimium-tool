package com.testimium.tool.sample;

public class Test {
    public static void main(String[] args) {
        String actualFileLocation = "com/testimium/printing/${prop.test.user}/filename.txt";

        System.out.println(actualFileLocation.substring(actualFileLocation.indexOf("${") + 2, actualFileLocation.indexOf("}")));
    }
}
