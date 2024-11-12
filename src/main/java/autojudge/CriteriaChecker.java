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

import javax.tools.JavaCompiler;

import com.kohlschutter.jdk.standaloneutil.ToolProvider;

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

    public EvaluationResult evaluateClass(File javaFile) throws ClassNotFoundException, IOException {
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

    public static Class<?> loadClass(File file) throws ClassNotFoundException, IOException {

        // Check if the file exists and is a .java file
        if (!file.exists() ) {
            throw new IllegalArgumentException("The file does not exist or is not a .java file.");
        }

        // Get the Java compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("Java compiler not available. Make sure JDK is installed.");
        }

        // Compile the Java file
        int compilationResult = compiler.run(null, null, null, file.getAbsolutePath());
        if (compilationResult != 0) {
            throw new IllegalStateException("Compilation failed. Check for errors in the source file.");
        }

        // Get the class name (file name without the .java extension)
        String className = file.getName().replace(".java", "");

        // Load the compiled class
        File parentDir = file.getParentFile();
        URL[] urls = { parentDir.toURI().toURL() };
        try (URLClassLoader classLoader = new URLClassLoader(urls)) {
            return classLoader.loadClass(className);
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
