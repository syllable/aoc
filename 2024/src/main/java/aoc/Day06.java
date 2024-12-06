package aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static test.Assert.assertEquals;

public class Day06 {

    private record Point(int x, int y) {}
    private record Position(int x, int y, char d) {
        Position moveInDirection() {
            return switch (this.d()) {
                case '^' -> new Position(x, y-1, '^');
                case 'v' -> new Position(x, y+1, 'v');
                case '<' -> new Position(x-1, y, '<');
                case '>' -> new Position(x+1, y, '>');
                default -> throw new IllegalStateException("Unexpected value: " + this.d());
            };
        }
        Position turn90Right() {
            return switch (this.d()) {
                case '^' -> new Position(x, y, '>');
                case 'v' -> new Position(x, y, '<');
                case '<' -> new Position(x, y, '^');
                case '>' -> new Position(x, y, 'v');
                default -> throw new IllegalStateException("Unexpected value: " + this.d());
            };
        }
    }

    private static final char OBSTACLE = '#';
    private static final char WALKABLE = '.';
    private static final char VISITED = 'X';
    private static final List<Character> POSITION_INDICATORS = List.of('^', 'v', '<', '>');

    private static char[][] readMap(Path path) throws Exception {
        String[] lines = Files.readAllLines(path).toArray(new String[0]);
        char[][] map = new char[lines[0].length()][lines.length];

        int xMax = map.length - 1;
        int yMax = map[0].length - 1;

        for (int x = 0; x <= xMax; x++) {
            for (int y = 0; y <= yMax; y++) {
                char c = lines[y].charAt(x);
                map[y][x] = c;
            }
        }
        return map;
    }

    public static void main(String[] args) throws Exception {
        char[][] map = readMap(Path.of("input/day6.txt"));

        // -- Part 1
        char[][] p1 = copy(map);
        walkUntilLeaveOrLoop(p1);
        int countP1 = 0;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; ++x) {
                if (p1[y][x] == 'X') {
                    ++countP1;
                }
            }
        }
        System.out.println(countP1);
        assertEquals(5531, countP1);

        // -- Part 2
        int countP2 = 0;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; ++x) {
                if (map[y][x] == WALKABLE) {
                    char[][] copy = copy(map);
                    copy[y][x] = OBSTACLE;
                    walkUntilLeaveOrLoop(copy);
                    if (findPosition(copy) != null) {
                        ++countP2;
                    }
                }
            }
        }
        System.out.println(countP2);
        assertEquals(2165, countP2);
    }

    private static void walkUntilLeaveOrLoop(char[][] map) {

        int xMax = map[0].length - 1;
        int yMax = map.length - 1;

        Position position = Objects.requireNonNull(findPosition(map), "no start position found");
        map[position.x][position.y] = VISITED;

        Set<Position> visitedPositions = new HashSet<>();
        visitedPositions.add(position);

        int steps = 0;
        while (true) {
            ++steps;
            Position maybeNext = position.moveInDirection();
            if (maybeNext.x() > xMax || maybeNext.x() < 0 || maybeNext.y() > yMax || maybeNext.y() < 0) {
                map[position.y()][position.x()] = 'X';
                break;
            }
            char element = map[maybeNext.y()][maybeNext.x()];

            if (element == OBSTACLE) {
                position = position.turn90Right();
                map[position.y()][position.x()] = position.d();
            } else if (element == WALKABLE || element == VISITED) {
                map[position.y()][position.x()] = 'X';
                map[maybeNext.y()][maybeNext.x()] = maybeNext.d();

                position = maybeNext;
                boolean newPosition = visitedPositions.add(position);
                if (!newPosition) {
                    break;
                }
            }
        }
    }

    private static Position findPosition(char[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                char c = map[y][x];
                if (POSITION_INDICATORS.contains(c)) {
                    return new Position(x, y, c);
                }
            }
        }
        return null;
    }

    private static char[][] copy(char[][] map) {
        char[][] copy = new char[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            System.arraycopy(map[i], 0, copy[i], 0, map[i].length);
        }
        return copy;
    }
}
