package aoc;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeUnit;

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
        Position startPosition = Objects.requireNonNull(findPosition(map), "start position not found");

        walkUntilLeaveOrLoop(map, startPosition);
        int countP1 = 0;
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; ++x) {
                if (map[y][x] == 'X') {
                    ++countP1;
                }
            }
        }

        System.out.println("Part 1" + countP1);
        assertEquals(5531, countP1);

        // -- Part 2
        long start = System.nanoTime();

        List<StructuredTaskScope.Subtask<Boolean>> tasks = new ArrayList<>();
        long countP2;
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map[0].length; ++x) {
                    if (map[y][x] == VISITED) { // re-use map from part1 to only place obstacle on reachable place
                        int yy = y;
                        int xx = x;
                        tasks.add(scope.fork(() -> {
                            char[][] copy = copy(map);
                            copy[yy][xx] = OBSTACLE;
                            return walkUntilLeaveOrLoop(copy, startPosition);
                        }));
                    }
                }
            }
            scope.join().throwIfFailed();
            countP2 = tasks.stream().filter(o -> Boolean.TRUE.equals(o.get())).count();
        }

        long stop = System.nanoTime();

        System.out.println("Part 2: " + countP2);
        assertEquals(2165, countP2);

        System.out.println("Part 2 took " + TimeUnit.NANOSECONDS.toMillis( (stop - start)) + " ms");
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
