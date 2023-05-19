package subway.domain.fare;

import subway.domain.Lines;

public class FareInformation {

    private final int distance;
    private Lines lines;

    public FareInformation(int distance, Lines lines) {
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
