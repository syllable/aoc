package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 {
    
    public static void main(String[] args) throws IOException {
        
        Pattern m = Pattern.compile("mul\\(" + 
        "(\\d+),(\\d+)" +
        "\\)");

        int sum = 0;
        List<String> lines = Files.readAllLines(Path.of("input/day3.txt"));
        for (var line : lines) {
            var matcher = m.matcher(line);
            while (matcher.find()) {
                var int1 = Integer.parseInt(matcher.group(1));
                var int2 = Integer.parseInt(matcher.group(2));
                System.out.println("%d,%d".formatted(int1, int2));
                sum += (int1 * int2);
            }
        }

        // wrong: 162813399

        System.out.println(sum);
    }
}
