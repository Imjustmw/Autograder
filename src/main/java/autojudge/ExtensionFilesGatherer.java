package autojudge;

import java.io.File;
import java.util.ArrayList;

public class ExtensionFilesGatherer {

    public static ArrayList<File> GrabFiles(String path, String extension) {
        ArrayList<File> filesWithExtension = new ArrayList<>();
        File directory = new File(path);

        // Check if the specified path is a directory
        if (directory.isDirectory()) {
            // Call helper function to populate the list
            findFiles(directory, extension, filesWithExtension);
        }

        return filesWithExtension;
    }

    // Helper function for recursion
    private static void findFiles(File dir, String extension, ArrayList<File> fileList) {
        // Get all files and directories in the current directory
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                // If it's a directory, recurse into it
                if (file.isDirectory()) {
                    findFiles(file, extension, fileList);
                } else {
                    // If it's a file and has the specified extension, add it to the list
                    if (file.getName().endsWith(extension)) {
                        fileList.add(file);
                    }
                }
            }
        }
    }
}
