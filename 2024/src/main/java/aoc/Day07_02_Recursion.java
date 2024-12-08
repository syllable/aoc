package aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

import static test.Assert.assertEquals;
import static test.Util.time;

public class Day07_02_Recursion {
    public static void main(String[] args) throws Exception {
        String exampleInput = """
                190: 10 19
                3267: 81 40 27
                83: 17 5
                156: 15 6
                7290: 6 8 6 15
                161011: 16 10 13
                192: 17 8 14
                21037: 9 7 18 13
                292: 11 6 16 20
                """;

        System.out.println("-- Example Input");
        run(Arrays.asList(exampleInput.split("\n")), 11387L);

        List<String> lines = Files.readAllLines(Path.of("input/day7.txt"));

        System.out.println("-- Full Input");
        time(
                () -> run(lines, 162042343638683L),
                "Recursive search"
        );
    }

    private static int run(List<String> lines, long expectedP2) throws Exception {

        long p2 = 0;
        for (var line : lines) {
            long result = Long.parseLong(line.substring(0, line.indexOf(':')));
            List<Integer> values = Arrays.stream(line.substring(line.indexOf(":") + 2).split(" ")).map(Integer::parseInt).toList();
            var eq = new Day07.Equation(result, values);

            boolean match
                    // = matches(eq, eq.values().getFirst(), 1);
                    = matchesRTL(eq, eq.result(), eq.values().size() - 1, '0');
            if (match) {
                p2 += eq.result();
            }
        }

        System.out.println(p2);
        assertEquals(expectedP2, p2);
        return 0;
    }

    private static boolean matches(
            Day07.Equation eq,
            long accumulator,
            int nextOperandIdx
    ) {
        long expectedResult = eq.result();
        List<Integer> values = eq.values();
        if (nextOperandIdx == values.size()) {
            return accumulator == expectedResult;
        }

        int next = values.get(nextOperandIdx);
        return matches(eq, accumulator + next, nextOperandIdx + 1)
                || matches(eq, accumulator * next, nextOperandIdx + 1)
                || matches(eq, concat(accumulator, next), nextOperandIdx + 1);
    }

    private static boolean matchesRTL(
            Day07.Equation equation,
            long accumulator,
            int nextOperandIdx,
            char lastOp
    ) {
        List<Integer> input = equation.values();
        if (nextOperandIdx == -1) {
            return (lastOp == '/' && accumulator == 1) || (lastOp != '/' && accumulator == 0);
        }

        Integer next = input.get(nextOperandIdx);
        if (accumulator % next == 0) {
            if (matchesRTL(equation, accumulator / next, nextOperandIdx - 1, '/')) {
                return true;
            }
        }
        if (accumulator > next) {
            if (matchesRTL(equation, accumulator - next, nextOperandIdx - 1, '-')) {
                return true;
            }
        }

        // 150129
        //    129
        // 150000
        // 150

        // String acc = String.valueOf(accumulator);
        // String nxt = String.valueOf(next);
        // if (acc.endsWith(nxt)) {
        //     int idx = acc.lastIndexOf(nxt);
        //     long nextAcc = idx == 0 ? 0 : Long.parseLong(acc.substring(0, idx));
        //     return matchesRTL(equation, nextAcc, nextOperandIdx - 1, '|');
        // }

        long diff = accumulator - next;
        long diffTrailingZeros = countTrailingZerosBase10(diff);
        if (diffTrailingZeros > 0) {
            int nextDigits = countDigitsBase10(next);
            if (diffTrailingZeros >= nextDigits) {
                long nextAcc = removeTrailingZerosBase10(diff, nextDigits);
                return matchesRTL(equation, nextAcc, nextOperandIdx - 1, '|');
            }
        }

        return false;
    }

    static long countTrailingZerosBase10(long l) {
        long c = 0;
        while (l % 10 == 0 && l != 0) {
            ++c;
            l /= 10;
        }
        return c;
    }

    static int countDigitsBase10(long l) {
        int d = 1;
        while ((l /= 10) > 0) {
            ++d;
        }
        return d;
    }

    static long removeTrailingZerosBase10(long l, int digits) {
        for (int i = 0; i < digits; ++i) {
            if (l % 10 == 0) {
                l /= 10;
            } else {
                break;
            }
        }
        return l;
    }

    private static long concat(long l1, long l2) {
        // return Long.parseLong("" + l1 + l2);

        // 12
        //   501
        // 12*10^3 501
        int m = 1;
        long l = l2;
        while (l >= 1) {
            m *= 10;
            l /= 10;
        }
        return l1 * m + l2;
    }
}
