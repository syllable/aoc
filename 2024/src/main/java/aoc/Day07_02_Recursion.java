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

        List<Day07.Equation> equations = lines.stream()
                .map(line -> {
                    long result = Long.parseLong(line.substring(0, line.indexOf(':')));
                    List<Integer> values = Arrays.stream(line.substring(line.indexOf(":") + 2).split(" ")).map(Integer::parseInt).toList();
                    return new Day07.Equation(result, values);
                })
                .toList();


        long p2 = 0;
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            List<StructuredTaskScope.Subtask<Long>> tasks = new ArrayList<>();
            for (var eq : equations) {
                tasks.add(scope.fork(() -> {
                    boolean match = matches(eq);
                    return match ? eq.result() : 0;
                }));
            }
            scope.join().throwIfFailed();
            p2 = tasks.stream().mapToLong(StructuredTaskScope.Subtask::get).sum();
        }

        System.out.println(p2);
        assertEquals(expectedP2, p2);
        return 0;
    }

    private static boolean matches(Day07.Equation eq) {
        return matches(eq.values(), eq.result(), 0, 0, '+')
                || matches(eq.values(), eq.result(), 0, 0, '*')
                || matches(eq.values(), eq.result(), 0, 0, '|');
    }

    private static boolean matches(
            List<Integer> input,
            long expectedResult,
            long accumulator,
            int nextOperandIdx,
            char operator
    ) {
        if (nextOperandIdx == input.size()) {
            return accumulator == expectedResult;
        }

        long prevAccumulator = accumulator;
        accumulator = eval(accumulator, input.get(nextOperandIdx), operator);
        // System.out.println("Eval: " + prevAccumulator + operand + input.get(nextOperandIdx) + "=" + accumulator);

        return matches(input, expectedResult, accumulator, nextOperandIdx + 1, '+')
                || matches(input, expectedResult, accumulator, nextOperandIdx + 1, '*')
                || matches(input, expectedResult, accumulator, nextOperandIdx + 1, '|');
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
