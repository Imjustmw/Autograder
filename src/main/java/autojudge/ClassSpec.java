package autojudge;

import java.util.List;

public class ClassSpec {
    private List<MethodSpec> methodSpecs;
    private List<VariableSpec> variableSpecs;
    private List<ConstructorSpec> constructorSpecs;
    private String className;
    private int totalMarks;

    public ClassSpec(String className,List<MethodSpec> methodSpecs, List<VariableSpec> variableSpecs, List<ConstructorSpec> constructorSpecs, int totalMarks) {
        this.methodSpecs = methodSpecs;
        this.variableSpecs = variableSpecs;
        this.constructorSpecs = constructorSpecs;
        this.totalMarks = totalMarks;
        this.className = className;
    }

    // Accessors
    public List<MethodSpec> getMethodSpecs() {
        return methodSpecs;
    }

    public List<VariableSpec> getVariableSpecs() {
        return variableSpecs;
    }

    public String getClassName(){
        return className;
    }

    public List<ConstructorSpec> getConstructorSpecs() {
        return constructorSpecs;
    }

    public int getTotalMarks() {
        return totalMarks;
    }
}

