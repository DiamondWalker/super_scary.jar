package diamondwalker.sscary.randomevent.common.calculation;

import net.minecraft.util.RandomSource;

import java.util.Arrays;

class CalculationQuestion {
    protected String question;
    protected String answer;

    protected CalculationQuestion(String question) {
        this(question, "");
    }

    protected CalculationQuestion(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
}
