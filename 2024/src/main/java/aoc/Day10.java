package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Day10 {

    public static void main(String[] args) throws IOException {
        String input = """
                89010123
                78121874
                87430965
                96549874
                45678903
                32019012
                01329801
                10456732
                """;
        // 5, 6, 5, 3, 1, 3, 5, 3, and 5 (=36)

        List<String> lines =
                // Arrays.asList(input.split("\n"));
                Files.readAllLines(Path.of("input/day10.txt"));

        run(lines);
    }

    static void run(List<String> lines) {

        int[][] map = new int[lines.size()][lines.getFirst().length()];
        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.getFirst().length(); x++) {
                String val = String.valueOf(lines.get(y).charAt(x));
                map[y][x] = val.equals(".")  ? -1 // example only
                        : Integer.parseInt(val);
            }
        }


        int sumP1 = 0;
        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.getFirst().length(); x++) {
                if (map[y][x] == 0) {
                    int walk = walk(copy(map), x, y);
                    sumP1 += walk;
                }
            }
        }
        System.out.println("P1 = " + sumP1);
    }

    private static int walk(int[][] map, int x, int y) {
        if (!checkBound(map, x, y)) {
            throw new AssertionError();
        }

        int current = map[y][x];
        if (current == 9) {
            map[y][x] = 100; // mark as reached to avoid duplicates (todo: another way?)
            return 1;
        }

        int res = 0;
        if (checkBound(map, x - 1, y)
                && map[y][x - 1] == current + 1) {
            res += walk(map, x - 1, y);
        }

        if (checkBound(map, x + 1, y)
            && map[y][x + 1] == current + 1
        ) {
            res += walk(map, x + 1, y);
        }

        if (checkBound(map, x, y - 1)
            && map[y - 1][x] == current + 1) {
            res += walk(map, x, y - 1);
        }

        if (checkBound(map, x, y + 1)
            && map[y + 1][x] == current + 1) {
            res += walk(map, x, y + 1);
        }

        return res;
    }

    private static boolean checkBound(int[][] map, int x, int y) {
        return x >= 0 && x < map[0].length
                && y >= 0 && y < map.length;
    }

    private static int[][] copy(int[][] map) {
        int[][] copy = new int[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            System.arraycopy(map[i], 0, copy[i], 0, map[i].length);
        }
        return copy;
    }
}