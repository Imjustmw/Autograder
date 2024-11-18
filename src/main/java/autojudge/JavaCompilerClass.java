package autojudge;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;


public class JavaCompilerClass {
    public  static List<File> compileJavaFiles(File[] javaFiles) {
        List<File> compiledClassFiles = new ArrayList<>();

        try {
            // Directory to store compiled classes
            File compileOutputDir = new File("compiled_classes");
            if (!compileOutputDir.exists()) {
                compileOutputDir.mkdirs();
            }

            // Collect paths of all Java files
            List<String> javaFilePaths = new ArrayList<>();
            for (File javaFile : javaFiles) {
                javaFilePaths.add(javaFile.getPath());
            }

            // Compile all Java files together
            JavaCompiler compiler = (JavaCompiler) ToolProvider.getSystemJavaCompiler();
            if (compiler == null) {
                System.out.println("Java compiler not available. Are you running on a JDK?");
                return compiledClassFiles; // Return empty list if compiler is not available
            }

            StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

            Iterable<? extends JavaFileObject> compilationUnits = fileManager
                    .getJavaFileObjectsFromStrings(javaFilePaths);

            // Specify compilation options, including the output directory for class files
            List<String> compileOptions = Arrays.asList("-d", compileOutputDir.getAbsolutePath());
            JavaCompiler.CompilationTask task = compiler.getTask(
                    null, fileManager, null, compileOptions, null, compilationUnits);

            boolean compilationSuccess = task.call();

            if (compilationSuccess) {
                System.out.println("All files compiled successfully.");

                // Collect all .class files from the output directory
                File[] classFiles = compileOutputDir.listFiles((dir, name) -> name.endsWith(".class"));
                if (classFiles != null) {
                    compiledClassFiles.addAll(Arrays.asList(classFiles));
                }
            } else {
                System.out.println("Compilation failed.");
                for (String filePath : javaFilePaths) {
                    System.out.println("Failed to compile: " + new File(filePath).getName());
                }
            }

            fileManager.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return compiledClassFiles;
    }

}
