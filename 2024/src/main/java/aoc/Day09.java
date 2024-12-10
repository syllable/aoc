package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static test.Assert.assertEquals;
import static test.Assert.assertTrue;

public class Day09 {

    public static void main(String[] args) throws IOException {
        String input = "2333133121414131402";
        String out = """
                00...111...2...333.44.5555.6666.777.888899
                009..111...2...333.44.5555.6666.777.88889.
                0099.111...2...333.44.5555.6666.777.8888..
                00998111...2...333.44.5555.6666.777.888...
                009981118..2...333.44.5555.6666.777.88....
                0099811188.2...333.44.5555.6666.777.8.....
                009981118882...333.44.5555.6666.777.......
                0099811188827..333.44.5555.6666.77........
                00998111888277.333.44.5555.6666.7.........
                009981118882777333.44.5555.6666...........
                009981118882777333644.5555.666............
                00998111888277733364465555.66.............
                0099811188827773336446555566..............
                """;

        // p1: 5, 6, 5, 3, 1, 3, 5, 3, and 5 (=36)
        // p2: 81

        String lines =
               // input;
               Files.readString(Path.of("input/day9.txt"));

        // run(lines.trim());
        runPart2(lines.trim());
    }

    static final Integer EMPTY = -1;
    static void run(String input) {
        System.out.println(input);

        // p1
        ArrayList<Integer> fs = new ArrayList<>();
        for (int i = 0; i < input.length(); i += 2) {
            int fileId = i / 2;
            int fileSpace = Integer.parseInt(String.valueOf(input.charAt(i)));
            int emptySpace = (i + 1) == input.length() ?
                    0 // final block needs no length
                    : Integer.parseInt(String.valueOf(input.charAt(i + 1)));

            for (int j = 0; j < fileSpace; ++j) {
                fs.add(fileId);
            }
            for (int j = 0; j < emptySpace; ++j) {
                fs.add(EMPTY);
            }
        }
        // System.out.println(fs);

        // move right to left (or left to right moving empty?)
        int head = fs.size();
        while (--head >= 0) {
            int m = fs.get(head);
            if (!EMPTY.equals(m)) {
                // System.out.println("Move: " + m);
                int firstEmptySpace = fs.indexOf(EMPTY);
                if (firstEmptySpace != -1 && firstEmptySpace < head) {
                    assertTrue(fs.set(firstEmptySpace, m).equals(EMPTY));
                    fs.set(head, EMPTY);
                }
            }
            // System.out.println(fs);
        }
        System.out.println(fs);

        long sumP1 = 0;
        for (int i = 0; i < fs.size(); ++i) {
            if (fs.get(i).equals(EMPTY)) {
                break;
            }
            sumP1 += ((long)i * fs.get(i));
        }
        System.out.println(sumP1);



    }

    private static void runPart2(String input) {

        ArrayList<Integer> fs = new ArrayList<>();
        for (int i = 0; i < input.length(); i += 2) {
            int fileId = i / 2;
            int fileSpace = Integer.parseInt(String.valueOf(input.charAt(i)));
            int emptySpace = (i + 1) == input.length() ?
                    0 // final block needs no length
                    : Integer.parseInt(String.valueOf(input.charAt(i + 1)));

            for (int j = 0; j < fileSpace; ++j) {
                fs.add(fileId);
            }
            for (int j = 0; j < emptySpace; ++j) {
                fs.add(EMPTY);
            }
        }

        // move right to left (or left to right moving empty?)
        List<Integer> sortedIds = fs.stream()
                .filter(i -> !EMPTY.equals(i))
                .distinct()
                .sorted(Comparator.reverseOrder()).toList();
        System.out.println(sortedIds);

        for (int fileId : sortedIds) {
            tryMove(fs, fileId);
        }

        long sumP2 = 0;
        for (int i = 0; i < fs.size(); ++i) {
            if (!fs.get(i).equals(EMPTY)) {
                sumP2 += ((long)i * fs.get(i));
            }
        } // 6436819084274
        System.out.println("P2: " + sumP2);
        assertEquals(6436819084274L, sumP2);
    }

    private static void tryMove(List<Integer> fs, int fileId) {
        int startFile = fs.indexOf(fileId);
        int endFile = fs.lastIndexOf(fileId);
        int requiredSpace = (endFile - startFile) + 1;
        assertTrue(requiredSpace >= 0);

        int loop = 0;
        int searchOffset = 0;
        while (searchOffset < startFile) {
            ++loop;
            int startSubList = fs.subList(searchOffset, fs.size()).indexOf(EMPTY);
            if (startSubList == -1) {
                break;
            }
            int start = startSubList + searchOffset;
            int end = start;
            while (
                    end + 1 < fs.size()
                    && fs.get(end + 1).equals(EMPTY)
            ) {
                ++end;
            }

            if (startFile < start) {
                break;
            }

            int emptySpace = (end - start) + 1;
            if (emptySpace >= requiredSpace) {

                // for (int i = s ; i < s + r ; ++i) => r iterations
                for (int i = start; i < start + requiredSpace; ++i) {
                    fs.set(i, fileId);
                }
                for (int i = startFile; i <= endFile; ++i) {
                    if (i == 0) {
                        System.out.println(".");
                    }
                    fs.set(i, EMPTY);
                }
                break;
            } else {
                searchOffset = end + 1;
            }

            if (loop >= 5_000) {
                throw new AssertionError("infinite loop? next searchOffset = " + searchOffset);
            }
        }
    }
}
