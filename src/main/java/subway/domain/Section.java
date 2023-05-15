package subway.domain;

import subway.exception.IllegalDistanceException;

public class Section {
    private final Station startStation;
    private final Station endStation;
    private final int distance;

    public Section(Station startStation, Station endStation, int distance) {
        validateDistance(distance);
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalDistanceException("구간의 길이는 1 이상 이어야 합니다.");
        }
    }

    public Station getStartStation() {
        return startStation;
    }

    public String getStartStationName() {
        return startStation.getName();
    }

    public Station getEndStation() {
        return endStation;
    }

    public String getEndStationName() {
        return endStation.getName();
    }

    public int getDistance() {
        return distance;
    }
}
