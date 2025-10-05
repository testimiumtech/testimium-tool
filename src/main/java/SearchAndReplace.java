import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SearchAndReplace {
    private static final List<String> files = new ArrayList<>();
    private static final List<String> updatedFilePaths = new ArrayList<>(); // <-- store updated files
    private static final String targetWord = "Receipt.getMachine().getId()";
    private static final String replacementWord = "Receipt.getMachine().getMachineId()";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nEnter the directory path where replacement should occur: ");
        String directoryPath = scanner.nextLine();

        try {
            String[] listOfFiles = getAllFiles(directoryPath);
            for (String filePath : listOfFiles) {
                if (filePath.endsWith(".html") || filePath.endsWith(".xml")) {
                    File file = new File(filePath);
                    try {
                        replaceWordInFile(file, targetWord, replacementWord);
                    } catch (IOException e) {
                        System.err.println("Error processing file: " + filePath + " - " + e.getMessage());
                    }
                }
            }

            // After processing all files, write updated paths to a file
            writeUpdatedFilesLog(directoryPath + File.separator + "updated_files_List.txt");

        } catch (IOException e) {
            System.err.println("Error reading directory: " + e.getMessage());
        }
    }

    private static String[] getAllFiles(String directoryPath) throws IOException {
        File dir = new File(directoryPath).getCanonicalFile();
        files.clear();
        collectFilesRecursively(dir);
        return files.toArray(new String[0]);
    }

    private static void collectFilesRecursively(File file) {
        if (file.isFile()) {
            files.add(file.getAbsolutePath());
        } else if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    collectFilesRecursively(child);
                }
            }
        }
    }

    private static void replaceWordInFile(File file, String target, String replacement) throws IOException {
        String content = new String(Files.readAllBytes(file.toPath()));

        if (content.contains(target)) {
            System.out.println("Found in: " + file.getAbsolutePath());

            String updatedContent = content.replace(target, replacement);
            if (!content.equals(updatedContent)) {
                Files.write(file.toPath(), updatedContent.getBytes());
                System.out.println("Updated file: " + file.getPath());

                // Log this updated file path
                updatedFilePaths.add(file.getAbsolutePath());
            }
        }
    }

    private static void writeUpdatedFilesLog(String outputFilePath) {
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            for (String path : updatedFilePaths) {
                writer.write(path + System.lineSeparator());
            }
            System.out.println("\nList of updated files written to: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Failed to write updated file list: " + e.getMessage());
        }
    }
}