package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 {
    
    public static void main(String[] args) throws IOException {
        
        String data = new String(Files.readAllBytes(Path.of("input/day3.txt")));
        Matcher m = Pattern.compile("\\(" + 
                "(\\d+),(\\d+)" +
                "\\)")
            .matcher("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))");

        int sum = 0;
        while (m.find()) {
            var int1 = Integer.parseInt(m.group(1));
            var int2 = Integer.parseInt(m.group(2));
            System.out.println("%d,%d".formatted(int1, int2));
            sum += (int1 * int2);
        }

        System.out.println(sum);
    }
}
