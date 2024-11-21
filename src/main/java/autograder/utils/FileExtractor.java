package autograder.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileExtractor {
    public static boolean extractZip(String zipFilePath, String targetDirectory) throws Exception {
        File targetDir = new File(targetDirectory);
        if (!targetDir.exists()) {
            targetDir.mkdirs(); // Create target directory if it doesn't exist
        }

        try (InputStream fis = Files.newInputStream(new File(zipFilePath).toPath());
             ZipInputStream zis = new ZipInputStream(fis)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                // Skip directories and non-Java files
                if (entry.isDirectory() || !entry.getName().endsWith(".java") || entry.getName().contains("/")) {
                    continue;
                }

                // Extract the .java file
                File extractedFile = new File(targetDirectory, new File(entry.getName()).getName());
                extractedFile.getParentFile().mkdirs(); // Ensure parent directories exist

                try (FileOutputStream fos = new FileOutputStream(extractedFile)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }
                zis.closeEntry();
            }
        }
        return true;
    }
}