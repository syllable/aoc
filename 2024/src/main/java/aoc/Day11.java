package aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

import static test.Assert.assertEquals;

public class Day11 {

    public static void main(String[] args) throws Exception {

        String input = "125 17";
        // p1: 5, 6, 5, 3, 1, 3, 5, 3, and 5 (=36)
        // p2: 81

        input = Files.readString(Path.of("input/" + MethodHandles.lookup().lookupClass().getSimpleName().toLowerCase() + ".txt"));
        List<String> stones = Arrays.asList(input.split(" "));

        var l1 = run(new ArrayList<>(stones), 25);
        System.out.println(l1);
        assertEquals(186175, l1);

        var l2 = run(new ArrayList<>(stones), 25);
        System.out.println(l2);
    }

    static long run(List<String> lines, int iterations) throws Exception {


        System.out.println(lines);
        long sum = 0;

        for (int stoneIdx = 0; stoneIdx < lines.size(); ++stoneIdx) {

            long start = System.nanoTime();

            List<String> st = new ArrayList<>();
            st.add(lines.get(stoneIdx));

            for (int i = 0; i < iterations; ++i) {

                for (int j = 0; j < st.size(); ++j) {

                    String stone = st.get(j);
                    if (stone.equals("0")) {
                        st.set(j, "1");
                    } else if (stone.length() % 2 == 0) {
                        String firstHalf = stone.substring(0, stone.length() / 2);
                        String secondHalf = stone.substring(stone.length() / 2);

                        firstHalf = String.valueOf(Integer.parseInt(firstHalf));
                        st.set(j, firstHalf);
                        secondHalf = String.valueOf(Integer.parseInt(secondHalf));
                        st.add(j + 1, secondHalf);
                        ++j;
                    } else {
                        long val = Long.parseLong(stone);
                        long res = Math.multiplyExact(val, 2024L);
                        st.set(j, String.valueOf(res));
                    }
                }
            }

            var stop = System.nanoTime();
            System.out.println(stoneIdx + " took " + Duration.ofNanos(stop - start));

            sum += st.size();
        }

        return sum;
    }
}
