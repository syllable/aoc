package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Day02 {

  public static void main(String[] args) throws IOException {
    List<int[]> lines = read();

    var count = lines.stream()
        .filter(level -> {
          boolean lineIsIncreasing = level[1] > level[0];
          for (int i = 1; i < level.length; ++i) {
            boolean isIncreasing = level[i] > level[i - 1];
            int diff = Math.abs(level[i] - level[i - 1]);
            if (lineIsIncreasing != isIncreasing || diff < 1 || diff > 3) {
              return false;
            }
          }
          return true;
        })
        .count();
    System.out.println(count);

    // p2
    var count2 = lines.stream()
        .filter(level -> {

          int unsafeIdx = getUnsafeIdx(level);
          if (unsafeIdx == -1) {
            return true;
          }

          for (int i = 0; i < level.length; ++i) {
            int[] withOneSkipped = copyExceptOne(level, i);
            if (getUnsafeIdx(withOneSkipped) == -1) {
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

  private static int getUnsafeIdx(int[] level) {
    boolean lineIsIncreasing = level[1] > level[0];
    for (int i = 1; i < level.length; ++i) {
      boolean isIncreasing = level[i] > level[i - 1];
      int diff = Math.abs(level[i] - level[i - 1]);
      if (lineIsIncreasing != isIncreasing || diff < 1 || diff > 3) {
        return i;
      }
    }
    return -1;
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
