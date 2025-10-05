package com.testimium.tool;

import java.io.File;

public class FileNameTest {
    public static void main(String[] args) {

        String plit = "Actual,dd_MM_YYYY,0,";
        String[] str = plit.split(",");
        System.out.println(str.length);
        File file = new File("C:/Testing/logs/1-DropCreateDatabase13-05-2021.17.49.46.log");

        // convert the file name into string
        String filePath = file.getAbsolutePath();
        int slashIndex = filePath.lastIndexOf('\\');
        //int index = fileName.lastIndexOf('.');
        String[] fileNameExt = new String[3];
        if(slashIndex > 0) {
            String extension = filePath.substring(slashIndex + 1);
            int index = extension.lastIndexOf('.');
            fileNameExt[0] = filePath.substring(0, slashIndex) + "\\";
            fileNameExt[1] = extension.substring(0, index);
            fileNameExt[2] = extension.substring(index);
            System.out.println("File extension is " + fileNameExt[0] + fileNameExt[1]+fileNameExt[2]);
        }
    }
}
