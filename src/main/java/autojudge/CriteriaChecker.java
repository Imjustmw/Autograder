package autojudge;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class CriteriaChecker {

    private ClassSpec classSpec;
    private boolean all_test_passed;

    public CriteriaChecker(ClassSpec classSpec) {
        this.classSpec = classSpec;
        all_test_passed = true;
    }

    public boolean Get_All_test_passed() {
        return all_test_passed;
    }

    public EvaluationResult evaluateClass(File javaFile) {
        EvaluationResult result = new EvaluationResult(javaFile.getName(), classSpec.getTotalMarks());

        // Load the Java class
        Class<?> cls = loadClass(javaFile);
        if (cls == null) {
            result.addComment("Class " + javaFile.getName() + " could not be loaded.");
            return result;
        }

        // Check against each ClassSpec
        // Check fields
        for (VariableSpec variableSpec : classSpec.getVariableSpecs()) {
            checkField(cls, variableSpec, result);
        }

        // Check methods
        for (MethodSpec methodSpec : classSpec.getMethodSpecs()) {
            checkMethod(cls, methodSpec, result);
        }

        // Check constructor
        List<ConstructorSpec> constructorSpec = classSpec.getConstructorSpecs();
        if (constructorSpec != null) {
            checkConstructor(cls, constructorSpec, result);
        }

        return result;
    }

    private void checkField(Class<?> cls, VariableSpec variableSpec, EvaluationResult result) {
        try {
            // Check if field with the specified name exists
            Field field = cls.getDeclaredField(variableSpec.getVariableName());

            // Get the expected type of the field
            Class<?> expectedType = mapToClass(variableSpec.getVariableType());

            if (field.getType().equals(expectedType)) {
                // Field exists and types match
                result.addComment("Field " + variableSpec.getVariableName() + " with type "
                        + variableSpec.getVariableType() + " found.");
                result.incrementScore(variableSpec.GetScore());
            } else {
                // Field exists but type does not match
                result.addComment("Field " + variableSpec.getVariableName() + " found, but type mismatch. Expected: "
                        + variableSpec.getVariableType() + ", Found: " + field.getType().getSimpleName());
            }
        } catch (NoSuchFieldException e) {
            // Field does not exist
            result.addComment("Field " + variableSpec.getVariableName() + " not found.");
        } catch (ClassNotFoundException e) {
            // Error in type mapping
            result.addComment(
                    "Error in determining type for field " + variableSpec.getVariableName() + ": " + e.getMessage());
        }
    }

    private void checkMethod(Class<?> cls, MethodSpec methodSpec, EvaluationResult result) {
        boolean methodFound = false;

        for (Method method : cls.getDeclaredMethods()) {
            if (method.getName().equals(methodSpec.getMethodName()) &&
                    method.getReturnType().equals(methodSpec.getReturnType()) &&
                    parametersMatch(methodSpec.getArguments(), method.getParameterTypes())) {

                methodFound = true;
                result.addComment("Method " + methodSpec.getMethodName() + " with correct signature found.");
                result.incrementScore(methodSpec.getImplementationMarks());
                // Test the method
                try {
                    boolean testPassed = methodSpec.testMethod(cls.getConstructor().newInstance());
                    if (testPassed) {
                        result.addComment("Method " + methodSpec.getMethodName() + " passed the functionality test.");

                    } else {
                        result.addComment(methodSpec.getFailedMessage());
                        all_test_passed = false;
                    }
                } catch (Exception e) {
                    result.addComment(
                            "Error while testing method " + methodSpec.getMethodName() + ": " + e.getMessage());
                }

                break;
            }
        }

        if (!methodFound) {
            result.addComment("Method " + methodSpec.getMethodName() + " with specified signature not found.");
        }
    }

    private void checkConstructor(Class<?> cls, List<ConstructorSpec> constructorSpecs, EvaluationResult result) {
        for (ConstructorSpec constructorSpec : constructorSpecs) {
            try {
                List<Class<?>> expectedParams = getParameterTypes(constructorSpec.getArgumentTypes());
                Constructor<?>[] constructors = cls.getConstructors();

                boolean constructorFound = false;
                for (Constructor<?> constructor : constructors) {
                    Class<?>[] actualParams = constructor.getParameterTypes();
                    if (parametersMatch(expectedParams, actualParams)) {
                        constructorFound = true;
                        result.addComment("Constructor " + constructorSpec.getConstructorSpec() + " with parameters "
                                + expectedParams + " found.");
                        result.incrementScore(constructorSpec.getScore());
                        break;
                    }
                }

                if (!constructorFound) {
                    result.addComment("Constructor " + constructorSpec.getConstructorSpec() + " with parameters "
                            + expectedParams + " not found.");
                }

            } catch (Exception e) {
                result.addComment(
                        "Error checking constructor " + constructorSpec.getConstructorSpec() + ": " + e.getMessage());
            }
        }
    }

    private boolean parametersMatch(List<Class<?>> expectedParams, Class<?>[] actualParams) {
        if (expectedParams.size() != actualParams.length) {
            return false;
        }
        for (int i = 0; i < expectedParams.size(); i++) {
            if (!expectedParams.get(i).equals(actualParams[i])) {
                return false;
            }
        }
        return true;
    }

    public static Class<?> loadClass(File file) {

        // Check if the file exists and is a .java file
        if (!file.exists() || !file.getAbsolutePath().endsWith(".java")) {
            System.out.println("The file does not exist or is not a .java file.");
            return null;
        }

        // Compile the Java file using an external javac command
        ProcessBuilder processBuilder = new ProcessBuilder("javac", file.getAbsolutePath());
        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("Compilation failed. Check the source file for errors.");
                return null;
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Compilation was interrupted or failed due to an I/O error.");
            return null;
        }

        // Load the compiled class
        String className = file.getName().replace(".java", "");
        File parentDir = file.getParentFile();
        try (URLClassLoader classLoader = new URLClassLoader(new URL[] { parentDir.toURI().toURL() })) {
            return classLoader.loadClass(className);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load the compiled class.");
            return null;
        }
    }

    private List<Class<?>> getParameterTypes(List<String> argumentNames) throws ClassNotFoundException {
        List<Class<?>> paramTypes = new ArrayList<>();
        for (String name : argumentNames) {
            paramTypes.add(mapToClass(name));
        }
        return paramTypes;
    }

    private Class<?> mapToClass(String name) throws ClassNotFoundException {
        switch (name.trim().toLowerCase()) {
            case "int":
                return int.class;
            case "double":
                return double.class;
            case "float":
                return float.class;
            case "boolean":
                return boolean.class;
            case "char":
                return char.class;
            case "byte":
                return byte.class;
            case "short":
                return short.class;
            case "long":
                return long.class;
            case "String":
                return String.class;
            // Add other common types as needed
            default:
                // For fully qualified class names, use Class.forName
                return Class.forName(name);
        }
    }

}
