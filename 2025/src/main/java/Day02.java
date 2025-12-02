import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class Day02 {
  public static void main(String[] args) throws Exception {

    List<String> split = List.of(Files.readString(Path.of("in/day2.txt")).split(","));
    List<String> example = List.of(("11-22,95-115,998-1012,1188511880-1188511890,222220-222224," +
                                  "1698522-1698528,446443-446449,38593856-38593862,565653-565659," +
                                  "824824821-824824827,2121212118-2121212124").split(","));

    System.out.println("Example P1: " + p1(example));
    assert p1(example) == 1227775554L : "Example 1 fail";

    System.out.println("Example P2: " + p2(example));
    assert p2(example) == 4174379265L : "Example 2 fail";



    System.out.println("Real P1: " + p1(split));
    System.out.println("Real P2: " + p2(split));
  }

  static long p1(List<String> ranges) {
    long sum = 0;
    for (String range : ranges) {

      int sep = range.indexOf("-");
      assert sep != -1;
      long start = Long.parseLong(range.substring(0, sep));
      long end = Long.parseLong(range.substring(sep + 1));

      for (long number = start; number <= end; number++) {
        String nr = String.valueOf(number);
        if (nr.length() % 2 == 0) {
          String s1 = nr.substring(0, nr.length() / 2);
          String s2 = nr.substring(nr.length() / 2);
          if (s1.equals(s2)) {
            sum += number;
          }
        }
      }
    }

    return sum;
  }

  static long p2(List<String> ranges) {
    long sum = 0;
    for (String range : ranges) {

      int sep = range.indexOf("-");
      assert sep != -1;
      long start = Long.parseLong(range.substring(0, sep));
      long end = Long.parseLong(range.substring(sep + 1));

      for (long id = start; id <= end; id++) {
        String idStr = String.valueOf(id);

        for (int subSeqLength = 1; subSeqLength <= idStr.length() / 2; subSeqLength++) {
          if (idStr.length() % subSeqLength != 0) {
            continue;
          }

          String subSeqStr = idStr.substring(0, subSeqLength);
          int subSeqOcc = idStr.length() / subSeqLength; // ssl=3 -> 9/3=3
          String repeated = subSeqStr.repeat(subSeqOcc);
          assert subSeqOcc > 1;
          if (repeated.equals(idStr)) {
            sum += id;
            break; // only sum ID once (e.g. 2, 22, 222 sequences in 222222)
          }
        }
      }
    }

    return sum;
  }
}
