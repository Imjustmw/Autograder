package autojudge;

public class VariableSpec {
    private String variableName;
    private String variableType;
    private String comment;
    private int score;
    private boolean isConstant;

    public VariableSpec(String variableName,String variableType, String comment, int score, boolean isConstant) {
        this.variableName = variableName;
        this.comment = comment;
        this.score =score; 
        this.variableType = variableType;
        this.isConstant = isConstant;
    }

    public VariableSpec(String variableName, String variableType) {
        this(variableName, variableType,"",0,false);
    }

    public String getVariableType()
    {
        return variableType;
    }

    // Accessors
    public String getVariableName() {
        return variableName;
    }

    public String getComment() {
        return comment;
    }

    public int GetScore(){
        return score;
    }

    public boolean getIsConstant(){
        return isConstant;
    }
}

