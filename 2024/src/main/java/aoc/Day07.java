package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static test.Assert.assertEquals;

public class Day07 {
    private record Equation(long result, List<Integer> values) {
    }

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

        run(Arrays.asList(exampleInput.split("\n")), 3749L, 11387L);
        run(Files.readAllLines(Path.of("input/day7.txt")), 1260333054159L, 162042343638683L);
    }

    private static void run(List<String> lines, long expectedP1, long expectedP2) {
        long p1 = 0;
        List<Equation> equations = lines.stream()
                .map(line -> {
                    long result = Long.parseLong(line.substring(0, line.indexOf(':')));
                    List<Integer> values = Arrays.stream(line.substring(line.indexOf(":") + 2).split(" ")).map(Integer::parseInt).toList();
                    return new Equation(result, values);
                })
                .toList();

        for (var eq : equations) {
            List<List<Character>> permutations = getPermutations(eq, List.of('+', '*'));
            for (var perm : permutations) {
                long result = eval(eq.values(), perm);
                if (eq.result() == result) {
                    p1 += result;
                    break;
                }
            }
        }
        System.out.println("Part 1: " + p1);
        assertEquals(expectedP1, p1);


        // -- Part 2
        long p2 = 0;
        for (var eq : equations) {
            List<List<Character>> permutations = getPermutations(eq, List.of('+', '*', '|'));
            for (var perm : permutations) {
                long result = eval(eq.values(), perm);
                if (eq.result() == result) {
                    p2 += result;
                    break;
                }
            }
        }
        System.out.println("Part 2: " + p2);
        assertEquals(expectedP2, p2);
    }

    private static long eval(List<Integer> ints, List<Character> ops) {
        long sum = ints.getFirst();
        for (int i = 1; i < ints.size(); ++i) {
            int right = ints.get(i);
            char op = ops.get(i - 1);
            if (op == '+') {
                sum += right;
            } else if (op == '*') {
                sum *= right;
            } else if (op == '|') {
                sum = Long.parseLong("" + sum + right);
            } else {
                throw new AssertionError("Unhandled operand: " + op);
            }
        }
        return sum;
    }

    private static List<List<Character>> getPermutations(Equation eq, List<Character> ops) {
        int size = eq.values().size() - 1;

        List<List<Character>> list = new ArrayList<>(Collections.nCopies(ops.size(), Collections.emptyList()));

        while (list.getFirst().size() < size) {
            List<List<Character>> newList = new ArrayList<>();

            for (var op : ops) {
                for (var l : list) {
                    List<Character> copy = new ArrayList<>(l);
                    copy.add(op);
                    newList.add(copy);
                }

            }
            list = newList;
        }

        return list;
    }
}
