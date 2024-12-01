package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import one.util.streamex.StreamEx;

public class Day01 {
    record Pair(int left, int right){}

    public static void main(String[] args) throws IOException {
        List<Pair> input = read();

        List<Integer> left = input.stream().map(Pair::left).sorted().toList();
        List<Integer> right = input.stream().map(Pair::right).sorted().toList();

        int result = StreamEx.zip(left, right, (l, r) -> Math.abs(l - r))
            .mapToInt(i -> (int)i)
            .sum();
        System.out.println(result);
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
