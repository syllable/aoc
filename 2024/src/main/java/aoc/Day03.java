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

    // idea: find the closest instruction to the left of current mul() match by remembering do()/dont'() indices
    Matcher doInstr = Pattern.compile("do\\(\\)").matcher(data);
    Matcher dontInstr = Pattern.compile("don't\\(\\)").matcher(data);
    List<Integer> doIdxs = new ArrayList<>();
    List<Integer> dontIdxs = new ArrayList<>();

    while (doInstr.find()) {
      doIdxs.add(doInstr.start());
    }
    while (dontInstr.find()) {
      dontIdxs.add(dontInstr.start());
    }
    doIdxs.sort(Collections.reverseOrder());
    dontIdxs.sort(Collections.reverseOrder());

    var matcher2 = m.matcher(data);
    int sum2 = 0;
    while (matcher2.find()) {
      int start = matcher2.start();
      Integer prevDont = dontIdxs.stream().filter(idx -> idx < start).findFirst().orElse(-1);
      Integer prevDo = doIdxs.stream().filter(idx -> idx < start).findFirst().orElse(0); // do wins if both empty: "At the beginning of the program, mul instructions are enabled."
      if (prevDo > prevDont) {
        var int1 = Integer.parseInt(matcher2.group(1));
        var int2 = Integer.parseInt(matcher2.group(2));
        sum2 += (int1 * int2);
      }
    }
    System.out.println(sum2);
    Assert.assertEquals(53783319, sum2, "P2");
  }
}
