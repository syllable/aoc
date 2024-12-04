package aoc;

import static test.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04 {

  public static void main(String[] args) throws IOException {

    List<String> lines = Files.readAllLines(Path.of("input/day4_full.txt"));
    part1(lines);
    part2(lines);
  }

  static void part1(List<String> lines) {
    System.out.println("-- Part 1");
    int count = 0;
    List<String> diag = diag(lines);
    List<String> diag2 = diag(lines.stream().map(l -> new StringBuilder(l).reverse().toString()).toList());
    List<String> transposed = transpose(lines);

    int normal = countXmas(lines);
    count += normal;
    System.out.println("normal: " + normal);

    int diagCount = countXmas(diag);
    count += diagCount;
    System.out.println("diag: " + diagCount);

    int diagCount2 = countXmas(diag2);
    count += diagCount2;
    System.out.println("diag2: " + diagCount2);

    int transposedCount = countXmas(transposed);
    count += transposedCount;
    System.out.println("transposed: " + transposedCount);

    System.out.println(count);
  }

  private static void part2(List<String> lines) {
    System.out.println("-- Part 2");

    int count = countXmasPart2(lines);
    System.out.println(count);
  }

  private static int countXmasPart2(List<String> lines) {

    /*
    M.S
    .A.
    M.S
     */
    /*
    S.S
    .A.
    M.M
     */
    int rows = lines.size();
    int cols = lines.getFirst().length();

    int count = 0;
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; ++col) {
        if (col + 2 > (cols-1) || row + 2 > (rows-1)) {
          continue;
        }

        boolean midA = lines.get(row+1).charAt(col+1) == 'A';
        boolean diag1 = (lines.get(row).charAt(col) == 'M' && lines.get(row+2).charAt(col+2) == 'S')
            || (lines.get(row).charAt(col) == 'S' && lines.get(row+2).charAt(col+2) == 'M');
        boolean diag2 = (lines.get(row).charAt(col+2) == 'M' && lines.get(row+2).charAt(col) == 'S')
            || (lines.get(row).charAt(col+2) == 'S' && lines.get(row+2).charAt(col) == 'M');


        if (midA && diag1 && diag2) {
          ++count;
        }
      }
    }

    return count;
  }

  private static final Pattern XMAS = Pattern.compile("XMAS");
  private static final Pattern SAMX = Pattern.compile("SAMX");

  private static int countXmas(List<String> lines) {
    int count = 0;
    for (String line : lines) {
      Matcher normal = XMAS.matcher(line);
      while (normal.find()) {
        ++count;
      }

      Matcher reversed = SAMX.matcher(line);
      while (reversed.find()) {
        ++count;
      }
    }
    return count;
  }

  private static List<String> transpose(List<String> lines) {
    int rows = lines.size();
    int cols = lines.getFirst().length();

    List<String> transposed = new ArrayList<>();
    for (int col = 0; col < cols; ++col) {
      var sb = new StringBuilder();
      for (int row = 0; row < rows; ++row) {
        sb.append(lines.get(row).charAt(col));
      }
      transposed.add(sb.toString());
    }
    return transposed;
  }

  private static List<String> diag(List<String> lines) {
    List<String> result = new ArrayList<>();

    int rows = lines.size();
    int cols = lines.getFirst().length();
    assertTrue(rows >= cols, "rows >= cols handled");

    for (int startCol = 0; startCol < cols; ++startCol) {
      int r = 0;
      int c = startCol;
      StringBuilder sb = new StringBuilder();
      while (r < rows && c < cols) {
        sb.append(lines.get(r).charAt(c));
        ++r;
        ++c;
      }
      result.add(sb.toString());
    }

    for (int startRow = 1; startRow < rows; ++startRow) {
      int r = startRow;
      int c = 0;
      StringBuilder sb = new StringBuilder();
      while (r < rows && c < cols) {
        sb.append(lines.get(r).charAt(c));
        ++r;
        ++c;
      }
      result.add(sb.toString());
    }

    return result;
  }
}
