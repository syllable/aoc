

void main() throws Exception {

    List<String> lines;
    try (var x = Files.lines(Path.of("in/1.txt"))) {
        lines = x.toList();
    }

    var zeros = countZeros(lines);
    IO.println("Zeros: " + zeros);
    assert zeros == 6498;
}

private int countZeros(List<String> lines) {
    int dial = 50;
    int zeros = 0;
    for (String line : lines) {
        int direction = line.charAt(0) == 'R' ? 1 : -1;
        int steps = Integer.parseInt(line.substring(1));
        assert steps != 0;

        int fullTurns = steps / 100;
        zeros += fullTurns;

        int prevDial = dial;
        dial += ((steps % 100) * direction);


        if (dial == 0
                || dial == 100
                || (dial > 0 && prevDial < 0)
                || (dial < 0 && prevDial > 0)
                || (dial < 100 && prevDial > 100)
                || (dial > 100 && prevDial < 100)
        ) {
            ++zeros;
        }

        if (dial >= 100) {
            dial -= 100;
        }
        if (dial < 0) {
            dial += 100;
        }
    }

    return zeros;
}
