package diamondwalker.sscary.util;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MathUtil {
    public static double logBase(double a, double base) {
        return Math.log(a) / Math.log(base);
    }

    public static List<Integer> getDigits(int number) {
        List<Integer> list = new ArrayList<>();

        do {
            list.add(number % 10);
            number /= 10;
        } while (number > 0);

        Collections.reverse(list);
        return list;
    }

    public static int max(int... numbers) {
        int current = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            current = Math.max(current, numbers[i]);
        }
        return current;
    }

    public static int min(int... numbers) {
        int current = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            current = Math.min(current, numbers[i]);
        }
        return current;
    }
}
