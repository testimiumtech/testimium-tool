package com.testimium.tool.logging;

import com.testimium.tool.utility.PropertyUtility;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LogsComprassor {

    public void compress() throws IOException {

        compressLogs(PropertyUtility.getLoggerPath() + PropertyUtility.getReportLocation());
        compressLogs(PropertyUtility.getReportLocation());
    }

    private void compressLogs(String inputDirectoryPath) throws IOException {
        //File inputDirectory = new File(PropertyUtility.getLoggerPath() + File.separator + PropertyReader.getProperty("tool.active.profile"));
        File inputDirectory = new File(inputDirectoryPath);
        System.out.println("./Reports = " +inputDirectory);
        //File outputZip = new File("C:/testimium/Testing/logs/sttf/zipDirectory.zip");
        // Create the GZIP output stream for output file
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd-mm-ss");
        String outputFile = inputDirectory.getAbsolutePath() + File.separator + dateFormatter.format(new Date()) + ".zip";
        if(new File(outputFile).exists())
            outputFile = inputDirectory.getAbsolutePath() + File.separator + dateTimeFormatter.format(new Date()) + ".zip";
        File outputZip = new File(outputFile);
        outputZip.getParentFile().mkdirs();
        List listFiles = new ArrayList();
        //Populate all files...iterate through directories/subdirectories...
        //recursively
        System.out.println("1. Input directory %s has following files:\n" + inputDirectory.getCanonicalPath());
        listFiles(listFiles, inputDirectory);

        //Create zip output stream to zip files
        OutputStream fileOutputStream = new FileOutputStream(outputZip);
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

        //Create zip files by recursively iterating through directories
        //and sub directories....
        System.out.println("\n2. Output Zipped file at location:" + outputZip.getCanonicalPath());
        createZipFile(listFiles, inputDirectory, zipOutputStream);
        fileOutputStream.close();
        zipOutputStream.close();
        deleteDirectory(inputDirectory);
    }


    private void createZipFile(List<File> listFiles, File inputDirectory,
                                      ZipOutputStream zipOutputStream) throws IOException {

        for (File file : listFiles) {
            String filePath = file.getCanonicalPath();
            int lengthDirectoryPath = inputDirectory.getCanonicalPath().length();
            int lengthFilePath = file.getCanonicalPath().length();

            //Get path of files relative to input directory.
            String zipFilePath = filePath.substring(lengthDirectoryPath + 1, lengthFilePath);

            ZipEntry zipEntry = new ZipEntry(zipFilePath);
            zipOutputStream.putNextEntry(zipEntry);

            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes)) >= 0) {
                zipOutputStream.write(bytes, 0, length);
            }
            inputStream.close();
            zipOutputStream.closeEntry();
            System.out.println("Zipped file:"+filePath);
        }
        zipOutputStream.close();
        //deleteDirectory(inputDirectory);
    }

    //Get list of all files recursively by iterating through sub directories
    private List listFiles(List listFiles, File inputDirectory)
            throws IOException {

        File[] allFiles = inputDirectory.listFiles();
        for (File file : allFiles) {
            if (file.isDirectory()) {
                listFiles(listFiles, file);
            } else if(!FilenameUtils.getExtension(file.getAbsolutePath()).matches("zip")){
                System.out.println(file.getCanonicalPath());
                listFiles.add(file);
            }
        }
        return listFiles;
    }

    private void deleteDirectory(File sourceDirectory) throws IOException {
        for (File file : sourceDirectory.listFiles()) {
            if(file.isDirectory())
                deleteDirectory(file);

            if(!FilenameUtils.getExtension(file.getAbsolutePath()).matches("zip")) {
               //file.deleteOnExit();
               FileUtils.forceDelete(file);
            }
        }
    }
}
