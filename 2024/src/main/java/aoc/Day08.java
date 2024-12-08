package aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static test.Assert.assertEquals;

public class Day08 {

    public static void main(String[] args) throws Exception {

        String input = """
                ............
                ........0...
                .....0......
                .......0....
                ....0.......
                ......A.....
                ............
                ............
                ........A...
                .........A..
                ............
                ............
                """;
        List<String> lines
                = Files.readAllLines(Path.of("input/day8.txt"));
        //   = Arrays.asList(input.split("\n"));

        char[][] map = new char[lines.size()][lines.getFirst().length()];
        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.getFirst().length(); x++) {
                map[y][x] = lines.get(y).charAt(x);
            }
        }

        int countP1 = 0;
        char[][] antiNodeMap = new char[lines.size()][lines.getFirst().length()];

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {

                char antenna = map[y][x];
                if (antenna != '.' && antenna != '#') {

                    // walk dx dy for each Antenna
                    for (int dy = -map.length - 1; dy < map.length; dy++) {
                        for (int dx = -map[0].length - 1; dx < map[0].length; dx++) {
                            int ox = x + dx;
                            int oy = y + dy;
                            boolean inMap = checkBound(map, ox, oy);
                            if (inMap && (dx != 0 || dy != 0)) {
                                char otherAntenna = map[oy][ox];
                                if (otherAntenna == antenna) {
                                    int ax = ox + dx;
                                    int ay = oy + dy;
                                    if (checkBound(map, ax, ay)) {

                                        if (antiNodeMap[ay][ax] != '#') {
                                            antiNodeMap[ay][ax] = '#';
                                            ++countP1;
                                        }

                                        // visual only since antinode must is not on map if antenna in same location
                                        if (map[ay][ax] == '.') {
                                            map[ay][ax] = '#';
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println("Part 1: " + countP1);
        assertEquals(409, countP1);
    }

    private static boolean checkBound(char[][] map, int x, int y) {
        return x >= 0 && x < map[0].length
                && y >= 0 && y < map.length;
    }
}
