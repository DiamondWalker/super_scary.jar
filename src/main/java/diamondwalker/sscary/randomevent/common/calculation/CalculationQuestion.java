package diamondwalker.sscary.randomevent.common.calculation;

public class CalculationQuestion {
    public String question;
    public String answer;
    public int secondsToRespond;

    protected CalculationQuestion(String question, String answer, int timeToRespond) {
        this.question = question;
        this.answer = answer;
        this.secondsToRespond = timeToRespond;
    }
}
