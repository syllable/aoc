package aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static test.Assert.assertEquals;

public class Day11 {

    public static void main(String[] args) throws IOException {

        String input = "125 17";
        // p1: 5, 6, 5, 3, 1, 3, 5, 3, and 5 (=36)
        // p2: 81

        input = Files.readString(Path.of("input/" + MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase() + ".txt"));
        List<String> stones = Arrays.asList(input.split(" "));

        var l1 = run(stones, 25);
        System.out.println(l1);
        assertEquals(
                186175, l1
        );

        var l2 = run(stones, 30);
        System.out.println(l2);
    }

    static int run(List<String> lines, int iterations) {

        List<String> stones = lines;
        System.out.println(stones);
        long s = 0;
        for (int i = 0; i < iterations; ++i) {
            stones = applyRules(stones);

            System.out.println("#" + i + " stones: " + stones.size());
        }
        return stones.size();
    }

    private static List<String> applyRules(List<String> stones) {
        List<String> newList = new ArrayList<>();

        for (String stone : stones) {
            if (stone.equals("0")) {
                newList.add("1");
            } else if (stone.length() % 2 == 0) {
                String firstHalf = stone.substring(0, stone.length() / 2);
                String secondHalf = stone.substring(stone.length() / 2);

                newList.add(String.valueOf(Integer.parseInt(firstHalf))); // long?
                newList.add(String.valueOf(Integer.parseInt(secondHalf)));

            } else {
                long val = Long.parseLong(stone);
                long res = Math.multiplyExact(val, 2024L);
                newList.add(String.valueOf(res));
            }
        }

        return newList;
    }
}
