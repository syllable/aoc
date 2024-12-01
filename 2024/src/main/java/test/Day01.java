package test;

import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import one.util.streamex.StreamEx;

import static test.Assert.assertTrue;

public class Day01 {
    record Pair(int left, int right) {
    }

    public static void main(String[] args) throws IOException {

        // Part 1
        List<Pair> input = readUsingFileChannelMap();
        assertTrue(input.equals(read()), "read input methods disagree");

        List<Integer> left = input.stream().map(Pair::left).sorted().toList();
        List<Integer> right = input.stream().map(Pair::right).sorted().toList();

        int resultP1 = StreamEx.zip(left, right, (l, r) -> Math.abs(l - r))
                .mapToInt(i -> (int) i)
                .sum();
        System.out.println(resultP1);

        // Part 2
        Map<Integer, Integer> countsRight = new HashMap<>();
        right.forEach(num -> countsRight.merge(num, 1, Integer::sum));

        int resultP2 = left.stream()
                .mapToInt(num -> num *= countsRight.getOrDefault(num, 0))
                .sum();
        System.out.println(resultP2);

        if (resultP1 != 3714264) {
            throw new AssertionError("P1 fail: got " + resultP1 + " expected: " + 3714264);
        }
        if (resultP2 != 18805872) {
            throw new AssertionError("P2 fail: got " + resultP2 + " expected: " + 18805872);
        }
    }

    private static List<Pair> read() throws IOException {
        try (var lines = Files.lines(Path.of("input/day1.txt"))) {
            return lines.map(line -> {
                var split = line.split("   ");
                return new Pair(
                        Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]));
            })
                    .toList();
        }
    }

    private static List<Pair> readUsingFileChannelMap() throws IOException {
        List<Pair> pairs = new ArrayList<>();
        try (var arena = Arena.ofConfined()) {
            FileChannel ch = FileChannel.open(Path.of("input/day1.txt"), StandardOpenOption.READ);
            MemorySegment seg = ch.map(MapMode.READ_ONLY, 0L, ch.size(), arena);

            for (int l = 0; l < ch.size(); l += 14) {
                var bb = seg.asSlice(l, 5).asByteBuffer();
                var dd = readInt(bb);

                var bb2 = seg.asSlice(l + 8, 5).asByteBuffer();
                var dd2 = readInt(bb2);
                pairs.add(new Pair(dd, dd2));
            }
        }
        return pairs;
    }

    private static int readInt(ByteBuffer buf) {
        int sum = 0;
        int m = 1;
        for (int p = buf.limit() - 1; p > -1; --p) {
            int d = buf.get(p) - '0';
            sum += d * m;
            m *= 10;
        }
        return sum;
    }
}
