package autograder.strategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import autograder.models.Student;
import autograder.utils.FileExtractor;

public class FileExtractionStrategy implements GradingStrategy {

    @Override
    public Object execute(Object source, Object destination) throws Exception {
        if (!(source instanceof String) || !(destination instanceof String)) {
            throw new IllegalArgumentException("Input must be Strings representing the source and destinations paths.");
        }   

        String sourcePath = (String) source;
        String destinationPath = (String) destination;
        File sourceDir = new File(sourcePath);

        if (!sourceDir.isDirectory()) {
            throw new IllegalArgumentException("Source path is not a directory");
        }

        File[] zipFiles = sourceDir.listFiles((dir, name) -> name.endsWith(".zip"));
        if (zipFiles == null || zipFiles.length == 0) {
            throw new IllegalStateException("No ZIP files found in: " + sourcePath);
        }

        List<Student> students = new ArrayList<>();

        for (File zipFile: zipFiles) {
            // Extract studentID and assignmentID
            String fileName = zipFile.getName();
            String[] parts = fileName.split("_");
            if (parts.length != 4 || !parts[3].endsWith(".zip")) {
                System.err.println("Invalid file format: " + fileName);
                continue;
            }

            String firstName = parts[0];
            String lastName = parts[1];
            String studentID = parts[2];
            String assignmentID = parts[3].replace(".zip", "");
            String studentDestPath = destinationPath + "/" + assignmentID + "/" + (firstName + '_' + lastName + '_' + studentID);

            // Create destination folder
            File targetDir = new File(studentDestPath);
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            //Extract files
            FileExtractor.extractZip(zipFile.getAbsolutePath(), studentDestPath);
            students.add(new Student(firstName, lastName, studentID, assignmentID, studentDestPath));
        }

        return students;
    }
}
