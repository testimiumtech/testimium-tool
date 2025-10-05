package com.testimium.tool.sample;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

public class GetLastModifiedFile {
    public static void main(String[] args) {
        File file =  getNewestFileFromDirectory("C:\\Users\\SandeepAgrawal\\Downloads");
        System.out.println(file.getAbsoluteFile());
    }

    public static File getNewestFileFromDirectory(String filePath) {
        File theNewestFile = null;
        File dir = new File(filePath);
        FileFilter fileFilter = new WildcardFileFilter("*");
        File[] files = dir.listFiles(fileFilter);

        if (files.length > 0) {
            /** The newest file comes first **/
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            theNewestFile = files[0];
        }

        return theNewestFile;
    }
}
