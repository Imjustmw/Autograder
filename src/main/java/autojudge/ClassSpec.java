package autojudge;

import java.util.List;

public class ClassSpec {
    private List<MethodSpec> methodSpecs;
    private List<VariableSpec> variableSpecs;
    private List<ConstructorSpec> constructorSpecs;
    private int totalMarks;

    public ClassSpec(List<MethodSpec> methodSpecs, List<VariableSpec> variableSpecs, List<ConstructorSpec> constructorSpecs, int totalMarks) {
        this.methodSpecs = methodSpecs;
        this.variableSpecs = variableSpecs;
        this.constructorSpecs = constructorSpecs;
        this.totalMarks = totalMarks;
    }

    // Accessors
    public List<MethodSpec> getMethodSpecs() {
        return methodSpecs;
    }

    public List<VariableSpec> getVariableSpecs() {
        return variableSpecs;
    }

    public List<ConstructorSpec> getConstructorSpecs() {
        return constructorSpecs;
    }

    public int getTotalMarks() {
        return totalMarks;
    }
}

