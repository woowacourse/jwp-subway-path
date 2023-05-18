package subway.line.domain.section.dto;

import subway.line.domain.section.domain.Distance;

public class SectionSavingRequest {
    String previousStationName;
    String nextStationName;
    Distance distance;
    boolean isDown;

    public SectionSavingRequest(String previousStationName, String nextStationName, int distance, boolean isDown) {
        this.previousStationName = previousStationName;
        this.nextStationName = nextStationName;
        this.distance = Distance.of(distance);
        this.isDown = isDown;
    }

    public String getPreviousStationName() {
        return previousStationName;
    }

    public String getNextStationName() {
        return nextStationName;
    }

    public Distance getDistance() {
        return distance;
    }

    public boolean isDown() {
        return isDown;
    }
}
