package subway.domain.fee;

import subway.domain.Lines;

public class FeeInformation {

    private final int distance;
    private Lines lines;

    public FeeInformation(int distance) {
        this.distance = distance;
    }

    public FeeInformation(int distance, Lines lines) {
        this.distance = distance;
        this.lines = lines;
    }

    public int getDistance() {
        return distance;
    }

    public Lines getLines() {
        return lines;
    }
}
