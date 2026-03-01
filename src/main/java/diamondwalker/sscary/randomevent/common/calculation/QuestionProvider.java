package diamondwalker.sscary.randomevent.common.calculation;

import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.client.event.ClientPauseChangeEvent;

import java.util.Arrays;

class QuestionProvider {
    protected static CalculationQuestion generateRegularQuestion(int grade, RandomSource random) {
        int selection = random.nextInt(Math.min(grade + 1, 5));

        if (selection == 0) return generateAddition(grade, random);
        if (selection == 1) return generateSubtraction(grade - 1, random);
        if (selection == 2) return generateMultiplication(grade - 2, random);
        if (selection == 3) return generateDivision(grade - 3, random);
        if (selection == 4) return generateExponent(grade - 4, random);

        throw new RuntimeException("Invalid Calculation question selection");
    }

    private static CalculationQuestion generateAddition(int level, RandomSource random) {
        if (level > 0) level--; // the first two levels should be the same

        // I do not know how to explain this function. I just screwed around with desmos until I was happy with the result.
        int b = (level % 9) + 1;
        int h = level / 9;
        int z = (int)Math.round(Math.pow(10, h + 1));
        int maxTerm = z * b;

        int operand1 = random.nextInt(maxTerm) + 1;
        int operand2 = random.nextInt(maxTerm) + 1;
        String answerString = operand1 + " + " + operand2 + " = ";

        return new CalculationQuestion(answerString, String.valueOf(operand1 + operand2));
    }

    private static CalculationQuestion generateSubtraction(int level, RandomSource random) {
        boolean allowNegativeResult = level > 0;
        if (level > 0) level--;

        // I do not know how to explain this function. I just screwed around with desmos until I was happy with the result.
        int b = (level % 9) + 1;
        int h = level / 9;
        int z = (int)Math.round(Math.pow(10, h + 1));
        int maxTerm = z * b;

        int operand1;
        int operand2;
        do {
            operand1 = random.nextInt(maxTerm) + 1;
            operand2 = random.nextInt(maxTerm) + 1;
        } while (operand1 == operand2);

        if (!allowNegativeResult) {
            if (operand2 > operand1) {
                // swap them
                int swap = operand2;
                operand2 = operand1;
                operand1 = swap;
            }
        }

        String answerString = operand1 + " - " + operand2 + " = ";

        return new CalculationQuestion(answerString, String.valueOf(operand1 - operand2));
    }

    private static CalculationQuestion generateMultiplication(int level, RandomSource random) {
        int extraZeroes = level / 2;
        int extraZeroes1 = random.nextInt(extraZeroes + 1);
        int extraZeroes2 = extraZeroes - extraZeroes1;

        int maxFactor1 = (int)Math.round(Math.pow(10, extraZeroes1 + 1));
        int maxFactor2 = (int)Math.round(Math.pow(10, extraZeroes2 + 1));

        int operand1 = random.nextInt(maxFactor1) + 1;
        int operand2 = random.nextInt(maxFactor2) + 1;
        if (level > 1) {
            if (random.nextBoolean()) operand1 = -operand1;
            if (random.nextBoolean()) operand2 = -operand2;
        }
        String answerString = operand1 + " x " + operand2 + " = ";

        return new CalculationQuestion(answerString, String.valueOf(operand1 * operand2));
    }

    private static CalculationQuestion generateDivision(int level, RandomSource random) {
        int extraZeroes = level / 2;
        int extraZeroes1 = random.nextInt(extraZeroes + 1);
        int extraZeroes2 = extraZeroes - extraZeroes1;

        int maxFactor1 = (int)Math.round(Math.pow(10, extraZeroes1 + 1));
        int maxFactor2 = (int)Math.round(Math.pow(10, extraZeroes2 + 1));

        int operand1 = random.nextInt(maxFactor1 - 1) + 1 + 1;
        int operand2 = random.nextInt(maxFactor2 - 1) + 1 + 1;
        int dividend = operand1 * operand2;
        String answerString = dividend + " / " + operand2 + " = ";

        return new CalculationQuestion(answerString, String.valueOf(operand1));
    }

    private static CalculationQuestion generateExponent(int level, RandomSource random) {
        int operand1 = random.nextInt(10) + 1;
        int operand2 = random.nextInt(level + 1) + 2;
        String answerString = operand1 + " ^ " + operand2 + " = ";

        return new CalculationQuestion(answerString, String.valueOf((int)Math.round(Math.pow(operand1, operand2))));
    }

    private static final String[] NAMES = new String[] {
            "Voorhees",
            "Myers",
            "Krueger",
            "Lecter"
    };
    protected static CalculationQuestion generateImpossibleQuestion(RandomSource random) {
        String question;
        switch (random.nextInt(4)) {
            case 0: {
                String name = NAMES[random.nextInt(NAMES.length)];
                int principal = (random.nextInt(99) + 1) * 1000;
                int rate = random.nextInt(5) + 1;
                question = "Mr. " + name + " has opened a savings account with a principal of " + principal + ". " +
                        "This account has an interest rate of " + rate + "%, compounded continuously. " +
                        "How much money will Mr. " + name + " have saved by the time the heat death of the universe occurs in a googol years? " +
                        "(Helpful tip: 1 googol = 10^100)";
                break;
            }
            case 1: {
                question = "What is the numerical value of the imaginary unit i?";
                break;
            }
            case 2: {
                int digits = 256 + random.nextInt(501);
                question = "Please type the first " + digits + " digits of PI.";
                break;
            }
            default: {
                int[] exponents = new int[4];
                for (int i = 0; i < exponents.length; i++) {
                    int newNum;
                    SEARCH: while (true) {
                        newNum = random.nextInt(9) + 1;
                        for (int j = 0; j < i; j++) {
                            if (exponents[j] == newNum) continue SEARCH;
                        }
                        break;
                    }

                    exponents[i] = newNum;
                }

                Arrays.sort(exponents);

                int bound1;
                int bound2;
                do {
                    bound1 = random.nextInt(-10, 10);
                    bound2 = random.nextInt(-10, 10);
                } while (bound1 == bound2);

                question = "Consider the graph of function f(x) = " + (random.nextInt(9) + 1) + "^" + exponents[3] + " + " +
                        (random.nextInt(9) + 1) + "^" + exponents[2] + " + " +
                        (random.nextInt(9) + 1) + "^" + exponents[1] + " + " +
                        (random.nextInt(9) + 1) + "^" + exponents[0] + ". " +
                        "Use integration to find the area under the graph of f when bounded by x = " + Math.min(bound1, bound2) +
                        " and x = " + Math.max(bound1, bound2) + ".";

                break;
            }
        }

        return new CalculationQuestion(question);
    }
}
