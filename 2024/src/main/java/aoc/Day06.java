package aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static test.Assert.assertEquals;

public class Day06 {

    private record Position(int x, int y, char direction) {
        Position moveInDirection() {
            return switch (this.direction()) {
                case '^' -> new Position(x, y-1, '^');
                case 'v' -> new Position(x, y+1, 'v');
                case '<' -> new Position(x-1, y, '<');
                case '>' -> new Position(x+1, y, '>');
                default -> throw new IllegalStateException("Unexpected value: " + this.direction());
            };
        }
        char turn90Right() {
            return switch (this.direction()) {
                case '^' -> '>';
                case 'v' -> '<';
                case '<' -> '^';
                case '>' -> 'v';
                default -> throw new IllegalStateException("Unexpected value: " + this.direction());
            };
        }
    }

    private static final char OBSTACLE = '#';
    private static final char WALKABLE = '.';
    private static final char VISITED = 'X';

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
        Position startPosition = Objects.requireNonNull(findPosition(map), "start position not found");

        walkUntilLeaveOrLoop(p1, startPosition);
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
                    boolean loop = walkUntilLeaveOrLoop(copy, startPosition);
                    if (loop) {
                        ++countP2;
                    }
                }
            }
        }

        System.out.println(countP2);
        assertEquals(2165, countP2);
    }



    private static boolean walkUntilLeaveOrLoop(char[][] map, final Position startPosition) {

        int xMax = map[0].length - 1;
        int yMax = map.length - 1;

        Position currentPosition = startPosition;

        Set<Position> visitedPositions = new HashSet<>();
        visitedPositions.add(currentPosition);

        while (true) {
            Position maybeNext = currentPosition.moveInDirection();
            if (maybeNext.x() > xMax || maybeNext.x() < 0 || maybeNext.y() > yMax || maybeNext.y() < 0) {
                map[currentPosition.y()][currentPosition.x()] = VISITED;
                return false;
            }
            char element = map[maybeNext.y()][maybeNext.x()];

            if (element == OBSTACLE) {
                currentPosition = new Position(currentPosition.x(), currentPosition.y(), currentPosition.turn90Right());
                map[currentPosition.y()][currentPosition.x()] = currentPosition.direction();
            } else {
                map[currentPosition.y()][currentPosition.x()] = VISITED;
                map[maybeNext.y()][maybeNext.x()] = maybeNext.direction();

                currentPosition = maybeNext;
                if (element == VISITED && !visitedPositions.add(currentPosition)) {
                    return true;
                }
            }
        }
    }

    private static Position findPosition(char[][] map) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                char c = map[y][x];
                if (c != OBSTACLE && c!= WALKABLE && c!= VISITED) {
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
