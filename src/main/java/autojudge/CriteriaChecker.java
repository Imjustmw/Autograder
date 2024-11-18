package autojudge;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
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

    public EvaluationResult evaluateClass(File classFile) {
        EvaluationResult result = new EvaluationResult(classFile.getName(), classSpec.getTotalMarks());
    
        try {
            // Ensure the file is a .class file
            if (!classFile.getName().endsWith(".class")) {
                result.addComment("File " + classFile.getName() + " is not a valid .class file.");
                return result;
            }
    
            // Load the compiled class dynamically
            String className = classFile.getName().replace(".class", "");
            File parentDir = classFile.getParentFile();
    
            try (URLClassLoader classLoader = new URLClassLoader(new URL[]{parentDir.toURI().toURL()})) {
                Class<?> cls = classLoader.loadClass(className);
    
                // Check against the ClassSpec
                // Check fields
                for (VariableSpec variableSpec : classSpec.getVariableSpecs()) {
                    checkField(cls, variableSpec, result);
                }
    
                // Check methods
                for (MethodSpec methodSpec : classSpec.getMethodSpecs()) {
                    checkMethod(cls, methodSpec, result);
                }
    
                // Check constructors
                List<ConstructorSpec> constructorSpecs = classSpec.getConstructorSpecs();
                if (constructorSpecs != null) {
                    checkConstructor(cls, constructorSpecs, result);
                }
            } catch (ClassNotFoundException e) {
                result.addComment("Class " + className + " could not be loaded: " + e.getMessage());
            }
        } catch (IOException e) {
            result.addComment("Error accessing class file " + classFile.getName() + ": " + e.getMessage());
        }
    
        return result;
    }
    

    private void checkField(Class<?> cls, VariableSpec variableSpec, EvaluationResult result) {
        try {
            // Check if field with the specified name exists
            Field field = cls.getDeclaredField(variableSpec.getVariableName());
    
            // Get the expected type of the field
            String expectedType = variableSpec.getVariableType();
    
            if (parameterMatch(expectedType, field.getType())) {
                // Check if the field is a constant (static and final)
                int modifiers = field.getModifiers();
                boolean isConstant = Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
    
                if (variableSpec.getIsConstant()) {
                    // Field exists, types match, and it is a constant
                    if(isConstant){

                        result.addComment("Constant field " + variableSpec.getVariableName() + " with type "
                        + variableSpec.getVariableType() + " found.");
                        result.incrementScore(variableSpec.GetScore() - 1);
                    }
                    else{
                        result.addComment("Field " + variableSpec.getVariableName() + " with type "
                            + variableSpec.getVariableType() + " found, but it is not a constant.");
                            result.incrementScore(variableSpec.GetScore());
                    }
                } else {
                    // Field exists, types match, but it is not a constant
                    result.addComment("Field " + variableSpec.getVariableName() + " with type "
                            + variableSpec.getVariableType() );
                            result.incrementScore(variableSpec.GetScore());
                }
            } else {
                // Field exists but type does not match
                result.addComment("Field " + variableSpec.getVariableName() + " found, but type mismatch. Expected: "
                        + variableSpec.getVariableType() + ", Found: " + field.getType().getSimpleName());
            }
        } catch (NoSuchFieldException e) {
            // Field does not exist
            result.addComment("Field " + variableSpec.getVariableName() + " not found.");
        }
    }
    private void checkMethod(Class<?> cls, MethodSpec methodSpec, EvaluationResult result) {
        boolean methodFound = false;

        for (Method method : cls.getDeclaredMethods()) {
            if (method.getName().equals(methodSpec.getMethodName()) &&
                    parameterMatch(methodSpec.getReturnType(),method.getReturnType() ) &&
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
                // Get the expected parameter types from the ConstructorSpec
                List<String> expectedParams = constructorSpec.getArgumentTypes();
                Constructor<?>[] constructors = cls.getDeclaredConstructors();
    
                boolean constructorFound = false;
    
                for (Constructor<?> constructor : constructors) {
                    // Get the actual parameter types for the current constructor
                    Class<?>[] actualParams = constructor.getParameterTypes();
    
                    // Check if the parameters match
                    if (parametersMatch(expectedParams, actualParams)) {
                        constructorFound = true;
    
                        // Add a success comment and increment score
                        result.addComment("Constructor " + constructorSpec.getConstructorSpec() +
                                " with parameters " + expectedParams + " found.");
                        result.incrementScore(constructorSpec.getScore());
                        break;
                    }
                }
    
                // If no matching constructor was found, log a comment
                if (!constructorFound) {
                    result.addComment("Constructor " + constructorSpec.getConstructorSpec() +
                            " with parameters " + expectedParams + " not found.");
                }
    
            } catch (Exception e) {
                // Handle any exceptions during the constructor check
                result.addComment("Error checking constructor " + constructorSpec.getConstructorSpec() +
                        ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
            }
        }
    }
    

    private boolean parametersMatch(List<String> expectedParams, Class<?>[] actualParams) {
        if (expectedParams.size() != actualParams.length) {
            return false;
        }
    
        for (int i = 0; i < actualParams.length; i++) {
            // Compare the simple names of the parameter types
            if (!actualParams[i].getSimpleName().equals(expectedParams.get(i))) {
                return false;
            }
        }
    
        return true;
    }

    private boolean parameterMatch(String expectedParam, Class<?> actualParam) {
        if (actualParam == null || expectedParam == null) {
            return false;
        }
    
        // Compare the simple names of the parameter types
        return actualParam.getSimpleName().equals(expectedParam);
    }
    
    

    
    

   

  

}
