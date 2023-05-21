package subway.domain.fare;

import subway.domain.Lines;

public class FareInformation {

    private final int distance;
    private final Lines lines;
    private final AgeGroup ageGroup;

    public FareInformation(final int distance, final Lines lines, final AgeGroup ageGroup) {
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
