package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import test.Assert;

public class Day03 {

  public static void main(String[] args) throws IOException {

    Pattern m = Pattern.compile("mul\\(" +
        "(\\d+),(\\d+)" +
        "\\)");

    int sum = 0;
    String data = Files.readString(Path.of("input/day3.txt"));

    var matcher = m.matcher(data);
    while (matcher.find()) {
      var int1 = Integer.parseInt(matcher.group(1));
      var int2 = Integer.parseInt(matcher.group(2));
      sum += (int1 * int2);
    }

    Assert.assertEquals(162813399, sum, "P1");
    System.out.println(sum);

    // P2

    Matcher matcher2 = Pattern.compile("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)").matcher(data);
    int sum2 = 0;

    boolean execNextMul = true;
    while (matcher2.find()) {
      String word = matcher2.group(0);
      if (word.equals("do()")) {
        execNextMul = true;
      } else if (word.equals("don't()")) {
        execNextMul = false;
      } else if (execNextMul) {
        var int1 = Integer.parseInt(matcher2.group(1));
        var int2 = Integer.parseInt(matcher2.group(2));
        sum2 += (int1 * int2);
      } else {
        // ignore mul
      }
    }
    System.out.println(sum2);
    Assert.assertEquals(53783319, sum2, "P2");
  }
}
