package autojudge;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileExtractor {

    public static String extractZipFile(String zipFilePath, String outputDir) {
        // Get the name of the zip file without the extension
        String zipFileName = new File(zipFilePath).getName();
        if (zipFileName.endsWith(".zip")) {
            zipFileName = zipFileName.substring(0, zipFileName.length() - 4);  // Remove ".zip" extension
        }

        // Create a new directory in the outputDir with the name of the zip file
        File destDir = new File(outputDir, zipFileName);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        // Extract files into the new directory
        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                File filePath = new File(destDir, entry.getName());  // Extract into the new directory

                if (!entry.isDirectory()) {
                    new File(filePath.getParent()).mkdirs();
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
                        byte[] buffer = new byte[4096];
                        int read;
                        while ((read = zipIn.read(buffer)) != -1) {
                            bos.write(buffer, 0, read);
                        }
                    }
                } else {
                    filePath.mkdirs();
                }

                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";  // Return an empty string if there's an error
        }

        // Return the path to the newly created directory
        return destDir.getAbsolutePath();
    }

    public static ArrayList<String> extractAllZipFiles(String directoryPath, String outputDir) {
        ArrayList<String> extractedDirs = new ArrayList<>();

        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("The provided path is not a directory or does not exist.");
            return extractedDirs;
        }

        // List all files in the directory
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                // Check if the file has a .zip extension
                if (file.isFile() && file.getName().toLowerCase().endsWith(".zip")) {
                    String zipFilePath = file.getAbsolutePath();

                    // Extract the zip file
                    String extractedDir = extractZipFile(zipFilePath, outputDir);

                    if (!extractedDir.isEmpty()) {
                        System.out.println("Extracted " + zipFilePath + " to " + extractedDir);
                        extractedDirs.add(extractedDir);  // Add extracted directory to the list
                    } else {
                        System.out.println("Failed to extract " + zipFilePath);
                    }
                }
            }
        }

        return extractedDirs;
    }
}
