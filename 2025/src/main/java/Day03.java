package main.java;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day03 {

    static void main() throws Exception {

        var lines = List.of(
                "987654321111111"
                ,"811111111111119"
                ,"234234234234278"
                ,"818181911112111"
        );
        lines = Files.readAllLines(Path.of("in/day3.txt"));

        long sum = 0;
        for (String line : lines) {

            List<Integer> ints = new ArrayList<>();
            List<Integer> res = new ArrayList<>();
            for (int i = 0; i < line.length(); ++i) {
                ints.add(Integer.parseInt(line.substring(i, i + 1)));
            }

            // 234234234234278
            // xxxx|||||||||||
            //
            // 11 in [ length - 1 - 11 + 1, length - 1]
            // 1 in [ 0 , length - 1 - 11 ]


            // pick max. digit in [0, length-12]
            // then [length-12+1, length-1] = [length-11,length-1] has 11 digits
            int start = 0;
            int end = line.length() - 12;

            int remaining = 12;
            while (remaining > 0) {
                int max = ints.get(start);
                int maxIdx = start;
                for (int j = start; j <= end; ++j) {
                    if (ints.get(j) > max) {
                        max = ints.get(j);
                        maxIdx = j;
                    }
                }
                res.add(max);

                start = maxIdx + 1;
                --remaining;

                end = line.length() - remaining;
            }

            var str = res.stream().map(String::valueOf).collect(Collectors.joining(""));
            System.out.println(str);
            sum += Long.parseLong(str);
        }

        System.out.println("sum p2: " + sum);
    }
}
