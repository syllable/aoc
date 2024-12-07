package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static test.Assert.assertEquals;
import static test.Util.time;

public class Day07_02_Recursion {
    public static void main(String[] args) throws IOException {
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

    private static void run(List<String> lines, long expectedP2) {

        List<Day07.Equation> equations = lines.stream()
                .map(line -> {
                    long result = Long.parseLong(line.substring(0, line.indexOf(':')));
                    List<Integer> values = Arrays.stream(line.substring(line.indexOf(":") + 2).split(" ")).map(Integer::parseInt).toList();
                    return new Day07.Equation(result, values);
                })
                .toList();


        long p2 = 0;
        for (var eq : equations) {
            boolean matches =
                    acc(eq.values(), eq.result(), 0, 0, '*')
                            || acc(eq.values(), eq.result(), 0, 0, '+')
                            || acc(eq.values(), eq.result(), 0, 0, '|');
            if (matches) {
                p2 += eq.result();
            }
        }

        System.out.println(p2);
        assertEquals(expectedP2, p2);
    }

    private static boolean acc(
            List<Integer> input,
            long expectedResult,
            long accumulator,
            int nextOperandIdx,
            char operand
    ) {
        // 7290: 6, 8, 6, 15

        if (nextOperandIdx == input.size()) {
            return accumulator == expectedResult;
        }

        long prevAccumulator = accumulator;
        accumulator = eval(accumulator, input.get(nextOperandIdx), operand);
        // System.out.println("Eval: " + prevAccumulator + operand + input.get(nextOperandIdx) + "=" + accumulator);

        return acc(input, expectedResult, accumulator, nextOperandIdx + 1, '+')
                || acc(input, expectedResult, accumulator, nextOperandIdx + 1, '*')
                || acc(input, expectedResult, accumulator, nextOperandIdx + 1, '|');
    }

    private static long eval(long l1, long l2, char op) {
        return switch (op) {
            case '+' -> l1 + l2;
            case '*' -> l1 * l2;
            case '|' -> Long.parseLong("" + l1 + l2);
            default -> throw new IllegalStateException("Unexpected operator: " + op);
        };
    }
}
