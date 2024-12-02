package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Day02 {

  public static void main(String[] args) throws IOException {
    List<int[]> levels = read();

    var count = levels.stream()
        .filter(Day02::isLevelSafe)
        .count();
    System.out.println(count);

    // p2
    var count2 = levels.stream()
        .filter(level -> {

          if (isLevelSafe(level)) {
            return true;
          }

          for (int excludeIdx = 0; excludeIdx < level.length; ++excludeIdx) {
            // brute force with array copy
            int[] withOneSkipped = copyExceptOne(level, excludeIdx);
            if (isLevelSafe(withOneSkipped)) {
              return true;
            }
          }

          return false;
        })
        .count();
    System.out.println(count2);
  }

  private static int[] copyExceptOne(int[] array, int excludeIdx) {
    int[] copy = new int[array.length - 1];
    System.arraycopy(array, 0, copy, 0, excludeIdx);
    System.arraycopy(array, excludeIdx + 1, copy, excludeIdx, array.length - excludeIdx - 1);
    return copy;
  }

  private static boolean isLevelSafe(int[] level) {
    boolean lineIsIncreasing = level[1] > level[0];
    for (int i = 1; i < level.length; ++i) {
      boolean isIncreasing = level[i] > level[i - 1];
      int diff = Math.abs(level[i] - level[i - 1]);
      if (lineIsIncreasing != isIncreasing || diff < 1 || diff > 3) {
        return false;
      }
    }
    return true;
  }

  private static List<int[]> read() throws IOException {
    try (var lines = Files.lines(Path.of("input/day2.txt"))) {
      return lines.map(line -> {
            String[] split = line.split(" ");
            return Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
          })
          .toList();
    }
  }
}
