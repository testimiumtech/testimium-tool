package com.testimium.tool.utility;

import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.reader.PropertyReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sandeep Agrawal
 */
public class FileUtility {
    private static List<String> files = new ArrayList<>();

    /**
     * Get all the files from all child or sub directories
     * @param src
     */
    private static void getAllFiles(File src) {
        if (!src.isDirectory()) {
            files.add(src.getAbsolutePath());
            //System.out.println(src.getName());
        } else {
            String[] file = src.list();
            for (int i = 0; i < file.length; i++) {
                getAllFiles(new File(src.getAbsolutePath(), file[i]));
            }
        }
    }

    /**
     *
     * @param src
     * @param fileName
     * @return
     */
    public static String getFile(File src, String fileName) {
        String matchedFile = null;
        if (!src.isDirectory()) {
            LogUtil.logToolMsg(src.getName());
            if(src.getName().equalsIgnoreCase(fileName)){
               return src.getAbsolutePath();
            }
        } else {
            String[] file = src.list();
            for (int i = 0; i < file.length; i++) {
                matchedFile = getFile(new File(src.getAbsolutePath(), file[i]), fileName);
                if(null != matchedFile)
                    break;
            }
        }
        return matchedFile;
    }
    /**
     *
     * @param file
     * @return
     */
    private static String[] getFiles(File file) {
        String[] fileList = file.list();
        String[] files = new String[fileList.length];
        for (int itr = 0; itr < fileList.length; itr++) {
            if(!new File(file.getAbsolutePath() + "\\" + fileList[itr]).isDirectory()) {
                files[itr] = file.getAbsolutePath() + "\\" + fileList[itr];
                System.out.println(files[itr]);
            }
        }
        return files;
    }


    /**
     * Read all the files from current directories
     * @return String[]
     * @throws IOException
     */
    public static String[] getPropertyFiles() throws IOException {
        File file = new File("config").getCanonicalFile();
        return getFiles(file);
    }

    /**
     * Get all the files from all child or sub directories
     * @return String[]
     * @throws IOException
     */
    public static String[] getAllFiles(String directoryPath) throws IOException {
        File file = new File(directoryPath).getCanonicalFile();
        files.clear();
        getAllFiles(file);
        return files.toArray(new String[files.size()]);
    }


    /*public static String[] getTestCaseFiles() throws IOException {
        File file = new File(PropertyReader.getProperty("testcases.path")).getCanonicalFile();
        return getFiles(file);
    }*/

    public static String getAbsolutePath(String path) throws IOException {
        File file = new File(path).getCanonicalFile();
        return file.getAbsolutePath();
    }

    public static File getFile(String path) throws IOException {
        File file = new File(path).getCanonicalFile();
        return file;
    }

    public static boolean isExists(String filePath) throws IOException {
        return getFile(filePath).exists();
    }

    public static boolean deleteIfExists(String filePath) throws IOException {
        File file = getFile(filePath);
        if(file.exists()){
            file.delete();
            return true;
        }
        return false;
    }
    /*public static void main(String[] args) throws IOException {
        File file = new File("config").getCanonicalFile();
        //getAllFiles(file);
        getFiles(file);
    }*/

    public static String createTempFile(String fileName, String content) throws IOException {
        //TODO add logger
        File tempFile = File.createTempFile(fileName, null);
        //file.createNewFile();
        System.out.println("TempFile= " + tempFile.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(content.getBytes());
        fos.close();
        return  tempFile.getAbsolutePath();
    }

    public static boolean createDirectory(String dirPath) {
        File dir = new File(dirPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        return dir.exists();
    }

    public static String[] getSeparatedFileName(String filePath) {
        File file = new File(filePath);
        String fileLocation = file.getAbsolutePath();
        int slashIndex = fileLocation.lastIndexOf('\\');
        //int index = fileName.lastIndexOf('.');
        String[] fileNameExt = new String[3];
        if(slashIndex > 0) {
            String extension = fileLocation.substring(slashIndex + 1);
            int index = extension.lastIndexOf('.');
            fileNameExt[0] = fileLocation.substring(0, slashIndex) + "\\";
            fileNameExt[1] = extension.substring(0, index);
            fileNameExt[2] = extension.substring(index);
            System.out.println("File name separated for file: " + fileNameExt[0] + fileNameExt[1] + fileNameExt[2]);
        }
        return fileNameExt;
    }

    public static String createFile(String filePath, byte[] content) throws IOException {
        //byte[] canvas_png = Base64.getDecoder().decode(pngData);
        deleteIfExists(filePath);
        FileOutputStream fos = null;
        File imgFile = null;
        try {
            imgFile = new File(filePath);
            fos = new FileOutputStream(imgFile);
            fos.write(content);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return imgFile.getAbsolutePath();
    }

    /**
     * Copy file from one place to another
     * @param from
     * @param to
     * @throws IOException
     */
    public static void copyFile(File from, File to) throws IOException {

        if(to.exists()) {
            FileUtils.delete(to);
        }
        FileUtils.copyFile(from, to);


        /*InputStream is = null;
        OutputStream os = null;
        try { is = new FileInputStream(src);
            os = new FileOutputStream(dest);
            // buffer size 1K
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buf)) > 0) {
                os.write(buf, 0, bytesRead);
            }
        } finally {
            is.close(); os.close();
        }*/
    }

    public static String getPathByReplaceWithProperty(String path){
        if(path.contains("${")) {
                /*String prop = actualFileLocation.substring(actualFileLocation.indexOf("${") + 2, actualFileLocation.indexOf("}"));
                actualFileLocation = actualFileLocation.replace("${" + prop + "}", PropertyReader.getProperty(prop));*/
            int propCount = StringUtils.countMatches(path, "${");
            String prop = null;
            while(propCount > 0){
                prop = path.substring(path.lastIndexOf("${") + 2, path.lastIndexOf("}"));
                path = path.replace("${" + prop + "}", PropertyReader.getProperty(prop));
                propCount = StringUtils.countMatches(path, "${");
            }
        }

        return path;
    }

    /**
     * Get the newest file from the given directory/folder.
     * @param directoryPath
     * @return
     */
    public static String getNewestFileFromDirectory(String directoryPath) {
        File theNewestFile = null;
        File dir = new File(directoryPath);
        FileFilter fileFilter = new WildcardFileFilter("*");
        File[] files = dir.listFiles(fileFilter);

        if (files.length > 0) {
            /** The newest file comes first **/
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            theNewestFile = files[0];
            return theNewestFile.getAbsolutePath();
        }

        return directoryPath;
    }

    /**
     * Get all folder in the given src
     * @param src
     */
    public static String[] getAllDirectory(File src) {
       /* List<String> dirList = new ArrayList<>();
        String[] files = src.list();
        for (int i = 0; i < files.length; i++) {
            File subSrc = new File(files[i]);
            if (!subSrc.isFile()) {
                dirList.add(files[i]);
            }
        }
        return dirList;*/
        String[] directories = src.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                if(null != name && name.equals("screenshot")){
                    return false;
                }
                return new File(current, name).isDirectory();
            }
        });
        new StringUtility().smartShot(directories);
        return directories;
    }
}
