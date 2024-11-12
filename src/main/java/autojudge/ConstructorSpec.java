package autojudge;

import java.util.List;

public class ConstructorSpec {
    private String constructorSpec;
    private List<String> argumentTypes;
    private int score;

    // Constructor to initialize both constructorSpec and argumentNames
    public ConstructorSpec(String constructorSpec, List<String> argumentNames,int score) {
        this.constructorSpec = constructorSpec;
        this.argumentTypes = argumentNames;
        this.score = score;
    }

    // Getter for constructorSpec
    public String getConstructorSpec() {
        return constructorSpec;
    }

    public int getScore(){
        return score;
    }
    // Getter for argumentNames
    public List<String> getArgumentTypes() {
        return argumentTypes;
    }
    
    // Optional: Add a method to display the constructor specification and arguments
    public String displaySpecWithArgs() {
        return "ConstructorSpec: " + constructorSpec + ", Arguments: " + argumentTypes;
    }
}


