package aoc;

import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static test.Assert.assertEquals;

public class Day11 {

    public static void main(String[] args) throws Exception {


        List<String> example = List.of("125", "17");
        assertEquals(22, solve(example, 6));
        assertEquals(55312, solve(example, 25));


        String input = Files.readString(Path.of("input/" + MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase() + ".txt"));
        List<String> stones = Arrays.asList(input.split(" "));

        long l1 = solve(stones, 25);
        System.out.println(l1);
        assertEquals(186175, l1);

        long l2 = solve(stones, 75);
        System.out.println(l2);
        assertEquals(220566831337810L, l2);
    }

    private static long solve(List<String> stones, int iterations) {
        Map<String, Long> stoneToCount = new HashMap<>();

        for (var stone : stones) {
            stoneToCount.merge(stone, 1L, Long::sum);
        }

        for (int i = 0; i < iterations; i++) {

            // 5688 becomes 56 88
            // -> if we have n x 5688, we get n x 56 and n x 88 in the next iteration

            Map<String, Long> itStoneToCount = new HashMap<>();
            for (var entry : stoneToCount.entrySet()) {
                String stone = entry.getKey();
                long count = entry.getValue();

                if (stone.equals("0")) {
                    itStoneToCount.merge("1", count, Long::sum);
                } else if (stone.length()%2 == 0) {
                    String firstHalf = stone.substring(0, stone.length() / 2);
                    String secondHalf = stone.substring(stone.length() / 2);
                    firstHalf = String.valueOf(Integer.parseInt(firstHalf));
                    secondHalf = String.valueOf(Integer.parseInt(secondHalf));
                    itStoneToCount.merge(firstHalf, count, Long::sum);
                    itStoneToCount.merge(secondHalf, count, Long::sum);
                } else {
                    long value = Long.parseLong(stone) * 2024L;
                    itStoneToCount.merge(String.valueOf(value), count, Long::sum);
                }
            }
            stoneToCount = itStoneToCount;

        }
        System.out.println(stoneToCount);

        return stoneToCount.values().stream()
                .mapToLong(l -> l)
                .sum();
    }
}
