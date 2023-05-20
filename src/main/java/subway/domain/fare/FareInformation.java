package subway.domain.fare;

import subway.domain.Lines;

public class FareInformation {

    private final int distance;
    private Lines lines;
    private AgeGroup ageGroup;

    public FareInformation(int distance, Lines lines) {
        this.distance = distance;
        this.lines = lines;
    }

    public FareInformation(int distance, Lines lines, AgeGroup ageGroup) {
        this.distance = distance;
        this.lines = lines;
        this.ageGroup = ageGroup;
    }

    public int getDistance() {
        return distance;
    }

    public Lines getLines() {
        return lines;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }
}
