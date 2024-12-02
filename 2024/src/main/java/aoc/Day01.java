package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import one.util.streamex.StreamEx;

public class Day01 {
    record Pair(int left, int right){}

    public static void main(String[] args) throws IOException {

        // Part 1
        List<Pair> input = read();

        List<Integer> left = input.stream().map(Pair::left).sorted().toList();
        List<Integer> right = input.stream().map(Pair::right).sorted().toList();

        int resultP1 = StreamEx.zip(left, right, (l, r) -> Math.abs(l - r))
            .mapToInt(i -> (int)i)
            .sum();
        System.out.println(resultP1);

        // Part 2
        Map<Integer, Integer> countsRight = new HashMap<>();
        right.forEach(num -> countsRight.merge(num, 1, Integer::sum));

        int resultP2 = left.stream()
            .mapToInt(num -> num * countsRight.getOrDefault(num, 0))
            .sum();
        System.out.println(resultP2);
    }

    private static List<Pair> read() throws IOException {
        try (var lines = Files.lines(Path.of("input/day1.txt"))) {
            return lines.map(line -> {
                var split = line.split("   ");
                return new Pair(
                    Integer.parseInt(split[0]), 
                    Integer.parseInt(split[1])
                );
            })
            .toList();
        }
    }
}
