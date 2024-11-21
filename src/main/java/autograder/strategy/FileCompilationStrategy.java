package autograder.strategy;

import java.util.List;

import autograder.models.Student;
import autograder.utils.FileCompiler;

public class FileCompilationStrategy implements GradingStrategy {
    
    @Override
    public Object execute(Object studentList, Object input2) throws Exception {
        if (!(studentList instanceof List)) {
            throw new IllegalArgumentException("Input must be a List of Students");
        }

        @SuppressWarnings("unchecked")
        List<Student> students = (List<Student>) studentList;

        for (Student student: students) {
            System.out.println("Compiling files for: " + student.getStudentID());
            boolean success = FileCompiler.compileJavaFiles(student.getDestinationPath());
            if (!success) {
                System.err.println("Compilation failed for: " + student.getStudentID());
            }
        }
        return null;
    }
}