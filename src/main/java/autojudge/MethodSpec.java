package autojudge;

import java.util.List;

public class MethodSpec {
    private String methodName;
    private Class<?> returnType;
    private List<Class<?>> arguments;
    private int implementationMarks;
    private String failedMessage;
    private TestMethod testImplementation;

    public MethodSpec(String methodName, Class<?> returnType, List<Class<?>> arguments, int totalMarks,
            String failedMessage, TestMethod testImplementation) {
        this.methodName = methodName;
        this.returnType = returnType;
        this.arguments = arguments;
        this.implementationMarks = totalMarks;
        this.failedMessage = failedMessage;
        this.testImplementation = testImplementation;
    }

    // Accessors
    public String getMethodName() {
        return methodName;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public List<Class<?>> getArguments() {
        return arguments;
    }

    public int getImplementationMarks() {
        return implementationMarks;
    }

    public String getFailedMessage() {
        return failedMessage;
    }

    public boolean testMethod(Object instance, Object... params) {
        return testImplementation.test(instance, params);
    }
}
