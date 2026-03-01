package diamondwalker.sscary.randomevent.common.calculation;

class CalculationQuestion {
    protected String question;
    protected String answer;
    protected int secondsToRespond;

    protected CalculationQuestion(String question, String answer, int timeToRespond) {
        this.question = question;
        this.answer = answer;
        this.secondsToRespond = timeToRespond;
    }
}
