package autojudge;

import java.util.List;

public class MethodSpec {
    private String methodName;
    private String returnType;
    private List<String> arguments;
    private int implementationMarks;
    private String failedMessage;
    private TestMethod testImplementation;
    

    public MethodSpec(String methodName, String returnType, List<String> arguments, int totalMarks, String failedMessage, TestMethod testImplementation) {
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

    public String getReturnType() {
        return returnType;
    }

    public List<String> getArguments() {
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
