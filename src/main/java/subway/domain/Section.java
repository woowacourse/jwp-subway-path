package subway.domain;

import subway.exception.IllegalDistanceException;

public class Section {
    private final Long id;
    private final Station startStation;
    private final Station endStation;
    private final int distance;

    public Section(Station startStation, Station endStation, int distance) {
        this(null, startStation, endStation, distance);
    }

    public Section(Long id, Station startStation, Station endStation, int distance) {
        validateDistance(distance);
        this.id = id;
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalDistanceException("구간의 길이는 1 이상 이어야 합니다.");
        }
    }

    public boolean hasStation(Station station) {
        return startStation.equals(station) || endStation.equals(station);
    }

    public boolean hasStationInStartPosition(Station station) {
        return startStation.equals(station);
    }

    public boolean hasStationInEndPosition(Station station) {
        return endStation.equals(station);
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public Long getStartStationId() {
        return startStation.getId();
    }

    public Long getEndStationId() {
        return endStation.getId();
    }

    public int getDistance() {
        return distance;
    }

    public Long getId() {
        return id;
    }
}
