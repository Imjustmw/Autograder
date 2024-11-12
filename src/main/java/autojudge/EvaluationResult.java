package autojudge;

import java.util.ArrayList;
import java.util.List;

public class EvaluationResult{
    private String className;
    private int score;
    private int totalScore;
    private List<String> comments;

    public EvaluationResult(String className, int totalScore) {
        this.className = className;
        this.score = 0;
        this.comments = new ArrayList<>();
        this.totalScore = totalScore;
    }

    public void addComment(String comment) {
        comments.add(comment);
    }

    public void incrementScore(int points) {
        score += points;
    }

    public int getScore() {
        return score;
    }

    public List<String> getComments() {
        return comments;
    }

    public String getClassName() {
        return className;
    }

    public String GetOverallResult(){
        return String.format("Implementation Score: %d/%d" ,score,totalScore);
    }
}

