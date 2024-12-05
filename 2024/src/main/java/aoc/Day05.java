package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static test.Assert.assertEquals;
import static test.Assert.assertTrue;

public class Day05 {

    public static void main(String[] args) throws IOException {
        String x = Files.readString(Path.of("input/day5_full.txt"));
        // 143



        // 47|53 :
        // 47 before 53

        // read rules to weight numbers?
        // e.g. 47 before 53



        List<String> input = Arrays.asList(x.split("\n"));
        Set<String> rules = input.stream()
                .takeWhile(p -> p.contains("|"))
                .map(String::trim)
                .collect(Collectors.toSet());
        List<String> updates = input.stream()
                .filter(p -> p.contains(","))
                .map(String::trim)
                .toList();

        System.out.println(rules);
        System.out.println(updates);

        // todo: rank ints 0-100 by rules first?
        //  but does it handle "missing rules" (partial ordering)
        List<String> ok = new ArrayList<>();
        List<String> nok = new ArrayList<>();

        for (String update : updates) {
            List<String> pages = Arrays.asList(update.split(","));
            boolean updateOk = true;
            for (int i = 0; i < pages.size(); i++) {

                String current = pages.get(i);
                for (int j = 0; j < i; ++j) {
                    String prev = pages.get(j); // prev must be larger
                    if (rules.contains(current + "|" + prev)) {
                        updateOk = false;
                        break;
                    }
                }
                for (int j = i + 1; j < pages.size(); ++j) {
                    String succ = pages.get(j); // succ must be smaller
                    if (rules.contains(succ + "|" + current)) {
                        updateOk = false;
                        break;
                    }
                }
            }
            if (updateOk) {
                ok.add(update);
            } else {
                nok.add(update);
            }
        }

        var sumP1 = ok.stream()
                .map(o -> o.split(","))
                .map(a -> a[a.length / 2])
                .mapToInt(Integer::parseInt)
                .sum();

        System.out.println("--P1--");
        System.out.println(sumP1);

        System.out.println("--P2--");
        int sumP2 = 0;
        for (var update : nok) {
            List<String> up = Arrays.asList(update.split(","));
            up.sort(Comparator.comparing(Function.identity(), (a, b) -> {
                if (rules.contains(a + "|" + b)) {
                    return -1;
                } else if (rules.contains(b + "|" + a)) {
                    return 1;
                } else {
                    return 0;
                }
            }));
            sumP2 += Integer.parseInt(up.get(up.size() / 2));
        }
        System.out.println(sumP2);


        assertEquals(6951, sumP1);
        assertEquals(4121, sumP2);
    }
}
